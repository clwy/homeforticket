package com.homeforticket.module.buyticket.parser;

import org.json.JSONObject;

import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.parser.AbstractParser;

public class TicketInfoParser extends AbstractParser<TicketInfo> {

    @Override
    public TicketInfo parseInner(String parser) throws Exception {
        TicketInfo message = new TicketInfo();
        JSONObject json = new JSONObject(parser);
        message.setProductName(getString(json, "productName"));
        message.setLatitude(getString(json, "latitude"));
        message.setAltitude(getString(json, "altitude"));
        message.setSceneId(getString(json, "sceneId"));
        message.setSceneName(getString(json, "sceneName"));
        message.setCity(getString(json, "city"));
        message.setSceneLevel(getString(json, "sceneLevel"));
        message.setPicName(getString(json, "picName"));
        message.setMarketPrice(getString(json, "marketPrice"));
        message.setRetailPrice(getString(json, "retailPrice"));
        message.setPrice(getString(json, "price"));

        return message;
    }
}
