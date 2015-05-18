package com.homeforticket.module.firstpage.parser;

import org.json.JSONObject;

import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.firstpage.model.DistributorInfo;
import com.homeforticket.module.firstpage.model.RecordInfo;
import com.homeforticket.parser.AbstractParser;

public class DistributorInfoParser extends AbstractParser<DistributorInfo> {

    @Override
    public DistributorInfo parseInner(String parser) throws Exception {
        DistributorInfo message = new DistributorInfo();
        JSONObject json = new JSONObject(parser);
        message.setId(getString(json, "id"));
        message.setImg(getString(json, "img"));
        message.setName(getString(json, "name"));
        message.setTotal(getString(json, "total"));
        message.setStatus(getString(json, "status"));
        message.setText(getString(json, "text"));
        return message;
    }
}
