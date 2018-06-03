package com.wishes.yearOld.model;

import com.wishes.yearOld.common.Constant;

/**
 * Created by zouyu on 2016/9/20.
 *
 * 搜索用户条件实体类
 *
 */

public class UserCondition {
    private String keyword;
    private Integer group;
    private String sort;

    private int pageNo = Constant.DEF_PAGE_NUM;//当前页
    private int pageSize = Constant.DEF_PAGE_SIZE;//页大小
    private int rowCount;// 记录个数
    private int pageCount;// 页数
    private int start;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

}
