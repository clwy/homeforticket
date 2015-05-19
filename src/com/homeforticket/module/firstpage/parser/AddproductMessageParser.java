
package com.homeforticket.module.firstpage.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.homeforticket.module.buyticket.model.BuyticketInfo;
import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.firstpage.model.AddProductMessage;
import com.homeforticket.module.firstpage.model.ResellerMessage;
import com.homeforticket.module.firstpage.model.StoreInfo;
import com.homeforticket.module.firstpage.model.StoreInfoMessage;
import com.homeforticket.module.firstpage.model.StoreStatisticsMessage;
import com.homeforticket.module.login.model.LoginMessage;
import com.homeforticket.parser.AbstractParser;

public class AddproductMessageParser extends AbstractParser<AddProductMessage> {

    @Override
    public AddProductMessage parseInner(String parser) throws Exception {

        AddProductMessage message = new AddProductMessage();
        JSONObject json = new JSONObject(parser);
        message.setCode(getString(json, "code"));
        message.setMessage(getString(json, "message"));

        return message;
    }
}
