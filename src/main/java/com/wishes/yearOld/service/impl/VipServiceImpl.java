package com.wishes.yearOld.service.impl;

import com.wishes.yearOld.common.Constant;
import com.wishes.yearOld.common.StringUtils;
import com.wishes.yearOld.dao.ActivityDao;
import com.wishes.yearOld.dao.IUserMapper;
import com.wishes.yearOld.dao.PhotoAlbumDao;
import com.wishes.yearOld.dao.RecordDownLimitMapper;
import com.wishes.yearOld.dao.VipMapper;
import com.wishes.yearOld.model.*;
import com.wishes.yearOld.service.ActivityService;
import com.wishes.yearOld.service.VipService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tmg-yesky on 2016/9/20.
 */
@Service("vipService")
public class VipServiceImpl implements VipService{

    @Autowired
    VipMapper vipMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		
		return vipMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insertSelective(Vip record) {
		
		return vipMapper.insertSelective(record);
	}

	@Override
	public Keyword selectByPrimaryKey(Integer id) {
		
		return vipMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(Vip record) {
		
		return vipMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<Vip> getAllvip() {
		
		return vipMapper.getAllvip();
	}

	@Override
	public List<Vip> selectVip(Vip record) {
		return vipMapper.selectVip(record);
	}
   
}
