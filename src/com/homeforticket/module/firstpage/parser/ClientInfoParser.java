package com.homeforticket.module.firstpage.parser;

import org.json.JSONObject;

import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.firstpage.model.ClientInfo;
import com.homeforticket.module.firstpage.model.RecordInfo;
import com.homeforticket.parser.AbstractParser;

public class ClientInfoParser extends AbstractParser<ClientInfo> {

    @Override
    public ClientInfo parseInner(String parser) throws Exception {
        ClientInfo message = new ClientInfo();
        JSONObject json = new JSONObject(parser);
        message.setHeadimg(getString(json, "photo"));
        message.setId(getString(json, "id"));
        message.setTel(getString(json, "tel"));
        message.setName(getString(json, "name"));
        message.setNote(getString(json, "note"));

        return message;
    }
}
