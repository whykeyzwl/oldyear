package com.wishes.yearOld.dao;

import com.wishes.yearOld.model.*;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by tmg-yesky on 2016/9/20.
 */
@Component("activityDao")
public interface ActivityDao {

    public List<Activity> list(Activity activity);

    public Activity detail(@Param("id")Integer activityId);

    public List<ActivityRule> queryRules(ActivityRule activityRule);

    public List<ActivitySupporter> supporters(@Param("support")ActivitySupporter activitySupporter, @Param("sort")Integer sort,@Param("curUserId")Integer curUserId);

    public List<ActivityModel> queryModels(ActivityModel activityModel);

    //public Object suportCallback(Integer activityId, Integer modelId, Integer ruleId);

    public ActivityRule loadActivityRule(Integer id);

    public ActivityModel loadActivityModel(Integer id);

    public ActivitySupporter findActivitySupporterByOrderId(ActivitySupporter activitySupporter);

    public int saveActivitySupporter(ActivitySupporter activitySupporter);

    public List<ActivitySupporter> activitySupporters(@Param("support")ActivitySupporter support,@Param("curUserId")Integer curUserId);

    List<User> queryPhotographer(@Param("photographers")String photographers);

    int updateModelRaised(@Param("raised")BigDecimal raised, @Param("modelId")Integer modelId,@Param("actId")Integer actId);

    int updateActivityRaised(@Param("raised")BigDecimal raised, @Param("id")Integer id);

    Integer getModelCount(@Param("activityId")Integer activityId);

    Integer getAlbumCount(@Param("activityId")Integer activityId,@Param("modelId")Integer modelId);

    Integer getSupporterCount(ActivitySupporter support);

    Integer getSupporterTimes(ActivitySupporter support);

    Activity loadActivity(Integer id);

    Integer getRuleCount(@Param("activityId")Integer activityId);

    List<Activity> queryByModelId(@Param("modelId")Integer modelId,@Param("start")Integer start,@Param("pageSize")Integer pageSize);
}
