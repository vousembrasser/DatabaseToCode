package com.dingwd.domain;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "模块")
public record ModelInfo(
        @Schema(title = "项目名称")
        String projectName,

        @Schema(title = "复合的包名")
        String multiplePackage,

        @Schema(title = "功能类别 最后一个包名")
        String featureName,

        @Schema(title = "功能类别 和 对应的包名")
        TypeNameAndKind typeNameAndKind,

        @Schema(title = "说明: 比如 true为: dns(功能featureName).model | false为: model.dns(功能featureName)")
        Boolean typeFirst,

        @Schema(title = "引用的模板")
        List<String> refTemplates
) {
}
