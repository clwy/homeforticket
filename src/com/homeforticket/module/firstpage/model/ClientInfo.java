package com.homeforticket.module.firstpage.model;

import java.io.Serializable;

import com.homeforticket.model.BaseType;

public class ClientInfo implements BaseType, Serializable {

    
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String headimg;
	private String tel;
	private String note;
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the headimg
     */
    public String getHeadimg() {
        return headimg;
    }
    /**
     * @param headimg the headimg to set
     */
    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }
    /**
     * @return the tel
     */
    public String getTel() {
        return tel;
    }
    /**
     * @param tel the tel to set
     */
    public void setTel(String tel) {
        this.tel = tel;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }


}
