package com.csjbot.blackgaga.model.http.product.listeners;

/**
 * @author : chenqi.
 * @e_mail : 1650699704@163.com.
 * @create_time : 2018/1/23.
 * @Package_name: BlackGaGa
 */

public interface BaseBackstageListener {
    /**
     * 下载成功
     * @param name
     */
    void loadSuccess(String name);

    /**
     * 内存不足
     */
    void cacheToMore();

    /**
     * 下载失败
     * @param name
     */
    void loadFailed(String name);

    /**
     * 下载全部完成
     */
    void loadAllSuccess();

    /**
     * 500错
     */
    void loadError500();
}
