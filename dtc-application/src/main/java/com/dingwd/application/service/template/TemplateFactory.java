package com.dingwd.application.service.template;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public interface TemplateFactory {

    //把自定义的方法 执行后 替换放入模板中
    String changeLine(Map<String, Object> parameters, String line) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException;

    Boolean useThis(String fileType);

    void createFile(Map<String, Object> input, String savePath, String fileName, File templateFile) throws Exception;

}
