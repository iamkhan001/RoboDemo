package com.csjbot.blackgaga.guide.splash_thread_model;


import java.util.List;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/12/17.
 * 定义一些需要使用的方法
 */

public interface CsjSplashThreadInterface<T>{
    /**
     * 添加新的线程
     * @param runnable
     */
    void addTh(T runnable, String tag);

    /**
     * 现在执行的Runnbale是什么
     * @return
     */
    List<ThreadPoolBean> getRunThreadNow();


    /**
     * 检查某个runnable是否关闭
     * @param tag
     */
    boolean isCloseByTag(String tag);

    /**
     * 检查某个runnable是否打开
     * @param tag
     */
    boolean isStartByTag(String tag);

    /**
     * 查找某个runnable对象
     * @param tag
     * @return
     */
    ThreadPoolBean findThreadByTag(String tag);

    /**
     * 查找所有的list
     * @return
     */
    List<ThreadPoolBean> findList();

    /**
     * 关闭并且删除list中的对象
     */
    boolean removeRunnable(String tag);

    /**
     * 关闭并且删除list中的所有任务
     */
    boolean removeRunnableAll();

    /**
     * 保存状态
     */
    void addNewStateThread(String tag, boolean is);
}
