package com.csjbot.blackgaga.feature.recent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.ai.HotelActivityAI;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.feature.recent.Util;
import com.csjbot.blackgaga.feature.recent.entity.HotelActivityBean;
import com.csjbot.blackgaga.feature.recent.widget.ListViewAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author : chenqi.
 * @e_mail : 1650699704@163.com.
 * @create_time : 2018/3/1.
 * @Package_name: BlackGaGa
 */
public class HotelActivitiesActivity extends BaseModuleActivity {
    @BindView(R.id.hotel_activity_left_list)
    ListView hotelActivityLeftList;

    @BindView(R.id.body_no_data)
    TextView bodyNoData;
    @BindView(R.id.in_data_setting)
    Button inDataSetting;
    @BindView(R.id.hotel_activity_body_layout)
    LinearLayout hotelActivityBodyLayout;
    @BindView(R.id.no_data_view_body_layout)
    LinearLayout noDataViewBodyLayout;

    HotelActivityAI mAI;
    @BindView(R.id.hotel_activity_button_top)
    ImageView hotelActivityButtonTop;
    @BindView(R.id.hotel_activity_button_bottom)
    ImageView hotelActivityButtonBottom;

    private ListViewAdapter adapter;
    private List<HotelActivityBean> list = new ArrayList<>();

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void init() {
        super.init();
        getTitleView().setBackVisibility(View.VISIBLE);
        getChatView().clearChatMsg();
        initView();
        initSpeak();
        mAI = new HotelActivityAI();
        mAI.initAI(this);
        mAI.setBean(list);
    }

    /**
     * question:在没有数据的时候
     */
    private void initView() {
        list = Util.getData();
        if (list.size() != 0) {
            adapter = new ListViewAdapter(this, R.layout.hotel_activities_listview_item, list);
            adapter.setSelectItem(0);
            refreshFragment(list.get(0), -1);
            hotelActivityLeftList.setAdapter(adapter);
            hotelActivityLeftList.setOnItemClickListener((parent, view, position, id) -> {
                refreshFragment(list.get(position), -1);
                adapter.setSelectItem(position);
                adapter.notifyDataSetChanged();
            });
            initButton();
            hotelActivityButtonTop.setOnClickListener((View v) -> hotelActivityLeftList.smoothScrollBy(-hotelActivityLeftList.getHeight(), 200));
            hotelActivityButtonBottom.setOnClickListener((View v) -> hotelActivityLeftList.smoothScrollBy(hotelActivityLeftList.getHeight(), 200));
        } else {
            noDataViewBodyLayout.setVisibility(View.VISIBLE);
            hotelActivityBodyLayout.setVisibility(View.GONE);
            bodyNoData.setText(R.string.no_activity);
            inDataSetting.setVisibility(View.GONE);
        }
    }

    private void initButton() {
        if (list.size() > 5) {
            hotelActivityButtonTop.setEnabled(true);
            hotelActivityButtonBottom.setEnabled(true);
            hotelActivityButtonTop.setVisibility(View.VISIBLE);
            hotelActivityButtonBottom.setVisibility(View.VISIBLE);
        } else {
            hotelActivityButtonTop.setEnabled(false);
            hotelActivityButtonBottom.setEnabled(false);
            hotelActivityButtonTop.setVisibility(View.INVISIBLE);
            hotelActivityButtonBottom.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public boolean isOpenChat() {
        return true;
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }
        mAI.getIntent(text);
        if (!mAI.isGet) {
            //没有匹配到
            prattle(answerText);
        }
        return true;
    }

    /**
     * 进入今日活动页面话术
     */
    public void initSpeak() {
        String actionString = getString(R.string.hotel_activity_speech2);
        new Handler().postDelayed(() -> {
            prattle(actionString);
        }, 500);
    }

    public void speakMsg(HotelActivityBean bean, int num) {
        new Handler().postDelayed(() -> {
            int index = list.indexOf(bean);
            adapter.setSelectItem(index);
            adapter.notifyDataSetChanged();
            hotelActivityLeftList.setSelection(index);
            HotelActivityBean.HotelResultBean hotelResultBean = bean.getBean().get(num);
            refreshFragment(bean, num);
            String text = hotelResultBean.getNaviString();
            if (text != null) {
                speak(text);
                setRobotChatMsg(text);
            }
        }, 500);
    }

    public void refreshFragment(HotelActivityBean bean, int num) {
        FragmentManager FM = getSupportFragmentManager();
        FragmentTransaction transaction = FM.beginTransaction();
        HotelActivitiesFragment f1 = new HotelActivitiesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("hotelbody", new Gson().toJson(bean.getBean()));
        bundle.putInt("bean", num);
        f1.setArguments(bundle);
        transaction.replace(R.id.fragment, f1);
        transaction.commit();
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_hotel_activities;
    }
}
