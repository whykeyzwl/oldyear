package com.wishes.yearOld.controller;

import com.tmg.utils.StringUtils;
import com.wishes.yearOld.common.DateUtils;
import com.wishes.yearOld.model.*;
import com.wishes.yearOld.service.ActivityService;
import com.wishes.yearOld.service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tmg-yesky on 2016/9/20.
 */
@Controller
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    ActivityService activityService;
    @Autowired
    IUserService userService;

    /**
     * 列举活动
     * 分页:pageNo,pageSize
     * @return
     */
    @RequestMapping("/list.json")
    @ResponseBody
    public Result list(Activity activity){
        List<Activity> list = activityService.list(activity);
        if(list==null || list.size() == 0){
            return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND,"没有活动");
        }
        return Result.BuildSuccessResult(list);
    }

    /**
     * 显示活动详情
     * @param activityId 活动ID
     * @return
     */
    @RequestMapping("/detail.json")
    @ResponseBody
    public Result detail(@RequestParam(value = "activity_id")Integer activityId,String passportId){
        if(activityId == null){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"活动Id不能为空");
        }
        Integer curUserId = null;
        if(StringUtils.isNotEmpty(passportId)){
            // 从缓存读个人信息
            User user = userService.loadUserFromCache(passportId);
            if(user!=null) curUserId = user.getId();
        }
        Activity activity = activityService.detail(activityId,curUserId);
        if(activity==null){
            return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND,"活动不存在");
        }
        return Result.BuildSuccessResult(activity);
    }

    /**
     * 显示模特详情页
     * param activityId 活动ID
     * param modelId 模特ID
     * @return
     */
    @RequestMapping("/model_detail.json")
    @ResponseBody
    public Result modelDetail(ActivityModel model,String passportId){
        if(model.getActivityId() == null){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"活动Id不能为空");
        }
        if(model.getModelId() == null){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"模特Id不能为空");
        }

        Integer curUserId = null;
        if(StringUtils.isNotEmpty(passportId)){
            // 从缓存读个人信息
            User user = userService.loadUserFromCache(passportId);
            if(user!=null) curUserId = user.getId();
        }

        ActivityModel detail = activityService.modelDetail(model,curUserId);
        if(detail==null){
            return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND,"模特不存在");
        }
        return Result.BuildSuccessResult(detail);
    }

    /**
     * 活动规则
     * @param activityId
     * @param modelId 模特id 可选，填入时会返回已支持数
     * @param passportId passportId 可选，填入时会返回登录用户是否在某规则下支持过某个模特
     * @return
     */
    @RequestMapping("/rules.json")
    @ResponseBody
    public Result rules(Integer activityId,Integer modelId,String passportId, Integer pageNo,Integer pageSize){
        if(activityId == null){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"活动Id不能为空");
        }        
        Map<String,Object> map = new HashMap<>();
        //查看活动是否结束
        Activity activity = activityService.loadActivity(activityId);
        boolean isFinish = DateUtils.isAfter(new Date(),activity.getEndTime());
        map.put("isFinish",isFinish);
        ActivityRule ruleVO = new ActivityRule();
        ruleVO.setActivityId(activityId);
        ruleVO.setPageSize(pageSize);
        ruleVO.setPageNo(pageNo);

        Integer curUserId = null;
        if(StringUtils.isNotEmpty(passportId)){
            // 从缓存读个人信息
            User user = userService.loadUserFromCache(passportId);
            if(user!=null) curUserId = user.getId();
        }

        List<ActivityRule> rules = activityService.queryRules(ruleVO,modelId,curUserId);
        for(ActivityRule r:rules){
            r.setFinish(isFinish);
        }
        if(rules==null || rules.size() == 0){
            return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND,"无对应规则");
        }
        map.put("rules",rules);
        return Result.BuildSuccessResult(map);
    }

    /**
     * 旅拍活动的支持者
     * 活动ID activityId
     * @param sort 排序方式 0：时间倒序 1 按支持金额
     * 模特id modelId 可选，不填则返回整个活动的数据
     * @return
     */
    @RequestMapping("/supporters.json")
    @ResponseBody
    public Result supporters(ActivitySupporter activitySupporter, @RequestParam(value = "sort", defaultValue = "0") Integer sort,String passportId){
        if(activitySupporter.getActivityId() == null){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"活动Id不能为空");
        }

        Integer curUserId = null;
        if(StringUtils.isNotEmpty(passportId)){
            // 从缓存读个人信息
            User user = userService.loadUserFromCache(passportId);
            if(user!=null)  curUserId = user.getId();
        }

        List<ActivitySupporter> supporters = activityService.supporters(activitySupporter, sort,curUserId);
        if(supporters==null || supporters.size() == 0){
            return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND,"没有支持者");
        }
        return Result.BuildSuccessResult(supporters);
    }

    /**
     * 按活动的支持者总金额排序
     * @return
     */
    @RequestMapping("/activitySupporters.json")
    @ResponseBody
    public Result activitySupporters(ActivitySupporter support,String passportId){
        if(support == null || support.getActivityId()==null){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"活动Id不能为空");
        }
        Integer curUserId = null;
        if(StringUtils.isNotEmpty(passportId)){
            // 从缓存读个人信息
            User user = userService.loadUserFromCache(passportId);
            if(user!=null) curUserId = user.getId();
        }
        List<ActivitySupporter> supporters = activityService.activitySupporters(support,curUserId);
        if(supporters==null || supporters.size() == 0){
            return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND,"没有支持者");
        }
        return Result.BuildSuccessResult(supporters);
    }

    /**
     * 活动的模特
     * @param activityModel
     * @return
     */
    @RequestMapping("/models.json")
    @ResponseBody
    public Result models(ActivityModel activityModel){
        if(activityModel.getActivityId() == null){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"活动Id不能为空");
        }
        List<ActivityModel> models = activityService.queryModels(activityModel);
        if(models==null || models.size() == 0){
            return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND, "没有相关模特");
        }
        return Result.BuildSuccessResult(models);
    }

    /**
     * 活动相关图集
     * @return
     */
    @RequestMapping("/albums.json")
    @ResponseBody
    public Result albums(Integer activityId,Integer modelId,Integer pageNo,Integer pageSize){
        if(activityId == null){
            return Result.BuildFailResult(ResponseCode.SC_BAD_REQUEST,"活动Id不能为空");
        }
        List<PhotoAlbum> albums = activityService.queryAlbums(activityId,modelId,pageNo,pageSize);
        if(albums==null || albums.size() == 0){
            return Result.BuildFailResult(ResponseCode.SC_NOT_FOUND,"没有图集");
        }
        return Result.BuildSuccessResult(albums);
    }
}
