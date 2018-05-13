package com.wishes.yearOld.model;

/**
 * 写真集标签
 * Created by tmg-yesky on 2016/9/20.
 */
public class Tag {

    /**
     * ID
     */
    private Integer id ;

    /**
     * 名称
     */
    private String tagName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
