package com.csjbot.blackgaga.base;


/**
 * Created by xiasuhuei321 on 2017/8/11.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public interface BasePresenter<T extends BaseView> {
    T getView();

    void initView(T view);

    void releaseView();
}
