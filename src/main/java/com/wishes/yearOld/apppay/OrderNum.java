package com.wishes.yearOld.apppay;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by tmg-yesky on 2016/10/20.
 */
public class OrderNum {

    public static final SimpleDateFormat dformat=new SimpleDateFormat("yyyyMMddHHmmssSSS");
    public static final DecimalFormat nformat = new DecimalFormat("000");

    /**
     * 返回"22"+系统当前时间(精确到毫秒)+三位随机数,作为一个唯一的订单编号
     * @return
     */
    public  static String getOrderNum(){
        return "22"+getDate() + getThree();
    }

    /**
     * 返回"23"+系统当前时间(精确到毫秒)+三位随机数,作为一个唯一的转账编号
     * @return
     */
    public static String getBatchNum() {
        return "23"+getDate() + getThree();
    }

    /**
     * 返回"24"+系统当前时间(精确到毫秒)+三位随机数,作为一个唯一的转账用户流水号
     * @return
     */
    public static String getBatchUserNum() {
        return "24"+getDate() + getThree();
    }

    /**
     * 获取系统当期年月日(精确到毫秒)
     * @return
     */
    private static String getDate(){
        Date date=new Date();
        return dformat.format(date);
    }

    /**
     * 产生随机的三位数
     * @return
     */
    private static String getThree(){
        Random rad=new Random();
        return nformat.format(rad.nextInt(1000));
    }

}
