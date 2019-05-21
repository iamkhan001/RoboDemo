package com.csjbot.blackgaga.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jingwc on 2018/5/14.
 */

public class StrUtil {
    public static final int INDEX_NOT_FOUND = -1;

    private StrUtil() {
    }

    public static String trimAllWhitespace(String str) {
        return str.replaceAll(" ", "").replaceAll("\n", "").replaceAll("\t", "").toString();
    }

    public static boolean isBlank(String str) {
        int strLen;
        if(str != null && (strLen = str.length()) != 0) {
            if("null".equalsIgnoreCase(str)) {
                return true;
            } else {
                for(int i = 0; i < strLen; ++i) {
                    if(!Character.isWhitespace(str.charAt(i))) {
                        return false;
                    }
                }

                return true;
            }
        } else {
            return true;
        }
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static String stripEnd(String str, String stripChars) {
        int end;
        if(str != null && (end = str.length()) != 0) {
            if(stripChars == null) {
                while(end != 0 && Character.isWhitespace(str.charAt(end - 1))) {
                    --end;
                }
            } else {
                if(stripChars.length() == 0) {
                    return str;
                }

                while(end != 0 && stripChars.indexOf(str.charAt(end - 1)) != -1) {
                    --end;
                }
            }

            return str.substring(0, end);
        } else {
            return str;
        }
    }

    public static String null2Zero(String str) {
        return str != null && !str.equals("")?str:"0";
    }

    public static boolean isNumeric(String str) {
        if(isBlank(str)) {
            return false;
        } else {
            Pattern pattern = Pattern.compile("^[0-9]+\\.?[0-9]*$");
            Matcher isNum = pattern.matcher(str);
            return isNum.matches();
        }
    }

    public static String rmStrInNum(String str) {
        String str1 = "";

        for(int i = 0; i < str.length(); ++i) {
            if(Character.isDigit(str.charAt(i))) {
                str1 = str1 + str.charAt(i);
            }
        }

        return str1;
    }

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static int getStrLength(String str, String code) {
        int length = 0;

        try {
            byte[] bt = str.getBytes(code);
            length = bt.length;
        } catch (UnsupportedEncodingException var4) {
            var4.printStackTrace();
        }

        return length;
    }

    public static int getStrLength(String str) {
        return getStrLength(str, "GBK");
    }

    public static int str2Int(String str) {
        int a = 0;
        if(isNumeric(str)) {
            a = Integer.valueOf(str).intValue();
        }

        return a;
    }

    public static long str2Long(String str) {
        long a = 0L;
        if(isNumeric(str)) {
            a = Long.valueOf(str).longValue();
        }

        return a;
    }

    public static float str2Float(String str) {
        float a = 0.0F;
        if(isNumeric(str)) {
            a = (float)Long.valueOf(str).longValue();
        }

        return a;
    }

    public static double str2Double(String str) {
        double a = 0.0D;
        if(isNumeric(str)) {
            a = (double)Long.valueOf(str).longValue();
        }

        return a;
    }

    public static BigDecimal str2BigDecimal(String str) {
        BigDecimal big = new BigDecimal(0);
        if(isNumeric(str)) {
            big = new BigDecimal(str);
        }

        return big;
    }

    public static <T> T checkNotNull(T reference) {
        if(reference == null) {
            throw new NullPointerException();
        } else {
            return reference;
        }
    }

    public static void main(String[] args) {
        System.out.println("==fhb===" + isNumeric("5"));
        System.out.println("==fhb2===" + stripEnd("wqfgdhgkj", "h"));
        System.out.println("==fhb3===" + str2Int("null"));
        System.out.println("==fhb4===" + str2Int(""));
        System.out.println("==fhb5===" + str2Float(""));
        System.out.println("==fhb6===" + str2Long(""));
        System.out.println("==fhb7===" + Integer.parseInt(""));
        System.out.println("==fhb6===" + str2Long(""));
        System.out.println("==isNumeric===" + isNumeric("1231434.") + "");
    }
}
