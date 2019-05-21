package com.csjbot.blackgaga.cart.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.cart.entity.RobotSpListBean;
import com.csjbot.blackgaga.feature.product.shopcart.ShoppingCartActivity;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.blackgaga.util.ShopcartUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.csjbot.blackgaga.model.http.product.ProductProxy.PRODUCT_IMG_PATH;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/10/16.
 */

public class ProductBoxAdapter extends RecyclerView.Adapter<ProductBoxAdapter.MyViewHolder> implements ItemTouchHelperAdapter, View.OnClickListener {
    /**
     * 筛选的数据
     */
    private List<RobotSpListBean.ResultBean.ProductBean> getData = new ArrayList<>();

    /**
     * 添加购物车监听
     */
    private OnAddCatListener addCatListener = null;

    public void setListener(ImgItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * 点击item
     */
    private ImgItemClickListener listener = null;

    private Context context;

    private static int item;
    /**
     * position
     */
    private RobotSpListBean.ResultBean.ProductBean productBean;

    private boolean isHide;

    public List<RobotSpListBean.ResultBean.ProductBean> getProductBean() {
        return getData;
    }

    public ProductBoxAdapter(Context context) {
        this.context = context;
        String mIndustry = SharedPreUtil.getString(SharedKey.MAINPAGE, SharedKey.MAINPAGE_KEY, "");
        if (mIndustry.equals("yinhang") || mIndustry.equals("jichang") || mIndustry.equals("bowuguan")) {
            isHide = true;
        }
    }

    public void setAddCatListener(OnAddCatListener addCatListener) {
        this.addCatListener = addCatListener;
    }

    public void updateRey(RobotSpListBean data) {
        getData = new ArrayList<>();
        if (data != null && data.getResult() != null) {
            getData = data.getResult().getProduct();
        }
        notifyDataSetChanged();
    }

    @Override
    public ProductBoxAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.product_item, parent, false);
        view.setOnClickListener(this);
        return new ProductBoxAdapter.MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if (isHide) {
            holder.layout_cart_buy.setVisibility(View.GONE);
            holder.price_introduction.setVisibility(View.GONE);
        }
        productBean = getData.get(position);
        holder.itemView.setTag(position);
        String imgName = getData.get(position).getImgName();
        String toFilePath = PRODUCT_IMG_PATH + imgName;
        if (!new File(toFilePath).exists()) {
            holder.productImg.setImageResource(R.drawable.no_product);
        } else {
            Glide.with(context).load(toFilePath).into(holder.productImg);
        }
        String title = getData.get(position).getName();
        String price = context.getString(R.string.yuan) + getData.get(position).getCurrentprice();//现价
        String oldPrice = context.getString(R.string.yuan) + getData.get(position).getOriginalprice();//原价

        holder.newPirce.setVisibility(View.VISIBLE);
        //如果是在打折
        holder.price.setText(oldPrice);
        holder.pPrice.setText(price);
        holder.price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中间横线
        int symbolSold = getData.get(position).getSell();
        holder.productTitle.setText(title);

        isShowCartPhuse(holder);
        setMarketType(holder);

        if (symbolSold >= 10000) {
            holder.symbolSold.setText(symbolSold / 10000 + context.getString(R.string.shrinkage_individual));
        } else if (symbolSold < 10000) {
            holder.symbolSold.setText(symbolSold + "");
        }

        holder.productImg.setOnClickListener(v -> {
            //            CSJToast.showToast(context, getData.get(position).getName(), 1000);
            if (listener != null) {
                listener.onClick(getData.get(position));
            }
        });

        holder.add_cat.setOnClickListener(v -> {
            item = position;
            if (addCatListener != null) {
                addCatListener.add(holder.productImg, getData.get(position));
            }
        });


