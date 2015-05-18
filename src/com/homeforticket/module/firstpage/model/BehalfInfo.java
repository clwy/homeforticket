package com.homeforticket.module.firstpage.model;

import java.io.Serializable;

import com.homeforticket.model.BaseType;

public class BehalfInfo implements BaseType, Serializable {

	private static final long serialVersionUID = 1L;
	private String productName;
	private String latitude;
	private String altitude;
	private String sceneId;
	private String sceneName;
	private String city;
	private String sceneLevel;
	private String picName;
	private String marketPrice;
	private String retailPrice;
	private String price;
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
     * @return the latitude
     */
    public String getLatitude() {
        return latitude;
    }
    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    /**
     * @return the altitude
     */
    public String getAltitude() {
        return altitude;
    }
    /**
     * @param altitude the altitude to set
     */
    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }
    /**
     * @return the sceneId
     */
    public String getSceneId() {
        return sceneId;
    }
    /**
     * @param sceneId the sceneId to set
     */
    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
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
     * @return the sceneLevel
     */
    public String getSceneLevel() {
        return sceneLevel;
    }
    /**
     * @param sceneLevel the sceneLevel to set
     */
    public void setSceneLevel(String sceneLevel) {
        this.sceneLevel = sceneLevel;
    }
    /**
     * @return the picName
     */
    public String getPicName() {
        return picName;
    }
    /**
     * @param picName the picName to set
     */
    public void setPicName(String picName) {
        this.picName = picName;
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
