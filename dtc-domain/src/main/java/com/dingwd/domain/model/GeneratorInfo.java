package com.dingwd.domain.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum GeneratorInfo {
    INSTANCE;

    //模块
    private final List<ProjectInfo> projectInfos = new ArrayList<>();
    private List<String> tableNames;

    private String author;
    private String fileSuffix = "java";
    private boolean numberToString = false;

    //默认表名
    private String removePrefix;

    public ProjectInfo setProjectInfo(ProjectInfo projectInfo) {
        this.projectInfos.add(projectInfo);
        return projectInfo;
    }

    public GeneratorInfo setTableName(String... tableName) {
        this.tableNames = List.of(tableName);
        return this;
    }

    public GeneratorInfo setAuthor(String author) {
        this.author = author;
        return this;
    }

    public GeneratorInfo setNumberToString(boolean toString) {
        this.numberToString = toString;
        return this;
    }

    public GeneratorInfo setFileSuffix(String fileSuffix) {
        if ('.' == fileSuffix.charAt(0)) {
            fileSuffix = fileSuffix.substring(1);
        }
        this.fileSuffix = fileSuffix;
        return this;
    }

    public GeneratorInfo setRemovePrefix(String removePrefix) {
        this.removePrefix = removePrefix;
        return this;
    }

}