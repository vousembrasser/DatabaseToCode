package com.dingwd.domain;

import com.dingwd.enums.PackageTypeEnum;

public record TypeNameAndKind(PackageTypeEnum packageTypeEnum, String typeName,
                              FileNamePrefixAndSuffix prefixAndSuffix) {
    public TypeNameAndKind(PackageTypeEnum packageTypeEnum) {
        this(packageTypeEnum, packageTypeEnum.getTypeName(), packageTypeEnum.getPrefixAndSuffix());
    }

    public TypeNameAndKind(PackageTypeEnum packageTypeEnum, String typeName) {
        this(packageTypeEnum, typeName, packageTypeEnum.getPrefixAndSuffix());
    }

    public TypeNameAndKind(PackageTypeEnum packageTypeEnum, FileNamePrefixAndSuffix prefixAndSuffix) {
        this(packageTypeEnum, packageTypeEnum.getTypeName(), prefixAndSuffix);
    }

}
