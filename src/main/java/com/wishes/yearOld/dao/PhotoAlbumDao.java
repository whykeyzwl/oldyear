package com.wishes.yearOld.dao;
import com.wishes.yearOld.model.*;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by tmg-yesky on 2016/9/19.
 */
@Component("photoAlbumDao")
public interface PhotoAlbumDao {

    public List<PhotoAlbum> search(@Param("keyword")String keyword, @Param("modelId")Integer modelId, @Param("sortType")Integer sortType, @Param("start")Integer start, @Param("pageSize")Integer pageSize);

    public PhotoAlbum detail(@Param("id")Integer id);

    public Photo photoinfo(@Param("albumId")Integer albumId, @Param("page")Integer page);

    //public Object buy(Integer albumId, Integer salesType);

    List<PhotoAlbum> queryPhotoAlbums(PhotoAlbum album);

    LinkedList<PhotoAlbum> getHomeAlbums(PhotoAlbum album);
    /**
     * 获取首页最热图
     * @date 2017-05-19 下午2：54
     * @param album
     * @return
     */
    LinkedList<PhotoAlbum> getHomeAlbumsHot(PhotoAlbum album);
    
    int insertSelective(PhotoAlbum record);
    int updateByPrimaryKeySelective(PhotoAlbum record);
    
    /* 随机取一条推荐数据 */
    public PhotoAlbum getHomeAlbumsRand(PhotoAlbum album);

    public Map<String,Object> queryCoverByAlbumId(@Param("albumId")Integer albumId);

    public List<AlbumViewLog> lastestView(@Param("userId")Integer userId, @Param("start")Integer start, @Param("pageSize")Integer pageSize);

    public void updateViewCount(Integer albumId);

    GodViewLikeVO selectIsLikeAlbum(GodViewLikeVO likeVO);

    GodViewLikeVO selectIsCollectionAlbum(GodViewLikeVO likeVO);

    void updateAlbumUp(GodViewLikeVO likeVO);

    void insertViewLike(GodViewLikeVO likeVO);

    void collectionDownAlbum(GodViewLikeVO likeVO);

    void collectionUpAlbum(GodViewLikeVO likeVO);

    Integer selectViewCount(Integer albumId);

    void updateAlbumDown(GodViewLikeVO likeVO);

    void deleteViewLike(GodViewLikeVO likeVO);

    Integer selectAlbumLikeCount(Integer albumId);

    List<Photo> photos(@Param("albumId")Integer albumId);

    List<Tag> tags(@Param("albumId")Integer albumId);

    void insertViewLog(AlbumViewLog log);

    List<PhotoAlbum> queryAlbums(@Param("activityId")Integer activityId,@Param("modelId")Integer modelId,@Param("start")Integer start,@Param("pageSize")Integer pageSize);

    List<Tag> randomTags(@Param("start")Integer start,@Param("pageSize")Integer pageSize);

    List<AlbumTag> queryAlbumTag(@Param("albumId")Integer albumId, @Param("tagId")Integer tagId);
    /**
     * 查询用户图集
     * @param userId
     * @return
     */
    ArrayList<Photoalbuminfo> queryBuminfo(Photoalbuminfo photoalbuminfo);
    /**
     * 查询用户图集下面的图片
     * @param photoalbumId
     * @return
     */
    ArrayList<Piclist> queryPiclist(@Param("photoalbumId")Integer photoalbumId);
    /**
     * 查询用户图集下面的所有图片
     * @param photoalbumId
     * @return
     */
    ArrayList<Piclist> queryPiclistAll(@Param("photoalbumId")Integer photoalbumId);
    
    void incAlbumTagCount(@Param("id")Integer id);

    void insertAlbumTag(AlbumTag albumTag);

    int updateAlbumSales(@Param("id")Integer album);

    Double queryAlbumStar(@Param("albumId")Integer albumId);

    int queryAlbumStarByUserId(@Param("albumId")Integer albumId, @Param("userId")Integer userId);

    int insertTag(Tag tag);

    Tag loadTagByName(@Param("tag_name")String tagName);

    public PhotoAlbum queryAlbumByPrimaryId(@Param("albumId")int albumId,@Param("userID")int userID);

}
