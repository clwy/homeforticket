package com.homeforticket.module.me.model;

import com.homeforticket.model.ReturnMessage;

public class UploadImgMessage extends ReturnMessage {

    /** 
     * @Fields serialVersionUID : TODO
     */ 
    private static final long serialVersionUID = 1L;
    
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
