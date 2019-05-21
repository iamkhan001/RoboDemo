package com.csjbot.blackgaga.ai;

import com.csjbot.blackgaga.cart.entity.RobotSpListBean;
import com.csjbot.blackgaga.cart.pactivity.introduce.ProductIntruductionActivity;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.cosclient.utils.CosLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jingwc on 2017/10/26.
 */

public class ProductIntruductionAI extends AI<ProductIntruductionActivity>{

    public static ProductIntruductionAI newInstance(){
        return new ProductIntruductionAI();
    }


    @Override
    public Boolean dynamicHandle(Object... o) {
        String pinyinText = strConvertPinyin(o[0].toString());
        CosLogger.debug("text------------------------------------>"+pinyinText);
        List<RobotSpListBean.ResultBean.ProductBean> productBeans = (List<RobotSpListBean.ResultBean.ProductBean>) o[1];
        for (int i = 0;i < productBeans.size();i++){
            CosLogger.debug("name:------------------------------------>"+productBeans.get(i).getName());
            if(pinyinText.contains(strConvertPinyin(productBeans.get(i).getName()))){
                CosLogger.debug("true------------------------------------>");
                activity.goProductDetailAct(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public Intent getIntent(String text) {
        //        汉字转拼音方法
        String pinyin = strConvertPinyin(text);

        Intent intent = null;

        boolean isStop = false;
        int size = AIParams.datas.size();
        for (int i = 0; i < size;i++){
            int paramSize = AIParams.datas.get(i)[0].length;
            for (int j = 0;j < paramSize;j++){
                if(pinyin.contains(AIParams.datas.get(i)[0][j].toString())){
                    intent = (Intent) AIParams.datas.get(i)[1][0];
                    isStop = true;
                    break;
                }
            }
            if(isStop){
                break;
            }
        }

        return intent;
    }

    @Override
    public void handleIntent(Enum e) {
        super.handleIntent(e);
        Intent intent = (Intent) e;
        switch (intent){
            case HOWMUCH:
            case PAYMENT:
            case CHECK:
            case ACCOUNT:
            case SHOPPING_CART:
                if(Constants.Scene.CurrentScene != Constants.Scene.YinHangScene){
                    activity.goShoppingCartAct();
                }
                break;
            default:
                break;
        }
    }

    static class AIParams{
        static final String[] SHOPPING_CART = {"GOUWUCHE"};
        static final String[] ACCOUNT = {"ACCOUNT"};//结账
        static final String[] CHECK = {"CHECK"};//买单
        static final String[] PAYMENT = {"PAYMENT"};//付款
        static final String[] HOWMUCH = {"HOWMUCH"};//多少钱
        static List<Object[][]> datas;

        static {
            datas = new ArrayList<>();
            datas.add(new Object[][]{SHOPPING_CART,{Intent.SHOPPING_CART}});
            datas.add(new Object[][]{ACCOUNT,{Intent.ACCOUNT}});
            datas.add(new Object[][]{CHECK,{Intent.CHECK}});
            datas.add(new Object[][]{PAYMENT,{Intent.PAYMENT}});
            datas.add(new Object[][]{HOWMUCH,{Intent.HOWMUCH}});
        }

    }

    public enum Intent{
        SHOPPING_CART, ACCOUNT, CHECK, PAYMENT, HOWMUCH
    }
}
