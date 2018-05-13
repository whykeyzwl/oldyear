package com.wishes.yearOld.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wishes.yearOld.common.Constant;

/**
 * Created by tmg-yesky on 2016/10/25.
 */
public class Resource {

    private Integer pageNo = Constant.DEF_PAGE_NUM;

    private Integer pageSize = Constant.DEF_PAGE_SIZE;

    private Integer start;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序类型:desc/asc
     */
    private String sortType = "desc";

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    @JsonIgnore
    public Integer getPageNo() {
        if(this.pageNo == null || this.pageNo <= 0){
            this.pageNo = Constant.DEF_PAGE_NUM;
        }
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }
    @JsonIgnore
    public Integer getPageSize() {
        if(this.pageSize == null || this.pageSize <= 0){
            this.pageSize = Constant.DEF_PAGE_SIZE;
        }
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    @JsonIgnore
    public Integer getStart() {
        return (pageNo-1)*pageSize;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

}
