package com.csjbot.blackgaga.feature.content3;

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
 * Created by 孙秀艳 on 2018/2/1.
 */
public class Content3Adapter extends RecyclerView.Adapter<Content3Adapter.BankQueryViewHolder>{
    private Context mContext;
    private List<ContentTypeBean.ResultBean.ContentTypeMessageBean.MessageListBean> mLists = new ArrayList<>();
    private Content3Adapter.OnItemClickListener onItemClickListener;
    private int defPosition = -1;

    //自定义监听事件
    public interface OnItemClickListener {
        void onItemClick(View view, int position, String speak);
    }

    public void setOnItemClickListener(Content3Adapter.OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public Content3Adapter(Context context, List<ContentTypeBean.ResultBean.ContentTypeMessageBean.MessageListBean> lists) {
        mContext = context;
        mLists = lists;
    }

    @Override
    public Content3Adapter.BankQueryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BankQueryViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_content3, parent, false));
    }

    @Override
    public void onBindViewHolder(BankQueryViewHolder holder, int position) {
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
        holder.tvActivityNameSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v, position, mLists.get(position).getSpeechScript());
            }
        });
        holder.tvActivityNameUnsel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v, position, mLists.get(position).getSpeechScript());
            }
        });
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

    class BankQueryViewHolder extends RecyclerView.ViewHolder {
        TextView tvActivityNameSelect;//活动名称 选中状态
        TextView tvActivityNameUnsel;//活动名称 未选中状态
        BankQueryViewHolder(View view) {
            super(view);
            tvActivityNameSelect = (TextView) view.findViewById(R.id.tvActivityNameSelect);
            tvActivityNameUnsel = view.findViewById(R.id.tvActivityNameUnsel);
        }
    }
}
