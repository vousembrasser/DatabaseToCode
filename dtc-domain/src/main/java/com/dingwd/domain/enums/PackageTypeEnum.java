package com.dingwd.domain.enums;

import com.dingwd.domain.model.FileNamePrefixAndSuffix;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor()
public enum PackageTypeEnum {

    DTO("dto", new FileNamePrefixAndSuffix(null, null, "DTO", "java")),
    DO("model", new FileNamePrefixAndSuffix(null, null, "DO", "java")),
    REPOSITORY("repository", new FileNamePrefixAndSuffix("I", null, "Repository", "java")),
    MAPPER("mapper", new FileNamePrefixAndSuffix(null, null, "Mapper", "xml")),
    INTERFACE("service", new FileNamePrefixAndSuffix("I", null, "Service", "java")),
    SERVICE_IMPL("service", new FileNamePrefixAndSuffix(null, null, "Service", "java")),
    CONTROLLER("controller", new FileNamePrefixAndSuffix(null, null, "Controller", "java")),
    ;

    @Getter
    private String typeName;

    @Getter
    private FileNamePrefixAndSuffix prefixAndSuffix;

}
