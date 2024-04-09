package com.dingwd.infrastructure.utils.typeturn;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
    DATETIMEV2("DATETIMEV2", "java.time.LocalDateTime");

    final String dbType;
    final String javaType;

    public static String getJavaType(String columnType, String fieldPrecision) {
        if (INT.dbType.equalsIgnoreCase(columnType) && Integer.parseInt(fieldPrecision) >= 10) {
            return "Long";
        }
        JavaTypeConstant type;
        try {
            type = JavaTypeConstant.valueOf(columnType.toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException(String.format("没有找到[%s]对应的类型", columnType));
        }
        return type.getJavaType();
    }

}

