package com.dingwd.service.template;

import com.dingwd.domain.ParaMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class TemplateService {

    private final List<TemplateFactory> templateFactories;

    private final ResourceLoader resourceLoader;

    public TemplateService(List<TemplateFactory> templateFactories, ResourceLoader resourceLoader) {
        this.templateFactories = templateFactories;
        this.resourceLoader = resourceLoader;
    }

    public void generatorFile(Map<String, Object> parameters, String templateFileName, String savePath, String fileName) {
        int have = templateFileName.lastIndexOf(".");

        String suffix;
        if (have == -1) {
            suffix = ".ftl";
            templateFileName += ".ftl";
        } else {
            suffix = templateFileName.substring(have);
        }
        TemplateFactory templateFactory = templateFactories.stream().filter(item -> item.useThis(suffix)).findFirst().orElseThrow();
        File templatesFile = null;
        try {

            File file = new File(savePath);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    throw new IOException("创建文件夹失败");
                }
            }

            templatesFile = readFile(templateFactory, templateFileName, suffix);
            templateFactory.createFile(parameters, savePath, fileName, templatesFile);
        } catch (Exception e) {
            log.error("生成文件失败", e);
        } finally {
            if (templatesFile != null) {
                templatesFile.deleteOnExit();
            }
        }
    }


    private File readFile(TemplateFactory templateFactory, String templateFileName, String suffix) {
        org.springframework.core.io.Resource resource = resourceLoader.getResource("classpath:templates/" + templateFileName);
        //创建临时文件
        File tempFile;
        try {
            tempFile = Files.createTempFile(templateFileName + "temp", suffix).toFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String line;

            Iterator<String> iterator = iterator(reader);
            while (iterator.hasNext()) {
                try {
                    line = templateFactory.changeLine(ParaMap.INSTANCE.getParameters(), iterator.next());
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

    private Iterator<String> iterator(BufferedReader reader) {
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


}
