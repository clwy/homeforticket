package com.homeforticket.module.buyticket.model;

import java.util.ArrayList;
import java.util.List;

import com.homeforticket.model.ReturnMessage;

public class SceneInfo extends ReturnMessage {
	private static final long serialVersionUID = 1L;
	private String sceneId;
	private String sceneName;
	private String sceneAddress;
	private String sceneLevel;
	private String provice;
	private String city;
	private String county;
	private String[] sceneImage;
	private String latitude;
	private String altitude;
	private String sceneDescription;
	private String list;
	
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
     * @return the sceneDescription
     */
    public String getSceneDescription() {
        return sceneDescription;
    }
    /**
     * @param sceneDescription the sceneDescription to set
     */
    public void setSceneDescription(String sceneDescription) {
        this.sceneDescription = sceneDescription;
    }
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
     * @return the sceneImage
     */
    public String[] getSceneImage() {
        return sceneImage;
    }
    /**
     * @param sceneImage the sceneImage to set
     */
    public void setSceneImage(String[] sceneImage) {
        this.sceneImage = sceneImage;
    }

}
