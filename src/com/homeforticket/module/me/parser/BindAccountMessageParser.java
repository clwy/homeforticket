
package com.homeforticket.module.me.parser;

import org.json.JSONObject;

import com.homeforticket.module.me.model.BindAccountMessage;
import com.homeforticket.module.me.model.FeedbackMessage;
import com.homeforticket.module.me.model.SetUserInfoMessage;
import com.homeforticket.parser.AbstractParser;

public class BindAccountMessageParser extends AbstractParser<BindAccountMessage> {
   
    @Override
    public BindAccountMessage parseInner(String parser) throws Exception {

        BindAccountMessage message = new BindAccountMessage();
        JSONObject json = new JSONObject(parser);
        message.setCode(getString(json, "code"));
        message.setMessage(getString(json, "message"));
        try {
            String content = getString(json, "responseBody");
            JSONObject jb = new JSONObject(content);
            message.setBname(getString(jb, "bname"));
            message.setBcard(getString(jb, "bcard"));
            message.setAvaBalance(getString(jb, "avaBalance"));
            message.setTime(getString(jb, "time"));
        } catch (Exception e) {

        }
        
        
        return message;
    }
}
