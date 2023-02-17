package io.lh.rpc.commom.utils;

import java.util.stream.IntStream;

/**
 * @author lh
 *
 *
 * The type Serialization util.
 */
public class SerializationUtil {

    private static final String PADDING_STRING = "0";

    /**
     * The constant MAX_SERIALIZATION_TYPE_COUNR.
     * 约定序列化类型最大长度 16
     */
    public static final int MAX_SERIALIZATION_TYPE_COUNR = 16;


    /**
     * Padding string string.
     *
     * @param str the str
     * @return 补0后的字符串
     */
    public static String paddingString(String str) {

        if (str.length() >= MAX_SERIALIZATION_TYPE_COUNR) {
            return str;
        }
        int paddingCount = MAX_SERIALIZATION_TYPE_COUNR - str.length();

        StringBuilder paddingString = new StringBuilder(str);
        IntStream.range(0, paddingCount).forEach((i) -> {
            paddingString.append(PADDING_STRING);
        });
        return paddingString.toString();
    }

    /**
     * Sub string 0 string.
     * 字符串去0操作。
     * @param str the str
     * @return the string
     */
    public static String subString0(String str) {
        str = tranNull2Empty(str);
        return str.replace(PADDING_STRING, "");
    }

    public static String tranNull2Empty(String str) {
        return str == null ? "" : str;
    }




}
