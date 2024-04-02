package com.dingwd.service.file.impl;

import com.dingwd.domain.ModelInfo;
import com.dingwd.utils.JavaTypeConstant;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class XmlFileTypeFactoryImpl implements FileTypeFactory {

    @Override
    public String getSavePath(String thePathToSaveToTheLocalComputer, ModelInfo modelInfo) {
        String pathPrefix = thePathToSaveToTheLocalComputer;
        if (!pathPrefix.endsWith(File.separator)) {
            pathPrefix = pathPrefix + File.separator;
        }
        String savePath = pathPrefix + modelInfo.projectName() + File.separator +
                "src" + File.separator + "main" + File.separator + "java" + File.separator +
                modelInfo.multiplePackage() + File.separator +
                (modelInfo.typeFirst() ? modelInfo.typeNameAndKind().typeName() + File.separator + modelInfo.featureName()
                        : modelInfo.featureName() + File.separator + modelInfo.typeNameAndKind().typeName());
        savePath = savePath.replace(".", File.separator);
        return savePath;
    }

    public String columnTypeToClassType(String columnType, String fieldPrecision) {
        return JavaTypeConstant.getJavaType(columnType, fieldPrecision);
    }

    @Override
    public Boolean useThis(String fileType) {
        return "xml".equalsIgnoreCase(fileType);
    }

}
