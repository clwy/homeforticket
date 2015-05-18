
package com.homeforticket.module.firstpage.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.homeforticket.module.buyticket.model.BuyticketInfo;
import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.firstpage.model.ClientInfo;
import com.homeforticket.module.firstpage.model.ClientInfoMessage;
import com.homeforticket.module.firstpage.model.RecordInfo;
import com.homeforticket.module.firstpage.model.RecordInfoMessage;
import com.homeforticket.module.firstpage.model.ResellerMessage;
import com.homeforticket.module.login.model.LoginMessage;
import com.homeforticket.parser.AbstractParser;

public class ClientInfoMessageParser extends AbstractParser<ClientInfoMessage> {

    @Override
    public ClientInfoMessage parseInner(String parser) throws Exception {

        ClientInfoMessage message = new ClientInfoMessage();
        JSONObject json = new JSONObject(parser);
        message.setCode(getString(json, "code"));
        message.setMessage(getString(json, "message"));

        try {
            String content = getString(json, "responseBody");
            JSONObject jb = new JSONObject(content);
            message.setCount(getString(jb, "count"));
            message.setCurrentPage(getString(jb, "currentPage"));
            message.setPageSize(getString(jb, "pageSize"));

            List<ClientInfo> infos = new ArrayList<ClientInfo>();
            String list = getString(jb, "list");
            JSONArray listJson = new JSONArray(list);
            for (int i = 0, size = listJson.length(); i < size; i++) {
                infos.add(new ClientInfoParser().parseInner(listJson.getString(i)));
            }
            message.setClientInfos(infos);
        } catch (Exception e) {

        }

        return message;
    }
}
