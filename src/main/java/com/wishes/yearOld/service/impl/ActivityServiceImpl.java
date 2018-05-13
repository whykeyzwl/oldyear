package com.wishes.yearOld.service.impl;

import com.wishes.yearOld.common.Constant;
import com.wishes.yearOld.common.StringUtils;
import com.wishes.yearOld.dao.ActivityDao;
import com.wishes.yearOld.dao.IUserMapper;
import com.wishes.yearOld.dao.PhotoAlbumDao;
import com.wishes.yearOld.dao.RecordDownLimitMapper;
import com.wishes.yearOld.model.*;
import com.wishes.yearOld.service.ActivityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tmg-yesky on 2016/9/20.
 */
@Service("activityService")
public class ActivityServiceImpl implements ActivityService{

    @Autowired
    ActivityDao activityDao;
    @Autowired
    PhotoAlbumDao photoAlbumDao;
    @Autowired
    RecordDownLimitMapper recordDownLimitMapper;
    @Autowired
    IUserMapper userMapper;

    @Override
    public List<Activity> list(Activity activity) {
        List<Activity> list = activityDao.list(activity);
        for(Activity act: list){
            ActivityModel modelVO = new ActivityModel();
            modelVO.setActivityId(act.getId());
            modelVO.setPageNo(1);
            modelVO.setPageSize(3);
            act.setModels(activityDao.queryModels(modelVO));
            for(ActivityModel am:act.getModels()){
                //查询支持者
                ActivitySupporter vo = new ActivitySupporter();
                vo.setActivityId(act.getId());
                vo.setModelId(am.getModelId());
                am.setSupporterCount(activityDao.getSupporterCount(vo));
            }


        }
        return list;
    }

    @Override
    public Activity detail(Integer activityId,Integer curUserId) {
        Activity activity = activityDao.detail(activityId);

        //查询摄影师
        if(StringUtils.isNotEmpty(activity.getPhotoGrathers())) {
            String[] strIds = StringUtils.split(activity.getPhotoGrathers(),',');
            List<Integer> ids = new ArrayList<Integer>();
            for(String id: strIds){
                if(StringUtils.isNumeric(id))
                    ids.add(Integer.parseInt(id));
            }
            activity.setPhotographer(queryPhotographer(ids));
        }
        //查询图集
        activity.setAlbumCount(activityDao.getAlbumCount(activityId,null));
        if(activity.getAlbumCount()>0)
            activity.setAlbums(queryAlbums(activity.getId(),null,null,null));
        //查询参与女神
//        activity.setModelCount(activityDao.getModelCount(activityId));
//        if(activity.getModelCount()>0) {
//            ActivityModel modelVO = new ActivityModel();
//            modelVO.setActivityId(activity.getId());
//            activity.setModels(activityDao.queryModels(modelVO));
//        }
        //查询支持者
        ActivitySupporter vo = new ActivitySupporter();
        vo.setActivityId(activityId);
        activity.setSupportCount(activityDao.getSupporterCount(vo));
        if(activity.getSupportCount()>0){
            activity.setSupporters(activityDao.activitySupporters(vo,curUserId));
        }
        //查询参与女神
        activity.setModelCount(activityDao.getModelCount(activityId));
        if(activity.getModelCount()>0) {
            ActivityModel modelVO = new ActivityModel();
            modelVO.setActivityId(activity.getId());
            List<ActivityModel> models = activityDao.queryModels(modelVO);
            activity.setModels(models);
            for (ActivityModel m : models){
                //查询支持次数
                vo.setModelId(m.getModelId());
                m.setSupportTimes(activityDao.getSupporterTimes(vo));
            }
        }

        return activity;
    }

