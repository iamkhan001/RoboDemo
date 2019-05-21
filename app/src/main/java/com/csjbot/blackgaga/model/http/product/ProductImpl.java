package com.csjbot.blackgaga.model.http.product;

import com.csjbot.blackgaga.cart.entity.RobotMenuListBean;
import com.csjbot.blackgaga.cart.entity.RobotSpListBean;
import com.csjbot.blackgaga.cart.pactivity.evaluate.AnswerResponse;
import com.csjbot.blackgaga.feature.clothing.bean.ClothListBean;
import com.csjbot.blackgaga.feature.clothing.bean.ClothTypeBean;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.model.http.base.BaseImpl;
import com.csjbot.blackgaga.model.http.bean.AdInfo;
import com.csjbot.blackgaga.model.http.bean.ContentBean;
import com.csjbot.blackgaga.model.http.bean.ContentTypeBean;
import com.csjbot.blackgaga.model.http.bean.PayResponse;
import com.csjbot.blackgaga.model.http.bean.PayResult;
import com.csjbot.blackgaga.model.http.bean.ProductInfo;
import com.csjbot.blackgaga.model.http.bean.SceneBean;
import com.csjbot.blackgaga.model.http.product.listeners.IProduct;
import com.csjbot.blackgaga.model.http.product.listeners.ObtainData;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import okhttp3.ResponseBody;

/**
 * Created by jingwc on 2017/9/18.
 */

public class ProductImpl extends BaseImpl implements IProduct, ObtainData {
    @Override
    public ProductInfo getLocalProduct() {
        String str = SharedPreUtil.getString(SharedKey.PRODUCT_SP, SharedKey.PRODUCT_INFO);
        return new Gson().fromJson(str, ProductInfo.class);
    }

    @Override
    public RobotMenuListBean getMenuList() {
        String lan = SharedKey.MENU_LAN.getLanguageStr(Constants.Language.getLanguageStr());
        String str = SharedPreUtil.getString(SharedKey.PRODUCT_MENU_LIST, lan);
        if (str == null) {
            return null;
        } else {
            return new Gson().fromJson(str, RobotMenuListBean.class);
        }
    }

    @Override
    public RobotSpListBean getSpList(String menuid) {
        String lan = SharedKey.SP_LAN.getLanguageStr(Constants.Language.getLanguageStr());
        String str = SharedPreUtil.getString(lan, menuid);
        if (str == null) {
            return null;
        } else {
            return new Gson().fromJson(str, RobotSpListBean.class);
        }
    }

    @Override
    public boolean removeSpList(String name) {
        String lan = SharedKey.SP_LAN.getLanguageStr(name);
        return SharedPreUtil.removeString(lan, name);
    }

    @Override
    public boolean removeMenuList() {
        String lan = SharedKey.MENU_LAN.getLanguageStr(Constants.Language.getLanguageStr());
        return SharedPreUtil.removeString(SharedKey.PRODUCT_MENU_LIST, lan);
    }

    @Override
    public SceneBean getSceneRes(String industry) {
        String str = SharedPreUtil.getString(SharedKey.SCENE_LIST, industry);
        if (str == null) {
            return null;
        } else {
            return new Gson().fromJson(str, SceneBean.class);
        }
    }

    @Override
    public boolean removeSceneRes() {
        return SharedPreUtil.removeString(SharedKey.SCENE_LIST);
    }

    @Override
    public List<RobotSpListBean.ResultBean.ProductBean> getAllSpInformation() {
        String lan = SharedKey.SP_LAN.getLanguageStr(Constants.Language.getLanguageStr());
        Map<String, String> list = (Map<String, String>) SharedPreUtil.getAllSp(lan);
        List<RobotSpListBean.ResultBean.ProductBean> spList = new ArrayList<>();
        if (list == null) {
            return null;
        } else {
            for (Map.Entry<String, String> entry : list.entrySet()) {
                RobotSpListBean robotSpListBean = new Gson().fromJson(entry.getValue(), RobotSpListBean.class);
                List<RobotSpListBean.ResultBean.ProductBean> o = robotSpListBean.getResult().getProduct();
                for (int i = 0; i < o.size(); i++) {
                    spList.add(o.get(i));
                }
            }
        }
        return spList;
    }

