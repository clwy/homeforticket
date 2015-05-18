package com.homeforticket.module.firstpage.model;

import java.io.Serializable;

import com.homeforticket.model.BaseType;

public class RecordInfo implements BaseType, Serializable {

    
	private static final long serialVersionUID = 1L;
	private String date;//发生时间
	private String income;//存入
	private String currentMoney; //交易后余额
	private String title; //流水项目类别
	private String remark; // 摘要
	private String withdraw;//支出
	private String type; //0：收入 1：支出
    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }
    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }
    /**
     * @return the income
     */
    public String getIncome() {
        return income;
    }
    /**
     * @param income the income to set
     */
    public void setIncome(String income) {
        this.income = income;
    }
    /**
     * @return the currentMoney
     */
    public String getCurrentMoney() {
        return currentMoney;
    }
    /**
     * @param currentMoney the currentMoney to set
     */
    public void setCurrentMoney(String currentMoney) {
        this.currentMoney = currentMoney;
    }
    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }
    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
    /**
     * @return the withdraw
     */
    public String getWithdraw() {
        return withdraw;
    }
    /**
     * @param withdraw the withdraw to set
     */
    public void setWithdraw(String withdraw) {
        this.withdraw = withdraw;
    }
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }
    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }


}
