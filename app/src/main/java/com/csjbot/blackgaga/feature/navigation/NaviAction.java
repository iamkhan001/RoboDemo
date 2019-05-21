package com.csjbot.blackgaga.feature.navigation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.csjbot.blackgaga.BaseApplication;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.localbean.NaviBean;
import com.csjbot.blackgaga.model.tcp.bean.Position;
import com.csjbot.blackgaga.model.tcp.tts.ISpeak;
import com.csjbot.blackgaga.util.GsonUtils;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.blackgaga.util.TimeoutUtil;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.listener.OnNaviSearchListener;
import com.csjbot.coshandler.listener.OnPositionListener;
import com.csjbot.coshandler.log.Csjlogger;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiasuhuei321 on 2017/11/23.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 * <p>
 * desc：全局的导航控制
 */

public class NaviAction {
    // TODO：回充状态监听

    public boolean hasData = false;
    private List<NaviBean> naviBeanList;
    private TaskStatusManager guideSingleTask = new TaskStatusManager();
    private ISpeak speaker;
    private TimeoutUtil timeoutUtil;
    private Context context = BaseApplication.getAppContext();
    private ArmLooper armLooper = new ArmLooper();
    public NaviBean current = null;

    private List<NaviActionListener> actionListeners = new ArrayList<>();
    private List<NaviEventListener> eventListeners = new ArrayList<>();

    public static final int GUIDE_ALL = 20086;
    public static final int GUIDE_SINGLE = 20087;
    public static final int GUIDE_BACK = 20088;
    public static final int GUIDE_DEFAULT = -11;

    public static final int MSG_TIMEOUT = -20086;
    // 超时消息下发超时
    public static final int MSG_TIMEOUTMSG_TIMEOUT = -20087;

    public int workType = GUIDE_DEFAULT;

    public static final int NO_YINBIN_POINT = 123456;

    private static class NaviActionHolder {
        private static final NaviAction INSTANCE = new NaviAction();
    }

    public volatile boolean isPause = true;

    private NaviAction() {
        timeoutUtil = new TimeoutUtil();
        initPositionListener();
        registerWakeup();
    }

    public void initPositionListener() {
        RobotManager.getInstance().addPositionListener(positionListener);
        Robot.getInstance().registerNaviSearchListener(naviSearchListener);
    }

    public void initData() {
        // 获取导览设置的所有点的数据
        String json = SharedPreUtil.getString(SharedKey.NAVI_NAME, SharedKey.NAVI_KEY);
        if (TextUtils.isEmpty(json)) {
            // 没有数据
            Csjlogger.debug("没有数据");
        } else {
            naviBeanList = GsonUtils.jsonToObject(json, new TypeToken<List<NaviBean>>() {
            }.getType());

            if (naviBeanList == null || naviBeanList.size() <= 0) {
                Csjlogger.debug("没有数据");
                return;
            }
            hasData = true;
            Csjlogger.debug("初始化成功，获取到数据：" + json);
        }
    }

    public static NaviAction getInstance() {
        return NaviActionHolder.INSTANCE;
    }

    public void setCurrent(NaviBean current) {
        this.current = current;
    }

    /**
     * 调用此方法开始导览
     */
    public void guideSingle(NaviBean naviBean) {
        isPause = false;
        // 如果是开始状态
        if (guideSingleTask.workStatus == TaskStatusManager.START) {
            if (speaker != null)
                speaker.startSpeaking(BaseApplication.getAppContext().getString(R.string.working_ing), null);
            return;
        }

        workType = GUIDE_SINGLE;

        Csjlogger.debug("Navi:立即前往");
        switch (guideSingleTask.workStatus) {
            case TaskStatusManager.AWAIT:
                guideSingleTask.start();
                startGuideSingle(naviBean);
                break;

            case TaskStatusManager.PAUSE:
                guideSingleTask.resume();
                resumeGuideSingle();
                break;

            case TaskStatusManager.START:
                guideSingleTask.pause();
                pauseGuideSingle();
                break;

        }

    }

