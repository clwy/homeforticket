
package com.homeforticket.module.buyticket.model;

import com.homeforticket.model.ReturnMessage;

public class ProductInfo extends ReturnMessage {
    private static final long serialVersionUID = 1L;

    private String productId;
    private String productName;
    private String notice; // 产品说明 是
    private String specialNote; // 特别说明 是
    private String trffice; // 交通讯息 是
    private String advanceDay; // 提前天数 是 0表示当天
    private String refundTicketType; // 退票说明 是 0：不可退；1：自动退：2:需景区同意退
    private String saleStartTime; // 使用开始时间 是
    private String saleEndTime; // 使用结束时间 是
    private String price;
    private String markPrice;
    private String retailPrice;
    private String isAuth; // 是否授权 是 0：否1：是
    private String week; // 哪天可买 是 1：可卖；0：表示不可卖；7位数的1、0表示
    private String isUnique; // 是否一票一证 是 0：否 1：是
    private String costs;//兑换方式
    private String priceId;
    private String isPackage;
    private String theatre;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getSpecialNote() {
        return specialNote;
    }

    public void setSpecialNote(String specialNote) {
        this.specialNote = specialNote;
    }

    public String getTrffice() {
        return trffice;
    }

    public void setTrffice(String trffice) {
        this.trffice = trffice;
    }

    public String getAdvanceDay() {
        return advanceDay;
    }

    public void setAdvanceDay(String advanceDay) {
        this.advanceDay = advanceDay;
    }

    public String getRefundTicketType() {
        return refundTicketType;
    }

    public void setRefundTicketType(String refundTicketType) {
        this.refundTicketType = refundTicketType;
    }

    public String getSaleStartTime() {
        return saleStartTime;
    }

    public void setSaleStartTime(String saleStartTime) {
        this.saleStartTime = saleStartTime;
    }

    public String getSaleEndTime() {
        return saleEndTime;
    }

    public void setSaleEndTime(String saleEndTime) {
        this.saleEndTime = saleEndTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMarkPrice() {
        return markPrice;
    }

    public void setMarkPrice(String markPrice) {
        this.markPrice = markPrice;
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(String isAuth) {
        this.isAuth = isAuth;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getIsUnique() {
        return isUnique;
    }

    public void setIsUnique(String isUnique) {
        this.isUnique = isUnique;
    }

    public String getCosts() {
        return costs;
    }

    public void setCosts(String costs) {
        this.costs = costs;
    }

    public String getPriceId() {
        return priceId;
    }

    public void setPriceId(String priceId) {
        this.priceId = priceId;
    }

    public String getIsPackage() {
        return isPackage;
    }

    public void setIsPackage(String isPackage) {
        this.isPackage = isPackage;
    }

    /**
     * @return the theatre
     */
    public String getTheatre() {
        return theatre;
    }

    /**
     * @param theatre the theatre to set
     */
    public void setTheatre(String theatre) {
        this.theatre = theatre;
    }

}
