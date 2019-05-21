package com.csjbot.blackgaga.feature.Learning;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.csjbot.blackgaga.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  Wql , 2018/3/7 11:49
 */
public class LearningEducationAdatper extends RecyclerView.Adapter<LearningEducationAdatper.ViewHolder> {
    private List<LearnBean> mLearnBeanList=new ArrayList<>();
    private OnItemClickListener learnListener;
    private Context mContext;

    public LearningEducationAdatper(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<LearnBean> list) {
        this.mLearnBeanList.clear();
        this.mLearnBeanList.addAll(list);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.learnListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_learningeducation_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mImageView.setOnClickListener(v -> {
            if (learnListener != null) {
                learnListener.itemClick(position);
            }
        });
        holder.mTextView.setText(mLearnBeanList.get(position).getAppName());
        holder.mImageView.setImageDrawable(mLearnBeanList.get(position).getIcon());
    }

    @Override
    public int getItemCount() {
        return mLearnBeanList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.iv_learning_picture);
            mTextView = itemView.findViewById(R.id.iv_learning_text);
        }
    }

    interface LearningAdatperListener {
        void down();
    }

    interface OnItemClickListener {
        void itemClick(int position);
    }
}