    @Override
    public ActivityModel modelDetail(ActivityModel vo,Integer curUserId) {
        List<ActivityModel> models = activityDao.queryModels(vo);
        if(models==null || models.size()==0)
            return null;

        //查询活动
        ActivityModel model = models.get(0);
        model.setActivity(activityDao.loadActivity(model.getActivityId()));

        //查询规则
        ActivityRule ruleVO = new ActivityRule();
        ruleVO.setActivityId(model.getActivityId());
        model.setRuleCount(activityDao.getRuleCount(model.getActivityId()));
        if(model.getRuleCount()>0) {
            List<ActivityRule> rules = queryRules(ruleVO,model.getModelId(),curUserId);
            model.setRules(rules);
        }

        //查询图集
        model.setAlbumCount(activityDao.getAlbumCount(model.getActivityId(),model.getModelId()));
        if(model.getAlbumCount()>0)
            model.setAlbums(queryAlbums(model.getActivityId(),model.getModelId(),null,null));


        ActivitySupporter supportVO = new ActivitySupporter();
        supportVO.setActivityId(model.getActivityId());
        supportVO.setModelId(model.getModelId());
        //查询支持次数
        model.setSupportTimes(activityDao.getSupporterTimes(supportVO));
        if(model.getSupportTimes()>0) {
            model.setSupportTimeList(activityDao.supporters(supportVO,0,curUserId));//按支持时间倒序
        }

        //查询支持者排行
        model.setSupporterCount(activityDao.getSupporterCount(supportVO));
        if(model.getSupporterCount()>0){
            model.setSupporterList(activityDao.activitySupporters(supportVO,curUserId));
        }
        //查询是否被关注
        model.setFocusByCurUser(userMapper.getFocusUserCount(curUserId,model.getModelId())>0);

        return model;
    }

    @Override
    public List<ActivityRule> queryRules(ActivityRule ruleVO,Integer modelId,Integer curUserId) {
        List<ActivityRule> rules =activityDao.queryRules(ruleVO);
        if(modelId!=null && rules!=null) {
            for (ActivityRule r : rules) {
                ActivitySupporter supportVO = new ActivitySupporter();
                supportVO.setRuleId(r.getId());
                supportVO.setModelId(modelId);
                supportVO.setActivityId(ruleVO.getActivityId());
                r.setSupportTimes(activityDao.getSupporterTimes(supportVO));
                r.setHasSupported(false);
                if(curUserId!=null && !r.isRandomFeeSupport()){
                    supportVO.setUserId(curUserId);
                    int count = activityDao.getSupporterTimes(supportVO);
                    if(count>0)
                        r.setHasSupported(true);
                }
            }
        }
        return rules;
    }

    @Override
    public List<ActivitySupporter> supporters(ActivitySupporter activitySupporter,Integer sort,Integer  curUserId) {
        return activityDao.supporters(activitySupporter, sort, curUserId);
    }

    @Override
    public List<ActivityModel> queryModels(ActivityModel activityModel) {
        ActivitySupporter vo = new ActivitySupporter();
        List<ActivityModel> activityModel_ = activityDao.queryModels(activityModel);
        for(ActivityModel a:activityModel_){
            vo.setActivityId(a.getActivityId());
            vo.setModelId(a.getModelId());
            a.setSupporters(activityDao.getSupporterCount(vo));
        }

        return activityModel_;
    }

    @Override
    public Object suportCallback(Integer activityId, Integer modelId, Integer ruleId) {
        //todo
        return null;
    }
    @Override
    public Activity loadActivity(Integer id) {
        return activityDao.loadActivity(id);
    }

    @Override
    public ActivityRule loadActivityRule(Integer id) {
        return activityDao.loadActivityRule(id);
    }

    @Override
    public ActivityModel loadActivityModel(Integer id) {
        return activityDao.loadActivityModel(id);
    }

    @Override
    public int countSupporterTimes(Integer activityid,Integer ruleid, Integer modelId) {
        ActivitySupporter vo = new ActivitySupporter();
        vo.setRuleId(ruleid);
        vo.setModelId(modelId);
        vo.setActivityId(activityid);
        return activityDao.getSupporterTimes(vo);
    }

