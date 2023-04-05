package com.meowing.loud.arms.utils;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import java.io.UnsupportedEncodingException;

public class StringUtils {

    private static final int BUFFER_SIZE = 8192;
    private static final char[] HEX_DIGITS_UPPER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final char[] HEX_DIGITS_LOWER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public StringUtils() {
    }

    public static boolean contrast(String str1, String str2) {
        if (str1 == null && str2 != null) {
            return false;
        } else if (str1 != null && str2 == null) {
            return false;
        } else if (str1 == null) {
            return true;
        } else {
            return str1.equals(str2);
        }
    }

    public static boolean contrastIgnoreCase(String str1, String str2) {
        if (str1 == null && str2 != null) {
            return false;
        } else if (str1 != null && str2 == null) {
            return false;
        } else if (str1 == null) {
            return true;
        } else {
            return str1.equalsIgnoreCase(str2);
        }
    }

    public static boolean containsIgnoreCase(String original, String str) {
        return !isStringNULL(original) && !isStringNULL(str) ? original.toLowerCase().contains(str.toLowerCase()) : false;
    }

    public static boolean isStringNULL(String str) {
        return str == null || str.equals("") || str.equals("null");
    }

    public static SpannableString getTintString(String originStr, int color, String... keywords) {
        if (originStr == null) {
            return null;
        } else {
            SpannableString ss = new SpannableString(originStr);
            String[] var4 = keywords;
            int var5 = keywords.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                String key = var4[var6];
                int length = key.length();
                int start = originStr.indexOf(key);
                if (start > -1) {
                    ss.setSpan(new ForegroundColorSpan(color), start, start + length, 33);
                }
            }

            return ss;
        }
    }

    public static String getWholeText(String text, int byteCount) {
        try {
            if (text != null && text.getBytes("utf-8").length > byteCount) {
                char[] tempChars = text.toCharArray();
                int sumByte = 0;
                int charIndex = 0;
                int i = 0;

                for (int len = tempChars.length; i < len; ++i) {
                    char itemChar = tempChars[i];
                    if (itemChar >= 0 && itemChar <= 127) {
                        ++sumByte;
                    } else if (itemChar >= 128 && itemChar <= 2047) {
                        sumByte += 2;
                    } else {
                        sumByte += 3;
                    }

                    if (sumByte > byteCount) {
                        charIndex = i;
                        break;
                    }
                }

                return String.valueOf(tempChars, 0, charIndex);
            }
        } catch (UnsupportedEncodingException var8) {
            var8.getStackTrace();
        }

        return text;
    }

    /**
     * Bytes to hex string.
     * <p>e.g. bytes2HexString(new byte[] { 0, (byte) 0xa8 }) returns "00A8"</p>
     *
     * @param bytes The bytes.
     * @return hex string
     */
    public static String bytes2HexString(final byte[] bytes) {
        return bytes2HexString(bytes, true);
    }

    /**
     * Bytes to hex string.
     * <p>e.g. bytes2HexString(new byte[] { 0, (byte) 0xa8 }, true) returns "00A8"</p>
     *
     * @param bytes       The bytes.
     * @param isUpperCase True to use upper case, false otherwise.
     * @return hex string
     */
    public static String bytes2HexString(final byte[] bytes, boolean isUpperCase) {
        if (bytes == null) return "";
        char[] hexDigits = isUpperCase ? HEX_DIGITS_UPPER : HEX_DIGITS_LOWER;
        int len = bytes.length;
        if (len <= 0) return "";
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = hexDigits[bytes[i] >> 4 & 0x0f];
            ret[j++] = hexDigits[bytes[i] & 0x0f];
        }
        return new String(ret);
    }
}
