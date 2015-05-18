
package com.homeforticket.module.firstpage.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.homeforticket.module.buyticket.model.BuyticketInfo;
import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.firstpage.model.BehalfInfo;
import com.homeforticket.module.firstpage.model.BehalfMessage;
import com.homeforticket.module.firstpage.model.ResellerMessage;
import com.homeforticket.module.login.model.LoginMessage;
import com.homeforticket.parser.AbstractParser;

public class BehalfMessageParser extends AbstractParser<BehalfMessage> {

    @Override
    public BehalfMessage parseInner(String parser) throws Exception {

        BehalfMessage message = new BehalfMessage();
        JSONObject json = new JSONObject(parser);
        message.setCode(getString(json, "code"));
        message.setMessage(getString(json, "message"));

        try {
            String content = getString(json, "responseBody");
            JSONObject jb = new JSONObject(content);
            message.setCount(getString(jb, "count"));
            message.setCurrentPage(getString(jb, "currentPage"));
            message.setPageSize(getString(jb, "pageSize"));

            List<BehalfInfo> infos = new ArrayList<BehalfInfo>();
            String list = getString(jb, "list");
            JSONArray listJson = new JSONArray(list);
            for (int i = 0, size = listJson.length(); i < size; i++) {
                BehalfInfoParser behalfInfoParser = new BehalfInfoParser();
                infos.add(behalfInfoParser.parseInner(listJson.getString(i)));
            }
            message.setBehalfInfos(infos);
        } catch (Exception e) {

        }

        return message;
    }
}
