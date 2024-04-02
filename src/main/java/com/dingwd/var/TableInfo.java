package com.dingwd.var;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "表信息")
public class TableInfo {

    @Schema(title = "表的主键 要在模板中判断是否存在主键")
    private String primaryKey;
    @Schema(title = "表的主键 驼峰格式 要在模板中判断是否存在主键")
    private String primaryKeyCamel;

    @Schema(title = "表的名称")
    private String tableName;
    @Schema(title = "表的名称 驼峰格式")
    private String tableNameCamel;
    @Schema(title = "表的名称 驼峰格式 首字母大写")
    private String tableNameFirstUpperCase;

    @Schema(title = "表的注释")
    private String tableComment;

    @Schema(title = "字段列表")
    private List<Field> fields;

}