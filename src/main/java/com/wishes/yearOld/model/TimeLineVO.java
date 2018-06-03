package com.wishes.yearOld.model;

/**
 * Created by IntelliJ IDEA.
 * User: zouyu
 * Date: 2016/10/25
 * Time: 20:46
 * To change this template use File | Settings | File Templates.
 */

import com.wishes.yearOld.common.Constant;

import java.util.Date;
import java.util.List;

/**
 * 用作返回个人动态列表信息的实体类
 */
public class TimeLineVO {

    private Integer id;//个人动态id
    private Integer userId;//查询用户id
    private String nickname;//用户昵称
    private String face;//头像
    private Byte contentType;//个人动态id
    private String content;//文章
    private List<String> photos;
    private Integer albumId;
    private PhotoAlbum album;
    private Date createdTime;
    private Integer likeCount;
    private Integer commentCount;
    private List<User> likeUsers;
    private List<Comment> comments;
    private boolean likedByCurUser;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Byte getContentType() {
        return contentType;
    }

    public void setContentType(Byte contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    public PhotoAlbum getAlbum() {
        return album;
    }

    public void setAlbum(PhotoAlbum album) {
        this.album = album;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public List<User> getLikeUsers() {
        return likeUsers;
    }

    public void setLikeUsers(List<User> likeUsers) {
        this.likeUsers = likeUsers;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }


    public boolean isLikedByCurUser() {
        return likedByCurUser;
    }

    public void setLikedByCurUser(boolean likedByCurUser) {
        this.likedByCurUser = likedByCurUser;
    }

    public String getFaceUrl() {
        if(face!=null && !face.isEmpty() && !face.startsWith("http://"))
            return Constant.IMAGE_BASE+face;
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }
}

