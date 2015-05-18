package com.homeforticket.module.buyticket.model;

import com.homeforticket.model.ReturnMessage;

public class PayUrlInfo extends ReturnMessage {

	private static final long serialVersionUID = 1L;
	private String url;
    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }
    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

}
