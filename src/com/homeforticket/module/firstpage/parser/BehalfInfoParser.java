package com.homeforticket.module.firstpage.parser;

import org.json.JSONObject;

import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.firstpage.model.BehalfInfo;
import com.homeforticket.parser.AbstractParser;

public class BehalfInfoParser extends AbstractParser<BehalfInfo> {

    @Override
    public BehalfInfo parseInner(String parser) throws Exception {
        BehalfInfo message = new BehalfInfo();
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
