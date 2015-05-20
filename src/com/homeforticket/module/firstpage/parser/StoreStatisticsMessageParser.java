
package com.homeforticket.module.firstpage.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.homeforticket.module.buyticket.model.BuyticketInfo;
import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.firstpage.model.ResellerMessage;
import com.homeforticket.module.firstpage.model.StoreInfo;
import com.homeforticket.module.firstpage.model.StoreInfoMessage;
import com.homeforticket.module.firstpage.model.StoreStatisticsMessage;
import com.homeforticket.module.login.model.LoginMessage;
import com.homeforticket.parser.AbstractParser;

public class StoreStatisticsMessageParser extends AbstractParser<StoreStatisticsMessage> {

    @Override
    public StoreStatisticsMessage parseInner(String parser) throws Exception {

        StoreStatisticsMessage message = new StoreStatisticsMessage();
        JSONObject json = new JSONObject(parser);
        message.setCode(getString(json, "code"));
        message.setMessage(getString(json, "message"));

        try {
            String content = getString(json, "responseBody");
            JSONArray ja = new JSONArray(content);
            JSONObject jb = ja.getJSONObject(0);
            message.setAddress(getString(jb, "address"));
            message.setAutoClose(getString(jb, "autoClose"));
            message.setAutoRecevie(getString(jb, "autoRecevie"));
            message.setDescription(getString(jb, "description"));
            message.setIsassurance(getString(jb, "isassurance"));
            message.setName(getString(jb, "name"));
            message.setTel(getString(jb, "tel"));
            message.setId(getString(jb, "id"));
            message.setImg(getString(jb, "img"));
            message.setUrl(getString(jb, "url"));
        } catch (Exception e) {

        }

        return message;
    }
}
