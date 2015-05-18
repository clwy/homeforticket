
package com.homeforticket.module.firstpage.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.homeforticket.module.buyticket.model.BuyticketInfo;
import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.firstpage.model.ResellerMessage;
import com.homeforticket.module.firstpage.model.StoreInfo;
import com.homeforticket.module.firstpage.model.StoreInfoMessage;
import com.homeforticket.module.login.model.LoginMessage;
import com.homeforticket.parser.AbstractParser;

public class StoreInfoMessageParser extends AbstractParser<StoreInfoMessage> {

    @Override
    public StoreInfoMessage parseInner(String parser) throws Exception {

        StoreInfoMessage message = new StoreInfoMessage();
        JSONObject json = new JSONObject(parser);
        message.setCode(getString(json, "code"));
        message.setMessage(getString(json, "message"));

        try {
            String content = getString(json, "responseBody");
            JSONObject jb = new JSONObject(content);
            message.setCount(getString(jb, "count"));
            message.setCurrentPage(getString(jb, "currentPage"));
            message.setPageSize(getString(jb, "pageSize"));

            List<StoreInfo> infos = new ArrayList<StoreInfo>();
            String list = getString(jb, "list");
            JSONArray listJson = new JSONArray(list);
            for (int i = 0, size = listJson.length(); i < size; i++) {
                StoreInfoParser storeInfoParser = new StoreInfoParser();
                infos.add(storeInfoParser.parseInner(listJson.getString(i)));
            }
            message.setStoreInfos(infos);
        } catch (Exception e) {

        }

        return message;
    }
}
