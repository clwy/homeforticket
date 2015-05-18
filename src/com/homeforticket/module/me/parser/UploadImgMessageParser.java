
package com.homeforticket.module.me.parser;

import org.json.JSONObject;

import com.homeforticket.module.me.model.SetUserInfoMessage;
import com.homeforticket.module.me.model.UploadImgMessage;
import com.homeforticket.parser.AbstractParser;

public class UploadImgMessageParser extends AbstractParser<UploadImgMessage> {

    @Override
    public UploadImgMessage parseInner(String parser) throws Exception {

        UploadImgMessage message = new UploadImgMessage();
        JSONObject json = new JSONObject(parser);
        message.setCode(getString(json, "code"));
        message.setMessage(getString(json, "message"));
        try {
            String content = getString(json, "responseBody");
            JSONObject jb = new JSONObject(content);
            message.setPath(getString(jb, "imgurl"));
        } catch (Exception e) {

        }

        return message;
    }
}
