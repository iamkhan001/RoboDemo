package com.csjbot.blackgaga.feature.viphall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.model.http.bean.ContentTypeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ben
 * @date 2018/3/6
 */

public class XianYangVipHallAdapter extends RecyclerView.Adapter<XianYangVipHallAdapter.ViewHolder> {

    private Context mContext;
    private List<ContentTypeBean.ResultBean.ContentTypeMessageBean.MessageListBean> mLists = new ArrayList<>();
    private OnItemClickListener mListener;

    private int mPosition = 0;

    public XianYangVipHallAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<ContentTypeBean.ResultBean.ContentTypeMessageBean.MessageListBean> lists) {
        this.mLists.clear();
        this.mLists.addAll(lists);
        notifyDataSetChanged();
    }

    public void setItemClick(int position){
        this.mPosition=position;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recy_xianyang_viphall, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(mLists.get(position).getName());
        if (mPosition == position) {
            holder.textView.setBackground(mContext.getResources().getDrawable(R.drawable.attr_select));
        } else {
            holder.textView.setBackground(mContext.getResources().getDrawable(R.drawable.attr_slide_bg));
        }
        holder.textView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_item_xianyang_viphall);
        }
    }

    public interface OnItemClickListener {
        /**
         * item点击事件
         * @param position
         */
        void onClick(int position);
    }

}
