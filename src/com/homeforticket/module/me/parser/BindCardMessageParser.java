
package com.homeforticket.module.me.parser;

import org.json.JSONObject;

import com.homeforticket.module.me.model.BindAccountMessage;
import com.homeforticket.module.me.model.BindCardMessage;
import com.homeforticket.module.me.model.FeedbackMessage;
import com.homeforticket.module.me.model.GetCodeMessage;
import com.homeforticket.module.me.model.SetUserInfoMessage;
import com.homeforticket.parser.AbstractParser;

public class BindCardMessageParser extends AbstractParser<BindCardMessage> {
   
    @Override
    public BindCardMessage parseInner(String parser) throws Exception {

        BindCardMessage message = new BindCardMessage();
        JSONObject json = new JSONObject(parser);
        message.setCode(getString(json, "code"));
        message.setMessage(getString(json, "message"));
        
        return message;
    }
}
