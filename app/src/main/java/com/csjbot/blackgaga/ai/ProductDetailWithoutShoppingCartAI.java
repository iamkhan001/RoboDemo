//package com.csjbot.blackgaga.ai;
//
///**
// * Created by Ben on 2018/4/18.
// */
//
//public class ProductDetailWithoutShoppingCartAI {
//}
package com.csjbot.blackgaga.ai;

import com.csjbot.blackgaga.feature.product.productDetail.ProductDetailWithoutShoppingCartActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jingwc on 2017/10/26.
 */

public class ProductDetailWithoutShoppingCartAI extends AI<ProductDetailWithoutShoppingCartActivity>{

    public static ProductDetailWithoutShoppingCartAI newInstance(){
        return new ProductDetailWithoutShoppingCartAI();
    }

    @Override
    public Intent getIntent(String text) {

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
            case ADD_SHOPPING_CART:
                activity.getProductDetailFragment().addShoppingCart();
                break;
            case PURCHASE:
                activity.getProductDetailFragment().purchase();
                break;
            case HOWMUCH:
            case PAYMENT:
            case CHECK:
            case ACCOUNT:
            case SHOPPING_CART:
                activity.goShoppingCartAct();
                break;
            default:
                break;
        }
    }

    static class AIParams{
        static final String[] ADD_SHOPPING_CART = {"JIARUGOUWUCHE"};//加入购物车
        static final String[] PURCHASE = {"GOUMAI"};//购买
        static final String[] SHOPPING_CART = {"GOUWUCHE"};//购物车
        static final String[] ACCOUNT = {"ACCOUNT"};//结账
        static final String[] CHECK = {"CHECK"};//买单
        static final String[] PAYMENT = {"PAYMENT"};//付款
        static final String[] HOWMUCH = {"HOWMUCH"};//多少钱

        static List<Object[][]> datas;

        static {
            datas = new ArrayList<>();
            datas.add(new Object[][]{ADD_SHOPPING_CART,{Intent.ADD_SHOPPING_CART}});
            datas.add(new Object[][]{PURCHASE,{Intent.PURCHASE}});
            datas.add(new Object[][]{SHOPPING_CART,{Intent.SHOPPING_CART}});
            datas.add(new Object[][]{ACCOUNT,{Intent.ACCOUNT}});
            datas.add(new Object[][]{CHECK,{Intent.CHECK}});
            datas.add(new Object[][]{PAYMENT,{Intent.PAYMENT}});
            datas.add(new Object[][]{HOWMUCH,{Intent.HOWMUCH}});
        }

    }

    public enum Intent{
        ADD_SHOPPING_CART,PURCHASE,SHOPPING_CART, ACCOUNT, CHECK, PAYMENT, HOWMUCH
    }
}