    @Override
    public ActivitySupporter findActivitySupporterByOrderId(ActivitySupporter activitySupporter) {
        return activityDao.findActivitySupporterByOrderId(activitySupporter);
    }

    @Override
    public int saveActivitySupporter(ActivitySupporter activitySupporter) {
        return activityDao.saveActivitySupporter(activitySupporter);
    }

    @Override
    public List<ActivitySupporter> activitySupporters(ActivitySupporter support,Integer curUserId) {
        return activityDao.activitySupporters(support,curUserId);
    }

    @Override
    public List<PhotoAlbum> queryAlbums(Integer activityId,Integer modelId, Integer pageNo,Integer pageSize){
        if(pageNo==null)
            pageNo = Constant.DEF_PAGE_NUM;
        if(pageSize==null)
            pageSize = Constant.DEF_PAGE_SIZE;
        List<PhotoAlbum> list = photoAlbumDao.queryAlbums(activityId, modelId, (pageNo-1)*pageSize, pageSize);
        for(PhotoAlbum album: list){
            //如果封面url为空，则选第一张图为封面
            if(StringUtils.isEmpty(album.getCoverUrl())) {
                Photo photo = photoAlbumDao.photoinfo(album.getId(),0);
                if(photo!=null)
                    album.setCover(photo.getFullPath());
            }
        }

        return list;
    }

    @Override
    public List<User> queryPhotographer(List<Integer> photographers){
        if(photographers!=null && photographers.size()>0)
            return activityDao.queryPhotographer(StringUtils.join(photographers,','));

        return null;
    }

    @Override
    public void createDownLimitForSupporter(ActivitySupporter activitySupporter) {
        RecordDownLimit record = new RecordDownLimit();
        record.setStatus(Constant.DOWN_LIMIT_UNUSED);
        record.setModelId(activitySupporter.getModelId());
        record.setActivityId(activitySupporter.getActivityId());
        record.setUserId(activitySupporter.getUserId());
        record.setCreateTime(new Date());
        record.setRuleId(activitySupporter.getRuleId());
        recordDownLimitMapper.insertSelective(record);
    }

    public int updateAsSupportSuccess(ActivitySupporter activitySupporter){
        saveActivitySupporter(activitySupporter);
        //Add by lyra: 活动支付成功后添加下载权限记录
        ActivityRule rule = loadActivityRule(activitySupporter.getRuleId());
        if(rule!=null && rule.getDownloadLimit()>0){
            for(int i=0;i<rule.getDownloadLimit();i++)
                createDownLimitForSupporter(activitySupporter);
        }
        //Add by lyra: 更新模特和活动的筹集金额
        activityDao.updateActivityRaised(activitySupporter.getMoney(),activitySupporter.getActivityId());
        activityDao.updateModelRaised(activitySupporter.getMoney(),activitySupporter.getModelId(),activitySupporter.getActivityId());
        return 1;
    }

    @Override
    public List<Activity> queryByModelId(Integer modelId, Integer pageNo, Integer pageSize){
        if(pageNo==null)
            pageNo = Constant.DEF_PAGE_NUM;
        if(pageSize==null)
            pageSize = Constant.DEF_PAGE_SIZE;

        List<Activity> list = activityDao.queryByModelId(modelId,(pageNo-1)*pageSize,pageSize);
        for(Activity act: list){
            ActivityModel modelVO = new ActivityModel();
            modelVO.setActivityId(act.getId());
            modelVO.setModelId(modelId);
            modelVO.setPageNo(Constant.DEF_PAGE_NUM);
            modelVO.setPageSize(Constant.DEF_PAGE_SIZE);
            act.setModels(activityDao.queryModels(modelVO));
            ActivitySupporter vo = new ActivitySupporter();
            vo.setActivityId(act.getId());
            vo.setModelId(modelId);
            act.setSupportCount(activityDao.getSupporterCount(vo));
        }
        return list;
    }
}
