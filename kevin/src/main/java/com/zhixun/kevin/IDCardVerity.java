package com.zhixun.kevin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ${张奎勋} on 2018/11/20.
 */

public class IDCardVerity {

    public static boolean validateIDCardNumber(String value) {
        try {
            value = value.replaceAll("\\s*", "");
            int len;
            if (null == value) {
                return false;
            } else {
                value = value.toLowerCase();
                len = value.length();
                if (len != 15 && len != 18) {
                    return false;
                }
            }
            // 省份代码
            String[] areasArray = {"11", "12", "13", "14", "15", "21", "22", "23",
                    "31", "32", "33", "34", "35", "36", "37", "41", "42", "43", "44",
                    "45", "46", "50", "51", "52", "53", "54", "61", "62", "63", "64", "65", "71", "81", "82", "91"};

            String valueStart2 = getSubstring(value, 0, 2);
            boolean areaFlag = false; //标识省份代码是否正确
            for (String areaCode : areasArray) {
                if (areaCode.equals(valueStart2)) {
                    areaFlag = true;
                    break;
                }
            }

            if (!areaFlag) {
                return false;
            }

            Pattern pattern;
            Matcher matcher;
            int year;
            //分为15位、18位身份证进行校验
            switch (len) {
                case 15:
                    //获取年份对应的数字
                    year = Integer.valueOf(getSubstring(value, 6, 2)) + 1900;
                    if (year % 4 == 0 || (year % 100 == 0 && year % 4 == 0)) {
                        //创建正则表达式 NSRegularExpressionCaseInsensitive：不区分字母大小写的模式
                        pattern = Pattern.compile("^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$");
                    } else {
                        pattern = Pattern.compile("^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$");
                    }
                    //使用正则表达式匹配字符串 NSMatchingReportProgress:找到最长的匹配字符串后调用block回调
                    //                numberofMatch = [regularExpression numberOfMatchesInString:value options:NSMatchingReportProgress range:NSMakeRange(0, value.length)];
                    matcher = pattern.matcher(value);

                    return matcher.find();
                case 18:
                    year = Integer.valueOf(getSubstring(value, 6, 4));
                    if (year % 4 == 0 || (year % 100 == 0 && year % 4 == 0)) {
                        pattern = Pattern.compile("^((1[1-5])|(2[1-3])|(3[1-7])|(4[1-6])|(5[0-4])|(6[1-5])|71|(8[12])|91)\\d{4}(((19|20)\\d{2}(0[13-9]|1[012])(0[1-9]|[12]\\d|30))|((19|20)\\d{2}(0[13578]|1[02])31)|((19|20)\\d{2}02(0[1-9]|1\\d|2[0-8]))|((19|20)([13579][26]|[2468][048]|0[048])0229))\\d{3}(\\d|X|x)?$");
                    } else {
                        pattern = Pattern.compile("^((1[1-5])|(2[1-3])|(3[1-7])|(4[1-6])|(5[0-4])|(6[1-5])|71|(8[12])|91)\\d{4}(((19|20)\\d{2}(0[13-9]|1[012])(0[1-9]|[12]\\d|30))|((19|20)\\d{2}(0[13578]|1[02])31)|((19|20)\\d{2}02(0[1-9]|1\\d|2[0-8]))|((19|20)([13579][26]|[2468][048]|0[048])0229))\\d{3}(\\d|X|x)?$");

                    }
                    matcher = pattern.matcher(value);

                    if (matcher.find()) {
                        int s = Integer.valueOf(getSubstring(value, 0, 1)) * 7 +
                                Integer.valueOf(getSubstring(value, 10, 1)) * 7 +
                                Integer.valueOf(getSubstring(value, 1, 1)) * 9 +
                                Integer.valueOf(getSubstring(value, 11, 1)) * 9 +
                                Integer.valueOf(getSubstring(value, 2, 1)) * 10 +
                                Integer.valueOf(getSubstring(value, 12, 1)) * 10 +
                                Integer.valueOf(getSubstring(value, 3, 1)) * 5 +
                                Integer.valueOf(getSubstring(value, 13, 1)) * 5 +
                                Integer.valueOf(getSubstring(value, 4, 1)) * 8 +
                                Integer.valueOf(getSubstring(value, 14, 1)) * 8 +
                                Integer.valueOf(getSubstring(value, 5, 1)) * 4 +
                                Integer.valueOf(getSubstring(value, 15, 1)) * 4 +
                                Integer.valueOf(getSubstring(value, 6, 1)) * 2 +
                                Integer.valueOf(getSubstring(value, 16, 1)) * 2 +
                                Integer.valueOf(getSubstring(value, 7, 1)) * 1 +
                                Integer.valueOf(getSubstring(value, 8, 1)) * 6 +
                                Integer.valueOf(getSubstring(value, 9, 1)) * 3;

                        int Y = s % 11;
                        String M;
                        String JYM = "10X98765432";
                        M = getSubstring(JYM, Y, 1);//[JYM substringWithRange:NSMakeRange(Y, 1)];// 3：获取校验位
                        String lastStr = getSubstring(value, 17, 1);//[value substringWithRange:NSMakeRange(17, 1)];
                        //4：检测ID的校验位
                        if (lastStr.equals("x")) {
                            return M.equals("X");
                        } else {
                            //[M isEqualToString:[value substringWithRange:NSMakeRange(17, 1)]]
                            return M.equals(getSubstring(value, 17, 1));

                        }

                    } else {
                        return false;
                    }
                default:
                    return false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String getSubstring(String value, int beginIndex, int endIndex) {
        return value.substring(beginIndex).substring(0, endIndex);
    }
}
