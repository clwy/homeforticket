
package com.homeforticket.module.firstpage.parser;

import org.json.JSONObject;

import com.homeforticket.module.firstpage.model.WalletMessage;
import com.homeforticket.parser.AbstractParser;

public class WalletMessageParser extends AbstractParser<WalletMessage> {
   
    @Override
    public WalletMessage parseInner(String parser) throws Exception {

        WalletMessage message = new WalletMessage();
        JSONObject json = new JSONObject(parser);
        message.setCode(getString(json, "code"));
        message.setMessage(getString(json, "message"));

        try {
            String content = getString(json, "responseBody");
            JSONObject jb = new JSONObject(content);
            message.setAvaBalance(getString(jb, "avaBalance"));
            message.setFrzBalance(getString(jb, "frzBalance"));
            message.setTotalSale(getString(jb, "totalSale"));
            message.setTotalWithdraw(getString(jb, "totalWithdraw"));
            message.setPreWithdraw(getString(jb, "preWithdraw"));
            message.setTotalCheck(getString(jb, "totalCheck"));
            message.setTotalRefund(getString(jb, "totalRefund"));
        } catch (Exception e) {
        }

        return message;
    }
}
