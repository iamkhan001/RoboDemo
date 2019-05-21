package com.csjbot.blackgaga.feature.navigation;

import android.content.Context;
import android.widget.Toast;

import com.csjbot.blackgaga.BaseApplication;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.localbean.NaviBean;
import com.csjbot.blackgaga.model.tcp.tts.ISpeak;
import com.csjbot.blackgaga.util.TimeoutUtil;

/**
 * Created by xiasuhuei321 on 2017/12/8.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class NaviGuideSingleTask extends TaskStatusManager {
    private NaviBean naviBean;
    private ISpeak speaker;
    private TimeoutUtil timeoutUtil = new TimeoutUtil();
    private Context context = BaseApplication.getAppContext();

    private TimeoutUtil.TimeoutListener listener = () -> Toast.makeText(context, context.getString(R.string.check_linux), Toast.LENGTH_SHORT).show();

    public void init(ISpeak speaker) {
        this.speaker = speaker;
    }

    /**
     * 开始工作
     */
    @Override
    public void start() {
        super.start();
        if (naviBean != null) {
            startGuideSingle(naviBean);
        }
    }

    /**
     * 设置点
     */
    public void setNaviBean(NaviBean bean) {
        this.naviBean = bean;
        timeoutUtil.start(5000, listener);

    }

    /**
     * 开始单点导览
     *
     * @param naviBean 需要去的点
     */
    private void startGuideSingle(NaviBean naviBean) {

    }
}
