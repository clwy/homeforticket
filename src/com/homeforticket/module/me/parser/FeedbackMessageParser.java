
package com.homeforticket.module.me.parser;

import org.json.JSONObject;

import com.homeforticket.module.me.model.FeedbackMessage;
import com.homeforticket.module.me.model.SetUserInfoMessage;
import com.homeforticket.parser.AbstractParser;

public class FeedbackMessageParser extends AbstractParser<FeedbackMessage> {
   
    @Override
    public FeedbackMessage parseInner(String parser) throws Exception {

        FeedbackMessage message = new FeedbackMessage();
        JSONObject json = new JSONObject(parser);
        message.setCode(getString(json, "code"));
        message.setMessage(getString(json, "message"));
        
        return message;
    }
}
