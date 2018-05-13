package com.wishes.yearOld.controller;

import com.tmg.utils.StringUtils;
import com.wishes.yearOld.model.Video;
import com.wishes.yearOld.service.VideoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:    lyra
 * Company: Yesky.com Inc
 * Date:    2017/1/10
 * Description:
 */

@Controller
@RequestMapping("/baidu")
public class BaiduXmlApiController {

    @Autowired
    private VideoService videoService;

    @RequestMapping(value="/videos.xml",produces = "application/xml;charset=UTF-8")
    @ResponseBody
    public String videoXml(){
        StringBuffer xml = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
        xml.append("<document>");
        xml.append("<webSite>www.qingdouke.com</webSite>");
        xml.append("<webMaster>qingdouke@qq.com</webMaster>");
        xml.append("<updatePeri>60</updatePeri>");
        List<Video> list = videoService.getSyncVideo();
        for (Video video : list){
            xml.append("<item>");
            if(video.getStatus()==1)
                xml.append("<op>add</op>");
            else if(video.getStatus()==-1)
                xml.append("<op>del</op>");

            if(StringUtils.isNotEmpty(video.getTitle()))
                xml.append("<title><![CDATA[").append(video.getTitle()).append("]]></title>");
            xml.append("<category><![CDATA[").append("美女和自拍").append("]]></category>");
            if(StringUtils.isNotEmpty(video.getPlayPageUrl()))
                xml.append("<playLink><![CDATA[").append(video.getPlayPageUrl()).append("]]></playLink>");
            if(StringUtils.isNotEmpty(video.getPicUrl()))
                xml.append("<imageLink><![CDATA[").append(video.getPicUrl()).append("]]></imageLink>");
            if(StringUtils.isNotEmpty(video.getPlayUrl()))
                xml.append("<videoLink><![CDATA[").append(video.getPlayUrl()).append("]]></videoLink>");
            xml.append("<userid><![CDATA[").append("青豆客").append("]]></userid>");
            xml.append("<userurl><![CDATA[").append("http://v.baidu.com/i/list?tag_id=21397").append("]]></userurl>");
            xml.append("<playNum><![CDATA[").append(video.getPlayNum()).append("]]></playNum>");
            xml.append("<definition><![CDATA[").append("1920X1080").append("]]></definition>");
            if(StringUtils.isNotEmpty(video.getTag())){
                String[] tags = StringUtils.split(video.getTag(),',');
                if(tags!=null && tags.length>0){
                    for(int i= 0; i<tags.length;i++){
                        xml.append("<tag><![CDATA[").append(tags[i]).append("]]></tag>");
                    }
                }
            }
            if(StringUtils.isNotEmpty(video.getContent()))
                xml.append("<comment><![CDATA[").append(video.getContent()).append("]]></comment>");
            if(video.getCreateTime()!=null) {
                Date displayTime = video.getUpdateTime()==null? video.getCreateTime(): video.getUpdateTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                xml.append("<pubDate>").append(sdf.format(displayTime)).append("</pubDate>");
            }
            xml.append("<duration>").append(video.getDuration()).append("</duration>");
            xml.append("</item>");
        }

        xml.append("</document>");

        return xml.toString();
    }


}
