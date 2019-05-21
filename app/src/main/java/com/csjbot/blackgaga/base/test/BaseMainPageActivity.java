package com.csjbot.blackgaga.base.test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.bean.OnUpdateAppEvent;
import com.csjbot.blackgaga.cart.entity.RobotSpListBean;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.feature.product.productDetail.ProductDetailActivity;
import com.csjbot.blackgaga.feature.settings.SettingsActivity;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.model.tcp.bean.Position;
import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;
import com.csjbot.blackgaga.util.GsonUtils;
import com.csjbot.blackgaga.util.NumberUtils;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.blackgaga.widget.NewRetailDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2018/7/18.
 */

public abstract class BaseMainPageActivity extends BaseFullScreenActivity {

    @Override
    public void init() {
        super.init();
        SharedPreUtil.putInt(SharedKey.STARTMODE, SharedKey.STARTMODE, 1);//热启动
        Constants.Charging.initCharging();
    }

    @Override
    protected boolean dealIntent(String intentJson, String source) {
        if(super.dealIntent(intentJson, source)){
            return true;
        }
        boolean isNotEmptyIntent = true;
        getLog().warn("BaseMainPageActivity:intentJson:"+intentJson);
        String intent = "";
        try {
            intent = new JSONObject(intentJson).getString("intent");
            getLog().warn("BaseMainPageActivity:intent:"+intent);
        } catch (JSONException e) {
            getLog().error("BaseMainPageActivity intent json 解析失败:"+e.toString());
        }
        switch (intent){
            case Intents.PRODUCT_BUY:
                product(intentJson,source);
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


            /**************产品购买(开始)*************/
            if (Constants.ProductKeyWord.products.size() == 0) {
                Constants.ProductKeyWord.initKeywords();
            }
            String intentRegEx = "[\"我想要\"|\"我要\"|\"我买\"|\"给我来\"|\"给我拿\"|\"我想买\"].*";
            String intentRegEx2 = ".*我要.*[\"个\"|\"盒\"|\"包\"|\"根\"|\"块\"|\"箱\"|\"瓶\"|\"件\"]";
            if (text.matches(intentRegEx)) {
                String regex = "[\"我想要\"|\"我要\"|\"我买\"|\"给我来\"|\"给我拿\"|\"我想买\"]+[0-9]+[\"个\"|\"盒\"|\"包\"|\"根\"|\"块\"|\"箱\"|\"瓶\"|\"件\"]";
                String regex2 = "[\"我想要\"|\"我要\"|\"我买\"|\"给我来\"|\"给我拿\"|\"我想买\"]+[\"个\"|\"盒\"|\"包\"|\"根\"|\"块\"|\"箱\"|\"瓶\"|\"件\"]";
                String regex3 = "[\"我想要\"|\"我要\"|\"我买\"|\"给我来\"|\"给我拿\"].*[\"个\"|\"盒\"|\"包\"|\"根\"|\"块\"|\"箱\"|\"瓶\"|\"件\"]";
                String regex4 = "[\"我想要\"|\"我要\"|\"我买\"|\"给我来\"|\"给我拿\"|\"我想买\"]";
                for (RobotSpListBean.ResultBean.ProductBean product : Constants.ProductKeyWord.products) {
                    if (text.matches((regex + product.getName() + "+[。|.]"))) {
                        String gson = new Gson().toJson(product);
                        Bundle bundle = new Bundle();
                        bundle.putString("productBean", gson);
                        int number = NumberUtils.getNumber(text);
                        getLog().info("number:" + number);
                        return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.PRODUCT_BUY + "\",\"result\":{\"action\":\"productBuy\",\"productBean\":"+gson+",\"number\":"+number+"}}";
                    } else if (text.matches((regex2 + product.getName() + "+[。|.]"))) {
                        String gson = new Gson().toJson(product);
                        Bundle bundle = new Bundle();
                        bundle.putString("productBean", gson);
                        return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.PRODUCT_BUY + "\",\"result\":{\"action\":\"productBuy\",\"productBean\":"+gson+",\"number\":0}}";
                    } else if (text.matches(regex3 + product.getName() + "+[。|.]")) {
                        String gson = new Gson().toJson(product);
                        Bundle bundle = new Bundle();
                        bundle.putString("productBean", gson);
                        int number = NumberUtils.numberParser(text);
                        getLog().info("number2:" + number);
                        if (number > 0) {
                            bundle.putInt("number", number);
                        }
                        return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.PRODUCT_BUY + "\",\"result\":{\"action\":\"productBuy\",\"productBean\":"+gson+",\"number\":"+number+"}}";
                    } else if (text.matches(regex4 + product.getName() + "+[。|.]")) {
                        getLog().error(text.matches(regex4 + product.getName() + "+[。|.]") + "");
                        getLog().error((regex4 + product.getName() + "+[。|.]"));
                        String gson = new Gson().toJson(product);
                        Bundle bundle = new Bundle();
                        bundle.putString("productBean", gson);
                        return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.PRODUCT_BUY + "\",\"result\":{\"action\":\"productBuy\",\"productBean\":"+gson+",\"number\":0}}";
                    } else {
                        if (text.contains(product.getName())) {
                            String gson = new Gson().toJson(product);
                            Bundle bundle = new Bundle();
                            bundle.putString("productBean", gson);
                            return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.PRODUCT_BUY + "\",\"result\":{\"action\":\"productBuy\",\"productBean\":"+gson+",\"number\":0}}";
                        }
                    }
                }
            } else if (text.matches(intentRegEx2)) {
                String regex5 = "我要.*[\"个\"|\"盒\"|\"包\"|\"根\"|\"块\"|\"箱\"|\"瓶\"|\"件\"]+[。|.]";
                String regex6 = "我要[0-9]+[\"个\"|\"盒\"|\"包\"|\"根\"|\"块\"|\"箱\"|\"瓶\"|\"件\"]+[。|.]";
                for (RobotSpListBean.ResultBean.ProductBean product : Constants.ProductKeyWord.products) {
                    if (text.matches(regex5)) {
                        String s = new StringBuilder(regex5).insert(0, product.getName()).toString();
                        if (text.matches(s)) {
                            String gson = new Gson().toJson(product);
                            Bundle bundle = new Bundle();
                            bundle.putString("productBean", gson);
                            int number = NumberUtils.numberParser(text);
                            getLog().info("number2:" + number);
                            if (number > 0) {
                                bundle.putInt("number", number);
                            }
                            return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.PRODUCT_BUY + "\",\"result\":{\"action\":\"productBuy\",\"productBean\":"+gson+",\"number\":"+number+"}}";
                        }
                    } else if (text.matches(regex6)) {
                        String s = new StringBuilder(regex6).insert(0, product.getName()).toString();
                        if (text.matches(s)) {
                            String gson = new Gson().toJson(product);
                            Bundle bundle = new Bundle();
                            bundle.putString("productBean", gson);
                            int number = NumberUtils.getNumber(text);
                            getLog().info("number:" + number);
                            if (number > 0) {
                                bundle.putInt("number", number);
                            }
                            return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.PRODUCT_BUY + "\",\"result\":{\"action\":\"productBuy\",\"productBean\":"+gson+",\"number\":"+number+"}}";
                        }
                    }
                }

            }
            /**************产品购买(结束)*************/

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return empty;
    }

    protected void product(String intentjson, String source){
        try {
            JSONObject result = new JSONObject(intentjson).getJSONObject("result");
            String action = result.getString("action");
            if(action.equals("productBuy")){
                String productJson = result.getString("productBean");
                int number = result.getInt("number");

                Bundle bundle = new Bundle();
                bundle.putString("productBean", productJson);
                if (number > 0) {
                    bundle.putInt("number", number);
                }
                jumpActivity(ProductDetailActivity.class, bundle);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void isUpdateApp(OnUpdateAppEvent event) {
        super.isUpdateApp(event);
        if (event.isUpdateApp()) {
            Log.e("检查App更新", "当前网络可用: ");
            //检查程序更新

            if (!getDialog().isShowing()) {
                showNewRetailDialog(getString(R.string.net_work_nouse), getString(R.string.dialog_update_app),
                        getString(R.string.sure), getString(R.string.cancel), new NewRetailDialog.OnDialogClickListener() {
                            @Override
                            public void yes() {
                                startActivity(new Intent(BaseMainPageActivity.this, SettingsActivity.class));
                                SharedPreUtil.putBoolean(SharedKey.UPDATE_APP, "check_app", true);
                                dismissNewRetailDialog();
                            }

                            @Override
                            public void no() {
                                SharedPreUtil.putBoolean(SharedKey.UPDATE_APP, "check_app", true);
                                dismissNewRetailDialog();
                            }
                        });
            }
        }
    }


    protected Handler welcomeHandler = new Handler();
    protected Runnable welcomeRb = () -> {
        try {
            if (SharedPreUtil.getBoolean(SharedKey.YINGBINGSETTING, SharedKey.ISACTIVE, false)) {
                //获取迎宾点
                String j = SharedPreUtil.getString(SharedKey.YINGBIN_NAME, SharedKey.YINGBIN_KEY);
                List<Position> positionList = GsonUtils.jsonToObject(j, new TypeToken<List<Position>>() {
                }.getType());
                if (positionList == null || positionList.size() <= 0) {
                    new Thread(() -> {
                        ServerFactory.getChassisInstance().moveBack();
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        ServerFactory.getChassisInstance().moveBack();
                    }).start();
                    return;
                }
                RobotManager.getInstance().robot.reqProxy.navi(positionList.get(0).toJson());
            }
        } catch (Exception e) {
        }

    };

}
