package com.csjbot.blackgaga.feature.navigation.map;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
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
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.BindView;

/**
 * Created by jingwc on 2017/9/23.
 */

public class NaviMapActivity extends BaseModuleActivity implements NaviMapContract.View{

    List<NaviBean> mNavis;

    NaviMapContract.Presenter mPresenter;

    Handler mHandle = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
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
        return R.layout.activity_navi_map;
    }

    @Override
    public void init() {
        super.init();
        mPresenter = new NaviMapPresenter();
        mPresenter.initView(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String json = SharedPreUtil.getString(SharedKey.NAVI_NAME,SharedKey.NAVI_KEY);
                mNavis = GsonUtils.jsonToObject(json,new TypeToken<List<NaviBean>>(){}.getType());
                mHandle.obtainMessage(0).sendToTarget();
                if(mNavis != null && mNavis.size()>0){
                    BlackgagaLogger.debug("navis:"+mNavis.size());
                    BlackgagaLogger.debug("json:----->"+json);
                }

            }
        }).start();
    }

    @Override
    public void showNaviMap(){
        if(mNavis == null){
            return;
        }
        for (NaviBean naviBean : mNavis){
            BlackgagaLogger.debug("name:"+naviBean.getName());
            Button button = new Button(this);
            button.setText(naviBean.getName());
            button.setTextColor(Color.BLUE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String json = "{\"x\": "+naviBean.getPos().getX()
                            +",\"y\": "+naviBean.getPos().getY()
                            +",\"z\": "+naviBean.getPos().getZ()
                            +",\"rotation\": "+naviBean.getPos().getRotation()+"}";
                    mPresenter.goNavi(json);
                }
            });
            button.setTranslationX(naviBean.getTranslationX());
            button.setTranslationY(naviBean.getTranslationY());
            rl_root.addView(button);
        }

    }
}
