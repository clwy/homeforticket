package com.homeforticket.module.firstpage.model;

import java.io.Serializable;

import com.homeforticket.model.BaseType;

public class ProductInfo implements BaseType, Serializable {

	private static final long serialVersionUID = 1L;
	private String createTime;
	private String orderId;
	private String orderState;
	private String paidAmount;
	private String productName;
	private String totalnum;
	private String sceneName;
	private String price;
	private String pic;
	private String buyName;
	private String contractNote;
    /**
     * @return the createTime
     */
    public String getCreateTime() {
        return createTime;
    }
    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    /**
     * @return the orderId
     */
    public String getOrderId() {
        return orderId;
    }
    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    /**
     * @return the orderState
     */
    public String getOrderState() {
        return orderState;
    }
    /**
     * @param orderState the orderState to set
     */
    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }
    /**
     * @return the paidAmount
     */
    public String getPaidAmount() {
        return paidAmount;
    }
    /**
     * @param paidAmount the paidAmount to set
     */
    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }
    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }
    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }
    /**
     * @return the totalnum
     */
    public String getTotalnum() {
        return totalnum;
    }
    /**
     * @param totalnum the totalnum to set
     */
    public void setTotalnum(String totalnum) {
        this.totalnum = totalnum;
    }
    /**
     * @return the sceneName
     */
    public String getSceneName() {
        return sceneName;
    }
    /**
     * @param sceneName the sceneName to set
     */
    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }
    /**
     * @return the price
     */
    public String getPrice() {
        return price;
    }
    /**
     * @param price the price to set
     */
    public void setPrice(String price) {
        this.price = price;
    }
    /**
     * @return the pic
     */
    public String getPic() {
        return pic;
    }
    /**
     * @param pic the pic to set
     */
    public void setPic(String pic) {
        this.pic = pic;
    }
    /**
     * @return the buyName
     */
    public String getBuyName() {
        return buyName;
    }
    /**
     * @param buyName the buyName to set
     */
    public void setBuyName(String buyName) {
        this.buyName = buyName;
    }
    /**
     * @return the contractNote
     */
    public String getContractNote() {
        return contractNote;
    }
    /**
     * @param contractNote the contractNote to set
     */
    public void setContractNote(String contractNote) {
        this.contractNote = contractNote;
    }
	 
}
