package com.wishes.yearOld.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zouyu on 2016/9/21.
 * 处理修改字段数据的类型
 */
public class UserClassUtil {

    /**
     * user字段类型为int的List
     */
    static private List intList= new ArrayList<String>();
    static{
        intList.add("user_group");
        intList.add("bust");
        intList.add("waist");
        intList.add("hip");
        intList.add("height");
        intList.add("albums");
        intList.add("fans");
        intList.add("follows");
    }

    public static String getType(String itemName) {

        String item = "String";
        if("weight".equals(itemName)){
            item = "Double";
        }else if(intList.contains(itemName)){
            item = "Integer";
        }else if("birthday".equals(itemName)){
            item = "Date";
        }

        return item;
    }
}
