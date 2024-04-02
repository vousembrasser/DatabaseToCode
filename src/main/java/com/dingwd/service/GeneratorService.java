package com.dingwd.service;


import com.dingwd.domain.GeneratorInfo;
import com.dingwd.domain.ModelInfo;
import com.dingwd.domain.ParaMap;
import com.dingwd.domain.ProjectInfo;
import com.dingwd.enums.PackageTypeEnum;
import com.dingwd.service.file.FileGetSavePathService;
import com.dingwd.var.ClassParam;
import com.dingwd.var.TableInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class GeneratorService {

    @Autowired
    private FreeMarkerService freeMarkerService;
    @Autowired
    private TableService tableService;
    @Autowired
    private FileGetSavePathService fileGetSavePathService;


    private GeneratorInfo generatorInfo;

    public GeneratorService build(GeneratorInfo generatorInfo) {
        this.generatorInfo = generatorInfo;
        return this;
    }


    public void execute() {
        this.execute(null);
    }

    public void execute(Map<String, String> additionalParameters) {

        Map<String, Object> parameters = ParaMap.INSTANCE.getParameters();
        if (additionalParameters != null) {
            parameters.putAll(additionalParameters);
        }

        parameters.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        parameters.put("year", new SimpleDateFormat("yyyy").format(new Date()));
        String author = generatorInfo.getAuthor();
        parameters.put("author", author);

        List<TableInfo> tables = tableService.getTablesInfo(generatorInfo.getTableNames(),
                generatorInfo.getRemovePrefix(),
                generatorInfo.getFileSuffix(),
                generatorInfo.isNumberToString());

        for (TableInfo table : tables) {
            ClassParam classParam = new ClassParam();
            parameters.put("tableInfo", table);
            for (ProjectInfo projectInfo : generatorInfo.getProjectInfos()) {
                projectInfo.getModels().forEach(modelInfo -> {
                    String className = getClassName(table, modelInfo);
                    String path = modelInfo.multiplePackage() + "." +
                            (modelInfo.typeFirst() ? modelInfo.typeNameAndKind().typeName() + "." + modelInfo.featureName()
                                    : modelInfo.featureName() + "." + modelInfo.typeNameAndKind().typeName()) + "." +
                            className;
                    setVar(modelInfo.typeNameAndKind().packageTypeEnum(), classParam, path, className);
                });
            }

            for (ProjectInfo projectInfo : generatorInfo.getProjectInfos()) {
                for (ModelInfo modelInfo : projectInfo.getModels()) {
                    classParam.setClassName(getClassName(table, modelInfo));
                    String path = modelInfo.multiplePackage() + "." +
                            (modelInfo.typeFirst() ? modelInfo.typeNameAndKind().typeName() + "." + modelInfo.featureName()
                                    : modelInfo.featureName() + "." + modelInfo.typeNameAndKind().typeName());
                    classParam.setPackagePath(path);
                    parameters.put("classParam", classParam);
                    for (String templateName : modelInfo.refTemplates()) {
                        String savePath = fileGetSavePathService.getSavePath(generatorInfo.getFileSuffix(),
                                projectInfo.getSavePath(), modelInfo);
                        String fileSuffix = modelInfo.typeNameAndKind().prefixAndSuffix().getFileSuffix();
                        freeMarkerService.generatorFile(parameters,
                                templateName,
                                savePath,
                                getClassName(table, modelInfo) + "." + fileSuffix);
                    }
                }
            }
        }
    }

    private static String getClassName(TableInfo table, ModelInfo modelInfo) {
        String preFix = modelInfo.typeNameAndKind().prefixAndSuffix().getPreFix();
        String middle = modelInfo.typeNameAndKind().prefixAndSuffix().getMiddle();
        String suffix = modelInfo.typeNameAndKind().prefixAndSuffix().getSuffix();
        return (StringUtils.hasText(preFix) ? preFix : "") +
                (StringUtils.hasText(middle) ? middle : table.getTableNameFirstUpperCase()) +
                (StringUtils.hasText(suffix) ? suffix : "");
    }

    private void setVar(PackageTypeEnum typeEnum, ClassParam paramVar, String path, String className) {
        switch (typeEnum) {
            case DO -> {
                paramVar.setDoPath(path);
                paramVar.setDoName(className);
            }
            case DTO -> {
                paramVar.setDtoPath(path);
                paramVar.setDtoName(className);
            }
            case MAPPER -> {
                paramVar.setMapperPath(path);
                paramVar.setMapperName(className);
            }
            case REPOSITORY -> {
                paramVar.setRepositoryPath(path);
                paramVar.setRepositoryName(className);
            }
            case INTERFACE -> {
                paramVar.setInterfacePath(path);
                paramVar.setInterfaceName(className);
            }
            case SERVICE_IMPL -> {
                paramVar.setServiceImplPath(path);
                paramVar.setServiceImplName(className);
            }
            case CONTROLLER -> {
                paramVar.setControllerPath(path);
                paramVar.setControllerName(className);
            }
        }
    }
}