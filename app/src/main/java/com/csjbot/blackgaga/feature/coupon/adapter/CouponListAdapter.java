package com.csjbot.blackgaga.feature.coupon.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.csjbot.blackgaga.R;

/**
 * Created by xiasuhuei321 on 2017/10/19.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class CouponListAdapter extends RecyclerView.Adapter {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coupon, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SimpleViewHolder h = (SimpleViewHolder) holder;

        if(position == 1){
            h.v.setBackgroundResource(R.drawable.coupon_blue_bg);
            h.useHint.setTextColor(Color.parseColor("#0074d9"));
            h.icon1.setImageResource(R.drawable.icon_date_blue);
        }else if(position == 2){
            h.v.setBackgroundResource(R.drawable.coupon_purple_bg);
            h.useHint.setTextColor(Color.parseColor("#583289"));
            h.icon1.setImageResource(R.drawable.icon_date_purple);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    class SimpleViewHolder extends RecyclerView.ViewHolder {
        private View v;
        private TextView useHint;
        private ImageView icon1;
        public SimpleViewHolder(View itemView) {
            super(itemView);
            v = itemView.findViewById(R.id.ll_container);
            useHint = (TextView)itemView.findViewById(R.id.tv_useHint);
            icon1 = (ImageView)itemView.findViewById(R.id.iv_icon1);
        }
    }
}
