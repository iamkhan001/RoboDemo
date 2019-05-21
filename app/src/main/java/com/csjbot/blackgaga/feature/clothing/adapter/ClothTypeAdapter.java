package com.csjbot.blackgaga.feature.clothing.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.csjbot.blackgaga.GlideApp;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.feature.clothing.bean.ClothTypeBean;

import java.util.List;

/**
 * @author ShenBen
 * @date 2018/11/13 10:07
 * @email 714081644@qq.com
 */

public class ClothTypeAdapter extends BaseQuickAdapter<ClothTypeBean.ResultBean, BaseViewHolder> {

    public ClothTypeAdapter(@LayoutRes int layoutResId, @Nullable List<ClothTypeBean.ResultBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ClothTypeBean.ResultBean item) {
        GlideApp.with(mContext).load(item.getGoodsPicture())
                .placeholder(R.drawable.iv_load_clothing_failed)
                .error(R.drawable.iv_load_clothing_failed)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
                .into((ImageView) helper.getView(R.id.iv_item_clothing));

        helper.setText(R.id.tv_item_clothing_name, item.getSecondLevel());
    }
}
