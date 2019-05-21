package com.csjbot.blackgaga.feature.settings.synchronous_data_setting;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.widget.BubbleProgressBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/10/20.
 */

public class SynchronousDataActivity extends BaseModuleActivity implements SynchronousDataContract.view {

    @BindView(R.id.syn)
    Button syn;
    @BindView(R.id.pb)
    BubbleProgressBar seekBar;
    @BindView(R.id.show_syc_error)
    TextView showSycError;
    private SynchronousDataContract.presenter presenter;

    private static final int MSG_PROGRESS_UPDATE = 0x110;

    int progress = 0;

    @Override
    public int getLayoutId() {
        return (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)|| BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))                ?  R.layout.vertical_activity_synchronous_data : R.layout.activity_synchronous_data;
    }

    @Override
    public void init() {
        super.init();
        presenter = new SynchronousDataPresenter(this);
        presenter.initView(this);
        getTitleView().setSettingsVisibility(View.GONE);
        getTitleView().setBackVisibility(View.VISIBLE);
//        progress = seekBar.getProgress();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.stopUpdate();
    }

    @OnClick(R.id.syn)
    public void actionSyn() {
        mHandler.sendEmptyMessage(MSG_PROGRESS_UPDATE);
        //点击同步
        presenter.synMenu();
        syn.setText(R.string.syc_is);
        seekBar.setProgress(0);
        syn.setEnabled(false);
        showSycInfor(getString(R.string.syc_menu_now));
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }


    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    protected void onResume() {
        super.onResume();
        showSycError.setVisibility(View.GONE);
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
    }

    @Override
    public void menuSuccess(int base) {
        showSycInfor(getString(R.string.menu_down_success_sp_begain));
        seekBar.setProgress(base);
        presenter.synSp();
    }

    @Override
    public void updatePr(int num) {
        seekBar.setProgress(num);
    }

    @Override
    public void spSuccess(int base) {
        syn.setEnabled(false);
        seekBar.setProgress(base);
        showSycInfor(getString(R.string.synchronous_data_success));
        syn.setText(R.string.syc_over);
        //同步数据成功

        Constants.ProductKeyWord.initKeywords();
    }

    private void showSycInfor(String msg) {
        showSycError.setVisibility(View.VISIBLE);
        showSycError.setText(msg);
    }

    @Override
    public void menuFailed() {
        syn.setText(R.string.synch_dedail);
        showSycInfor(getString(R.string.menu_down_failed));
        syn.setEnabled(true);
    }

    @Override
    public void spFailed() {
        syn.setEnabled(true);
        syn.setText(R.string.synch_dedail);
        showSycInfor(getString(R.string.sp_down_failed));
    }

    @Override
    public void cacheError() {
        syn.setEnabled(true);
        syn.setText(R.string.synch_dedail);
        showSycInfor(getString(R.string.cache_error));
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (progress >= 100) {
                mHandler.removeMessages(MSG_PROGRESS_UPDATE);
            }
            mHandler.sendEmptyMessageDelayed(MSG_PROGRESS_UPDATE, 100);
        }
    };
}
