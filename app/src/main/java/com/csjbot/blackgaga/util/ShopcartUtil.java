package com.csjbot.blackgaga.util;

import android.content.Context;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.cart.entity.RobotSpListBean;
import com.csjbot.blackgaga.model.http.bean.OrderBean;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by 孙秀艳 on 2017/10/24.
 * 购物车常用类
 */

public class ShopcartUtil {
    private static ArrayList<OrderBean> orderList= new ArrayList<OrderBean>();
    private static boolean isBack = false;

    public static boolean isAddShopcart(RobotSpListBean.ResultBean.ProductBean productBean) {
        boolean isAdd = true;
        if (orderList != null && orderList.size() > 0) {
            for (int i=0; i<orderList.size(); i++) {
                OrderBean orderBean = orderList.get(i);
                if (orderBean.getProduct_id().equals(productBean.getProduct_id())) {
                    if (productBean.getStock() > orderBean.getSell()) {
                        isAdd = true;
                    } else {
                        isAdd = false;
                    }
                    break;
                }
            }
        } else {
            if (productBean.getStock() > 0) {
                isAdd = true;
            } else {
                isAdd = false;
            }
        }
        return isAdd;
    }

    /**
     * 购物车里添加商品(一个商品或者一类商品)
     * @param productBean
     */
    public static void addShopcartProduct(RobotSpListBean.ResultBean.ProductBean productBean) {
        if (orderList != null && orderList.size() > 0) {
            boolean isAdd = false;
            for (int i=0; i<orderList.size(); i++) {
                OrderBean orderBean = orderList.get(i);
                if (orderBean.getProduct_id().equals(productBean.getProduct_id())) {
                    orderBean.setSell(orderBean.getSell()+1);
                    orderBean.setStock(productBean.getStock());
                    isAdd = true;
                    break;
                }
            }
            if (!isAdd) {
                orderList.add(addOrder(productBean));
            }
        } else {
            orderList = new ArrayList<OrderBean>();
            orderList.add(addOrder(productBean));
        }
    }

    private static OrderBean addOrder(RobotSpListBean.ResultBean.ProductBean productBean) {
        OrderBean orderBean = new OrderBean();
        orderBean.setProduct_id(productBean.getProduct_id());
        orderBean.setName(productBean.getName());
        orderBean.setIntroduction(productBean.getIntroduction());
        orderBean.setCurrentprice(productBean.getCurrentprice());
        orderBean.setCurrency(productBean.getCurrency());
        orderBean.setImgName(productBean.getImgName());
        orderBean.setSell(1);
        orderBean.setStock(productBean.getStock());
        orderBean.setChecked(true);
        return orderBean;
    }



    /**
     * 购物车里添加商品(一个商品或者一类商品)
     * @param productBean
     */
    public static void addShopcartProduct(RobotSpListBean.ResultBean.ProductBean productBean,int number) {
        if (orderList != null && orderList.size() > 0) {
            boolean isAdd = false;
            for (int i=0; i<orderList.size(); i++) {
                OrderBean orderBean = orderList.get(i);
                if (orderBean.getProduct_id().equals(productBean.getProduct_id())) {
                    orderBean.setSell(orderBean.getSell()+number);
                    orderBean.setStock(productBean.getStock());
                    isAdd = true;
                    break;
                }
            }
            if (!isAdd) {
                orderList.add(addOrder(productBean,number));
            }
        } else {
            orderList = new ArrayList<OrderBean>();
            orderList.add(addOrder(productBean,number));
        }
    }

    private static OrderBean addOrder(RobotSpListBean.ResultBean.ProductBean productBean,int number) {
        OrderBean orderBean = new OrderBean();
        orderBean.setProduct_id(productBean.getProduct_id());
        orderBean.setName(productBean.getName());
        orderBean.setIntroduction(productBean.getIntroduction());
        orderBean.setCurrentprice(productBean.getCurrentprice());
        orderBean.setCurrency(productBean.getCurrency());
        orderBean.setImgName(productBean.getImgName());
        orderBean.setSell(number);
        orderBean.setStock(productBean.getStock());
        orderBean.setChecked(true);
        return orderBean;
    }


    public static void addShopcartProduct(int position) {
        if (orderList != null && orderList.size() > position) {
            OrderBean orderBean = orderList.get(position);
            orderBean.setSell(orderBean.getSell()+1);
        }
    }


    /**
     * 删除购物车一个产品
     * @param product_id
     */
    public static void removeShopcartProduct(String product_id) {
        if (orderList != null && orderList.size() > 0) {
            for (int i=0; i<orderList.size(); i++) {
                OrderBean orderBean = orderList.get(i);
                if (orderBean.getProduct_id().equals(product_id)) {
                    orderBean.setSell(orderBean.getSell() - 1);
                    break;
                }
            }
        }
    }

