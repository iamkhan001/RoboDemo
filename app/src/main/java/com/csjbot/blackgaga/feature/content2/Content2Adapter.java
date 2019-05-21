package com.csjbot.blackgaga.feature.content2;

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
 * Created by 孙秀艳 on 2018/1/24.
 */

public class Content2Adapter extends RecyclerView.Adapter<Content2Adapter.TodayActivityViewHolder>{
    private Context mContext;
    private List<ContentTypeBean.ResultBean.ContentTypeMessageBean.MessageListBean> mLists = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private int defPosition = -1;

    //自定义监听事件
    public interface OnItemClickListener {
        void onItemClick(View view, int position, String speak);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public Content2Adapter(Context context, List<ContentTypeBean.ResultBean.ContentTypeMessageBean.MessageListBean> lists) {
        mContext = context;
        mLists = lists;
    }

    @Override
    public TodayActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TodayActivityViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_content2, parent, false));
    }

    @Override
    public void onBindViewHolder(TodayActivityViewHolder holder, int position) {
        String activityName = mLists.get(position).getName();
        holder.tvActivityNameSelect.setText(activityName);
        holder.tvActivityNameUnsel.setText(activityName);
        if (defPosition == position) {
            holder.tvActivityNameSelect.setVisibility(View.VISIBLE);
            holder.tvActivityNameUnsel.setVisibility(View.GONE);
        } else {
            holder.tvActivityNameUnsel.setVisibility(View.VISIBLE);
            holder.tvActivityNameSelect.setVisibility(View.GONE);
        }
        holder.tvActivityNameSelect.setOnClickListener(v -> onItemClickListener.onItemClick(v, position, mLists.get(position).getSpeechScript()));
        holder.tvActivityNameUnsel.setOnClickListener(v -> onItemClickListener.onItemClick(v, position, mLists.get(position).getSpeechScript()));
    }

    @Override
    public int getItemCount() {
        if (mLists == null || mLists.size() <= 0) {
            return 0;
        }
        return mLists.size();
    }

    public void setItemClickBg(int position) {
        defPosition = position;
        notifyDataSetChanged();
    }

    public void setmLists(ArrayList<ContentTypeBean.ResultBean.ContentTypeMessageBean.MessageListBean> lists) {
        this.mLists = lists;
        notifyDataSetChanged();
    }

    class TodayActivityViewHolder extends RecyclerView.ViewHolder {
        TextView tvActivityNameSelect;//活动名称 选中状态
        TextView tvActivityNameUnsel;//活动名称 未选中状态
        TodayActivityViewHolder(View view) {
            super(view);
            tvActivityNameSelect = view.findViewById(R.id.tvActivityNameSelect);
            tvActivityNameUnsel = view.findViewById(R.id.tvActivityNameUnsel);
        }
    }
}
