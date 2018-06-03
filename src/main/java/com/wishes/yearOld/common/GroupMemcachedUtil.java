package com.wishes.yearOld.common;


import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User:    garmbrood
 * Company: TianJi Media Group
 * DateTime:2009-4-25 19:40:09
 * Description: memcache的分组实现，原理如下：
 *    保存时将　组名＋分隔符＋key 作为ｋｅｙ保存，然后查找是否存在以组名为key的hashset，没有则创建，将key加入hashset后保存到cache中。
 *    读取时则根据 组名＋分隔符＋key规则 读取对象，单条删除同理
 *    批量删除时，先从cache中读取key为groupName的hashset，然后逐条删除。
 */
public class GroupMemcachedUtil {

    public static final String GROUP_PREFIX = "@group_";

    public static final String SPLIT_CHAR = "_";

    /**
     *  获取groupName组中key对应的对象
     * @param groupName 组名
     * @param key 键
     * @return 缓存的对象
     */
    public static Serializable get(String groupName,String key) {
        return (Serializable) MemcachedUtil.get(new StringBuilder().append(groupName).append(SPLIT_CHAR).append(key).toString());
    }

    /**
     * 删除groupName组中的所有对象
     * @param groupName 组名
     */
    @Deprecated
    public static void remove(String groupName) {
		/*HashSet<String> group = (HashSet<String>) MemcachedUtil.get(GROUP_PREFIX + groupName);
		if (group != null && !group.isEmpty()) {
			MemcachedUtil.set(GROUP_PREFIX + groupName, new HashSet<String>());
			for (String key : group) {
				MemcachedUtil.delete(new StringBuilder().append(groupName).append(SPLIT_CHAR).append(key).toString());
			}
		}*/
    }

    /**
     * 删除groupName中的key对应的对象
     * @param groupName 组名
     * @param key 键
     */
    public  static void remove(String groupName, String key) {
       /* HashSet<String> group = (HashSet<String>) MemcachedUtil.get(GROUP_PREFIX + groupName);
        group.remove(key);
		if (!StringUtils.isBlank(groupName)) {
			MemcachedUtil.set(GROUP_PREFIX + groupName, group);
		    MemcachedUtil.delete(new StringBuilder().append(groupName).append(SPLIT_CHAR).append(key).toString());
		}*/
        MemcachedUtil.delete(new StringBuilder().append(groupName).append(SPLIT_CHAR).append(key).toString());
    }

    /**
     *  将value对象以key缓存到groupName组中，过期时间为time
     * @param groupName 组名
     * @param key 键
     * @param value 缓存对象
     * @param time 过期时间 单位：毫秒
     */
    public static  void store(String groupName, String key, Serializable value, long time) {
        //HashSet<String> group = (HashSet<String>) MemcachedUtil.get(GROUP_PREFIX + groupName);
		/*if (group == null)
			group = new HashSet<String>();
		group.add(key);
		MemcachedUtil.set(GROUP_PREFIX + groupName, group);*/
        MemcachedUtil.set(new StringBuilder().append(groupName).append(SPLIT_CHAR).append(key).toString(),
                value, time);
    }

    /**
     * 将value对象以key缓存到groupName组中，只有主动删除该对象才会导致该缓存过期
     * @param groupName  组名
     * @param key 键
     * @param value 缓存对象
     */
    public static  void store(String groupName,String key,Serializable value){
        store(groupName,key,value,-1);
    }

    public static boolean contains(String groupName,String key){
        return MemcachedUtil.contains(new StringBuilder().append(groupName).append(SPLIT_CHAR).append(key).toString());
    }

    public static void main(String[] args) {
        //GroupMemcachedUtil.store("groupName","name","garmbrood",600000);
        String name = (String ) GroupMemcachedUtil.get("groupName", "name");
        System.out.println("name = " + name);
    }
}