package com.wishes.yearOld.common;

import com.wishes.yearOld.model.User;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zouyu on 2016/9/20.
 * 根据不同group返回不同的user
 */

public class GroupUser {

    /*
    * @returns userMap
    */

    public static Map<String,Object> groupToMap(User user) {

        Map<String,Object> userMap = new HashMap<String, Object>();
        if(user!=null) {
            int groupId = user.getGroupId();

            userMap.put("id", user.getId());
            userMap.put("groupId", user.getGroupId());
            userMap.put("loginType", user.getLoginType());
            if(StringUtils.isNotEmpty(user.getMobile()))
                userMap.put("mobile", user.getMobile());
            if(StringUtils.isNotEmpty(user.getEmail()))
                userMap.put("email", user.getEmail());
            if(StringUtils.isNotEmpty(user.getNickName()))
                userMap.put("nickName", user.getNickName());
            if(StringUtils.isNotEmpty(user.getSex()))
                userMap.put("sex", user.getSex());
            if(StringUtils.isNotEmpty(user.getFaceUrl()))
                userMap.put("faceUrl", user.getFaceUrl());
            if(StringUtils.isNotEmpty(user.getProvince()))
                userMap.put("province", user.getProvince());
            if(StringUtils.isNotEmpty(user.getCity()))
                userMap.put("city", user.getCity());
            if(StringUtils.isNotEmpty(user.getEditorCoverUrl()))
                userMap.put("editorCoverUrl", user.getEditorCoverUrl());
            if(StringUtils.isNotEmpty(user.getZhifubao()))
                userMap.put("zhifubao", user.getZhifubao());
            if(StringUtils.isNotEmpty(user.getTimelineCoverUrl()))
                userMap.put("timelineCoverUrl", user.getTimelineCoverUrl());
            userMap.put("qingdou", user.getQingdou());
            userMap.put("fans", user.getFans());
            userMap.put("follows", user.getFollows());
            userMap.put("timeline", user.getTimeline());
            userMap.put("isFocusByCurUser", user.isFocusByCurUser());
            userMap.put("receiverName", user.getReceiverName());
            userMap.put("receiverMobile", user.getReceiverMobile());
            userMap.put("receiverCity", user.getReceiverCity());
            userMap.put("receiverAddress", user.getReceiverAddress());
            userMap.put("receiverPostCode", user.getReceiverPostCode());
            //todo 用户分组
            if (groupId == Constant.USER_GROUP_MODEL) {//模特
                if(user.getBust()!=null)
                    userMap.put("bust", user.getBust());
                if(user.getWaist()!=null)
                    userMap.put("waist", user.getWaist());
                if(user.getHip()!=null)
                    userMap.put("hip", user.getHip());
                userMap.put("albums", user.getAlbums());
                userMap.put("likeCount", user.getLikeCount());
            }
            else if (groupId == Constant.USER_GROUP_PHOTOGRAPHER) {
                userMap.put("albums", user.getAlbums());
            }
        }
        return userMap;
    }
}
