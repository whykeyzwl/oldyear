package com.wishes.yearOld.service.impl;

import com.wishes.yearOld.common.StringUtils;
import com.wishes.yearOld.dao.GodMomentsDao;
import com.wishes.yearOld.dao.PhotoAlbumDao;
import com.wishes.yearOld.model.GodMoments;
import com.wishes.yearOld.model.Photo;
import com.wishes.yearOld.model.PhotoAlbum;
import com.wishes.yearOld.service.IGodMomentsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/20 0020.
 */
@Service("godMomentsService")
public class GodMomentsServiceImpl implements IGodMomentsService {

    @Autowired
    private GodMomentsDao godMomentsDao;
    @Autowired
    private PhotoAlbumDao photoAlbumDao;

    public LinkedList<GodMoments> queryGodNess(GodMoments moments){
        LinkedList<GodMoments> mList = godMomentsDao.queryGodNess(moments);
        Integer pageNo = moments.getPageNo();
        GodMoments _god = null;
        if(pageNo <= 1 || pageNo == null){//当是第一页的时候，随机推荐一个女神
            _god = godMomentsDao.queryGodNessRand(moments);
        }
        if(_god != null){
            mList.addFirst(_god);
        }
        for (GodMoments tg : mList){
        	//按级别查询图集
        	tg.setControlLevel(moments.getControlLevel());
            List<PhotoAlbum> phList = godMomentsDao.queryAlbumByGodmom(tg);
            for(PhotoAlbum album : phList){
                if(StringUtils.isTrimEmpty(album.getCover())){
                    List<Photo> photos = photoAlbumDao.photos(album.getId());
                    if(photos != null && photos.size() > 0) {
                        album.setCover(photos.get(0).getFullPath());
                    }
                }
            }
            tg.setAlbumList(phList);
        }
        return mList;
    }
}
