
package com.homeforticket.module.buyticket.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.homeforticket.module.buyticket.model.BuyticketInfo;
import com.homeforticket.module.buyticket.model.SaveOrderMessage;
import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.firstpage.model.ResellerMessage;
import com.homeforticket.module.login.model.LoginMessage;
import com.homeforticket.parser.AbstractParser;

public class SaveOrderMessageParser extends AbstractParser<SaveOrderMessage> {

    @Override
    public SaveOrderMessage parseInner(String parser) throws Exception {

        SaveOrderMessage message = new SaveOrderMessage();
        JSONObject json = new JSONObject(parser);
        message.setCode(getString(json, "code"));
        message.setMessage(getString(json, "message"));

        try {
            String content = getString(json, "responseBody");
            JSONObject jb = new JSONObject(content);
            message.setOrderId(getString(jb, "orderID"));
            message.setSerialID(getString(jb, "serialID"));
        } catch (Exception e) {

        }

        return message;
    }
}
