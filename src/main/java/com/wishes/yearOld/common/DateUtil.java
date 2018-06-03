package com.wishes.yearOld.common;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.ibm.icu.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: dell-1
 * Date: 16-9-20
 * Time: 下午8:14
 * To change this template use File | Settings | File Templates.
 */
public class DateUtil {
    /**
     * 获取当天开始时间
     * @return
     */
    public static Date getCurrentDateBegin() {
        Calendar currentDate = new GregorianCalendar();
        currentDate.setTime(new Date());
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        Date date = currentDate.getTime();
        return date;
    }
    /**
     * 获取当前时间
     * @return
     */
    public static String getCurrentDate() {
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    	String dfDate = df.format(new Date());// new Date()为获取当前系统时间
        return dfDate;
    }
    /**
     * 获取昨天开始时间
     * @return
     */
    public static Date getYesterDayBegin() {
        Calendar currentDate = new GregorianCalendar();
        currentDate.setTime(new Date());
        currentDate.add(Calendar.DATE,-1);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        Date date = currentDate.getTime();
        return date;
    }

    /**
     * 计算两个时间差
     * @param start 开始时间
     * @param end   结束时间
     * @return
     */
    public static String getTimeDifference(Date start, Date end) {
        if (start == null || end == null) {
            return null;
        }
        if (start.getTime() > end.getTime()) {
            return null;
        }
        long nd = 1000 * 24 * 60 * 60;//一天的毫秒数
        long nh = 1000 * 60 * 60;//一小时的毫秒数
        long nm = 1000 * 60;//一分钟的毫秒数
        long ns = 1000;//一秒钟的毫秒数
        long diff;//获得两个时间的毫秒时间差异
        diff = end.getTime() - start.getTime();
        long day = diff / nd;//计算差多少天
        long hour = diff % nd / nh;//计算差多少小时
        long min = diff % nd % nh / nm;//计算差多少分钟
        long sec = diff % nd % nh % nm / ns;//计算差多少秒//输出结果
        return day + "天" + hour + "小时" + min + "分钟" + sec + "秒";
    }
}