    /**
     * 删除购物车一种产品 根据产品ID
     * @param product_id
     */
    public static void removeShopcartProducts(String product_id) {
        if (orderList != null && orderList.size() > 0) {
            for (int i=0; i<orderList.size(); i++) {
                if (orderList.get(i).getProduct_id().equals(product_id)) {
                    orderList.remove(i);
                    break;
                }
            }
        }
    }

    /**
     * 删除购物车一种产品 根据位置
     * @param position
     */
    public static void removeShopcartProducts(int position) {
        if (orderList != null && orderList.size() > position) {
            orderList.remove(position);
        }
    }

    /**
     * 返回购物车当前所选商品总金额
     * @return
     */
    public static String getShopcartPrice() {
        BigDecimal price = new BigDecimal(0);
        if (orderList != null && orderList.size() > 0) {
            for (int i=0; i<orderList.size(); i++) {
                if (orderList.get(i).isChecked()) {
                    price = price.add(new BigDecimal(orderList.get(i).getSell() * orderList.get(i).getCurrentprice()).setScale(2, BigDecimal.ROUND_HALF_UP));
                }
            }
        }
        DecimalFormat format = new DecimalFormat("###,###,###,##0.00");
        String str = format.format(price);
        if (str.contains(".00")) {
            str = str.replace(".00", "");
        } else {
            int index = str.indexOf(".");
            if (index > 0 && str.endsWith("0")) {
                str = str.substring(0, index+1) + str.substring(index+1, str.length()-1);
            }
        }
        return str;
    }

    /**
     * 返回购物车当前所选商品数量
     * @return
     */
    public static int getShopcartNum() {
        int num = 0;
        if (orderList != null && orderList.size() > 0) {
            for (int i=0; i<orderList.size(); i++) {
                if (orderList.get(i).isChecked()) {
                    num += orderList.get(i).getSell();
                }
            }
        }
        return num;
    }

    /**
     * 返回购物车当前所选商品种类
     * @return
     */
    public static int getShopcartItems() {
        int num = 0;
        if (orderList != null && orderList.size() > 0) {
            num = orderList.size();
        }
        return num;
    }

    public static CharSequence getShopcartChatContent(Context context) {
        RichTextUtil richTextUtil = new RichTextUtil();
        richTextUtil.append(context.getString(R.string.shopcart_you_choose) + ",", context.getResources().getColor(R.color.chat_text_color));
        for (int i=0; i<orderList.size(); i++) {
            OrderBean orderBean = orderList.get(i);
            if (orderBean.isChecked()) {
                String strOrder = orderBean.getName() + "/" + orderBean.getSell() + context.getString(R.string.box) + "，";
                richTextUtil.append(strOrder, context.getResources().getColor(R.color.chat_text_color));
            }
        }
        richTextUtil.append(context.getString(R.string.shopcart_total_RMB) + "：", context.getResources().getColor(R.color.chat_text_color)).append("  ");
        richTextUtil.append(context.getString(R.string.yuan)+getShopcartPrice(), context.getResources().getColor(R.color.chat_text_bold_color), 40, null);
//        richTextUtil.append("。" + context.getString(R.string.have), Color.parseColor("#333333"));
//        richTextUtil.append("2", Color.parseColor("#ff0000"));
//        richTextUtil.append(context.getString(R.string.page), Color.parseColor("#333333"));
//        richTextUtil.append("5" + context.getString(R.string.yuan) + context.getString(R.string.discount), Color.parseColor("#ff0000"));
//        richTextUtil.append(context.getString(R.string.may_use) + "，", Color.parseColor("#333333"));
//        richTextUtil.append(context.getString(R.string.discount_price), Color.parseColor("#333333"));
//        richTextUtil.append(getShopcartPrice() + context.getString(R.string.yuan), Color.parseColor("#ff0000"));
        richTextUtil.append("。\r\n", context.getResources().getColor(R.color.chat_text_color));
        richTextUtil.append(context.getString(R.string.is_confirm_payment), context.getResources().getColor(R.color.chat_text_color)).append("  ");
        richTextUtil.append(context.getString(R.string.confirm_payment), context.getResources().getColor(R.color.chat_text_bold_color), 40, null);
        richTextUtil.append("。\r\n", context.getResources().getColor(R.color.chat_text_color));
        richTextUtil.append(context.getString(R.string.if_need_modify), context.getResources().getColor(R.color.chat_text_color));
        richTextUtil.finish();
        return richTextUtil.finish();
    }

    public static ArrayList<OrderBean> getOrderList() {
        if (orderList != null && orderList.size() > 0) {
            return orderList;
        }
        return null;
    }

    /**
     * 清空购物车
     */
    public static void clearShopcart() {
        if (orderList != null) {
            orderList.clear();
        }
    }

    /**
     * 是否取消订单
     */
    public static void setOrderIsBack(boolean isFlag) {
        isBack = isFlag;
    }

    public static boolean getOrderIsBack() {
        return isBack;
    }
}
