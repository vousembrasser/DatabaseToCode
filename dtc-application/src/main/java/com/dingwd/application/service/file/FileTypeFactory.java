package com.dingwd.application.service.file;


import com.dingwd.domain.model.ModelInfo;

public interface FileTypeFactory {

    String getSavePath(String thePathToSaveToTheLocalComputer, ModelInfo modelInfo);

    String columnTypeToClassType(String columnType, String fieldPrecision);

    Boolean useThis(String fileType);


}
