package com.dingwd.service;

import com.dingwd.domain.ParaMap;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@Service
public class FreeMarkerService {

    public FreeMarkerService() {
        configuration = new Configuration(Configuration.VERSION_2_3_32);
    }

    @Resource
    private final Configuration configuration;

    @Resource
    private ResourceLoader resourceLoader;

    public void generatorFile(Map<String, Object> input, String templateFileName, String savePath, String fileName)  {
        if (!templateFileName.contains(".ftl")) {
            templateFileName = templateFileName + ".ftl";
        }
        File templatesFile = null;

        try {
            templatesFile = readFile(templateFileName);
            File templatesDir = templatesFile.getParentFile();

            // 创建 FileTemplateLoader
            FileTemplateLoader templateLoader = new FileTemplateLoader(templatesDir);

            // 将 FileTemplateLoader 设置为配置中的模板加载器
            configuration.setTemplateLoader(templateLoader);

            // 获取模板
            Template template = configuration.getTemplate(templatesFile.getName());

            File file = new File(savePath);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    throw new IOException("创建文件夹失败");
                }
            }
            Writer writer;
            writer = new OutputStreamWriter(Files.newOutputStream(Paths.get(savePath + "\\" + fileName)), StandardCharsets.UTF_8);
            template.process(input, writer);
        } catch (Exception e) {
            log.error("生成文件失败", e);
        } finally {
            if (templatesFile != null) {
                templatesFile.deleteOnExit();
            }
        }
    }

    private File readFile(String templateFileName) {
        org.springframework.core.io.Resource resource = resourceLoader.getResource("classpath:templates/" + templateFileName);
        //创建临时文件
        File tempFile;
        try {
            tempFile = Files.createTempFile(templateFileName + "temp", ".ftl").toFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String line;

            Iterator<String> iterator = iterator(reader);
            while (iterator.hasNext()) {
                try {
                    line = changLine(ParaMap.INSTANCE.getParameters(), iterator.next());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                writer.write(line);
                if (iterator.hasNext()) {
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tempFile;
    }

    public Iterator<String> iterator(BufferedReader reader) {
        return new Iterator<>() {
            String nextLine = null;

            @Override
            public boolean hasNext() {
                if (nextLine != null) {
                    return true;
                } else {
                    try {
                        nextLine = reader.readLine();
                        return (nextLine != null);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }
            }

            @Override
            public String next() {
                if (nextLine != null || hasNext()) {
                    String line = nextLine;
                    nextLine = null;
                    return line;
                } else {
                    throw new NoSuchElementException();
                }
            }
        };
    }

    private List<List<int[]>> getBracketPairs(String s) {
        Stack<Integer> stack = new Stack<>();
        List<int[]> pairs = new ArrayList<>();
        List<List<int[]>> list = new ArrayList<>();
        boolean have = false;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!have && c == '&' && (i + 1 <= s.length()) && s.charAt(i + 1) == '{') {
                have = true;
            } else if (have && c == '{') {
                stack.push(i);
            } else if (have && c == '}') {
                if (!stack.isEmpty()) {
                    int leftIndex = stack.pop();
                    pairs.add(new int[]{leftIndex, i});
                    if (stack.isEmpty()) {
                        have = false;
                        list.add(pairs);
                    }
                    if (!have) {
                        pairs = new ArrayList<>();
                    }

                } else {
                    // 处理右括号没有匹配的情况
                    throw new RuntimeException("Unmatched '}': " + s);
                }
            }
        }

        while (!stack.isEmpty()) {
            // 处理左括号没有匹配的情况
            throw new RuntimeException("Unmatched '{': " + s);
        }

        return list;
    }

    private String changLine(Map<String, Object> parameters, String line) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        List<List<int[]>> listss = getBracketPairs(line);

        for (List<int[]> startAndEndIndex : listss) {
            if (startAndEndIndex.size() > 1) {
                for (int i = 0; i < startAndEndIndex.size(); i++) {
                    int[] temp = startAndEndIndex.get(i);
                    if (i != startAndEndIndex.size() - 1) {
                        String ss = line.substring(temp[0] + 1, temp[1]);
                        if (line.charAt(temp[0] - 1) == '$' && ss.contains(".")) {
                            Field nameField = parameters.get(ss.split("\\.")[0]).getClass().getDeclaredField(ss.split("\\.")[1]);
                            // 设置访问权限
                            nameField.setAccessible(true);
                            String res = (String) nameField.get(parameters.get(ss.split("\\.")[0]));
                            if (res == null) {
                                throw new RuntimeException("字段" + ss.split("\\.")[0] + "." + nameField.getName() + "为空");
                            }
                            changLineIndex(temp, res, listss);
                            line = line.substring(0, temp[0] - 1) + res + line.substring(temp[1] + 1);
                        }
                    } else {
                        if (line.charAt(temp[0] - 1) == '&') {
                            String res = invokeNesting(line.substring(temp[0] + 1, temp[1]));
                            changLineIndex(temp, res, listss);
                            line = line.substring(0, temp[0] - 1) + res + line.substring(temp[1] + 1);
                        }
                    }
                }
            } else {
                int[] temp = startAndEndIndex.getFirst();
                if (line.charAt(temp[0] - 1) == '&') {
                    String res = invoke(line.substring(temp[0] + 1, temp[1]));
                    changLineIndex(temp, res, listss);
                    line = line.substring(0, temp[0] - 1) + res + line.substring(temp[1] + 1);
                }
            }
        }
        return line;
    }

    private void changLineIndex(int[] temp, String nameValue, List<List<int[]>> listss) {
        int oldStr = (temp[1] + 1) - (temp[0] - 1);
        int newStr = nameValue.length();
        int res = newStr - oldStr;
        for (List<int[]> ints : listss) {
            for (int[] change : ints) {
                if (change[0] == temp[0] && change[1] == temp[1]) {
                    continue;
                }
                if (change[0] > temp[1]) {
                    change[0] = change[0] + res;
                    change[1] = change[1] + res;
                } else if (change[0] < temp[1] && change[1] > temp[1]) {
                    change[1] = change[1] + res;
                }
            }
        }
    }


    private static String invoke(String str) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        String[] parts = str.split("\\.");
        String className = "com.dingwd.var.util." + parts[0];
        String methodNameWithParams = parts[1];

        // 提取方法名和参数
        String[] methodParts = methodNameWithParams.split("\\(");
        String methodName = methodParts[0];
        String paramsString = methodParts[1].replaceAll("[()]", "");
        String[] params = paramsString.split(",");

        // 加载类
        Class<?> clazz = Class.forName(className);
        // 获取方法
        Method method = null;
        for (Method tempMethod : clazz.getMethods()) {
            if (methodName.equals(tempMethod.getName())
                    && tempMethod.getParameterCount() == params.length) {
                method = tempMethod;
                break;
            }
        }
        if (method == null) {
            throw new RuntimeException("Method not found");
        }

        // 创建对象并调用方法
        Object instance = clazz.getDeclaredConstructor().newInstance();
        Object result = method.invoke(instance, params);
        return (String) result;
    }
    public String invokeNesting(String methodCall) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        String[] funsTemp = methodCall.splitWithDelimiters("utilFun", 0);
        List<String> funs = new ArrayList<>();
        for (String fun : funsTemp) {
            if (!"utilFun".equals(fun)) {
                funs.add(fun);
            }
        }
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < methodCall.length(); i++) {
            char c = methodCall.charAt(i);
            if (c == '(') {
                stack.push(i);
            } else if (c == ')') {
                if (!stack.isEmpty()) {
                    int size = stack.size();
                    stack.pop();
                    String temp = funs.get(size);
                    if (isRepeat(temp)) {
                        String neesString = temp.substring(temp.indexOf(")") + 1);
                        temp = temp.substring(0, temp.indexOf(")") + 1);
                        temp = invoke("utilFun" + temp);
                        if (stack.isEmpty()) {
                            return temp;
                        }
                        if (size > 1) {
                            String change = funs.get(size - 1);
                            funs.set(size - 1, change + temp + neesString);
                        }
                    } else if (temp.split("\\)").length == 1) {
                        return invoke("utilFun" + temp);
                    } else {
                        throw new RuntimeException("error");
                    }
                }
            }
        }
        throw new RuntimeException("error");
    }

    public boolean isRepeat(String str) {
        char[] chars = str.toCharArray();
        int count = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ')') {
                count++;
            }
        }
        return count > 1;
    }

}