package com.homeforticket.module.firstpage.model;

import java.io.Serializable;

import com.homeforticket.model.BaseType;

public class DistributorInfo implements BaseType, Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String img;
	private String total;
	private String status;
	private String text;
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
     * @return the img
     */
    public String getImg() {
        return img;
    }
    /**
     * @param img the img to set
     */
    public void setImg(String img) {
        this.img = img;
    }
    /**
     * @return the total
     */
    public String getTotal() {
        return total;
    }
    /**
     * @param total the total to set
     */
    public void setTotal(String total) {
        this.total = total;
    }
    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }
    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
    /**
     * @return the text
     */
    public String getText() {
        return text;
    }
    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

}
