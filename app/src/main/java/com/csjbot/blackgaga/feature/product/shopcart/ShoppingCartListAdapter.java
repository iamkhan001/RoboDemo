package com.csjbot.blackgaga.feature.product.shopcart;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.model.http.bean.OrderBean;
import com.csjbot.blackgaga.util.CSJToast;
import com.csjbot.blackgaga.util.ShopcartUtil;
import com.csjbot.blackgaga.util.TouchUtil;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

import skin.support.widget.SkinCompatCheckBox;

/**
 * Created by 孙秀艳 on 2017/10/16.
 * 订单列表Adapter
 */

public class ShoppingCartListAdapter extends RecyclerView.Adapter<ShoppingCartListAdapter.OrderViewHolder>{
    private Context mContext;
    private ArrayList<OrderBean> mLists = new ArrayList<>();
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    //自定义监听事件
    public interface OnRecyclerViewItemClickListener {
        void onItemSubClick(View view, int position);
        void onItemAddClick(View view, int position);
        void onItemDeleteClick(View view, int position);
        void onItemCheckboxClick(View view, int position);
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener) {
        onRecyclerViewItemClickListener = listener;
    }

    public ShoppingCartListAdapter(Context context, ArrayList<OrderBean> lists) {
        mContext = context;
        mLists = lists;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_order_list, parent, false));
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        OrderBean orderBean = mLists.get(position);
        String strProductName = orderBean.getName();
        String strProductInfo = orderBean.getIntroduction();
        double productPrice = orderBean.getCurrentprice();
        int productNum = orderBean.getSell();
        int stock = orderBean.getStock();
//        String price = new BigDecimal(productPrice*productNum).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        String imagePath = Constants.PRODUCT_IMG_PATH + orderBean.getImgName();
        holder.tvProductName.setText(strProductName);
        holder.tvProductIntro.setSelected(true);
        holder.tvProductIntro.setText(strProductInfo);
        holder.tvProductPrice.setText(mContext.getString(R.string.yuan)+productPrice);
        holder.tvProductNum.setText(productNum + "");
        holder.tvItemTotalPrice.setText(mContext.getString(R.string.yuan)+getPrice(productNum, productPrice));
        File file = new File(imagePath);
        if (file.exists()) {
            Glide.with(mContext)
                    .load(imagePath)
                    .into(holder.ivProductThumb);
        } else {
            holder.ivProductThumb.setBackgroundResource(R.drawable.no_product);
        }
        if (productNum == 1) {
            holder.btnSub.setBackgroundResource(R.drawable.order_subb);
        } else {
            holder.btnSub.setBackgroundResource(R.drawable.order_sub);
        }

        if (orderBean.isChecked()) {
            holder.cbProduct.setChecked(true);
            holder.cbProduct.setButtonDrawable(R.drawable.check_select);
        } else {
            holder.cbProduct.setChecked(false);
            holder.cbProduct.setButtonDrawable(R.drawable.check_unselect);
        }
        holder.btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productNum > 1) {
                    onRecyclerViewItemClickListener.onItemSubClick(v, position);
                } else {
                    CSJToast.showToast(mContext, mContext.getString(R.string.shopcart_not_reduce_product), 1000);
                }
            }
        });
        holder.btnAdd.setOnClickListener(v -> {
            if (productNum + 1 > stock) {
                CSJToast.showToast(mContext, mContext.getString(R.string.shopcart_not_more_product), 1000);
            } else {
                onRecyclerViewItemClickListener.onItemAddClick(v, position);
            }
        });
        holder.tvDelete.setOnClickListener(v -> {
            onRecyclerViewItemClickListener.onItemDeleteClick(v, position);
            if (deleteDetailListener!=null){
                deleteDetailListener.del();
            }
        });
        holder.cbProduct.setOnClickListener(v -> {
            if (mLists.get(position).isChecked()) {
                mLists.get(position).setChecked(false);
                holder.cbProduct.setButtonDrawable(R.drawable.check_unselect);
            } else {
                mLists.get(position).setChecked(true);
                holder.cbProduct.setButtonDrawable(R.drawable.check_select);
            }
            onRecyclerViewItemClickListener.onItemCheckboxClick(v, position);
        });
        TouchUtil.expandViewTouchDelegate(holder.btnSub, 30, 30, 50, 30);
        TouchUtil.expandViewTouchDelegate(holder.btnAdd, 30, 30, 30, 50);
    }

    @Override
    public int getItemCount() {
        if (mLists == null || mLists.size() <= 0) {
            return 0;
        }
        return mLists.size();
    }

    //删除一个item
    public void removeItem(final int position) {
        ShopcartUtil.removeShopcartProducts(position);
        mLists = ShopcartUtil.getOrderList();
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, ShopcartUtil.getShopcartItems());
        notifyDataSetChanged();
    }

    //更新一个item flag=true菜品数量增加  flag=false菜品数量减
    public void updateItem(final int position, boolean flag) {
        if (flag) {
            ShopcartUtil.addShopcartProduct(position);
        } else {
            ShopcartUtil.removeShopcartProduct(mLists.get(position).getProduct_id());
        }
        mLists = ShopcartUtil.getOrderList();
        notifyItemRangeChanged(position, 1);
    }

    public String getPrice(int num, double price) {
        DecimalFormat format = new DecimalFormat("###,###,###,##0.00");
        String str = format.format(new BigDecimal(price*num).setScale(2, BigDecimal.ROUND_HALF_UP));
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

    class OrderViewHolder extends RecyclerView.ViewHolder {
        SkinCompatCheckBox cbProduct;//产品复选框
        ImageView ivProductThumb;//订单产品缩略图
        LinearLayout llProductInfo;//订单产品信息
        TextView tvProductName;//订单产品名称
        TextView tvProductIntro;//订单产品简介
        TextView tvProductPrice;//订单产品价格
        ImageView btnSub;//－按钮
        ImageView btnAdd;//+按钮
        TextView tvProductNum;//订单产品数量
        TextView tvItemTotalPrice;//单种产品总计
        TextView tvDelete;//删除按钮
        OrderViewHolder(View view) {
            super(view);
            cbProduct = (SkinCompatCheckBox) view.findViewById(R.id.cbProduct);
            ivProductThumb = (ImageView) view.findViewById(R.id.ivProductThumb);
            llProductInfo = (LinearLayout) view.findViewById(R.id.llProductInfo);
            tvProductName = (TextView) view.findViewById(R.id.tvProductName);
            tvProductIntro = (TextView) view.findViewById(R.id.tvProductIntro);
            tvProductPrice = (TextView) view.findViewById(R.id.tvProductPrice);
            btnSub = (ImageView) view.findViewById(R.id.btnSub);
            btnAdd = (ImageView) view.findViewById(R.id.btnAdd);
            tvProductNum = (TextView) view.findViewById(R.id.tvProductNum);
            tvItemTotalPrice = (TextView) view.findViewById(R.id.tvItemTotalPrice);
            tvDelete = (TextView) view.findViewById(R.id.tvDelete);
        }
    }

    private DeleteDetailListener deleteDetailListener = null;

    public void setDeleteDetailListener(DeleteDetailListener deleteDetailListener) {
        this.deleteDetailListener = deleteDetailListener;
    }

    public interface DeleteDetailListener{
        void del();
    }
}
