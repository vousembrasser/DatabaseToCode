package com.dingwd.infrastructure.utils.stringutils;

import com.google.common.base.Ascii;
import com.google.common.base.CaseFormat;

public class MyStringUtils {

    /**
     * 首字母大写
     */
    public static String upperFirstWord(String word) {
        return Ascii.toUpperCase(word.charAt(0)) + word.substring(1);
    }

    /***
     * 首字母小写
     */
    public static String lowerFirstWord(String word) {
        return Ascii.toLowerCase(word.charAt(0)) + word.substring(1);
    }

    /***
     * 去掉下划线 并用驼峰原则进行转化
     */
    public static String underlineToHump(String columnName) {
        // 下划线转驼峰
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName);
    }

    //驼峰转化 下划线
    public static String humpToUnderline(String columnName) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, columnName);
    }

    public static String humpToSlashes(String columnName) {
        return humpToUnderline(columnName).replaceAll("_", "/");
    }

}