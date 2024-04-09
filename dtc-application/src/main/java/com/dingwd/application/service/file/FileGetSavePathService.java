package com.dingwd.application.service.file;

import com.dingwd.domain.model.ModelInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileGetSavePathService {


    private final List<FileTypeFactory> fileTypeFactories;

    public FileGetSavePathService(List<FileTypeFactory> fileTypeFactories) {
        this.fileTypeFactories = fileTypeFactories;
    }

    public String getSavePath(String fileSuffix, String savePath, ModelInfo modelInfo) {
        return fileTypeFactories.stream()
                .filter(fileTypeFactory -> fileTypeFactory.useThis(fileSuffix))
                .findFirst()
                .map(fileTypeFactory -> fileTypeFactory.getSavePath(savePath, modelInfo))
                .orElseThrow(() -> new RuntimeException("文件类型: " + fileSuffix + " 没有实现类"));
    }

    public String columnTypeToClassType(String fileType, String attributeType, String fieldPrecision) {
        return fileTypeFactories.stream()
                .filter(fileTypeFactory -> fileTypeFactory.useThis(fileType))
                .findFirst()
                .map(fileTypeFactory -> fileTypeFactory.columnTypeToClassType(attributeType, fieldPrecision))
                .orElseThrow(() -> new RuntimeException("文件类型: " + fileType + " 没有实现类"));
    }

}
