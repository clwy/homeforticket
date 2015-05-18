package com.homeforticket.module.firstpage.model;

import java.io.Serializable;

import com.homeforticket.model.BaseType;

public class ProductChannelInfo implements BaseType, Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String flag;
	
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getFlag() {
        return flag;
    }
    public void setFlag(String flag) {
        this.flag = flag;
    }

}
