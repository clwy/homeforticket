package com.homeforticket.module.firstpage.parser;

import org.json.JSONObject;

import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.firstpage.model.RecordInfo;
import com.homeforticket.parser.AbstractParser;

public class RecordInfoParser extends AbstractParser<RecordInfo> {

    @Override
    public RecordInfo parseInner(String parser) throws Exception {
        RecordInfo message = new RecordInfo();
        JSONObject json = new JSONObject(parser);
        message.setCurrentMoney(getString(json, "balance"));
        message.setDate(getString(json, "createTime"));
        message.setTitle(getString(json, "title"));
        message.setType(getString(json, "type"));
        message.setRemark(getString(json, "remark"));
        message.setIncome(getString(json, "deposit"));
        message.setWithdraw(getString(json, "withdraw"));
        return message;
    }
}
