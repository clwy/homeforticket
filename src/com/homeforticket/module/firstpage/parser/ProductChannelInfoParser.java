package com.homeforticket.module.firstpage.parser;

import org.json.JSONObject;

import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.firstpage.model.BehalfInfo;
import com.homeforticket.module.firstpage.model.OrderStatusInfo;
import com.homeforticket.module.firstpage.model.ProductChannelInfo;
import com.homeforticket.parser.AbstractParser;

public class ProductChannelInfoParser extends AbstractParser<ProductChannelInfo> {

    @Override
    public ProductChannelInfo parseInner(String parser) throws Exception {
        ProductChannelInfo message = new ProductChannelInfo();
        JSONObject json = new JSONObject(parser);
        message.setId(getString(json, "id"));
        message.setName(getString(json, "name"));
        message.setFlag(getString(json, "flag"));

        return message;
    }
}
