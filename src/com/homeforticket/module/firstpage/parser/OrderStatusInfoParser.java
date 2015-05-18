package com.homeforticket.module.firstpage.parser;

import org.json.JSONObject;

import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.firstpage.model.BehalfInfo;
import com.homeforticket.module.firstpage.model.OrderStatusInfo;
import com.homeforticket.parser.AbstractParser;

public class OrderStatusInfoParser extends AbstractParser<OrderStatusInfo> {

    @Override
    public OrderStatusInfo parseInner(String parser) throws Exception {
        OrderStatusInfo message = new OrderStatusInfo();
        JSONObject json = new JSONObject(parser);
        message.setId(getString(json, "id"));
        message.setName(getString(json, "name"));

        return message;
    }
}
