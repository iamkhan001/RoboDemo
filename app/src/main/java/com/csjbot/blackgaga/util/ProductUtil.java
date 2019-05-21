package com.csjbot.blackgaga.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.cart.entity.RobotSpListBean;
import com.csjbot.blackgaga.feature.product.productDetail.ProductDetailActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 孙秀艳 on 2017/12/6.
 */

public class ProductUtil {
    public static RichTextUtil getHotProduct(Context context, RichTextUtil richTextUtil, List<RobotSpListBean.ResultBean.ProductBean> products) {
        List<RobotSpListBean.ResultBean.ProductBean> hotList = new ArrayList<RobotSpListBean.ResultBean.ProductBean>();
        if(products != null && products.size()>0){
            for (int i=0; i<products.size(); i++) {
                if (products.get(i).getMarketingtype() == 5) {//爆款
                    hotList.add(products.get(i));
                }
            }
        }
        if (hotList != null && hotList.size() > 0) {//超过两个推荐两次，少于等于2个全部推荐
            richTextUtil.append("\t"+context.getString(R.string.hot_recommend)).append("  ");
            int color = Color.parseColor("#0099ff");
            int hotSize = hotList.size();
            if (hotSize > 2) {
                hotSize = 2;
            }
            for (int i=0; i<hotSize; i++) {
                RobotSpListBean.ResultBean.ProductBean productBean = hotList.get(i);
                richTextUtil.append("\t"+hotList.get(i).getName() + ((i==hotSize-1)?"":"，"), color, 40, v->{
                    goProductDetailAct(context, productBean);
                });
            }
        }
        richTextUtil.append("  ");
        return  richTextUtil;
    }

    public static RichTextUtil getNewProduct(Context context, RichTextUtil richTextUtil, List<RobotSpListBean.ResultBean.ProductBean> products) {
        List<RobotSpListBean.ResultBean.ProductBean> newList = new ArrayList<>();
        if(products != null && products.size()>0){
            for (int i=0; i<products.size(); i++) {
                if (products.get(i).getMarketingtype() == 3) {//新品
                    newList.add(products.get(i));
                }
            }
        }
        if (newList != null && newList.size() > 0) {//超过两个推荐两次，少于等于2个全部推荐
            richTextUtil.append("\t"+context.getString(R.string.new_recommend)).append("  ");
            int color = Color.parseColor("#0099ff");
            int hotSize = newList.size();
            if (hotSize > 2) {
                hotSize = 2;
            }
            for (int i=0; i<hotSize; i++) {
                RobotSpListBean.ResultBean.ProductBean productBean = newList.get(i);
                richTextUtil.append("\t"+newList.get(i).getName() + ((i==hotSize-1)?"":"，"), color, 40, v->{
                    goProductDetailAct(context, productBean);
                });
            }
        }
        richTextUtil.append("  ");
        return  richTextUtil;
    }

    public static RichTextUtil getSaleProduct(Context context, RichTextUtil richTextUtil, List<RobotSpListBean.ResultBean.ProductBean> products) {
        List<RobotSpListBean.ResultBean.ProductBean> saleList = new ArrayList<>();
        if(products != null && products.size()>0){
            for (int i=0; i<products.size(); i++) {
                if (products.get(i).getMarketingtype() == 1) {//特价
                    saleList.add(products.get(i));
                }
            }
        }
        if (saleList != null && saleList.size() > 0) {//超过两个推荐两次，少于等于2个全部推荐
            richTextUtil.append("\t"+context.getString(R.string.today_sales_product)).append("  ");
            int color = Color.parseColor("#0099ff");
            int hotSize = saleList.size();
            if (hotSize > 2) {
                hotSize = 2;
            }
            for (int i=0; i<hotSize; i++) {
                RobotSpListBean.ResultBean.ProductBean productBean = saleList.get(i);
                richTextUtil.append("\t"+saleList.get(i).getName() + ((i==hotSize-1)?"":"，"), color, 40, v->{
                    goProductDetailAct(context, productBean);
                });
            }
        }
        richTextUtil.append("  ");
        return  richTextUtil;
    }

    public static void goProductDetailAct(Context context, RobotSpListBean.ResultBean.ProductBean productBean){
        Bundle bundle = new Bundle();
        String gson = new Gson().toJson(productBean);
        bundle.putString("productBean", gson);
        Intent intent = new Intent(context, ProductDetailActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
