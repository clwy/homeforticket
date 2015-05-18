package com.homeforticket.module.firstpage.parser;

import org.json.JSONObject;

import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.firstpage.model.StoreInfo;
import com.homeforticket.parser.AbstractParser;

public class StoreInfoParser extends AbstractParser<StoreInfo> {

    @Override
    public StoreInfo parseInner(String parser) throws Exception {

        StoreInfo message = new StoreInfo();
        JSONObject json = new JSONObject(parser);
        message.setAddress(getString(json, "address"));
        message.setCommission(getString(json, "commission"));
        message.setId(getString(json, "id"));
        message.setImg(getString(json, "img"));
        message.setLevel(getString(json, "level"));
        message.setName(getString(json, "name"));
        message.setPrice(getString(json, "price"));

        return message;
    }
}
