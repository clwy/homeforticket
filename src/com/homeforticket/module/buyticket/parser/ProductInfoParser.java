
package com.homeforticket.module.buyticket.parser;


import org.json.JSONArray;
import org.json.JSONObject;

import android.text.Html;

import com.homeforticket.module.buyticket.model.ProductInfo;
import com.homeforticket.module.buyticket.model.SceneInfo;
import com.homeforticket.module.buyticket.model.SceneTicketInfo;
import com.homeforticket.parser.AbstractParser;

public class ProductInfoParser extends AbstractParser<ProductInfo> {
            
    @Override
    public ProductInfo parseInner(String parser) throws Exception {

        ProductInfo message = new ProductInfo();
        JSONObject json = new JSONObject(parser);
        message.setCode(getString(json, "code"));
        message.setMessage(getString(json, "message"));
        
        try {
            String content = getString(json, "responseBody");
            JSONObject jb = new JSONObject(content);
            message.setProductId(getString(jb, "productId"));
            message.setProductName(getString(jb, "productName"));
            message.setNotice(getString(jb, "notice"));
            message.setSpecialNote(getString(jb, "specialNote"));
            message.setTrffice(getString(jb, "trffice"));
            message.setAdvanceDay(getString(jb, "advanceDay"));
            message.setRefundTicketType(getString(jb, "refundTicketType"));
            message.setSaleStartTime(getString(jb, "saleStartTime"));
            message.setSaleEndTime(getString(jb, "saleEndTime"));
            message.setPrice(getString(jb, "price"));
            message.setMarkPrice(getString(jb, "markPrice"));
            message.setRetailPrice(getString(jb, "retailPrice"));
            message.setIsAuth(getString(jb, "isAuth"));
            message.setWeek(getString(jb, "week"));
            message.setIsUnique(getString(jb, "isUnique"));
            message.setCosts(getString(jb, "costs"));
            message.setPriceId(getString(jb, "priceid"));
            message.setIsPackage(getString(jb, "isPackage"));
            message.setTheatre(getString(jb, "theatre"));
        } catch (Exception e) {

        }

        return message;
    }
}