    @Override
    public ContentBean getContent() {
        String lan = SharedKey.CONTENT_LAN.getLanguageStr(Constants.Language.getLanguageStr());
        String str = SharedPreUtil.getString(SharedKey.CONTENT_NAME, lan);
        if (str == null) {
            return null;
        } else {
            return new Gson().fromJson(str, ContentBean.class);
        }
    }

    @Override
    public ContentTypeBean getContentType(String parent_id) {
        String lan = SharedKey.CONTENT_LAN.getLanguageStr(Constants.Language.getLanguageStr());
        String str = SharedPreUtil.getString(lan, parent_id);
        if (str == null) {
            return null;
        } else {
            return new Gson().fromJson(str, ContentTypeBean.class);
        }
    }


    @Override
    public void getAdInfo(Observer<AdInfo> observer) {
        scheduler(getRetrofit().getAdInfo()).subscribe(observer);
    }

    @Override
    public void generateOrder(String json, Observer<PayResponse> observer) {
        scheduler(getRetrofit().generateOrder(getBody(json))).subscribe(observer);
    }

    @Override
    public void sendEvalution(String json, Observer<AnswerResponse> observer) {
        scheduler(getRetrofit().sendEvalution(getBody(json))).subscribe();
    }

    @Override
    public void searchPayResult(String orderid, Observer<PayResult> observer) {
        scheduler(getRetrofit().searchPayResult(orderid)).subscribe(observer);
    }

    @Override
    public void getRobotMenuList(String sn, String language, Observer<RobotMenuListBean> observer) {
        scheduler(getRetrofit().getRobotMenuList(sn, language)).subscribe(observer);
    }


    @Override
    public void dynamicGetRobotSpList(String url, String sn, Observer<RobotSpListBean> observer) {
        scheduler(getRetrofit().dynamicGetRobotSpList(url, sn)).subscribe(observer);
    }

    @Override
    public void fullDynamicGetRobotSpList(String url, Observer<RobotSpListBean> observer) {
        scheduler(getRetrofit().fullDynamicGetRobotSpList(url)).subscribe(observer);
    }

    @Override
    public void getProductImage(String url, Observer<ResponseBody> observer) {
        scheduler(getRetrofit().getProductImage(url)).subscribe(observer);
    }

    @Override
    public void getScene(String json, Observer<SceneBean> observable) {
        scheduler(getRetrofit().getScene(getBody(json))).subscribe(observable);
    }

    @Override
    public void getSkin(String url, Observer<ResponseBody> observer) {
        scheduler(getRetrofit().downloadSceneSKINUrl(url)).subscribe(observer);
    }

    @Override
    public void getContent(String lan, String sn, Observer<ContentBean> observer) {
        scheduler(getRetrofit().getContent(lan, sn)).subscribe(observer);
    }

    @Override
    public void getContentType(String url, Observer<ContentTypeBean> observer) {
        scheduler(getRetrofit().getContentType(url)).subscribe(observer);
    }

    @Override
    public void getAllClothType(String sn, Observer<ClothTypeBean> observer) {
        scheduler(getRetrofit().getAllClothType(sn)).subscribe(observer);
    }

    @Override
    public void getClothingList(String sn, String secondLevel, String season, double minPrice, double maxPrice, Observer<ClothListBean> observer) {
        scheduler(getRetrofit().getClothingList(sn, secondLevel, season, minPrice, maxPrice)).subscribe(observer);
    }

    @Override
    public ProductService getRetrofit() {
        return getRetrofit(ProductService.class);
    }

}
