
package com.homeforticket.module.firstpage.parser;

import org.json.JSONObject;

import com.homeforticket.module.firstpage.model.ResellerMessage;
import com.homeforticket.module.login.model.LoginMessage;
import com.homeforticket.parser.AbstractParser;

public class ResellerMessageParser extends AbstractParser<ResellerMessage> {

    @Override
    public ResellerMessage parseInner(String parser) throws Exception {

        ResellerMessage message = new ResellerMessage();
        JSONObject json = new JSONObject(parser);
        message.setCode(getString(json, "code"));
        message.setMessage(getString(json, "message"));

        try {
            String content = getString(json, "responseBody");
            JSONObject jb = new JSONObject(content);

            message.setPhoto(getString(jb, "photo"));
            message.setName(getString(jb, "resellerName"));
            message.setTel(getString(jb, "mobile"));
            message.setBind(getString(jb, "isbund"));
            message.setTotalMoney(getString(jb, "t_money"));
            message.setCurrentMoney(getString(jb, "m_money"));
        } catch (Exception e) {
        }

        return message;
    }
}
