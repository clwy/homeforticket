package com.homeforticket.module.firstpage.parser;

import org.json.JSONObject;

import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.firstpage.model.RecordInfo;
import com.homeforticket.module.firstpage.model.StatisticsInfo;
import com.homeforticket.parser.AbstractParser;

public class StatisticsInfoParser extends AbstractParser<StatisticsInfo> {

    @Override
    public StatisticsInfo parseInner(String parser) throws Exception {
        StatisticsInfo message = new StatisticsInfo();
        JSONObject json = new JSONObject(parser);
        message.setDate(getString(json, "date"));
        message.setTotal(getString(json, "total"));
        return message;
    }
}
