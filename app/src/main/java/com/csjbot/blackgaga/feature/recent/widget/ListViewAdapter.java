package com.csjbot.blackgaga.feature.recent.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.feature.recent.entity.HotelActivityBean;

import java.util.List;

/**
 * @author : chenqi.
 * @e_mail : 1650699704@163.com.
 * @create_time : 2018/3/2.
 * @Package_name: BlackGaGa
 */

public class ListViewAdapter extends ArrayAdapter {
    private int resourceId;
    private List<HotelActivityBean> been;
    private int selectItem = -1;


    public ListViewAdapter(Context context, int textViewResourceId, List<HotelActivityBean> objects) {
        super(context, textViewResourceId, objects);
        this.resourceId = textViewResourceId;
        this.been = objects;
    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
            holder.text = convertView.findViewById(R.id.hotel_activity_item_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (selectItem == position) {
            holder.text.setSelected(true);
            holder.text.setPressed(true);
            holder.text.setBackgroundResource(R.drawable.hotel_activity_new_list_item_click_effect);
        } else {
            holder.text.setSelected(false);
            holder.text.setPressed(false);
            holder.text.setBackgroundResource(R.drawable.hotel_activity_new_list_item_check_unselect);
        }
        holder.text.setText(been.get(position).getData());
        return convertView;
    }

    class ViewHolder {
        TextView text;
    }
}
