package com.wishes.yearOld.service;

import com.wishes.yearOld.model.Video;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:    lyra
 * Company: Yesky.com Inc
 * Date:    2017/1/10
 * Description:
 */
public interface VideoService {
    List<Video> getSyncVideo();
}
