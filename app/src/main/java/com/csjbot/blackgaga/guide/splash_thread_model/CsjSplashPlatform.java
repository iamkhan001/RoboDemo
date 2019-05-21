package com.csjbot.blackgaga.guide.splash_thread_model;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.csjbot.blackgaga.guide.splash_thread_model.base.BaseMessageListener;
import com.csjbot.blackgaga.guide.splash_thread_model.base.BaseRunnable;
import com.csjbot.blackgaga.util.BlackgagaLogger;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/12/18.
 * 提供外部调用的方法
 */

public class CsjSplashPlatform<T extends BaseRunnable> extends CsjSplashThreadPool<T> {
    public static Handler handler = null;

    public CsjSplashPlatform() {
        handler = new Handler();
    }

    public static CsjSplashPlatform getInstants() {
        return poolFind.CsjSplashPlatform;
    }

    public static class poolFind {
        public static CsjSplashPlatform CsjSplashPlatform = new CsjSplashPlatform();
    }

    public CsjSplashPlatform addRunnable(Class<T> runnable, String tag) {
        try {
            addTh(runnable.newInstance(), tag);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 开启一个线程
     * @param tag
     */
    public CsjSplashPlatform openCsjSplashThread(String tag, ThreadOpenListener listener) {
        if (handler == null) {
            if (listener != null) {
                listener.openFailed();
            }
            return this;
        }
        ThreadPoolBean threadPoolBean = findThreadByTag(tag);
        BlackgagaLogger.warn("chenqi 打开的方法是：" + threadPoolBean.getRunnable().getClass().getSimpleName());
        if (threadPoolBean == null) {
            if (listener != null) {
                listener.openFailed();
            }
            return this;
        }

        if (!threadPoolBean.isOpenRun()) {
            handler.post(threadPoolBean.getRunnable());
            addNewStateThread(tag, true);
            if (listener != null) {
                listener.openSuccess();
            }
        } else {
            if (listener != null) {
                listener.openFailed();
            }
            Log.d("chenqi", "已经执行了");
        }
        return this;
    }



    /**
     * 延迟多少秒执行
     * @param tag
     */
    public CsjSplashPlatform openCsjSplashThread(String tag, long time, ThreadOpenListener listener) {
        if (handler == null) {
            if (listener != null) {
                listener.openFailed();
            }
            return this;
        }
        ThreadPoolBean threadPoolBean = findThreadByTag(tag);
        BlackgagaLogger.warn("chenqi 打开的方法是：" + threadPoolBean.getRunnable().getClass().getSimpleName());
        if (threadPoolBean == null) {
            if (listener != null) {
                listener.openFailed();
            }
            return this;
        }

        if (!threadPoolBean.isOpenRun()) {
            handler.postDelayed(threadPoolBean.getRunnable(),time);
            addNewStateThread(tag, true);
            if (listener != null) {
                listener.openSuccess();
            }
        } else {
            if (listener != null) {
                listener.openFailed();
            }
            Log.d("chenqi", "已经执行了");
        }
        return this;
    }

    /**
     * @param tag runnable的名字
     * @param delay 延迟多少时间开始
     * @param looptime 每隔多少秒循环
     * @param listener 监听是否开启成功
     * @return
     */
    public CsjSplashPlatform openCsjSplashThreadDelayLoop(String tag, long delay, long looptime, int flags, ThreadOpenListener listener) {
        if (handler == null) {
            if (listener != null) {
                listener.openFailed();
            }
            return this;
        }
        ThreadPoolBean threadPoolBean = findThreadByTag(tag);
        if (threadPoolBean == null) {
            if (listener != null) {
                listener.openFailed();
            }
            return this;
        }

        if (!threadPoolBean.isOpenRun()) {
            threadPoolBean.getRunnable().setDelay(true);
            threadPoolBean.getRunnable().setTimes(flags);
            threadPoolBean.getRunnable().setDelayTime(looptime);
            handler.postDelayed(threadPoolBean.getRunnable(), delay);
            addNewStateThread(tag, true);
            if (listener != null) {
                listener.openSuccess();
            }
        } else {
            if (listener != null) {
                listener.openFailed();
            }
        }
        return this;
    }

    /**
     * 通过tag添加关闭的线程
     *
     * @param tag
     */
    public CsjSplashPlatform closeCsjSplashThread(String tag, ThreadCloseListener listener) {
        if (handler == null) {
            if (listener != null) {
                listener.closeFailed();
            }
            return this;
        }
        ThreadPoolBean threadPoolBean = findThreadByTag(tag);
        if (threadPoolBean == null) {
            if (listener != null) {
                listener.closeFailed();
            }
            return this;
        }

        if (threadPoolBean.isOpenRun()) {
            handler.removeCallbacks(threadPoolBean.getRunnable());
            addNewStateThread(tag, false);
            if (listener != null) {
                listener.closeSuccess();
            }
        } else {
            if (listener != null) {
                listener.closeFailed();
            }
        }
        return this;
    }

    public static void sendMessage(Message message) {
        handler.sendMessage(message);
    }

    public <T extends BaseMessageListener> void setMessage(T listener) {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (listener != null) {
                    listener.sendMessage(msg);
                }
            }
        };
    }

    @Override
    public boolean removeRunnable(String tag) {
        ThreadPoolBean threadPoolBean = findThreadByTag(tag);
        handler.removeCallbacks(threadPoolBean.getRunnable());
        findList().remove(threadPoolBean);
        if (threadPoolBean == null) {
            return true;
        } else
            return false;
    }

    @Override
    public boolean removeRunnableAll() {
        if (findList() == null || findList().size() == 0) {
            return true;
        }
        for (int i = 0; i < findList().size(); i++) {
            if (findList().get(i).isOpenRun()) {
                handler.removeCallbacks(findList().get(i).getRunnable());
            }
        }
        findList().clear();
        return findList().size() == 0 ? true : false;
    }

    public interface ThreadCloseListener {
        void closeSuccess();

        void closeFailed();
    }

    public interface ThreadOpenListener {
        void openSuccess();

        void openFailed();
    }
}
