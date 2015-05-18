package com.homeforticket.module.firstpage.model;

import com.homeforticket.model.ReturnMessage;

public class WalletMessage extends ReturnMessage {
	private static final long serialVersionUID = 1L;
	private String frzBalance; //冻结余额
	private String avaBalance; // 可用余额
	private String totalWithdraw; //已提金额
	private String preWithdraw; //预提金额
	private String totalSale; //销售总额
	private String totalCheck; //已检金额 
	private String totalRefund; //退票金额
    /**
     * @return the frzBalance
     */
    public String getFrzBalance() {
        return frzBalance;
    }
    /**
     * @param frzBalance the frzBalance to set
     */
    public void setFrzBalance(String frzBalance) {
        this.frzBalance = frzBalance;
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
     * @return the totalWithdraw
     */
    public String getTotalWithdraw() {
        return totalWithdraw;
    }
    /**
     * @param totalWithdraw the totalWithdraw to set
     */
    public void setTotalWithdraw(String totalWithdraw) {
        this.totalWithdraw = totalWithdraw;
    }
    /**
     * @return the preWithdraw
     */
    public String getPreWithdraw() {
        return preWithdraw;
    }
    /**
     * @param preWithdraw the preWithdraw to set
     */
    public void setPreWithdraw(String preWithdraw) {
        this.preWithdraw = preWithdraw;
    }
    /**
     * @return the totalSale
     */
    public String getTotalSale() {
        return totalSale;
    }
    /**
     * @param totalSale the totalSale to set
     */
    public void setTotalSale(String totalSale) {
        this.totalSale = totalSale;
    }
    /**
     * @return the totalCheck
     */
    public String getTotalCheck() {
        return totalCheck;
    }
    /**
     * @param totalCheck the totalCheck to set
     */
    public void setTotalCheck(String totalCheck) {
        this.totalCheck = totalCheck;
    }
    /**
     * @return the totalRefund
     */
    public String getTotalRefund() {
        return totalRefund;
    }
    /**
     * @param totalRefund the totalRefund to set
     */
    public void setTotalRefund(String totalRefund) {
        this.totalRefund = totalRefund;
    }

}
