package com.wishes.yearOld.service;

import com.wishes.yearOld.model.ApplyCashout;
import com.wishes.yearOld.model.RecordsCashout;

/**
 * Created with IntelliJ IDEA.
 * User: dell-1
 * Date: 16-11-23
 * Time: 上午11:10
 * To change this template use File | Settings | File Templates.
 */
public interface RecordsCashoutService {

    RecordsCashout cashout(RecordsCashout recordsCashout);

    int updateByPrimaryKeySelective(RecordsCashout record);

    int insert(RecordsCashout record);

    RecordsCashout selectByBatchNo(String batchNo);

    boolean checkNotify(String notify_id);

    void updateApply(ApplyCashout vo);
}
