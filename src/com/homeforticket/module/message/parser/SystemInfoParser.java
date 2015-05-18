package com.homeforticket.module.message.parser;

import org.json.JSONObject;

import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.firstpage.model.RecordInfo;
import com.homeforticket.module.message.model.SystemInfo;
import com.homeforticket.parser.AbstractParser;

public class SystemInfoParser extends AbstractParser<SystemInfo> {

    @Override
    public SystemInfo parseInner(String parser) throws Exception {
        SystemInfo message = new SystemInfo();
        JSONObject json = new JSONObject(parser);
        message.setNewsId(getString(json, "news_id"));
        message.setNewsDate(getString(json, "news_date"));
        message.setTitle(getString(json, "title"));
        message.setContent(getString(json, "content"));
        message.setType(getString(json, "type"));
        return message;
    }
}
