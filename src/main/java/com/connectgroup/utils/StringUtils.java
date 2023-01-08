package com.connectgroup.utils;

public class StringUtils {
    private StringUtils() {
    }

    public static String[] splitLine(String line){
        if (line == null)throw  new IllegalArgumentException("Argument is null");
        return line.split(",");
    }
}