        holder.buy.setOnClickListener(v -> {
            item = position;
            purchase(holder, position);
        });
    }

    //设置产品营销类型
    private void setMarketType(MyViewHolder holder) {
        if (productBean.getMarketingtype() == 1) {//促销
            holder.product_market_type.setBackgroundResource(R.drawable.cx);
        } else if (productBean.getMarketingtype() == 2) {//热卖
            holder.product_market_type.setBackgroundResource(R.drawable.rm);
        } else if (productBean.getMarketingtype() == 3) {//新品
            holder.product_market_type.setBackgroundResource(R.drawable.xp);
        } else if (productBean.getMarketingtype() == 4) {//满赠
            holder.product_market_type.setBackgroundResource(R.drawable.mz);
        } else if (productBean.getMarketingtype() == 5) {//爆款
            holder.product_market_type.setBackgroundResource(R.drawable.bk);
        }
    }

    /**
     * 是否显示购物车或者购买
     */
    private void isShowCartPhuse(MyViewHolder holder) {
        if (productBean.isIsshowbtnshopcatr()) {
            holder.add_cat.setVisibility(View.VISIBLE);
        } else {
            holder.add_cat.setVisibility(View.GONE);
        }

        if (productBean.isIsshowbtnpay()) {
            holder.buy.setVisibility(View.VISIBLE);
        } else {
            holder.buy.setVisibility(View.GONE);
        }

        if (!productBean.isIsshowbtnshopcatr() && !productBean.isIsshowbtnpay()) {
            holder.layout_cart_buy.setVisibility(View.GONE);
        } else {
//            String Industry = SharedPreUtil.getString(SharedKey.MAINPAGE, SharedKey.MAINPAGE_KEY, "");
//            if (Industry.equals("yinhang") || Industry.equals("jichang")) {
//                holder.layout_cart_buy.setVisibility(View.GONE);
//            } else {
//                holder.layout_cart_buy.setVisibility(View.VISIBLE);
//            }
        }
    }

    /**
     * 购买
     */
    public void purchase(MyViewHolder holder, int position) {
        if (ShopcartUtil.isAddShopcart(getData.get(position))) {//库存充足，添加一件商品至购物车，跳转到购物车界面
            ShopcartUtil.addShopcartProduct(getData.get(position));
        }
        Intent intent = new Intent(holder.itemView.getContext(), ShoppingCartActivity.class);
        holder.itemView.getContext().startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return getData.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(getData, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        getData.remove(position);
        notifyItemRemoved(position);
    }


    public RobotSpListBean.ResultBean.ProductBean getChooseProduct() {
        return getData.get(item);
    }


    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }


    /**
     * 外部调用接口
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener = null;


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productTitle;
        ImageView productImg;
        TextView price;
        TextView pPrice;
        TextView symbolSold;
        LinearLayout buy;
        TextView add_st;
        LinearLayout oldPirce;
        LinearLayout newPirce;
        TextView productPrice;//价格 不打折的时候
        LinearLayout add_cat;
        LinearLayout product_img_now;
        LinearLayout layout_cart_buy;
        ImageView product_market_type;//营销类型
        LinearLayout price_introduction;

        //控件
        public MyViewHolder(View itemView) {
            super(itemView);
            productTitle = itemView.findViewById(R.id.product_title);
            productImg = itemView.findViewById(R.id.product_img);
            price = itemView.findViewById(R.id.price);//原价多少
            pPrice = itemView.findViewById(R.id.price_present);//现价多少
            symbolSold = itemView.findViewById(R.id.symbol_sold);//已经卖出去了多少件
            buy = itemView.findViewById(R.id.buy);//购买
            add_st = itemView.findViewById(R.id.add_st);//添加购物车
            oldPirce = itemView.findViewById(R.id.price_old);//打折时候的原价
            newPirce = itemView.findViewById(R.id.price_new);//打折时候的原价
            productPrice = itemView.findViewById(R.id.product_price);
            add_cat = itemView.findViewById(R.id.add_cat);
            product_img_now = itemView.findViewById(R.id.product_img_now);
            layout_cart_buy = itemView.findViewById(R.id.layout_cart_buy);
            product_market_type = itemView.findViewById(R.id.product_market_type);
            price_introduction = itemView.findViewById(R.id.price_introduction);
        }
    }


    public interface OnAddCatListener {
        <T> void add(T view, RobotSpListBean.ResultBean.ProductBean productBean);
    }

    public interface ImgItemClickListener {
        void onClick(RobotSpListBean.ResultBean.ProductBean productBean);
    }
}
