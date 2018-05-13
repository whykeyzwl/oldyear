package com.wishes.yearOld.model;

/**
 * Created by Administrator on 2017/3/30 0030.
 */
public class PhotoParam {
    private String make;//品牌
    private String model;//型号
    private String focalLength;//焦距
    private String fNumber;//光圈
    private String shutterSpeed;//快门速度
    private String iSOSpeedRatings;//曝光补偿
    private String lensSerial;// 镜头

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFocalLength() {
        return focalLength;
    }

    public void setFocalLength(String focalLength) {
        this.focalLength = focalLength;
    }

    public String getfNumber() {
        return fNumber;
    }

    public void setfNumber(String fNumber) {
        this.fNumber = fNumber;
    }

    public String getShutterSpeed() {
        return shutterSpeed;
    }

    public void setShutterSpeed(String shutterSpeed) {
        this.shutterSpeed = shutterSpeed;
    }

    public String getiSOSpeedRatings() {
        return iSOSpeedRatings;
    }

    public void setiSOSpeedRatings(String iSOSpeedRatings) {
        this.iSOSpeedRatings = iSOSpeedRatings;
    }

    public String getLensSerial() {
        return lensSerial;
    }

    public void setLensSerial(String lensSerial) {
        this.lensSerial = lensSerial;
    }
}
