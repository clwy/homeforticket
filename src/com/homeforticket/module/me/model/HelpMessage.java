package com.homeforticket.module.me.model;

import com.homeforticket.model.ReturnMessage;

public class HelpMessage extends ReturnMessage {

    private static final long serialVersionUID = 1L;
    private String help;
    /**
     * @return the help
     */
    public String getHelp() {
        return help;
    }
    /**
     * @param help the help to set
     */
    public void setHelp(String help) {
        this.help = help;
    }

}
