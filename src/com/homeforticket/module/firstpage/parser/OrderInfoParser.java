package com.homeforticket.module.firstpage.parser;

import org.json.JSONObject;

import com.homeforticket.module.buyticket.model.TicketInfo;
import com.homeforticket.module.firstpage.model.ClientInfo;
import com.homeforticket.module.firstpage.model.OrderInfo;
import com.homeforticket.module.firstpage.model.RecordInfo;
import com.homeforticket.parser.AbstractParser;

public class OrderInfoParser extends AbstractParser<OrderInfo> {

//  createTime  订单生成时间  是   
//  orderId 平台生成的唯一标识码  是   平台在新增订单成功是生成
//  orderState  订单状态    是   -1:已取消0:未支付 1:已支付 2:联票检票中3:已检票 4:已结算，5:已结算已支付8：请求退款9：已退票10：过期票
//  paidAmount  实付金额    是   
//  productName 产品名称    是   
//  total_num   票数  是   
//  sceneName   景点名称    是   
//  price   价格  是   
//  pic 图片路径    是   
//  buyName 购买人 是   
//  contractNote    留言  否 
    @Override
    public OrderInfo parseInner(String parser) throws Exception {
        OrderInfo message = new OrderInfo();
        JSONObject json = new JSONObject(parser);

        message.setCreateTime(getString(json, "createTime"));
        message.setOrderId(getString(json, "orderId"));
        message.setOrderState(getString(json, "orderState"));
        message.setPaidAmount(getString(json, "paidAmount"));
        message.setProductName(getString(json, "productName"));
        message.setTotalnum(getString(json, "total_num"));
        message.setSceneName(getString(json, "sceneName"));
        message.setPrice(getString(json, "price"));
        message.setPic(getString(json, "pic"));
        message.setBuyName(getString(json, "buyName"));
        message.setContractNote(getString(json, "contractNote"));
        return message;
    }
}
