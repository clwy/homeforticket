package com.homeforticket.module.message.model;

import java.io.Serializable;

import com.homeforticket.model.BaseType;

public class MessageInfo implements BaseType, Serializable {

	private static final long serialVersionUID = 1L;
	private String code; //
	private String message; //
	private String wbName; // 用户名
	private String tel; // 用户电话
	private String photo; // 用户头像地址
	private String token;// 用户身份验证码

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
    public String getWbName() {
        return wbName;
    }

    /**
     * @param wbName the wbName to set
     */
    public void setWbName(String wbName) {
        this.wbName = wbName;
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

}
