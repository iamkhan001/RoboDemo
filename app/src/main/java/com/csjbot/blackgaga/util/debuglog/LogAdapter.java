package com.csjbot.blackgaga.util.debuglog;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by xiasuhuei321 on 2017/11/16.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class LogAdapter extends RecyclerView.Adapter {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

     class BaseViewHolder extends RecyclerView.ViewHolder{

         public BaseViewHolder(View itemView) {
             super(itemView);
         }
     }
}
