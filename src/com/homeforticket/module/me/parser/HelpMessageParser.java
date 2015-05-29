
package com.homeforticket.module.me.parser;

import org.json.JSONObject;

import com.homeforticket.module.me.model.BindAccountMessage;
import com.homeforticket.module.me.model.FeedbackMessage;
import com.homeforticket.module.me.model.HelpMessage;
import com.homeforticket.module.me.model.SetUserInfoMessage;
import com.homeforticket.parser.AbstractParser;

public class HelpMessageParser extends AbstractParser<HelpMessage> {
   
    @Override
    public HelpMessage parseInner(String parser) throws Exception {

        HelpMessage message = new HelpMessage();
        JSONObject json = new JSONObject(parser);
        message.setCode(getString(json, "code"));
        message.setMessage(getString(json, "message"));
        try {
            String content = getString(json, "responseBody");
            JSONObject jb = new JSONObject(content);
            message.setHelp(getString(jb, "help"));
        } catch (Exception e) {

        }
        
        
        return message;
    }
}
