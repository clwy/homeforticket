package com.homeforticket.module.firstpage.parser;

import org.json.JSONObject;

import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.firstpage.model.ClientInfo;
import com.homeforticket.module.firstpage.model.OrderInfo;
import com.homeforticket.module.firstpage.model.ProductInfo;
import com.homeforticket.module.firstpage.model.RecordInfo;
import com.homeforticket.parser.AbstractParser;

public class ProductInfoParser extends AbstractParser<ProductInfo> {

    @Override
    public ProductInfo parseInner(String parser) throws Exception {
        ProductInfo message = new ProductInfo();
        JSONObject json = new JSONObject(parser);
        message.setPrice(getString(json, "price"));
        message.setId(getString(json, "id"));
        message.setImg(getString(json, "img"));
        message.setName(getString(json, "name"));
        message.setLevel(getString(json, "level"));
        message.setAddress(getString(json, "address"));
        message.setCommission(getString(json, "commission"));
        message.setType(getString(json, "type"));
        message.setRetailPrice(getString(json, "retailPrice"));
        message.setAltitude(getString(json, "altitude"));
        message.setCity(getString(json, "city"));
        message.setLatitude(getString(json, "latitude"));
        message.setPicName(getString(json, "picName"));
        message.setProductName(getString(json, "productName"));
        message.setSceneId(getString(json, "sceneId"));
        message.setSceneLevel(getString(json, "sceneLevel"));
        message.setSceneName(getString(json, "sceneName"));
        message.setMarketPrice(getString(json, "marketPrice"));

        return message;
    }
}
