package com.csjbot.blackgaga.home.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.home.bean.HomeIconBean;

import java.util.List;

/**
 * @author ShenBen
 * @date 2018/11/12 16:42
 * @email 714081644@qq.com
 */

public class HomeIconAdapter extends BaseQuickAdapter<HomeIconBean, BaseViewHolder> {

    public HomeIconAdapter(@LayoutRes int layoutResId, @Nullable List<HomeIconBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeIconBean item) {
        helper.setImageResource(R.id.iv, item.getIcon())
                .setText(R.id.tv, item.getTitle());
    }
}
