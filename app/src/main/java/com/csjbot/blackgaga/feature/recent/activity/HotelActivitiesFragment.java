package com.csjbot.blackgaga.feature.recent.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.csjbot.blackgaga.GlideApp;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.feature.recent.Util;
import com.csjbot.blackgaga.feature.recent.entity.HotelActivityBean;
import com.csjbot.blackgaga.feature.recent.widget.CarouselReAdapter;
import com.csjbot.blackgaga.feature.recent.widget.SmoothLinearLayoutManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author : chenqi.
 * @e_mail : 1650699704@163.com.
 * @create_time : 2018/3/1.
 * @Package_name: BlackGaGa
 */

public class HotelActivitiesFragment extends Fragment {
    @BindView(R.id.pager)
    RecyclerView recyclerView;
    @BindView(R.id.hotel_activity_detail)
    LinearLayout hotelActivityDetail;
    @BindView(R.id.viewPager_show)
    RelativeLayout viewShow;
    @BindView(R.id.dots)
    LinearLayout dots;

    @BindView(R.id.hotel_activity_title)
    TextView hotelActivityTitle;
    @BindView(R.id.hotel_activity_data)
    TextView hotelActivityData;
    @BindView(R.id.hotel_activity_place)
    TextView hotelActivityPlace;
    @BindView(R.id.hotel_activity_ns)
    TextView hotelActivityNs;
    @BindView(R.id.hotel_activity_detail_photo)
    ImageView hotelActivityDetailPhoto;

    private List<ImageView> dotList;//显示点View的集合
    private CarouselReAdapter adapter;
    private List<HotelActivityBean.HotelResultBean> bean;
    private Unbinder unbinder;
    private SmoothLinearLayoutManager layoutManager;
    private int index = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hotel_activity_fragment, null);
        unbinder = ButterKnife.bind(this, view);
        Bundle bundle = getArguments();//从activity传过来的Bundle
        if (bundle != null) {
            bean = new Gson().fromJson(bundle.getString("hotelbody"), new TypeToken<List<HotelActivityBean.HotelResultBean>>() {
            }.getType());
            index = bundle.getInt("bean");
        }
        initView();
        return view;
    }

    private void initView() {
        initDots();
        if (bean.size() == 1) {
            recyclerView.setNestedScrollingEnabled(false);
        }
        adapter = new CarouselReAdapter(getContext(), bean, handler);
        layoutManager = new SmoothLinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(bean.size() * 10);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        initListener();
    }

    private void initListener() {
        if (index > -1) {
            toDetail();
        } else {
            sendDelayMessage();
        }
        //点击回到轮播页
        hotelActivityDetail.setOnClickListener(v -> {
            viewShow.setVisibility(View.VISIBLE);
            hotelActivityDetail.setVisibility(View.GONE);
            sendDelayMessage();
        });
        //跳到详情也
        adapter.setCarouselReBodyListener(p -> {
            stopDelayMessage();
            index = p;
            toDetail();
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取第一个可见view的位置
                    int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
                    System.out.println("chenqi  跳转的position = ");// 遍历小点的集合
                    for (int i = 0; i < dotList.size(); i++) {
                        // 如果当前的索引值和i相等
                        if (firstItemPosition % dotList.size() == i) {
                            // 设置小点为亮色
                            dotList.get(i).setImageResource(R.drawable.focus);
                        } else {
                            // 否则暗色
                            dotList.get(i).setImageResource(R.drawable.nomal);
                        }
                    }
                }
            }
        });
    }

    private void toDetail() {
        viewShow.setVisibility(View.GONE);
        hotelActivityDetail.setVisibility(View.VISIBLE);
        hotelActivityTitle.setText(bean.get(index).getTitle());
        hotelActivityData.setText(getString(R.string.time) + bean.get(index).getStartData() + "-" + bean.get(index).getEndData());
        hotelActivityPlace.setText(getString(R.string.place) + bean.get(index).getPlace());
        hotelActivityNs.setText("(" + bean.get(index).getNaviString() + ")");
        GlideApp.with(getActivity()).load(Util.BASEPATH + bean.get(index).getNaviPhoto()).error(R.mipmap.ic_launcher).into(hotelActivityDetailPhoto);
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {// 先去获取到当前条目索引
            recyclerView.smoothScrollToPosition(layoutManager.findFirstVisibleItemPosition() + 1);
            sendDelayMessage();
        }
    };

    /**
     * 发送延时消息
     */
    private void sendDelayMessage() {
        if (bean.size() > 1) {
            handler.sendEmptyMessageDelayed(0, 2000);
        }
    }

    /**
     * 发送延时消息
     */
    private void stopDelayMessage() {
        if (bean.size() > 1) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    private void initDots() {
        dotList = new ArrayList<>();
        int size = bean.size();
        if (size > 1) {
            for (int i = 0; i < size; i++) {
                ImageView imageView = new ImageView(getContext());
                if (i == 0) {
                    imageView.setImageResource(R.drawable.focus);
                } else {
                    imageView.setImageResource(R.drawable.nomal);
                }
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
                params.setMargins(5, 0, 5, 0);
                dots.addView(imageView, params);
                dotList.add(imageView);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopDelayMessage();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopDelayMessage();
        recyclerView.removeAllViews();
        recyclerView.removeCallbacks(null);
        unbinder.unbind();
    }
}
