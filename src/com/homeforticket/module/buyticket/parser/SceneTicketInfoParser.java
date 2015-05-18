
package com.homeforticket.module.buyticket.parser;


import org.json.JSONArray;
import org.json.JSONObject;

import android.text.Html;

import com.homeforticket.module.buyticket.model.SceneInfo;
import com.homeforticket.module.buyticket.model.SceneTicketInfo;
import com.homeforticket.parser.AbstractParser;

public class SceneTicketInfoParser extends AbstractParser<SceneTicketInfo> {
            
    @Override
    public SceneTicketInfo parseInner(String parser) throws Exception {

        SceneTicketInfo message = new SceneTicketInfo();
        JSONObject jsonObject = new JSONObject(parser);

        try {
            message.setPrice(getString(jsonObject, "price"));
            message.setMarketPrice(getString(jsonObject, "marketPrice"));
            message.setRetailPrice(getString(jsonObject, "retailPrice"));
            message.setProductName(getString(jsonObject, "productName"));
            message.setProductId(getString(jsonObject, "productId"));
            
        } catch (Exception e) {

        }

        return message;
    }
}
