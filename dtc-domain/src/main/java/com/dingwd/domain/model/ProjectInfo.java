package com.dingwd.domain.model;

import com.dingwd.domain.enums.PackageTypeEnum;
import lombok.Getter;

import java.io.File;
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
        //文件路径分隔符
        if(!savePath.endsWith(File.separator)){
            savePath += File.separator;
        }
        this.savePath = savePath;
        return GeneratorInfo.INSTANCE;
    }

    public ProjectInfo makePackage(PackageTypeEnum packageTypeEnum, FileNamePrefixAndSuffix prefixAndSuffix, String multiplePackage, String featureName, String... refTemplates) {
        models.add(new ModelInfo(projectName, multiplePackage, featureName, new TypeNameAndKind(packageTypeEnum, prefixAndSuffix), false, List.of(refTemplates)));
        return this;
    }

    public ProjectInfo makePackage(PackageTypeEnum packageTypeEnum, String multiplePackage, String featureName, String... refTemplates) {
        return this.makePackage(packageTypeEnum, packageTypeEnum.getPrefixAndSuffix(), multiplePackage, featureName, refTemplates);
//        models.add(new ModelInfo(projectName, multiplePackage, featureName, new TypeNameAndKind(packageTypeEnum), false, List.of(refTemplates)));
//        return this;
    }

    public ProjectInfo makePackage(PackageTypeEnum packageTypeEnum, FileNamePrefixAndSuffix prefixAndSuffix, String multiplePackage, String featureName, boolean typeFirst, String... refTemplates) {
        return this.makePackage(packageTypeEnum, prefixAndSuffix, multiplePackage, featureName,typeFirst, refTemplates);
//        models.add(new ModelInfo(projectName, multiplePackage, featureName, new TypeNameAndKind(packageTypeEnum, prefixAndSuffix), typeFirst, List.of(refTemplates)));
//        return this;
    }

    public ProjectInfo makePackage(PackageTypeEnum packageTypeEnum, String multiplePackage, String featureName, boolean typeFirst, String... refTemplates) {
        return this.makePackage(packageTypeEnum, packageTypeEnum.getPrefixAndSuffix(), multiplePackage, featureName,typeFirst, refTemplates);
//        models.add(new ModelInfo(projectName, multiplePackage, featureName, new TypeNameAndKind(packageTypeEnum), typeFirst, List.of(refTemplates)));
//        return this;
    }




    public ProjectInfo setSub(PackageTypeEnum packageTypeEnum, FileNamePrefixAndSuffix prefixAndSuffix, String... refTemplates) {
        ModelInfo modelInfo = models.getLast();
        models.add(new ModelInfo(modelInfo.projectName(),
                modelInfo.multiplePackage() + "." + modelInfo.featureName(),
                modelInfo.typeNameAndKind().typeName(), new TypeNameAndKind(packageTypeEnum, prefixAndSuffix), modelInfo.typeFirst(), List.of(refTemplates)));
        return this;
    }
}
