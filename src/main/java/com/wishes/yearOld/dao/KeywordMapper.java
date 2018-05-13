package com.wishes.yearOld.dao;

import com.wishes.yearOld.model.Keyword;

import org.springframework.stereotype.Component;

import java.util.List;

@Component("keywordMapper")
public interface KeywordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Keyword record);

    int insertSelective(Keyword record);

    Keyword selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Keyword record);

    int updateByPrimaryKey(Keyword record);

    List<String> keywords();
}