
package com.homeforticket.parser;


import org.json.JSONObject;

import com.homeforticket.model.ReturnMessage;

public class ReturnMessageParser extends AbstractParser<ReturnMessage> {


    @Override
    protected ReturnMessage parseInner(String parser) throws Exception {
        ReturnMessage returnMessage = new ReturnMessage();
        JSONObject json = new JSONObject(parser);

        returnMessage.setCode(getString(json, "code"));
        returnMessage.setMessage(getString(json, "message"));
        returnMessage.setToken(getString(json, "token"));

        return returnMessage;
    }

}
