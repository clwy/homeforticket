package com.homeforticket.module.message.model;

import java.io.Serializable;

import com.homeforticket.model.BaseType;

public class SystemInfo implements BaseType, Serializable {

    
	private static final long serialVersionUID = 1L;
	private String newsId;
	private String newsDate;
	private String type;
	private String content;
	private String title;
    /**
     * @return the newsId
     */
    public String getNewsId() {
        return newsId;
    }
    /**
     * @param newsId the newsId to set
     */
    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }
    /**
     * @return the newsDate
     */
    public String getNewsDate() {
        return newsDate;
    }
    /**
     * @param newsDate the newsDate to set
     */
    public void setNewsDate(String newsDate) {
        this.newsDate = newsDate;
    }
    /**
     * @return the tyep
     */
    public String getType() {
        return type;
    }
    /**
     * @param tyep the tyep to set
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }
    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
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


}
