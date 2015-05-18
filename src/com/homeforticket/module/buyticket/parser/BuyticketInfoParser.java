
package com.homeforticket.module.buyticket.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.homeforticket.module.buyticket.model.BuyticketInfo;
import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.firstpage.model.ResellerMessage;
import com.homeforticket.module.login.model.LoginMessage;
import com.homeforticket.parser.AbstractParser;

public class BuyticketInfoParser extends AbstractParser<BuyticketInfo> {

    @Override
    public BuyticketInfo parseInner(String parser) throws Exception {

        BuyticketInfo message = new BuyticketInfo();
        JSONObject json = new JSONObject(parser);
        message.setCode(getString(json, "code"));
        message.setMessage(getString(json, "message"));

        try {
            String content = getString(json, "responseBody");
            JSONObject jb = new JSONObject(content);
            message.setCount(getString(jb, "count"));
            message.setCurrentPage(getString(jb, "currentPage"));
            message.setPageSize(getString(jb, "pageSize"));

            List<TicketInfo> ticketInfos = new ArrayList<TicketInfo>();
            String list = getString(jb, "list");
            JSONArray listJson = new JSONArray(list);
            for (int i = 0, size = listJson.length(); i < size; i++) {
                TicketInfoParser ticketInfoParser = new TicketInfoParser();
                ticketInfos.add(ticketInfoParser.parseInner(listJson.getString(i)));
            }
            message.setTicketInfos(ticketInfos);
        } catch (Exception e) {

        }

        return message;
    }
}
