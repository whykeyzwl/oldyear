package com.wishes.yearOld.model;

public class TgodPhoto  {
    private Integer id;

    private Integer albumid;

    private Integer page;

    private Integer level;

    private String path;

    private String title;

    private String description;

    private String filesize;

    private Integer height;

    private Integer weight;

    private String previewPath;

    private String bigImage;

    private String exif;

    /**
     * 0-正常 1-未通过审批
     */
    private Byte auditStatus;

    private String auditComment;

    public TgodPhoto(Integer id, Integer albumid, Integer page, Integer level, String path, String title, String description,  Integer height, Integer weight, String bigImage, String exif, Byte auditStatus,String filesize, String auditComment) {
        this.id = id;
        this.albumid = albumid;
        this.page = page;
        this.level = level;
        this.path = path;
        this.title = title;
        this.description = description;
        this.filesize = filesize;
        this.height = height;
        this.weight = weight;
        this.bigImage = bigImage;
        this.exif = exif;
        this.auditStatus = auditStatus;
        this.auditComment = auditComment;
    }

    public TgodPhoto() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAlbumid() {
        return albumid;
    }

    public void setAlbumid(Integer albumid) {
        this.albumid = albumid;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

  

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

 

    public void setPreviewPath(String previewPath) {
        this.previewPath = previewPath;
    }

    public String getBigImage() {
        return bigImage;
    }

    public void setBigImage(String bigImage) {
        this.bigImage = bigImage;
    }

    public String getExif() {
        return exif;
    }

    public void setExif(String exif) {
        this.exif = exif;
    }

    public Byte getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Byte auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getAuditComment() {
        return auditComment;
    }

    public void setAuditComment(String auditComment) {
        this.auditComment = auditComment;
    }
}