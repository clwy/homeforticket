
package com.homeforticket.module.firstpage.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.homeforticket.module.buyticket.model.BuyticketInfo;
import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.firstpage.model.OrderContentInfo;
import com.homeforticket.module.firstpage.model.OrderContentMessage;
import com.homeforticket.module.firstpage.model.RecordInfo;
import com.homeforticket.module.firstpage.model.RecordInfoMessage;
import com.homeforticket.module.firstpage.model.ResellerMessage;
import com.homeforticket.module.login.model.LoginMessage;
import com.homeforticket.parser.AbstractParser;

public class OrderContentMessageParser extends AbstractParser<OrderContentMessage> {

    @Override
    public OrderContentMessage parseInner(String parser) throws Exception {

        OrderContentMessage message = new OrderContentMessage();
        JSONObject json = new JSONObject(parser);
        message.setCode(getString(json, "code"));
        message.setMessage(getString(json, "message"));

        try {
            String content = getString(json, "responseBody");
            JSONObject jb = new JSONObject(content);
            message.setCreateTime(getString(jb, "createTime"));
            message.setOrderAmount(getString(jb, "orderAmount"));
            message.setOrderID(getString(jb, "orderID"));
            message.setPaidAmount(getString(jb, "paidAmount"));
            message.setRefundAmount(getString(jb, "refundAmount"));
            message.setSms(getString(jb, "sms"));
            message.setTotal_num(getString(jb, "total_num"));
            message.setSceneName(getString(jb, "sceneName"));
            message.setOrderState(getString(jb, "orderState"));
            message.setStartTime(getString(jb, "startTime"));
            message.setExpTime(getString(jb, "expTime"));
            message.setRefundTicketType(getString(jb, "refundTicketType"));
            message.setNotice(getString(jb, "notice"));
            message.setCosts(getString(jb, "costs"));
            message.setCity(getString(jb, "city"));
            message.setSceneAddress(getString(jb, "sceneAddress"));
            message.setProvice(getString(jb, "provice"));
            message.setCounty(getString(jb, "county"));
            message.setPrice(getString(jb, "price"));
            message.setShowStartTime(getString(jb, "showStartTime"));
            message.setShowEndTime(getString(jb, "showEndTime"));

            List<OrderContentInfo> infos = new ArrayList<OrderContentInfo>();
            String list = getString(jb, "list");
            JSONArray listJson = new JSONArray(list);
            for (int i = 0, size = listJson.length(); i < size; i++) {
                infos.add(new OrderContentInfoParser().parseInner(listJson.getString(i)));
            }
            
            message.setInfos(infos);
        } catch (Exception e) {

        }

        return message;
    }
}
