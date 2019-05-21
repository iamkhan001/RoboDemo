package com.csjbot.blackgaga.guide.splash_thread_model;

import com.csjbot.blackgaga.guide.splash_thread_model.base.BaseRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/12/17.
 * 提供一个基础运行功能存放的工具
 */

public abstract class CsjSplashThreadPool<T extends BaseRunnable> implements CsjSplashThreadInterface<T> {
    private List<ThreadPoolBean> list;

    /**
     * 外部注入Runnable
     */
    public CsjSplashThreadPool() {
        list = new ArrayList<>();
    }

    /**
     * @param runnable 实现Runnable的名称
     * @param tag      标记
     */
    @Override
    public void addTh(T runnable, String tag) {
        //这个是实现了Runnable接口
        if (list.size() == 0) {
            //不允许添加
            ThreadPoolBean threadPoolBean = new ThreadPoolBean(runnable, tag, false);
            list.add(threadPoolBean);//将对象添加到threadPoolBean
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getTag().equals(tag)) {
                    ThreadPoolBean threadPoolBean = new ThreadPoolBean(runnable, tag, false);
                    list.set(i, threadPoolBean);
                    break;
                } else if (i == (list.size() - 1)) {
                    ThreadPoolBean threadPoolBean = new ThreadPoolBean(runnable, tag, false);
                    list.add(threadPoolBean);//将对象添加到threadPoolBean
                    break;
                }
            }

        }
    }

    @Override
    public List<ThreadPoolBean> getRunThreadNow() {
        List<ThreadPoolBean> dd = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isOpenRun()) {
                dd.add(list.get(i));
            } else continue;
        }
        return dd;
    }

    @Override
    public boolean isCloseByTag(String tag) {
        return findThreadByTag(tag).isOpenRun();
    }

    @Override
    public boolean isStartByTag(String tag) {
        return findThreadByTag(tag).isOpenRun();
    }

    @Override
    public ThreadPoolBean findThreadByTag(String tag) {
        ThreadPoolBean text = null;
        for (int i = 0; i < findList().size(); i++) {
            if (findList().get(i).getTag().equals(tag)) {
                text = findList().get(i);
                break;
            }
        }
        return text;
    }

    @Override
    public List<ThreadPoolBean> findList() {
        return list;
    }

    /**
     * 获取到当前集合的size
     *
     * @return
     */

    public int getRunnableSize() {
        return list.size();
    }

    @Override
    public void addNewStateThread(String tag, boolean is) {
        for (int i = 0; i < findList().size(); i++) {
            if (findList().get(i).getTag().equals(tag)) {
                ThreadPoolBean t = new ThreadPoolBean(findList().get(i).getRunnable(), tag, is);
                findList().set(i, t);
            } else continue;
        }
    }
}
