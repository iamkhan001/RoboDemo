package com.csjbot.blackgaga.feature.settings.change_skin;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.csjbot.blackgaga.R;

import java.util.ArrayList;

import skin.support.widget.SkinCompatCheckBox;

/**
 * Created by 孙秀艳 on 2017/12/19.
 */

public class ChangeSkinAdapter extends RecyclerView.Adapter<ChangeSkinAdapter.SkinViewHolder>{
    private Context mContext;
    private ArrayList<SkinBean> mLists = new ArrayList<>();
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener) {
        onRecyclerViewItemClickListener = listener;
    }

    public ChangeSkinAdapter(Context context, ArrayList<SkinBean> lists) {
        mContext = context;
        mLists = lists;
    }

    @Override
    public SkinViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SkinViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_change_skin, parent, false));
    }

    @Override
    public void onBindViewHolder(SkinViewHolder holder, int position) {
        SkinBean skinBean = mLists.get(position);
        String title = skinBean.getSkinName();
        int drawableId = skinBean.getDrawableId();
        holder.tvSkinTitle.setText(title);
        holder.ivSkinThumb.setBackgroundResource(drawableId);
        if (skinBean.isChecked()) {
            holder.cbSkin.setChecked(true);
            holder.cbSkin.setButtonDrawable(R.drawable.check_select);
        } else {
            holder.cbSkin.setChecked(false);
            holder.cbSkin.setButtonDrawable(R.drawable.check_unselect);
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecyclerViewItemClickListener.onItemClick(v, position);
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

    //自定义监听事件
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    class SkinViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout;//换肤布局
        SkinCompatCheckBox cbSkin;//皮肤复选框
        ImageView ivSkinThumb;//皮肤缩略图
        TextView tvSkinTitle;//皮肤标题
        SkinViewHolder(View view) {
            super(view);
            layout = view.findViewById(R.id.layout_item);
            cbSkin = (SkinCompatCheckBox) view.findViewById(R.id.cbSkin);
            ivSkinThumb = (ImageView) view.findViewById(R.id.ivSkinThumb);
            tvSkinTitle = (TextView) view.findViewById(R.id.tvTitle);
        }
    }
}
