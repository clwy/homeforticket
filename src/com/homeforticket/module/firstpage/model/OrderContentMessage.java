package com.homeforticket.module.firstpage.model;

import java.util.ArrayList;
import java.util.List;

import com.homeforticket.model.ReturnMessage;

public class OrderContentMessage extends ReturnMessage {
	private static final long serialVersionUID = 1L;
	private String list;
	private String createTime;
	private String orderAmount;
	private String orderID;
	private String paidAmount;
	private String refundAmount;
	private String sms;
	private String total_num;
	private String sceneName;
	private String orderState;
	private String startTime;
	private String expTime;
	private String refundTicketType;
	private String notice;
	private String costs;
	private List<OrderContentInfo> infos = new ArrayList<OrderContentInfo>();
	private String city;
	private String sceneAddress;
	private String provice;
	private String county;
    /**
     * @return the list
     */
    public String getList() {
        return list;
    }
    /**
     * @param list the list to set
     */
    public void setList(String list) {
        this.list = list;
    }
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
     * @return the orderAmount
     */
    public String getOrderAmount() {
        return orderAmount;
    }
    /**
     * @param orderAmount the orderAmount to set
     */
    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }
    /**
     * @return the orderID
     */
    public String getOrderID() {
        return orderID;
    }
    /**
     * @param orderID the orderID to set
     */
    public void setOrderID(String orderID) {
        this.orderID = orderID;
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
     * @return the refundAmount
     */
    public String getRefundAmount() {
        return refundAmount;
    }
    /**
     * @param refundAmount the refundAmount to set
     */
    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }
    /**
     * @return the sms
     */
    public String getSms() {
        return sms;
    }
    /**
     * @param sms the sms to set
     */
    public void setSms(String sms) {
        this.sms = sms;
    }
    /**
     * @return the total_num
     */
    public String getTotal_num() {
        return total_num;
    }
    /**
     * @param total_num the total_num to set
     */
    public void setTotal_num(String total_num) {
        this.total_num = total_num;
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
     * @return the startTime
     */
    public String getStartTime() {
        return startTime;
    }
    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    /**
     * @return the expTime
     */
    public String getExpTime() {
        return expTime;
    }
    /**
     * @param expTime the expTime to set
     */
    public void setExpTime(String expTime) {
        this.expTime = expTime;
    }
    /**
     * @return the refundTicketType
     */
    public String getRefundTicketType() {
        return refundTicketType;
    }
    /**
     * @param refundTicketType the refundTicketType to set
     */
    public void setRefundTicketType(String refundTicketType) {
        this.refundTicketType = refundTicketType;
    }
    /**
     * @return the notice
     */
    public String getNotice() {
        return notice;
    }
    /**
     * @param notice the notice to set
     */
    public void setNotice(String notice) {
        this.notice = notice;
    }
    /**
     * @return the costs
     */
    public String getCosts() {
        return costs;
    }
    /**
     * @param costs the costs to set
     */
    public void setCosts(String costs) {
        this.costs = costs;
    }
    /**
     * @return the infos
     */
    public List<OrderContentInfo> getInfos() {
        return infos;
    }
    /**
     * @param infos the infos to set
     */
    public void setInfos(List<OrderContentInfo> infos) {
        this.infos = infos;
    }
    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }
    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }
    /**
     * @return the provice
     */
    public String getProvice() {
        return provice;
    }
    /**
     * @param provice the provice to set
     */
    public void setProvice(String provice) {
        this.provice = provice;
    }
    /**
     * @return the sceneAddress
     */
    public String getSceneAddress() {
        return sceneAddress;
    }
    /**
     * @param sceneAddress the sceneAddress to set
     */
    public void setSceneAddress(String sceneAddress) {
        this.sceneAddress = sceneAddress;
    }
    /**
     * @return the county
     */
    public String getCounty() {
        return county;
    }
    /**
     * @param county the county to set
     */
    public void setCounty(String county) {
        this.county = county;
    }


}
