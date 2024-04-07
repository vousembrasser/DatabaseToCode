package com.dingwd.service.template.impl;

import com.dingwd.service.template.TemplateFactory;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@Service
public class FreeMarkerService implements TemplateFactory {

    public FreeMarkerService() {
        configuration = new Configuration(Configuration.VERSION_2_3_32);
    }

    private final Configuration configuration;

    @Override
    public void createFile(Map<String, Object> input, String savePath, String fileName, File templateFile) throws Exception {
        // 创建 FileTemplateLoader
        FileTemplateLoader templateLoader = new FileTemplateLoader(templateFile.getParentFile());

        // 将 FileTemplateLoader 设置为配置中的模板加载器
        configuration.setTemplateLoader(templateLoader);

        // 获取模板
        Template template = configuration.getTemplate(templateFile.getName());

        Writer writer;
        writer = new OutputStreamWriter(Files.newOutputStream(Paths.get(savePath + "\\" + fileName)), StandardCharsets.UTF_8);
        template.process(input, writer);
    }


    @Override
    public String changeLine(Map<String, Object> parameters, String line) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        List<List<int[]>> listss = findBracketPairs(line);

        for (List<int[]> startAndEndIndex : listss) {
            if (startAndEndIndex.size() > 1) {
                for (int i = 0; i < startAndEndIndex.size(); i++) {
                    line = processPair(parameters, line, listss, startAndEndIndex, i);
                }
            } else {
                line = processPair(parameters, line, listss, startAndEndIndex, 0);
            }
        }
        return line;
    }

    @Override
    public Boolean useThis(String fileType) {
        return "ftl".equals(fileType) || ".ftl".equals(fileType);
    }


    private String processPair(Map<String, Object> parameters, String line, List<List<int[]>> listss, List<int[]> startAndEndIndex, int index) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        int[] temp = startAndEndIndex.get(index);
        if (line.charAt(temp[0] - 1) == '$') {
            String ss = line.substring(temp[0] + 1, temp[1]);
            if (ss.contains(".")) {
                Object clazz = parameters.get(ss.split("\\.")[0]);
                if (clazz == null) {
                    throw new NullPointerException("为查询到" + ss.split("\\.")[0] + "的变量");
                }
                Field nameField = clazz.getClass().getDeclaredField(ss.split("\\.")[1]);
                nameField.setAccessible(true);
                String res = (String) nameField.get(clazz);
                if (res == null) {
                    throw new NullPointerException("字段" + ss.split("\\.")[0] + "." + nameField.getName() + "为空");
                }
                updateLineIndexes(temp, res.length(), listss);
                return line.substring(0, temp[0] - 1) + res + line.substring(temp[1] + 1);
            }
        } else if (line.charAt(temp[0] - 1) == '&') {
            String res = invoke(line.substring(temp[0] + 1, temp[1]));
            updateLineIndexes(temp, res.length(), listss);
            return line.substring(0, temp[0] - 1) + res + line.substring(temp[1] + 1);
        }
        return line;
    }

    private void updateLineIndexes(int[] indices, int newStringLength, List<List<int[]>> lineChanges) {
        int oldStr = (indices[1] + 1) - (indices[0] - 1);
        int lengthDifference = newStringLength - oldStr;
        for (List<int[]> changes : lineChanges) {
            for (int[] change : changes) {
                if (change[0] == indices[0] && change[1] == indices[1]) {
                    continue;
                }
                if (change[0] > indices[1]) {
                    change[0] += lengthDifference;
                    change[1] += lengthDifference;
                } else if (change[0] < indices[1] && change[1] > indices[1]) {
                    change[1] += lengthDifference;
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
            throw new RuntimeException("Method not found: " + methodName + "(" + Arrays.toString(params) + ")");
        }

        // 创建对象并调用方法
        Object instance = clazz.getDeclaredConstructor().newInstance();
        Object result = method.invoke(instance, params);
        return (String) result;
    }


    private List<List<int[]>> findBracketPairs(String s) {
        Stack<Integer> openBrackets = new Stack<>();
        List<List<int[]>> bracketPairsList = new ArrayList<>();
        List<int[]> currentPairList = new ArrayList<>();

        boolean haveFlag = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (!haveFlag && c == '&' && i + 1 < s.length() && s.charAt(i + 1) == '{') {
                haveFlag = true;
                continue;
            }

            if (!haveFlag) {
                continue;
            }

            if (c == '{') {
                openBrackets.push(i);
            } else if (c == '}') {
                if (!openBrackets.isEmpty()) {
                    int leftIndex = openBrackets.pop();
                    currentPairList.add(new int[]{leftIndex, i});

                    if (openBrackets.isEmpty()) {
                        haveFlag = false;
                        bracketPairsList.add(currentPairList);
                        currentPairList = new ArrayList<>();
                    }
                } else {
                    throw new IllegalArgumentException("Unmatched '}': " + s);
                }
            }
        }

        if (!openBrackets.isEmpty()) {
            throw new IllegalArgumentException("Unmatched '{': " + s);
        }

        return bracketPairsList;
    }

}