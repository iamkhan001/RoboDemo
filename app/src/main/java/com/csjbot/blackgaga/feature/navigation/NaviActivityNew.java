package com.csjbot.blackgaga.feature.navigation;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.localbean.NaviBean;
import com.csjbot.blackgaga.router.BRouterKey;
import com.csjbot.blackgaga.router.BRouterPath;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xiasuhuei321 on 2017/12/20.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

@Route(path = BRouterPath.NAVI_MAIN_NEW)
public class NaviActivityNew extends BaseModuleActivity implements
        NaviAction.NaviActionListener, NaviAction.NaviEventListener {
    @BindView(R.id.rl_cancel)
    RelativeLayout rl_cancel;
    @BindView(R.id.rl_start)
    RelativeLayout rl_start;
    @BindView(R.id.iv_btn)
    ImageView iv_btn;
    @BindView(R.id.tv_goImm)
    TextView tv_goImm;
    @BindView(R.id.iv_map)
    ImageView iv_map;
    @BindView(R.id.rl_map)
    RelativeLayout rl_map;
    @BindView(R.id.rl_info)
    RelativeLayout rl_info;
    @BindView(R.id.tv_hint)
    TextView tv_hint;
    @BindView(R.id.tv_navi_name)
    TextView tv_navi_name;
    @BindView(R.id.tv_desc)
    TextView tv_desc;

    // 如果为 true 说明是说了一个没有的点
    @Autowired(name = BRouterKey.FROM_AI_GUIDE)
    boolean isFromAI = false;

    public TaskStatusManager guideSingle;
    private NaviBean current = null;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_navi_new;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 初始化数据
        NaviAction.getInstance().initData();
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {

    }

    @OnClick(R.id.rl_start)
    public void goImm() {
        if (current == null) {
            speak(R.string.no_select_point, true);
        } else {
            NaviAction.getInstance().guideSingle(current);
        }
    }


    /**
     * 开始走点回调
     */
    @Override
    public void start() {

    }

    /**
     *
     */
    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void arrived(NaviBean naviBean) {

    }

    @Override
    public void error(int code) {

    }

    @Override
    public void sendSuccess() {

    }

    /**
     * ui 变为 start 状态
     */
    public void uiStart() {
        tv_goImm.setText(R.string.go_imm);
        iv_btn.setBackgroundResource(R.drawable.navi_start_icon);
    }

    /**
     * ui 变为 resume 状态
     */
    public void uiResume() {
        uiPause();
    }

    public void uiPause() {
        tv_goImm.setText(R.string.pause);
        iv_btn.setBackgroundResource(R.drawable.navi_stop_icon);
    }
}
