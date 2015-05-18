
package com.homeforticket.module.message.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.homeforticket.module.buyticket.model.BuyticketInfo;
import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.firstpage.model.RecordInfo;
import com.homeforticket.module.firstpage.model.RecordInfoMessage;
import com.homeforticket.module.firstpage.model.ResellerMessage;
import com.homeforticket.module.login.model.LoginMessage;
import com.homeforticket.module.message.model.SystemInfo;
import com.homeforticket.module.message.model.SystemInfoMessage;
import com.homeforticket.parser.AbstractParser;

public class SystemInfoMessageParser extends AbstractParser<SystemInfoMessage> {

    @Override
    public SystemInfoMessage parseInner(String parser) throws Exception {

        SystemInfoMessage message = new SystemInfoMessage();
        JSONObject json = new JSONObject(parser);
        message.setCode(getString(json, "code"));
        message.setMessage(getString(json, "message"));

        try {
            String content = getString(json, "responseBody");
            JSONObject jb = new JSONObject(content);
            message.setCount(getString(jb, "count"));
            message.setCurrentPage(getString(jb, "currentPage"));
            message.setPageSize(getString(jb, "pageSize"));
            message.setUnRead(getString(jb, "nrcount"));

            List<SystemInfo> infos = new ArrayList<SystemInfo>();
            String list = getString(jb, "list");
            JSONArray listJson = new JSONArray(list);
            for (int i = 0, size = listJson.length(); i < size; i++) {
                SystemInfoParser systemInfoParser = new SystemInfoParser();
                infos.add(systemInfoParser.parseInner(listJson.getString(i)));
            }
            message.setSystemInfos(infos);
        } catch (Exception e) {

        }

        return message;
    }
}
