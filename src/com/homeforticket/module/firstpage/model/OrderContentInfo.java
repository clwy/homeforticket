package com.homeforticket.module.firstpage.model;

import java.io.Serializable;

import com.homeforticket.model.BaseType;

public class OrderContentInfo implements BaseType, Serializable {

    
	private static final long serialVersionUID = 1L;
	private String buyerICard;
	private String buyerMobile;
	private String buyerName;
	private String seat;
    /**
     * @return the buyerICard
     */
    public String getBuyerICard() {
        return buyerICard;
    }
    /**
     * @param buyerICard the buyerICard to set
     */
    public void setBuyerICard(String buyerICard) {
        this.buyerICard = buyerICard;
    }
    /**
     * @return the buyerMobile
     */
    public String getBuyerMobile() {
        return buyerMobile;
    }
    /**
     * @param buyerMobile the buyerMobile to set
     */
    public void setBuyerMobile(String buyerMobile) {
        this.buyerMobile = buyerMobile;
    }
    /**
     * @return the buyerName
     */
    public String getBuyerName() {
        return buyerName;
    }
    /**
     * @param buyerName the buyerName to set
     */
    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }
    /**
     * @return the seat
     */
    public String getSeat() {
        return seat;
    }
    /**
     * @param seat the seat to set
     */
    public void setSeat(String seat) {
        this.seat = seat;
    }
        

}
