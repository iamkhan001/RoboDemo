package com.csjbot.blackgaga.feature.recent.widget;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.csjbot.blackgaga.GlideApp;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.feature.recent.Util;
import com.csjbot.blackgaga.feature.recent.entity.HotelActivityBean;

import java.util.List;

/**
 * @author : chenqi.
 * @e_mail : 1650699704@163.com.
 * @create_time : 2018/3/5.
 * @Package_name: BlackGaGa
 */

public class CarouselReAdapter extends RecyclerView.Adapter<CarouselReAdapter.CarouselReHolder> {
    //上下文
    private Context context;
    //图片的地址
    private List<HotelActivityBean.HotelResultBean> beanList;
    private Handler handler;
    private CarouselReBodyListener listener;


    public CarouselReAdapter(Context context, List<HotelActivityBean.HotelResultBean> paths, Handler handler) {
        this.context = context;
        this.beanList = paths;
        this.handler = handler;
    }

    public void setCarouselReBodyListener(CarouselReBodyListener listener) {
        this.listener = listener;
    }

    @Override
    public CarouselReHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_activity_carousel_item, parent, false);
        return new CarouselReHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(CarouselReHolder holder, int position) {
        int realPosition = position % beanList.size();
        GlideApp.with(context).load(Util.BASEPATH + beanList.get(realPosition).getExhPhotos()).error(R.mipmap.ic_launcher).into(holder.imageView);
        holder.hotelIndexActivity.setText(beanList.get(realPosition).getTitle());
        holder.hotelIndexPlace.setText(beanList.get(realPosition).getPlace());
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    class CarouselReHolder extends RecyclerView.ViewHolder implements View.OnTouchListener, View.OnClickListener {
        private ImageView imageView;
        private TextView hotelIndexActivity;
        private TextView hotelIndexPlace;
        private CarouselReBodyListener listener;

        public CarouselReHolder(View itemView, CarouselReBodyListener listener) {
            super(itemView);
            this.listener = listener;
            itemView.setOnTouchListener(this);
            itemView.setOnClickListener(this);
            imageView = itemView.findViewById(R.id.item_image);
            hotelIndexActivity = itemView.findViewById(R.id.hotel_index_activity);
            hotelIndexPlace = itemView.findViewById(R.id.hotel_index_place);
        }

        long pressStartTime;
        float pressedX;
        float pressedY;
        private boolean mScrolling;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressStartTime = System.currentTimeMillis();
                    pressedX = event.getX();
                    pressedY = event.getY();
                    mScrolling = false;
                    break;
                case MotionEvent.ACTION_UP:
                    if (beanList.size() > 1) {
                        handler.removeCallbacksAndMessages(null);
                        handler.sendEmptyMessageDelayed(0, 2000);
                    }
                    mScrolling = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (beanList.size() > 1) {
                        handler.removeCallbacksAndMessages(null);
                        handler.sendEmptyMessageDelayed(0, 2000);
                    }
                    if (Math.abs(pressedX - event.getX()) >= ViewConfiguration.get(context).getScaledTouchSlop() || Math.abs(pressedY - event.getY()) >= ViewConfiguration.get(context).getScaledTouchSlop()) {
                        mScrolling = true;
                    } else {
                        mScrolling = false;
                    }
                    break;
            }
            return mScrolling;
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.down(getPosition() % beanList.size());
            }
        }
    }

    public interface CarouselReBodyListener {
        void down(int realPosition);
    }
}
