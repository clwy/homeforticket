package com.homeforticket.module.buyticket.model;

import com.homeforticket.model.ReturnMessage;

public class SaveOrderMessage extends ReturnMessage {

	private static final long serialVersionUID = 1L;
	private String orderId;
	private String serialID;
	
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public String getSerialID() {
        return serialID;
    }
    public void setSerialID(String serialID) {
        this.serialID = serialID;
    }

}
