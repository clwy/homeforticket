package com.homeforticket.module.message.model;

import java.util.ArrayList;
import java.util.List;

import com.homeforticket.model.ReturnMessage;

public class SystemInfoMessage extends ReturnMessage {

	private static final long serialVersionUID = 1L;
	private String count;
	private String currentPage;
	private String list;
	private String pageSize;
	private String unRead; 
	private List<SystemInfo> systemInfos = new ArrayList<SystemInfo>();
    /**
     * @return the count
     */
    public String getCount() {
        return count;
    }
    /**
     * @param count the count to set
     */
    public void setCount(String count) {
        this.count = count;
    }
    /**
     * @return the currentPage
     */
    public String getCurrentPage() {
        return currentPage;
    }
    /**
     * @param currentPage the currentPage to set
     */
    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
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
     * @return the pageSize
     */
    public String getPageSize() {
        return pageSize;
    }
    /**
     * @param pageSize the pageSize to set
     */
    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }
    /**
     * @return the systemInfos
     */
    public List<SystemInfo> getSystemInfos() {
        return systemInfos;
    }
    /**
     * @param systemInfos the systemInfos to set
     */
    public void setSystemInfos(List<SystemInfo> systemInfos) {
        this.systemInfos = systemInfos;
    }
    /**
     * @return the unRead
     */
    public String getUnRead() {
        return unRead;
    }
    /**
     * @param unRead the unRead to set
     */
    public void setUnRead(String unRead) {
        this.unRead = unRead;
    }




}
