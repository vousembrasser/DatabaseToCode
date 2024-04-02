package com.dingwd.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
public enum JavaTypeConstant {
    /***
     * 类型常量
     */
    INT("INT", "Integer"),
    SMALLINT("SMALLINT", "Integer"),
    TINYINT("TINYINT", "Integer"),
    BIGINT("BIGINT", "Long"),
    TIMESTAMP("TIMESTAMP", "Long"),
    DECIMAL("DECIMAL", "java.math.BigInteger"),
    VARCHAR("VARCHAR", "String"),
    TEXT("TEXT", "String"),
    DATETIME("DATETIME", "java.time.LocalDateTime"),
    DATE("DATE", "java.time.LocalDateTime"),
    TIME("TIME", "java.time.LocalDateTime"),
    DATETIMEV2("DATETIMEV2","java.time.LocalDateTime");

    final String dbType;
    final String javaType;

    public static String getJavaType(String columnType, String fieldPrecision) {
        JavaTypeConstant type;
        try {
            type = JavaTypeConstant.valueOf(columnType.toUpperCase());
        } catch (Exception e) {
            return "String";
        }
        if (DECIMAL.dbType.equalsIgnoreCase(columnType)) {
            return type.getJavaType();
        } else if (INT.dbType.equalsIgnoreCase(columnType) && Integer.parseInt(fieldPrecision) >= 10) {
            return "Long";
        } else {
            return type.getJavaType();
        }
    }

}

