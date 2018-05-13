package com.wishes.yearOld.service;

import com.wishes.yearOld.model.*;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * Created by tmg-yesky on 2016/9/19.
 */
public interface PhotoAlbumService {

    List<PhotoAlbum> search(String keyword, Integer modelId, Integer sortType, Integer pageNo, Integer pageSize,User user);

    List<PhotoAlbum> list(PhotoAlbum photoAlbum);

    LinkedList<PhotoAlbum> getHomeAlbums(PhotoAlbum album);
    /**
     * 首页最热列表
     * @param album
     * @return
     */
    LinkedList<PhotoAlbum> getHomeAlbumsHot(PhotoAlbum album);

    /* 随机取一条推荐数据 */
    public PhotoAlbum getHomeAlbumsRand(PhotoAlbum album);
    
    int insertSelective(PhotoAlbum record);
    int updateByPrimaryKeySelective(PhotoAlbum record);

    PhotoAlbum findById(Integer id);

    PhotoAlbum detail(Integer id, User viewUser, boolean updateViewCount);

    Photo photoinfo(Integer albumId, Integer page);

    Object buy(Integer albumId, Integer salesType);

    List<List<AlbumViewLog>> lastestView(Integer userId, Integer pageNo, Integer pageSize);

    Integer selectViewCount(Integer albumId);

    GodViewLikeVO selectIsLikeAlbum(GodViewLikeVO likeVO);

    GodViewLikeVO selectIsCollectionAlbum(GodViewLikeVO likeVO);

    void giveUpAlbum(GodViewLikeVO likeVO);

    void giveDownAlbum(GodViewLikeVO likeVO);

    void collectionDownAlbum(GodViewLikeVO likeVO);

    void collectionUpAlbum(GodViewLikeVO likeVO);

    Integer selectAlbumLikeCount(Integer albumId);

    PhotoAlbum loadPhotoAlbum(Integer id);

    List<Tag> randomTags(Integer start,Integer pageSize);

    void submitAlbumTag(Integer albumId,Integer tagId);

    int updateQDPayForAlbum(User user, PhotoAlbum album);

    int updateDownLimitForAlbum(User user, PhotoAlbum album);

    AlbumPermission calUserAlbumPermission(PhotoAlbum p,Integer userId);

    int saveUserAlbumLog(Integer userId, Integer albumId,String tags,Integer starCount, String userComment);

    Double queryAlbumStar(Integer albumId);

    int queryAlbumStarByUserId(Integer albumId, Integer userId);

    UserAlbumLog queryUserAlbumLog(Integer albumId, Integer userId);
    
    /**
     * 查询用户图集
     * @param userId
     * @return
     */
    ArrayList<Photoalbuminfo> queryBuminfo(Photoalbuminfo photoalbuminfo,Integer start,Integer pageSize);
    /**
     * 查询用户图集下面的图片
     * @param photoalbumId
     * @return
     */
    ArrayList<Piclist> queryPiclist(Integer photoalbumId);
    
    PhotoAlbum queryAlbumByPrimaryId(int albumId,User user ,boolean updateViewCount) throws MalformedURLException, Exception;
    /**
     * 查询用户图集下面所有的图片
     * @param photoalbumId
     * @return
     */
	ArrayList<Piclist> queryPiclistAll(Integer photoalbumId);
}
