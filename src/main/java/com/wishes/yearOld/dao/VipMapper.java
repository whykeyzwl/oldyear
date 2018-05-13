package com.wishes.yearOld.dao;

import com.wishes.yearOld.model.Keyword;
import com.wishes.yearOld.model.Vip;

import org.springframework.stereotype.Component;

import java.util.List;

@Component("vipMapper")
public interface VipMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(Vip record);

    Keyword selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Vip record);

    List<Vip> getAllvip();
    
    List<Vip> selectVip(Vip record);
}