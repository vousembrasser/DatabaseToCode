package com.dingwd.domain;

import com.dingwd.enums.PackageTypeEnum;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ProjectInfo {


    private final List<ModelInfo> models = new ArrayList<>();

    private String savePath;
    private final String projectName;

    public ProjectInfo(String projectName) {
        this.projectName = projectName;
    }

    public GeneratorInfo saveWith(String savePath) {
        this.savePath = savePath;
        return GeneratorInfo.INSTANCE;
    }

    public ProjectInfo makePackage(PackageTypeEnum packageTypeEnum, FileNamePrefixAndSuffix prefixAndSuffix, String multiplePackage, String featureName, String... refTemplates) {
        models.add(new ModelInfo(projectName, multiplePackage, featureName, new TypeNameAndKind(packageTypeEnum, prefixAndSuffix), false, List.of(refTemplates)));
        return this;
    }

    public ProjectInfo makePackage(PackageTypeEnum packageTypeEnum, String multiplePackage, String featureName, String... refTemplates) {
        models.add(new ModelInfo(projectName, multiplePackage, featureName, new TypeNameAndKind(packageTypeEnum), false, List.of(refTemplates)));
        return this;
    }

    public ProjectInfo setSub(PackageTypeEnum packageTypeEnum, FileNamePrefixAndSuffix prefixAndSuffix, String... refTemplates) {
        ModelInfo modelInfo = models.getLast();
        models.add(new ModelInfo(modelInfo.projectName(),
                modelInfo.multiplePackage() + "." + modelInfo.featureName(),
                modelInfo.typeNameAndKind().typeName(), new TypeNameAndKind(packageTypeEnum, prefixAndSuffix), modelInfo.typeFirst(), List.of(refTemplates)));
        return this;
    }
}
