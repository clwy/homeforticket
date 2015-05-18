
package com.homeforticket.module.login.parser;

import org.json.JSONObject;

import com.homeforticket.module.login.model.LoginMessage;
import com.homeforticket.parser.AbstractParser;

public class LoginMessageParser extends AbstractParser<LoginMessage> {

    @Override
    public LoginMessage parseInner(String parser) throws Exception {
        JSONObject json = new JSONObject(parser);
        LoginMessage message = new LoginMessage();
        message.setCode(getString(json, "code"));
        message.setMessage(getString(json, "message"));

        try {

            String content = getString(json, "responseBody");
            JSONObject jb = new JSONObject(content);

            message.setToken(getString(jb, "token"));
            message.setPhoto(getString(jb, "photo"));
            message.setName(getString(jb, "resellerName"));
            message.setTel(getString(jb, "mobile"));
            message.setBind(getString(jb, "isbund"));
            message.setTotalMoney(getString(jb, "t_money"));
            message.setCurrentMoney(getString(jb, "m_money"));
            message.setRole(getString(jb, "role"));
        } catch (Exception e) {
            // TODO: handle exception
        }

        return message;
    }
}
