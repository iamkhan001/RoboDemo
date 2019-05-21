package com.csjbot.blackgaga.feature.navigation.map;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.RelativeLayout;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.localbean.NaviBean;
import com.csjbot.blackgaga.util.BlackgagaLogger;
import com.csjbot.blackgaga.util.GsonUtils;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.blackgaga.widget.MoveView;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jingwc on 2017/9/23.
 */

public class NaviConfigMapActivity extends BaseModuleActivity implements NaviConfigMapContract.View{

    List<NaviBean> mNavis;

    List<MoveView> mMoveViews;

    NaviConfigMapContract.Presenter mPresenter;

    Handler mHandle = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            /* 显示地图 */
            showNaviMap();
        }
    };
    @BindView(R.id.rl_root)
    RelativeLayout rl_root;

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_navi_confgi_map;
    }

    @Override
    public void init() {
        super.init();
        mPresenter = new NaviConfigMapPresenter();
        mPresenter.initView(this);
        mMoveViews = new ArrayList<>();
        /* 读取本地地图数据 */
        new Thread(new Runnable() {
            @Override
            public void run() {
                String json = SharedPreUtil.getString(SharedKey.NAVI_NAME,SharedKey.NAVI_KEY);
                mNavis = GsonUtils.jsonToObject(json,new TypeToken<List<NaviBean>>(){}.getType());
                /* 读取完毕,发送handler处理 */
                mHandle.obtainMessage(0).sendToTarget();
                if(mNavis != null && mNavis.size()>0){
                    BlackgagaLogger.debug("navis:"+mNavis.size());
                    BlackgagaLogger.debug("json:----->"+json);
                }

            }
        }).start();
    }

    /**
     * 显示地图
     */
    @Override
    public void showNaviMap(){
        if(mNavis == null){
            return;
        }
        for (NaviBean naviBean : mNavis){
            BlackgagaLogger.debug("name:"+naviBean.getName());
            MoveView moveView = new MoveView(this);
            moveView.setText(naviBean.getName());
            moveView.setTextColor(Color.BLUE);
            moveView.setTag(naviBean);
            /* 设置平移坐标 */
            moveView.setTranslationX(naviBean.getTranslationX());
            moveView.setTranslationY(naviBean.getTranslationY());
            rl_root.addView(moveView);
            mMoveViews.add(moveView);
        }

    }

    /**
     * 保存地图
     */
    @OnClick(R.id.bt_save_map)
    public void saveMap(){
        if(mMoveViews != null && mMoveViews.size()>0){
            for (MoveView moveView: mMoveViews){
                NaviBean naviBean = (NaviBean) moveView.getTag();
                /* 设置平移坐标 */
                naviBean.setTranslationY(moveView.getTranslationY());
                naviBean.setTranslationX(moveView.getTranslationX());
            }
            SharedPreUtil.putString(SharedKey.NAVI_NAME,SharedKey.NAVI_KEY,GsonUtils.objectToJson(mNavis));
        }
    }
}
