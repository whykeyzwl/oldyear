package com.wishes.yearOld.dao;

import com.wishes.yearOld.model.Reference;
import com.wishes.yearOld.model.SysMessage;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component("sysMessageMapper")
public interface SysMessageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysMessage record);

    int insertSelective(SysMessage record);

    SysMessage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysMessage record);

    int updateByPrimaryKeyWithBLOBs(SysMessage record);

    int updateByPrimaryKey(SysMessage record);

    List<SysMessage> selectSysMessageList(@Param("start")Integer start, @Param("pageSize")Integer pageSize,@Param("userId")Integer userId,@Param("userGroup")Integer userGroup,@Param("currentTime")Date currentTime);

    List<Reference> selectReferenceByUserId(@Param("start")Integer start, @Param("pageSize")Integer pageSize, @Param("userId")Integer userId,@Param("status")Byte status);

    Integer queryNewSysMessageCount(@Param("lastUpdateTime") Date lastUpdateTime,@Param("userId")Integer userId,@Param("userGroup")Integer userGroup,@Param("currentTime")Date currentTime);

    Integer queryNewReferenceCount(@Param("lastUpdateTime") Date lastUpdateTime,@Param("userId")Integer userId);
}