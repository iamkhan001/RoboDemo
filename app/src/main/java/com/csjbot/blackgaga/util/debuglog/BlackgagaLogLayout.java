package com.csjbot.blackgaga.util.debuglog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.csjbot.blackgaga.BaseApplication;
import com.csjbot.blackgaga.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiasuhuei321 on 2017/11/16.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class BlackgagaLogLayout {
    private WindowManager.LayoutParams params;
    private final WindowManager windowManager;
    private RecyclerView logListRv;
    private Context mContext = BaseApplication.getAppContext();
    private final View contentView;

    public static final int V = Log.VERBOSE;
    public static final int W = Log.WARN;
    public static final int E = Log.ERROR;
    public static final int D = Log.DEBUG;
    public static final int A = Log.ASSERT;
    public static final int I = Log.INFO;

    private List<LogBean> logList = new ArrayList<>();

    private static class BlackgagaLogLayoutHolder {
        private static final BlackgagaLogLayout INSTANCE = new BlackgagaLogLayout();
    }

    public BlackgagaLogLayout getInstance() {
        return BlackgagaLogLayoutHolder.INSTANCE;
    }

    private BlackgagaLogLayout() {
        // 设置悬浮窗参数
        params = new WindowManager.LayoutParams();
        windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.format = PixelFormat.RGB_888;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        Point xy = new Point();
        windowManager.getDefaultDisplay().getSize(xy);

        params.width = xy.x / 2;
        params.height = xy.y;

        contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_debug_log, null);
        contentView.findViewById(R.id.logListRv);

        windowManager.addView(contentView, params);
    }

    public void v(String msg){
        LogBean logBean = createLog(msg);
        logBean.color = Color.parseColor("#BBBBBB");
        logList.add(logBean);
    }

    public void w(String msg){
        LogBean logBean = createLog(msg);
        logBean.color = Color.parseColor("#BBBB23");
        logList.add(logBean);
    }

    public void e(String msg){
        LogBean logBean = createLog(msg);
        logBean.color = Color.parseColor("#FF6B68");
        logList.add(logBean);
    }

    public void d(String msg){
        LogBean logBean = createLog(msg);
        logBean.color = Color.parseColor("#0070BB");
        logList.add(logBean);
    }

    public void a(String msg){
        LogBean logBean = createLog(msg);
        logBean.color = Color.parseColor("#8F1B1B");
        logList.add(logBean);
    }

    public void i(String msg){
        LogBean logBean = createLog(msg);
        logBean.color = Color.parseColor("#48BB31");
        logList.add(logBean);
    }

    public void destroy() {
        // 释放资源
    }

    private LogBean createLog(String msg){
        LogBean logBean = new LogBean();
        logBean.msg = msg;
        return logBean;
    }


}
