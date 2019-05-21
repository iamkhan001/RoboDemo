package com.csjbot.blackgaga.feature.clothing.adapter;

import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.csjbot.blackgaga.GlideApp;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.feature.clothing.bean.ClothListBean;

import java.util.List;

/**
 * @author ShenBen
 * @date 2018/11/12 11:23
 * @email 714081644@qq.com
 */

public class ClothListAdapter extends BaseQuickAdapter<ClothListBean.ResultBean.GoodsListBean, BaseViewHolder> {


    public ClothListAdapter(@LayoutRes int layoutResId, @Nullable List<ClothListBean.ResultBean.GoodsListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ClothListBean.ResultBean.GoodsListBean item) {
        GlideApp.with(mContext).load(item.getGoodsPicture())
                .placeholder(R.drawable.iv_load_clothing_failed)
                .error(R.drawable.iv_load_clothing_failed)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
                .into((ImageView) helper.getView(R.id.iv_item_clothing));
        helper.setText(R.id.tv_item_clothing_name, item.getGoodsName())
                .setText(R.id.tv_item_original_price, "￥" + item.getOriginalPrice())
                .setText(R.id.tv_item_present_price, "￥" + item.getPresentPrice());
        TextView tv = helper.getView(R.id.tv_item_original_price);
        tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

    }
}
