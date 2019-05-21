package com.csjbot.blackgaga.cart.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.localbean.NaviBean;

import java.util.ArrayList;
import java.util.List;


/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/10/16.
 */

public class NaviMenuAdatper extends RecyclerView.Adapter<NaviMenuAdatper.MyViewHolder> {
    public List<NaviBean> loadData = new ArrayList<>();
    public int index = -1;

    public void setItemClickListener(ItemClickListener imgItemClickListener) {
        this.itemClickListener = imgItemClickListener;
    }

    private ItemClickListener itemClickListener = null;
    private Context context;

    public NaviMenuAdatper(Context context) {
        this.context = context;
    }

    public void setLoadData(List<NaviBean> lists) {
        loadData = lists;
    }

    public void setIndex(int index) {
        this.index = index;
        notifyDataSetChanged();
    }

    @Override
    public NaviMenuAdatper.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        int id;
        if (Constants.Scene.CurrentScene.equals(Constants.Scene.XingZheng)
                || Constants.Scene.CurrentScene.equals(Constants.Scene.CheGuanSuo)
                ) {
            id = R.layout.item_menu_navi_new;
        } else {
            id = R.layout.item_menu_navi;
        }
        View view = inflater.inflate(id, parent, false);
        return new NaviMenuAdatper.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NaviMenuAdatper.MyViewHolder holder, final int position) {
        NaviBean naviBean = loadData.get(position);
        holder.naviName.setText(naviBean.getName());
        if (position == index) {
            holder.naviName.setTextColor(context.getResources().getColor(R.color.navi2_menu_item_select_coor));
            holder.naviName.setBackgroundResource(R.drawable.navi2_menu_item_select);
        } else {
            holder.naviName.setTextColor(context.getResources().getColor(R.color.navi2_menu_item_coor));
            holder.naviName.setBackgroundResource(R.drawable.navi2_menu_item);
        }
        holder.layoutItem.setOnClickListener(v -> {
            if (itemClickListener != null && index != position) {
                int temp = index;
                index = position;
                notifyItemChanged(temp);
                notifyItemChanged(index);
            }
            itemClickListener.onClick(loadData.get(position));
        });
    }

    @Override
    public int getItemCount() {
        if (loadData != null) {
            return loadData.size();
        } else {
            return 0;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView naviName;
        private LinearLayout layoutItem;

        //控件
        public MyViewHolder(View itemView) {
            super(itemView);
            naviName = itemView.findViewById(R.id.tv_navi_name);
            layoutItem = itemView.findViewById(R.id.layout_item);
        }
    }

    public interface ItemClickListener {
        void onClick(NaviBean naviBean);
    }
}
