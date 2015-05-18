
package com.homeforticket.module.firstpage.parser;

import org.json.JSONObject;

import com.homeforticket.module.firstpage.model.SetStoreInfoMessage;
import com.homeforticket.module.me.model.FeedbackMessage;
import com.homeforticket.module.me.model.SetUserInfoMessage;
import com.homeforticket.parser.AbstractParser;

public class SetStoreInfoMessageParser extends AbstractParser<SetStoreInfoMessage> {
   
    @Override
    public SetStoreInfoMessage parseInner(String parser) throws Exception {

        SetStoreInfoMessage message = new SetStoreInfoMessage();
        JSONObject json = new JSONObject(parser);
        message.setCode(getString(json, "code"));
        message.setMessage(getString(json, "message"));
        
        return message;
    }
}
