package com.homeforticket.module.me.model;

import com.homeforticket.model.ReturnMessage;

public class BindAccountMessage extends ReturnMessage {

    private static final long serialVersionUID = 1L;
    private String bname;
    private String bcard;
    private String avaBalance;
    private String time;
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
    /**
     * @return the avaBalance
     */
    public String getAvaBalance() {
        return avaBalance;
    }
    /**
     * @param avaBalance the avaBalance to set
     */
    public void setAvaBalance(String avaBalance) {
        this.avaBalance = avaBalance;
    }
    /**
     * @return the time
     */
    public String getTime() {
        return time;
    }
    /**
     * @param time the time to set
     */
    public void setTime(String time) {
        this.time = time;
    }

}
