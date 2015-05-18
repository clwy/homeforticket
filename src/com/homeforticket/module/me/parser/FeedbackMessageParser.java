
package com.homeforticket.module.me.parser;

import org.json.JSONObject;

import com.homeforticket.module.me.model.SetUserInfoMessage;
import com.homeforticket.parser.AbstractParser;

public class FeedbackMessageParser extends AbstractParser<SetUserInfoMessage> {
   
    @Override
    public SetUserInfoMessage parseInner(String parser) throws Exception {

        SetUserInfoMessage message = new SetUserInfoMessage();
        JSONObject json = new JSONObject(parser);
        message.setCode(getString(json, "code"));
        message.setMessage(getString(json, "message"));
        
        return message;
    }
}
