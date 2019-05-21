package com.csjbot.blackgaga.cart.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.csjbot.blackgaga.BaseApplication;
import com.csjbot.blackgaga.R;

import java.util.List;


public class MapListAdapter extends BaseAdapter {
    private int checkPosition;
    private List<PoiInfo> dataList;

    public MapListAdapter(int checkPosition, List<PoiInfo> dataList) {
        this.checkPosition = checkPosition;
        this.dataList = dataList;
    }

    public void setCheckposition(int checkPosition) {
        this.checkPosition = checkPosition;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    @SuppressLint({"SetTextI18n", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(BaseApplication.getAppContext()).inflate(R.layout.list_item, null);

            holder.textView = convertView.findViewById(R.id.text_name);
            holder.textAddress = convertView.findViewById(R.id.text_address);
            holder.imageLl = convertView.findViewById(R.id.image);
            holder.tv_distance = convertView.findViewById(R.id.tv_distance);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(dataList.get(position).name);
        holder.textAddress.setText(dataList.get(position).address);
        holder.tv_distance.setText(parent.getContext().getString(R.string.distance) + dataList.get(position).postCode);
        if (checkPosition == position) {
            holder.imageLl.setVisibility(View.VISIBLE);
        } else {
            holder.imageLl.setVisibility(View.GONE);
        }


        return convertView;
    }

    class ViewHolder {
        TextView textView;
        TextView tv_distance;
        TextView textAddress;
        ImageView imageLl;
    }
}
