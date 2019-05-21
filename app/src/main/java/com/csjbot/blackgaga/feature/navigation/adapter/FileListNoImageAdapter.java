package com.csjbot.blackgaga.feature.navigation.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.csjbot.blackgaga.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by xiasuhuei321 on 2017/11/3.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class FileListNoImageAdapter extends BaseAdapter {

    List<File> files;

    public void initData(List<File> files) {
        this.files = files;
    }

    @Override
    public int getCount() {
        return files == null ? 0 : files.size();
    }

    @Override
    public Object getItem(int position) {
        return files.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file, null);
        }

        File f = files.get(position);
        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tv_time = (TextView) convertView.findViewById(R.id.tv_time);

        // 设置文件名
        tv_name.setText(f.getName());
        // 设置文件创建时间
        String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date(f.lastModified()));
        tv_time.setText(parent.getContext().getString(R.string.file_create_time) + time);

        return convertView;
    }
}
