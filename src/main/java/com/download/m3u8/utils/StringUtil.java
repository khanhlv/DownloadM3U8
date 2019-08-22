package com.download.m3u8.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {

    public static String stripAccents(String input) {
        String str = StringUtils.trim(input).toLowerCase();

        str = StringUtils.stripAccents(str);
        str = str.replaceAll("đ", "d");
        str = str.replaceAll("[^\\p{Alpha}\\p{Digit}]+", " ");

        return str;
    }

    public static String stripAccents(String input, CharSequence character) {
        String str = StringUtils.trim(input).toLowerCase();

        str = StringUtils.stripAccents(str);
        str = str.replaceAll("đ", "d");
        str = str.replaceAll("[^\\p{Alpha}\\p{Digit}]+", character.toString());
        str = str.replaceAll("-$", "");
        str = str.replaceAll("^-", "");
        return str;
    }

    public static String stripAccentsNone(String input, CharSequence character) {
        String str = StringUtils.trim(input);
        str = str.replaceAll("\\s+", character.toString());
        str = str.replaceAll("(~|!|@|#|$|%|^|&|\\*|\\(|\\)|\\{|\\}|:|'|\\?|\\]|\\[|\\.|\\,|\\;|-)+", character.toString());
        str = str.replaceAll(character.toString() + "+", character.toString());
        str = str.replaceAll(character.toString() + "$", "");
        str = str.replaceAll("^" + character.toString(), "");
        return str;
    }
}
