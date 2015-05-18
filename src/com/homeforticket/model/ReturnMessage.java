
package com.homeforticket.model;

import java.io.Serializable;


public class ReturnMessage implements BaseType, Serializable {
    private static final long serialVersionUID = 1L;
    protected String code;
    protected String message;
    protected String token;

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

}
