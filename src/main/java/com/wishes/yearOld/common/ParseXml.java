package com.wishes.yearOld.common;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tmg-yesky on 2016/10/18.
 */
public class ParseXml {

    public static void readXml(String path) throws Exception{
        //创建SAXReader对象
        SAXReader reader = new SAXReader();
        //读取文件 转换成Document
        Document document = reader.read(new File(path));
        //获取根节点元素对象
        Element root = document.getRootElement();
        //遍历
        listNodes(root);
    }

    public static void readXml(InputStream stream) throws Exception{
        //创建SAXReader对象
        SAXReader reader = new SAXReader();
        //读取文件 转换成Document
        Document document = reader.read(stream);
        //获取根节点元素对象
        Element root = document.getRootElement();
        //遍历
        listNodes(root);
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

}
