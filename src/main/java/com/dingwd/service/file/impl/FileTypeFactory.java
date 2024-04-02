package com.dingwd.service.file.impl;

import com.dingwd.domain.ModelInfo;

public interface FileTypeFactory {

    String getSavePath(String thePathToSaveToTheLocalComputer, ModelInfo modelInfo);

    String columnTypeToClassType(String columnType, String fieldPrecision);

    Boolean useThis(String fileType);


}
