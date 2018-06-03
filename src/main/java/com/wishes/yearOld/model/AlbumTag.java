package com.wishes.yearOld.model;

/**
 * 写真集-标签关联
 * Created by tmg-yesky on 2016/9/20.
 */
public class AlbumTag {

    /**
     * ID
     */
    private Integer id;

    /**
     * 写真集ID
     */
    private Integer albumId;

    /**
     * 标签ID
     */
    private Integer tagId;

    private Integer count;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
