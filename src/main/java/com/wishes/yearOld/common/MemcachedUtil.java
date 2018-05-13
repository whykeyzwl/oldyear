package com.wishes.yearOld.common;

import net.rubyeye.xmemcached.Counter;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

/**
 * Created with IntelliJ IDEA.
 * User: dell-1
 * Date: 16-9-20
 * Time: 上午10:04
 * To change this template use File | Settings | File Templates.
 */

/**
 * Memcached 常用操作
 *
 * @author <a href="mailto:fmlou@163.com">HongzeZhang</a>
 * @version 1.0
 *          <p/>
 *          2009-3-4
 */
public class MemcachedUtil {

    private static Logger logger = Logger.getLogger(MemcachedUtil.class);

    public static MemcachedClient mcc = null;

    static Timer timer = new Timer();

    static {
        init();
    }

    /**
     * 初始化连接池
     */
    public static void init() {
        Properties properties = new Properties();
        try {
            properties.load(MemcachedUtil.class.getClassLoader().getResourceAsStream("memcache.properties"));
            MemcachedClientBuilder builder = new XMemcachedClientBuilder(
                    AddrUtil.getAddresses(properties.getProperty("memcache.serverlist")));
            builder.setConnectionPoolSize(Integer.parseInt(properties.getProperty("memcache.maxConn")));
            builder.setName(properties.getProperty("memcache.poolName"));
            builder.setFailureMode(true);
            builder.setHealSessionInterval(5000);
            mcc = builder.build();
            set("testConnection","connect");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void checkClientConnection(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int j = 0;
                for (int i = 0; i<5 ;i++) {
                    String result = (String) get("testConnection");
                    if(result == null || result.equals("")){
                        j++;
                    }
                }
                if(j >= 3){
                    try {
                        closePool();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    init();
                }
            }
        },0,5000);
    }

    /**
     * 关闭连接池
     */
    public static void closePool() throws IOException {
        mcc.shutdown();
        logger.info("Memcached pool closed");
    }

    /**
     * 设置缓存
     *
     * @param key 键
     * @param obj 值
     */
    public static boolean set(String key, Serializable obj) {
        try {
            return mcc.set(key, 0, obj);
        } catch (Exception e) {
            logger.error("Pool set error!");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 设置缓存
     *
     * @param key  键
     * @param obj  值
     * @param time 缓存时间（毫秒）
     */
    public static boolean set(String key, Serializable obj, long time) {
        try {
            return mcc.set(key,  (int) (time / 1000) ,obj);
        } catch (Exception e) {
            logger.error("Pool set error!");
            e.printStackTrace();
        }
        return false;
    }

    public static void replace(String key, Serializable value, long cachelTime) {
        try {
            mcc.replace(key, (int) (cachelTime / 1000), value);
        } catch (Exception e) {
            logger.error(" pool set error!");
        }
    }

    /**
     * 获取缓存
     *
     * @param key 键
     * @return 值
     */
    public static Object get(String key) {
        Object result = null;
        try {
            result = mcc.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 缓存数值
     *
     * @param key   键
     * @param count 数值
     */
    public static void setCounter(String key, long count) {
        try {
            mcc.set(key, 0, count);
        } catch (Exception e) {
            logger.error("Pool setCounter error!");
        }
    }

    /**
     * 缓存的数值加1
     *
     * @param key 键
     */
    public static void addCounter(String key) {
        try {
            mcc.incr(key, 1);
        } catch (Exception e) {
            logger.error("Pool setCounter error!");
        }
    }

    /**
     * 增加缓存数值
     *
     * @param key      键
     * @param addValue 增加的值
     */
    public static void addCounter(String key, long addValue) {
        try {
            mcc.incr(key, addValue);
        } catch (Exception e) {
            logger.error(" pool setCounter error!");
        }
    }

    /**
     * 获取缓存数值
     *
     * @param key 键
     * @return 值
     */
    public static long getCounter(String key) {
        try {
            Counter counter = mcc.getCounter(key);
            return counter.get() ;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return -1;
    }

    /**
     * 删除缓存
     *
     * @param key 键
     */
    public static boolean delete(String key) {
        try {
            return mcc.delete(key);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return false;
    }

    /**
     * 删除缓存数值
     *
     * @param key 键
     */
    public static long deleteCounter(String key) {
        try {
            return mcc.decr(key, 1);
        } catch (Exception e) {
            logger.error(" pool setCounter error!");
        }
        return 0;
    }

    public static boolean contains(String key){
        //xmemcache中没找到keyExists方法，用这种方式替代。
        try {
            return mcc.get(key) != null;
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MemcachedException e) {
            e.printStackTrace();
        }
        return false;
    }
}