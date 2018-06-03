package com.wishes.yearOld.service.impl;

import com.wishes.yearOld.dao.SysMessageMapper;
import com.wishes.yearOld.model.Reference;
import com.wishes.yearOld.service.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zouyu
 * Date: 2016/11/9
 * Time: 17:18
 * To change this template use File | Settings | File Templates.
 */

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private SysMessageMapper sysMessageMapper;

    @Override
    public List<Reference> selectReferenceByUserId(Integer start, Integer pageSize, Integer userId,Byte status) {
        return sysMessageMapper.selectReferenceByUserId(start,pageSize,userId,status);
    }
}
