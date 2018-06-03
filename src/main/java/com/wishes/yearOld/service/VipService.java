package com.wishes.yearOld.service;




import com.wishes.yearOld.model.*;

import java.util.List;

/**
 * Created by tmg-yesky on 2016/9/20.
 */
public interface VipService {

	    int deleteByPrimaryKey(Integer id);

	    int insertSelective(Vip record);

	    Keyword selectByPrimaryKey(Integer id);

	    int updateByPrimaryKeySelective(Vip record);

	    List<Vip> getAllvip();
	    
	    List<Vip> selectVip(Vip record);
}
