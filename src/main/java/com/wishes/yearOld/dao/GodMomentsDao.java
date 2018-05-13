package com.wishes.yearOld.dao;

import com.wishes.yearOld.model.GodMoments;
import com.wishes.yearOld.model.PhotoAlbum;

import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/20 0020.
 */
@Component("godMomentsDao")
public interface GodMomentsDao {
    public List<PhotoAlbum> queryAlbumByModuleID(int modelID);
    
    /**
     * 按级别查询图集相册
     * @Date 2017-05-12
     * @param moments
     * @return图集PhotoAlbum
     */
    
    public LinkedList<PhotoAlbum> queryAlbumByGodmom(GodMoments moments);

    public LinkedList<GodMoments> queryGodNess(GodMoments moments);

    public GodMoments queryGodNessRand(GodMoments moments);
}
