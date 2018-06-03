package com.wishes.yearOld.utils;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by tmg-yesky on 2016/10/18.
 */
public class XmlUtil {

    public static String readXml(String path, String nodeName) throws Exception{
        if(nodeName == null ||"".equals(nodeName)) return null;
        //创建SAXReader对象
        SAXReader reader = new SAXReader();
        //读取文件 转换成Document
        Document document = reader.read(new File(path));
        //获取根节点元素对象
        Element root = document.getRootElement();
        //遍历
        return listNodes(root, nodeName);
    }

    public static String readXml(InputStream stream, String nodeName) throws Exception{
        if(nodeName == null ||"".equals(nodeName)) return null;
        //创建SAXReader对象
        SAXReader reader = new SAXReader();
        //读取文件 转换成Document
        Document document = reader.read(stream);
        //获取根节点元素对象
        Element root = document.getRootElement();
        //遍历
        return listNodes(root, nodeName);
    }

    public static String parseXml(String xml, String nodeName){
        if(nodeName == null ||"".equals(nodeName)) return null;
        String result = null;
        try {
            Document document = DocumentHelper.parseText(xml);
            Element root = document.getRootElement();
            result = listNodes(root, nodeName);
        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    public static Map<String,String> parseXml(String xml){
        if(xml == null ||"".equals(xml)) return null;
        Map<String,String> map = new HashMap<>();
        try {
            Document document = DocumentHelper.parseText(xml);
            Element root = document.getRootElement();
            Iterator<Element> iterator = root.elementIterator();
            while(iterator.hasNext()){
                Element e = iterator.next();
                map.put(e.getName(), e.getTextTrim());
            }
        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        }
        return map;
    }

    private static String listNodes(Element node, String nodeName){
        if(nodeName.equals(node.getName())){
            return node.getTextTrim();
        }else{
            String result = null;
            Iterator<Element> iterator = node.elementIterator();
            while(iterator.hasNext()){
                Element e = iterator.next();
                result = listNodes(e, nodeName);
                if(result != null) {
                    return result;
                }
            }
            return null;
        }
    }

    //遍历当前节点下的所有节点
    private static void listNodes(Element node){
        System.out.println("当前节点的名称：" + node.getName());
        //首先获取当前节点的所有属性节点
        List<Attribute> list = node.attributes();
        //遍历属性节点
        for(Attribute attribute : list){
            System.out.println("属性"+attribute.getName() +":" + attribute.getValue());
        }
        //如果当前节点内容不为空，则输出
        if(!(node.getTextTrim().equals(""))){
            System.out.println( node.getName() + "：" + node.getText());
        }
        //同时迭代当前节点下面的所有子节点
        //使用递归
        Iterator<Element> iterator = node.elementIterator();
        while(iterator.hasNext()){
            Element e = iterator.next();
            listNodes(e);
        }
    }

    public static String createXml(Map<String, String> sPara){
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("UTF-8");
        Element root = document.addElement("xml");
        for(Map.Entry<String,String> entry:sPara.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            root.addElement(key).setText(value);
        }
        return document.asXML();
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("total_fee","30");
        map.put("product_id","284");
        String xml = createXml(map);
        System.out.println(xml);
        String total_fee = parseXml(xml, "total_fee");
        System.out.println(total_fee);
        Map<String, String> m = parseXml(xml);
        System.out.println(m);
    }

}
