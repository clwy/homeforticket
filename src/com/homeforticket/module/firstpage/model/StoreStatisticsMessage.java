
package com.homeforticket.module.firstpage.model;

import java.util.ArrayList;
import java.util.List;

import com.homeforticket.model.ReturnMessage;

public class StoreStatisticsMessage extends ReturnMessage {

    private static final long serialVersionUID = 1L;
    private String description;
    private String tel;
    private String address;
    private String name;
    private String isassurance; // 是否担保 0：否 1：是
    private String autoClose; // 自动关闭 以小时为单位
    private String autoRecevie; // 自动接收 以天为单位
    private String id;
    private String img;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsassurance() {
        return isassurance;
    }

    public void setIsassurance(String isassurance) {
        this.isassurance = isassurance;
    }

    public String getAutoClose() {
        return autoClose;
    }

    public void setAutoClose(String autoClose) {
        this.autoClose = autoClose;
    }

    public String getAutoRecevie() {
        return autoRecevie;
    }

    public void setAutoRecevie(String autoRecevie) {
        this.autoRecevie = autoRecevie;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

}
