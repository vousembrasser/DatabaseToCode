package com.dingwd.infrastructure.table.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FieldEntity {

    private String columnName;
    private String columnType;
    private String collationName;
    private String isNullable;
    private String columnKey;
    private String columnDefault;
    private String extra;
    private String columnComment;

}
