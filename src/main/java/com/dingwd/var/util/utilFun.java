package com.dingwd.var.util;

import com.dingwd.utils.MyStringUtils;

public class utilFun {

    public static String humpTo(String var, String to) {
        return MyStringUtils.humpToUnderline(var).replaceAll("_", to);
    }

    public static String lowerFirstWord(String var) {
        return MyStringUtils.lowerFirstWord(var);
    }

    public static String subString(String var, String beginIndex) {
        return var.substring(Integer.parseInt(beginIndex));
    }

    public static String subString(String var, String beginIndex, String endIndex) {
        return var.substring(Integer.parseInt(beginIndex), Integer.parseInt(endIndex));
    }
}
