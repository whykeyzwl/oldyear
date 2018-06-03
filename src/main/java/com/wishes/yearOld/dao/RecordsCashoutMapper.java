package com.wishes.yearOld.dao;

import com.wishes.yearOld.model.ApplyCashout;
import com.wishes.yearOld.model.RecordsCashout;

import org.springframework.stereotype.Component;

@Component("recordsCashoutMapper")
public interface RecordsCashoutMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RecordsCashout record);

    int insertSelective(RecordsCashout record);

    RecordsCashout selectByPrimaryKey(Integer id);

    RecordsCashout selectByBatchNo(String batchNo);

    int updateByPrimaryKeySelective(RecordsCashout record);

    int updateByPrimaryKey(RecordsCashout record);

    void updateApply(ApplyCashout vo);
}