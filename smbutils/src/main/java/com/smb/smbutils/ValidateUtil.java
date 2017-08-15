package com.smb.smbutils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则校验类
 * Created by mtf on 2017/6/7.
 */
public class ValidateUtil {

    /**
     * @param name
     * @return 名字必须是中文, 支持少数民族的人名, 或者外国人的中译名, 例如：阿沛·阿旺晋美、卡尔·马克思
     */
    public static boolean isName(String name) {
        boolean flag = false;
        try {
            Pattern p = Pattern.compile("[\u4E00-\u9FA5]{2,5}(?:·[\u4E00-\u9FA5]{2,5})*");
            Matcher m = p.matcher(name);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 判断输入是否为数字
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断手机号是否合法
     */
    public static boolean isPhone(String mobiles) {
        //Pattern p = Pattern.compile("^((13[0-9])|(17[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Pattern p = Pattern.compile("1[0-9]{10}");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 判断是否含有中文
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否为纯英文
     */
    public static boolean isLetter(String str) {
        Pattern p = Pattern.compile("^[A-Za-z]+$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 判断必须是否为密码类型
     */
    public static boolean isPassword(String str) {
        // Pattern p =
        // Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$");
        Pattern p = Pattern
                .compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z.*]{6,20}$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 判断邮件email是否正确格式
     */
    public static boolean isEmail(String email) {
        if (null == email || "".equals(email))
            return false;
        // Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p = Pattern
                .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");// 复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 用于判断用户长度6-20位
     *
     * @param str
     * @return
     */
    public static boolean isLength(String str) {
        Pattern pattern = Pattern.compile(".{6,20}");
        return pattern.matcher(str).matches();
    }

    /**
     * 中文占两个字符，英文占一个
     */
    public static int String_length(String value) {
        int valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength += 1;
            }
        }
        return valueLength;
    }

    /**
     * 过滤特殊字符
     */
    public static String stringFilter(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }

}
