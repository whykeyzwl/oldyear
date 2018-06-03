package com.wishes.yearOld.common;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by lyra on 2016/9/19.
 * 加密工具类
 */
public class EncryptUtil {

   public static String EncryptBySHA_256(String strSrc) {
        return Encrypt(strSrc, null);
    }

    public static String EncryptByMD5(String strSrc) {
        return Encrypt(strSrc, "MD5");
    }

    /**
     * 对字符串加密,加密算法使用MD5,SHA-1,SHA-256,默认使用SHA-256
     *
     * @param strSrc
     *            要加密的字符串
     * @param encName
     *            加密类型
     * @return
     */
    private static String Encrypt(String strSrc, String encName) {
        MessageDigest md = null;
        String strDes = null;

        byte[] bt = strSrc.getBytes();
        try {
            if (encName == null || encName.equals("")) {
                encName = "SHA-256";
            }
            md = MessageDigest.getInstance(encName);
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }

    /**
     * 验证密码是否正确，true正确;false错误;
     *
     * @param password
     *            密码
     * @param encryptPassword
     *            加密后的密码
     * @return
     */
    public static boolean verifyPassword(String password, String encryptPassword) {
        boolean result = false;
        if (null != password && !"".equals(password)) {
            String encNew = EncryptUtil.EncryptBySHA_256(password);
            result = encNew.equals(encryptPassword);
        }
        return result;
    }


    private static final int SaltLength = 4;

    public static String createDbPassword(String userPassword)
    {
        byte[] unsaltedPassword = hashString(userPassword);

        //Create a salt value
        byte[] saltValue = new byte[SaltLength];
        Random random = new SecureRandom();
        random.nextBytes(saltValue);

        byte[] saltedPassword = createSaltedPassword(saltValue, unsaltedPassword);

        return (new BASE64Encoder()).encode(saltedPassword);
    }

    private static byte[] hashString(String str) {
        try {
            byte[] pwd = str.getBytes("utf-8");

            MessageDigest md = MessageDigest.getInstance("sha-1");
            md.update(pwd);
            return md.digest();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] createSaltedPassword(byte[] saltValue, byte[] unsaltedPassword) {
        // add the salt to the hash
        byte[] rawSalted  = new byte[unsaltedPassword.length + saltValue.length];
        System.arraycopy(unsaltedPassword, 0, rawSalted, 0, unsaltedPassword.length);
        System.arraycopy(saltValue, 0, rawSalted, unsaltedPassword.length, saltValue.length);

        //Create the salted hash
        try {
            MessageDigest md = MessageDigest.getInstance("sha-1");
            md.update(rawSalted);
            byte[] saltedPassword = md.digest();

            // add the salt value to the salted hash
            byte[] dbPassword  = new byte[saltedPassword.length + saltValue.length];
            System.arraycopy(saltedPassword, 0, dbPassword, 0, saltedPassword.length);
            System.arraycopy(saltValue, 0, dbPassword, saltedPassword.length, saltValue.length);

            return dbPassword;
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean comparePasswords(String dbPassword,String userPassword) {
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] dbPwd = decoder.decodeBuffer(dbPassword);
            byte[] hashedPwd = hashString(userPassword);

            if(dbPwd.length ==0 || hashedPwd.length ==0 || dbPwd.length !=hashedPwd.length + SaltLength)
                return false;

            byte[] saltValue = new byte[SaltLength];
            int saltOffset = hashedPwd.length;
            for (int i = 0; i < SaltLength; i++)
                saltValue[i] = dbPwd[saltOffset + i];

            byte[] saltedPassword = createSaltedPassword(saltValue, hashedPwd);

            // compare the values
            return Arrays.equals(dbPwd, saltedPassword);
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

}
