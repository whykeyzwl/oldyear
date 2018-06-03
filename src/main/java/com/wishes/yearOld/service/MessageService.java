package com.wishes.yearOld.service;

import com.wishes.yearOld.model.Reference;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zouyu
 * Date: 2016/11/9
 * Time: 17:17
 * To change this template use File | Settings | File Templates.
 */
public interface MessageService {

    List<Reference> selectReferenceByUserId(Integer start, Integer pageSize, Integer userId,Byte status);
}
