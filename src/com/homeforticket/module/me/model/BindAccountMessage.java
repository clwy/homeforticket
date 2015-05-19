package com.homeforticket.module.me.model;

import com.homeforticket.model.ReturnMessage;

public class BindAccountMessage extends ReturnMessage {

    private static final long serialVersionUID = 1L;
    private String bname;
    private String bcard;
    /**
     * @return the bname
     */
    public String getBname() {
        return bname;
    }
    /**
     * @param bname the bname to set
     */
    public void setBname(String bname) {
        this.bname = bname;
    }
    /**
     * @return the bcard
     */
    public String getBcard() {
        return bcard;
    }
    /**
     * @param bcard the bcard to set
     */
    public void setBcard(String bcard) {
        this.bcard = bcard;
    }

}
