package com.csjbot.blackgaga.feature.navigation.setting;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.localbean.NaviBean;
import com.csjbot.blackgaga.util.BlackgagaLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 孙秀艳 on 2017/11/3.
 */

public class NaviGuideListAdapter extends RecyclerView.Adapter<NaviGuideListAdapter.NaviGuideHolder>{
    private Context mContext;
    private List<NaviBean> mLists = new ArrayList<>();

    public NaviGuideListAdapter(Context context, ArrayList<NaviBean> lists) {
        mContext = context;
        mLists = lists;
    }

    public NaviGuideListAdapter(Context context) {
        mContext = context;
    }

    public void setGuideList(List<NaviBean> naviBeanList) {
        mLists = naviBeanList;
        notifyDataSetChanged();
    }

    @Override
    public NaviGuideHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NaviGuideHolder(LayoutInflater.from(mContext).inflate(R.layout.item_guide_list, parent, false));
    }

    @Override
    public void onBindViewHolder(NaviGuideHolder holder, int position) {
        NaviBean naviBean = mLists.get(position);
        holder.tvGuideIndex.setText(/*position+1+*/"");
        holder.tvGuideName.setText(limitNaviName(naviBean.getName()));
    }

    @Override
    public int getItemCount() {
        if (mLists == null || mLists.size() <= 0) {
            return 0;
        }
        return mLists.size();
    }

    private String limitNaviName(String name) {
        String naviName = name;
        if (name != null) {
            if (name.length() > 18) {
                naviName = name.substring(0, 18) + "...";
            }
        }
        return naviName;
    }

    class NaviGuideHolder extends RecyclerView.ViewHolder {
        TextView tvGuideIndex;//单种产品总计
        TextView tvGuideName;//删除按钮
        NaviGuideHolder(View view) {
            super(view);
            tvGuideIndex = (TextView) view.findViewById(R.id.tvGuideIndex);
            tvGuideName = (TextView) view.findViewById(R.id.tvGuideName);
        }
    }
}
