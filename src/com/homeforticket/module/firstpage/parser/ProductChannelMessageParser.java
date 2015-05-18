
package com.homeforticket.module.firstpage.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.homeforticket.module.buyticket.model.BuyticketInfo;
import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.firstpage.model.BehalfInfo;
import com.homeforticket.module.firstpage.model.BehalfMessage;
import com.homeforticket.module.firstpage.model.OrderStatusInfo;
import com.homeforticket.module.firstpage.model.OrderStatusMessage;
import com.homeforticket.module.firstpage.model.ProductChannelInfo;
import com.homeforticket.module.firstpage.model.ProductChannelMessage;
import com.homeforticket.module.firstpage.model.ResellerMessage;
import com.homeforticket.module.login.model.LoginMessage;
import com.homeforticket.parser.AbstractParser;

public class ProductChannelMessageParser extends AbstractParser<ProductChannelMessage> {

    @Override
    public ProductChannelMessage parseInner(String parser) throws Exception {

        ProductChannelMessage message = new ProductChannelMessage();
        JSONObject json = new JSONObject(parser);
        message.setCode(getString(json, "code"));
        message.setMessage(getString(json, "message"));

        try {
            String content = getString(json, "responseBody");
            JSONObject jb = new JSONObject(content);
            message.setCount(getString(jb, "count"));
            message.setCurrentPage(getString(jb, "currentPage"));
            message.setPageSize(getString(jb, "pageSize"));

            List<ProductChannelInfo> infos = new ArrayList<ProductChannelInfo>();
            String list = getString(jb, "list");
            JSONArray listJson = new JSONArray(list);
            for (int i = 0, size = listJson.length(); i < size; i++) {
                infos.add(new ProductChannelInfoParser().parseInner(listJson.getString(i)));
            }
            message.setInfos(infos);
        } catch (Exception e) {

        }

        return message;
    }
}
