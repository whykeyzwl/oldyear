package com.wishes.yearOld.common;

import java.util.regex.Pattern;

/**
 * Created by zouyu on 2016/9/22.
 * 检验格式工具类
 * 验证用户名,密码,手机号,邮箱,身份证,出生日期...
 */
public class ValidatorUtil {

        /**
         * 正则表达式：验证用户名
         */
        public static final String REGEX_NICKNAME = "^[a-zA-Z]\\w{5,17}$";

        /**
         * 正则表达式：验证密码
         */
        public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$";

        /**
         * 正则表达式：验证手机号
         */
        public static final String REGEX_MOBILE = "^((13[0-9])|(14[5,7,9])|(15[^4,\\D])|(17[0,1,3,5-8])|(18[0-9]))\\d{8}$";

        /**
         * 正则表达式：验证邮箱
         */
        public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

        /**
         * 正则表达式：验证身份证
         */
        public static final String REGEX_ID_NUMBER = "(^\\d{18}$)|(^\\d{15}$)";

        /**
         * 正则表达式：验证出生日期
         */
        public static final String REGEX_BIRTHDAY = "^(\\d{2}|\\d{4})(((0?[13578]|10|12)(0?[1-9]|[12]\\d|3[01]))|((0?[469]|11)(0?[1-9]|[12]\\d|30))|(0?2(0?[1-9]|[12]\\d)))$";



        /**
         * 校验用户名
         *
         * @param nickName
         * @return 校验通过返回true，否则返回false
         */
        public static boolean isUsername(String nickName) {
            return Pattern.matches(REGEX_NICKNAME, nickName);
        }

        /**
         * 校验密码
         *
         * @param password
         * @return 校验通过返回true，否则返回false
         */
        public static boolean isPassword(String password) {
            return Pattern.matches(REGEX_PASSWORD, password);
        }

        /**
         * 校验手机号
         *
         * @param mobile
         * @return 校验通过返回true，否则返回false
         */
        public static boolean isMobile(String mobile) {
            return Pattern.matches(REGEX_MOBILE, mobile);
        }

        /**
         * 校验邮箱
         *
         * @param email
         * @return 校验通过返回true，否则返回false
         */
        public static boolean isEmail(String email) {
            return Pattern.matches(REGEX_EMAIL, email);
        }


        /**
         * 校验身份证
         *
         * @param idNumber
         * @return 校验通过返回true，否则返回false
         */
        public static boolean isIdNumber(String idNumber) {
            return Pattern.matches(REGEX_ID_NUMBER, idNumber);
        }

        /**
         * 校验身份证
         *
         * @param birthday
         * @return 校验通过返回true，否则返回false
         */
        public static boolean isBirthday(String birthday) {
            return Pattern.matches(REGEX_BIRTHDAY,birthday);
        }

}
