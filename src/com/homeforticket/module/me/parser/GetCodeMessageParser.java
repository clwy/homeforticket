
package com.homeforticket.module.me.parser;

import org.json.JSONObject;

import com.homeforticket.module.me.model.BindAccountMessage;
import com.homeforticket.module.me.model.FeedbackMessage;
import com.homeforticket.module.me.model.GetCodeMessage;
import com.homeforticket.module.me.model.SetUserInfoMessage;
import com.homeforticket.parser.AbstractParser;

public class GetCodeMessageParser extends AbstractParser<GetCodeMessage> {
   
    @Override
    public GetCodeMessage parseInner(String parser) throws Exception {

        GetCodeMessage message = new GetCodeMessage();
        JSONObject json = new JSONObject(parser);
        message.setCode(getString(json, "code"));
        message.setMessage(getString(json, "message"));
        
        return message;
    }
}
