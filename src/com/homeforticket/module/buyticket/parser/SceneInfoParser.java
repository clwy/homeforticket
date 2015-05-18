
package com.homeforticket.module.buyticket.parser;


import org.json.JSONArray;
import org.json.JSONObject;

import android.text.Html;

import com.homeforticket.module.buyticket.model.SceneInfo;
import com.homeforticket.parser.AbstractParser;

public class SceneInfoParser extends AbstractParser<SceneInfo> {

    @Override
    public SceneInfo parseInner(String parser) throws Exception {

        SceneInfo message = new SceneInfo();
        JSONObject json = new JSONObject(parser);
        message.setCode(getString(json, "code"));
        message.setMessage(getString(json, "message"));

        try {
            String content = getString(json, "responseBody");
            JSONObject jb = new JSONObject(content);
            message.setAltitude(getString(jb, "altitude"));
            message.setCity(getString(jb, "city"));
            message.setCounty(getString(jb, "county"));
            message.setLatitude(getString(jb, "latitude"));
            message.setProvice(getString(jb, "provice"));
            message.setSceneAddress(getString(jb, "sceneAddress"));
            message.setSceneDescription(Html.fromHtml(getString(jb, "sceneDescription")).toString());
            message.setSceneId(getString(jb, "sceneId"));
            message.setSceneLevel(getString(jb, "sceneLevel"));
            message.setSceneName(getString(jb, "sceneName"));
            message.setSceneImage(getString(jb, "sceneImage").split(","));
            message.setList(getString(jb, "list"));
            
        } catch (Exception e) {

        }

        return message;
    }
}
