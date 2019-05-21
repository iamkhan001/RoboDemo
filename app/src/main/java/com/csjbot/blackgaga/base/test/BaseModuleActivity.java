package com.csjbot.blackgaga.base.test;

import android.text.TextUtils;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.router.BRouter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/7/18.
 */

public abstract class BaseModuleActivity extends BaseFullScreenActivity {

    @Override
    protected boolean dealIntent(String intentJson, String source) {
        if(super.dealIntent(intentJson, source)){
            return true;
        }
        boolean isNotEmptyIntent = true;
        getLog().warn("BaseModuleActivity:intentJson:"+intentJson);
        String intent = "";
        try {
            intent = new JSONObject(intentJson).getString("intent");
            getLog().warn("BaseModuleActivity:intent:"+intent);
        } catch (JSONException e) {
            getLog().error("BaseModuleActivity intent json 解析失败:"+e.toString());
        }
        switch (intent){
            case Intents.PAGE:
                page(intentJson,source);
                break;
            default:
                isNotEmptyIntent = false;
                break;
        }
        if(!isNotEmptyIntent){
            chat(source);
        }
        return isNotEmptyIntent;
    }

    protected void page(String intentJson,String source){
        try {
            JSONObject result = new JSONObject(intentJson).getJSONObject("result");
            String action = result.getString("action");
            if(action.equals("homePage")){
                BRouter.toHome();
            }else if(action.equals("back")){
                this.finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getIntentType(String json) {
        String intentJson = super.getIntentType(json);
        if(!TextUtils.isEmpty(intentJson)){
            return intentJson;
        }
        String empty = "";
        JSONObject result = null;
        try {
            result = new JSONObject(json).getJSONObject("result");
            String text = result.getString("text");
            if (text.contains(getString(R.string.main)) || text.contains(getString(R.string.homepage))) {
                return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.PAGE + "\",\"result\":{\"action\":\"homePage\"}}";
            }else if(text.contains(getString(R.string.back)) || text.contains(getString(R.string.quit))){
                return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.PAGE + "\",\"result\":{\"action\":\"back\"}}";
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return empty;
    }
}
