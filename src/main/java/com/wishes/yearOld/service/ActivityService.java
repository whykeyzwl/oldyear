package com.wishes.yearOld.service;

import com.wishes.yearOld.model.*;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by tmg-yesky on 2016/9/20.
 */
public interface ActivityService {

    public List<Activity> list(Activity activity);

    public Activity detail(Integer activityID,Integer curUserId);

    ActivityModel modelDetail(ActivityModel model,Integer curUserId);

    public List<ActivityRule> queryRules(ActivityRule activityRule,Integer modelId,Integer curUserId);

    public List<ActivitySupporter> supporters(ActivitySupporter activitySupporter , Integer sort,Integer curUserId);

    public List<ActivityModel> queryModels(ActivityModel activityModel);

    public Object suportCallback(Integer activityId, Integer modelId, Integer ruleId);

    public ActivityRule loadActivityRule(Integer id);

    Activity loadActivity(Integer id);

    public ActivityModel loadActivityModel(Integer id);

    public int countSupporterTimes(Integer activityid,Integer ruleid, Integer modelId);

    public ActivitySupporter findActivitySupporterByOrderId(ActivitySupporter activitySupporter);

    public int saveActivitySupporter(ActivitySupporter activitySupporter);

    public List<ActivitySupporter> activitySupporters(ActivitySupporter support,Integer curUserId);

    List<PhotoAlbum> queryAlbums(Integer activityId,Integer modelId,Integer pageNo,Integer pageSize);

    List<User> queryPhotographer(List<Integer> photographers);

    void createDownLimitForSupporter(ActivitySupporter activitySupporter);

    int updateAsSupportSuccess(ActivitySupporter activitySupporter);

    List<Activity> queryByModelId(Integer modelId, Integer pageNo, Integer pageSize);
}
