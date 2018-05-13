package com.wishes.yearOld.service.impl;

import com.wishes.yearOld.dao.VideoMapper;
import com.wishes.yearOld.model.Video;
import com.wishes.yearOld.service.VideoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:    lyra
 * Company: Yesky.com Inc
 * Date:    2017/1/10
 * Description:
 */

@Service("videoService")
public class VideoServiceImpl implements VideoService{

    @Autowired
    private VideoMapper videoMapper;

    @Override
    public List<Video> getSyncVideo(){
        return videoMapper.getSyncVideo();
    }
}
