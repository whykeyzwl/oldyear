package com.wishes.yearOld.service;

import com.wishes.yearOld.model.GodMoments;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/20 0020.
 */
public interface IGodMomentsService {

    public LinkedList<GodMoments> queryGodNess(GodMoments moments);

}
