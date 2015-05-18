package com.homeforticket.module.login.model;

import com.homeforticket.model.ReturnMessage;

public class LoginMessage extends ReturnMessage {

	private static final long serialVersionUID = 1L;
	private String name; // 用户名
	private String tel; // 用户电话 
	private String photo; // 用户头像地址
	private String bind; // 是否绑定账户
	private String totalMoney; // 总收入
	private String currentMoney; //当前月收入
	private String role;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

    /**
     * @return the wbName
     */
    public String getName() {
        return name;
    }

    /**
     * @param wbName the wbName to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the tel
     */
    public String getTel() {
        return tel;
    }

    /**
     * @param tel the tel to set
     */
    public void setTel(String tel) {
        this.tel = tel;
    }

    /**
     * @return the photo
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * @param photo the photo to set
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * @return the bind
     */
    public String getBind() {
        return bind;
    }

    /**
     * @param bind the bind to set
     */
    public void setBind(String bind) {
        this.bind = bind;
    }

    /**
     * @return the totalMoney
     */
    public String getTotalMoney() {
        return totalMoney;
    }

    /**
     * @param totalMoney the totalMoney to set
     */
    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
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
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

}
