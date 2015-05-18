package com.homeforticket.module.buyticket.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.homeforticket.model.BaseType;
import com.homeforticket.model.ReturnMessage;

public class SceneTicketInfo implements BaseType, Serializable {
	private static final long serialVersionUID = 1L;
	private String productId;
	private String productName;
	private String retailPrice;
	private String marketPrice;
	private String price;
	
    /**
     * @return the productId
     */
    public String getProductId() {
        return productId;
    }
    /**
     * @param productId the productId to set
     */
    public void setProductId(String productId) {
        this.productId = productId;
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
     * @return the retailPrice
     */
    public String getRetailPrice() {
        return retailPrice;
    }
    /**
     * @param retailPrice the retailPrice to set
     */
    public void setRetailPrice(String retailPrice) {
        this.retailPrice = retailPrice;
    }
    /**
     * @return the marketPrice
     */
    public String getMarketPrice() {
        return marketPrice;
    }
    /**
     * @param marketPrice the marketPrice to set
     */
    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
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




}
