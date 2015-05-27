package com.homeforticket.module.firstpage.parser;

import org.json.JSONObject;

import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.firstpage.model.OrderContentInfo;
import com.homeforticket.module.firstpage.model.RecordInfo;
import com.homeforticket.parser.AbstractParser;

public class OrderContentInfoParser extends AbstractParser<OrderContentInfo> {

    @Override
    public OrderContentInfo parseInner(String parser) throws Exception {
        OrderContentInfo message = new OrderContentInfo();
        JSONObject json = new JSONObject(parser);
        message.setBuyerICard(getString(json, "buyerICard"));
        message.setBuyerMobile(getString(json, "buyerMobile"));
        message.setBuyerName(getString(json, "buyerName"));
        message.setSeat(getString(json, "seat"));
        return message;
    }
}
