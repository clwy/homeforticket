
package com.homeforticket.module.me.parser;

import org.json.JSONObject;

import com.homeforticket.module.me.model.BindAccountMessage;
import com.homeforticket.module.me.model.FeedbackMessage;
import com.homeforticket.module.me.model.FetchMessage;
import com.homeforticket.module.me.model.SetUserInfoMessage;
import com.homeforticket.parser.AbstractParser;

public class FetchMessageParser extends AbstractParser<FetchMessage> {
   
    @Override
    public FetchMessage parseInner(String parser) throws Exception {

        FetchMessage message = new FetchMessage();
        JSONObject json = new JSONObject(parser);
        message.setCode(getString(json, "code"));
        message.setMessage(getString(json, "message"));
        
        
        return message;
    }
}
