package com.csjbot.blackgaga.guide;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.csjbot.blackgaga.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/12/14.
 */

public class GuideAdapter extends RecyclerView.Adapter<GuideAdapter.MyViewHolder> {
    /*添加resID*/
    private List<Integer> data = new ArrayList<>();
    private List<Bitmap> datas = new ArrayList<>();
    /**
     * 支持格式
     * bitmap，string，resID，file
     * @param data
     */
    public GuideAdapter(List<Integer> data) {
        this.data = data;
    }

    @Override
    public GuideAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.guide_item, parent, false);
        return new GuideAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GuideAdapter.MyViewHolder holder, int position) {
        holder.imageView.setImageResource(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_name);
        }
    }
}
