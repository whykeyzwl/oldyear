package com.wishes.yearOld.model;

public class Keyword {
    private Integer id;

    private String keyword;

    private Byte status;

    public Keyword(Integer id, String keyword, Byte status) {
        this.id = id;
        this.keyword = keyword;
        this.status = status;
    }

    public Keyword() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword == null ? null : keyword.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}