    public void setSpeaker(ISpeak speaker) {
        this.speaker = speaker;
    }

    public void startGuideSingle(NaviBean naviBean) {
        if (speaker != null) {
            speaker.startSpeaking(BaseApplication.getAppContext()
                    .getString(R.string.go_with_me), null);
        }
        current = naviBean;
        timeoutUtil.start(5000, guideSingleListener);
        RobotManager.getInstance().robot.reqProxy.navi(naviBean.toJson());

    }

    public void resumeGuideSingle() {

    }

    public void pauseGuideSingle() {

    }

    private OnPositionListener positionListener = new OnPositionListener() {
        @Override
        public void positionInfo(String json) {

        }

        @Override
        public void moveResult(String json) {
            h.removeMessages(MSG_TIMEOUT);
            h.removeMessages(MSG_TIMEOUTMSG_TIMEOUT);
            if (isPause) return;
            Csjlogger.debug("进入了移动结果回调，返回json：" + json);
            if (workType == GUIDE_BACK) {
                armLooper.stopLooper();
                if (speaker != null) {
                    speaker.startSpeaking(context.getString(R.string.i_am_go_back), null);
                }

            } else {
                try {
                    JSONObject jo = new JSONObject(json);
                    int error_code = jo.optInt("error_code");
                    if (guideSingleTask.workStatus == TaskStatusManager.START) {
                        guideSingleTask.stop();
                    }
                    h.removeMessages(MSG_TIMEOUT);
                    h.removeMessages(MSG_TIMEOUTMSG_TIMEOUT);
                    if (error_code == 0) {
                        // 到达目标点
                        h.post(() -> {
                            if (isPause) return;
                            armLooper.stopLooper();
                            for (NaviActionListener listener : actionListeners) {
                                listener.arrived(current);
                            }
                        });
                    } else {
                        // 未能成功到达目标点
                        // 暂时不区分是否正确到达，暂时也没办法做处理！
//                    Toast.makeText(context, "错误，error_code:" + error_code, Toast.LENGTH_SHORT).show();
                        h.post(() -> {
                            if (isPause) return;
                            armLooper.stopLooper();
                            for (NaviActionListener listener : actionListeners) {
                                listener.arrived(current);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            isPause = true;
        }

        @Override
        public void moveToResult(String json) {
            if (isPause) return;
            Csjlogger.debug("消息下发成功");
            timeoutUtil.cancel();
            for (NaviActionListener listener : actionListeners) {
                listener.sendSuccess();
            }
            if (workType == GUIDE_BACK && speaker != null) {
//                speaker.startSpeaking(context.getString(R.string.navi_finish), null);
            }
            h.sendEmptyMessageDelayed(MSG_TIMEOUT, 20000);
            h.post(() -> armLooper.startLooper());
        }

        @Override
        public void cancelResult(String json) {
            // 取消任务
            h.removeMessages(MSG_TIMEOUT);
            h.removeMessages(MSG_TIMEOUTMSG_TIMEOUT);
            h.removeMessages(CANCEL_TASK);
        }
    };

    Handler h = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TIMEOUT:
                    if (isPause) return;
                    Csjlogger.debug("超时，查询导航状态");
                    moveResultTimeout();
                    break;
                case MSG_TIMEOUTMSG_TIMEOUT:
                    if (isPause) return;
                    Csjlogger.debug("超时消息下发超时");
                    moveResultTimeout();
                    break;
                case CANCEL_TASK:
                    if (isPause) return;
                    Toast.makeText(context, context.getString(R.string.cancel_failed), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    /**
     * 超过 20s 未收到
     */
    private void moveResultTimeout() {
        Robot.getInstance().reqProxy.search();
        h.sendEmptyMessageDelayed(MSG_TIMEOUTMSG_TIMEOUT, 5000);
    }

    public OnNaviSearchListener naviSearchListener = json -> {
        h.post(() -> {
            h.removeMessages(MSG_TIMEOUT);
            h.removeMessages(MSG_TIMEOUTMSG_TIMEOUT);

            if (isPause) return;
            try {
                JSONObject jo = new JSONObject(json);
                int state = jo.optInt("state");
                if (state == 0) {
                    if (guideSingleTask.workStatus == TaskStatusManager.START)
                        back();
                    Csjlogger.debug("Navi：state==0，尝试重新走点");
                } else if (state == 1) {
//                naviTimeout.start(20000, listener);
                    h.sendEmptyMessageDelayed(MSG_TIMEOUT, 20000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    };

    private static final int CANCEL_TASK = 11111111;

    public void cancelTask() {
        h.removeMessages(MSG_TIMEOUT);
        h.removeMessages(MSG_TIMEOUTMSG_TIMEOUT);
        timeoutUtil.cancel();
        h.sendEmptyMessageDelayed(CANCEL_TASK, 5000);
        RobotManager.getInstance().robot.reqProxy.cancelNavi();
    }

    private TimeoutUtil.TimeoutListener guideSingleListener = new TimeoutUtil.TimeoutListener() {
        @Override
        public void timeOut() {
            if (speaker != null) {
                Toast.makeText(context, context.getString(R.string.check_linux), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void registerWakeup() {
        IntentFilter filter = new IntentFilter(Constants.WAKE_UP);
        context.registerReceiver(receiver, filter);
    }

    /**
     * 收到唤醒信号
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (workType == GUIDE_SINGLE && guideSingleTask.workStatus == TaskStatusManager.START) {
//                guideSingleTask.pause();
//                cancelTask();
            }
        }
    };

    // 返回迎宾点
    public void back() {
        isPause = false;
        workType = GUIDE_BACK;
        String json = SharedPreUtil.getString(SharedKey.YINGBIN_NAME, SharedKey.YINGBIN_KEY);
        List<Position> positionList = GsonUtils.jsonToObject(json, new TypeToken<List<Position>>() {
        }.getType());

        if (positionList == null || positionList.size() <= 0) {
            Toast.makeText(context, R.string.no_yinbin, Toast.LENGTH_SHORT).show();
            for (NaviActionListener listener : actionListeners) {
                listener.error(NO_YINBIN_POINT);
            }
            Csjlogger.debug("Navi:尚未设置迎宾点");
        } else {
            Position position = positionList.get(0);
            RobotManager.getInstance().robot.reqProxy.navi(position.toJson());
            Csjlogger.debug(json);
        }
    }

    public List<NaviBean> getData() {
        return naviBeanList;
    }

    public int getSingleWorkState() {
        return guideSingleTask.workStatus;
    }

    public int getWorkType() {
        return workType;
    }

    @Override
    protected void finalize() throws Throwable {
        context.unregisterReceiver(receiver);
        RobotManager.getInstance().removePositionListener(positionListener);
        Robot.getInstance().unregisterNaviSearchListener(naviSearchListener);
        super.finalize();
    }

    public interface NaviActionListener {
        void arrived(NaviBean naviBean);

        void error(int code);

        void sendSuccess();
    }

    public interface NaviEventListener {
        /**
         * 导览开始
         */
        void start();

        /**
         * 导览被暂停
         */
        void pause();

        /**
         * 导览被恢复
         */
        void resume();
    }

    public void registerActionListener(NaviActionListener listener) {
        actionListeners.add(listener);
    }

    public void unregisterActionListener(NaviActionListener listener) {
        actionListeners.remove(listener);
    }

    public void registerEventListener(NaviEventListener listener) {
        eventListeners.add(listener);
    }

    public void unregisterEventListener(NaviEventListener listener) {
        eventListeners.remove(listener);
    }

}
