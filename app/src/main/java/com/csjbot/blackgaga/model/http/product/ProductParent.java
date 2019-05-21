package com.csjbot.blackgaga.model.http.product;

import android.content.Context;

import com.csjbot.blackgaga.cart.entity.RobotMenuListBean;
import com.csjbot.blackgaga.cart.entity.RobotSpListBean;
import com.csjbot.blackgaga.model.http.bean.ContentBean;
import com.csjbot.blackgaga.model.http.bean.SceneBean;
import com.csjbot.blackgaga.model.http.product.listeners.BaseBackstageListener;
import com.csjbot.blackgaga.model.http.product.listeners.ContentListListener;
import com.csjbot.blackgaga.model.http.product.listeners.ContentTypeListListener;
import com.csjbot.blackgaga.model.http.product.listeners.EvalutionListener;
import com.csjbot.blackgaga.model.http.product.listeners.MenuListListener;
import com.csjbot.blackgaga.model.http.product.listeners.OrderCodeUrlListener;
import com.csjbot.blackgaga.model.http.product.listeners.SpListListener;

import java.util.List;

/**
 * @author : chenqi.
 * @e_mail : 1650699704@163.com.
 * @create_time : 2018/1/23.
 * @Package_name: BlackGaGa
 */

public abstract class ProductParent {
    /**
     * 获取产品列表信息
     *
     * @param listener
     */
    public abstract void getRobotMenuList(MenuListListener listener);

    /**
     * 主动加载
     * 根据类名下载和查找
     * 二级加载次加载列表数据
     * 包括主列表和产品列表
     */
    public abstract void robotSpList(String menuid, Context context, SpListListener listener);


    public abstract void getRobotSpListByUrl(String url, String menuid, SpListListener spListListener);

    /**
     * 下载皮肤包和其他资源
     *
     * @param bean
     * @param listener
     */
    public abstract void downLoadScene(SceneBean bean, BaseBackstageListener listener);

    /**
     * 下载产品列表图片
     */
    public abstract void downLoadSpImage(RobotSpListBean productInfo, SpListListener listener);

    /**
     * 下载导航列表图片
     * 下载导航列表的时候把产品列表也更新掉
     */
    public abstract void downLoadMenuImage(RobotMenuListBean productInfo, MenuListListener listener);

    /**
     * 下载产品明细中轮播图片或者视频资源
     */
    public abstract void downLoadSpViewContent(RobotSpListBean.ResultBean.ProductBean productInfo, SpListListener listener);

    /**
     * 下载轮播图片
     */
    public abstract void downLoadLoopViewContent(List<String> imageList, SpListListener listener);

    /**
     * 下载视频资源
     */
    public abstract void downLoadVideoViewContent(String videoUrl, SpListListener listener);

    /**
     *
     * @param json
     * @param listener
     */
    public abstract void generateOrder(String json, OrderCodeUrlListener listener);

    /**
     * 获取评价数据
     * @param json
     * @param listener
     */
    public abstract void sendEvalution(String json, EvalutionListener listener);

    /**
     * 开启应用加载
     * 一级加载次加载列表数据
     * 包括主列表和产品列表
     */
    public abstract void getRobotMenuList(boolean b);

    /**
     * 供外部调用获取场景的方法
     */
    public abstract void getScene(BaseBackstageListener listener);

    /**
     * 下载ContentMessage
     * @param listener
     */
    public abstract void getContent(ContentListListener listener);

    /**
     * 下载子级菜单
     * @param listener
     * @param url
     */
    public abstract void getContentTypeListener(String url,ContentTypeListListener listener);

    /**
     * 下载导航列表图片
     * 下载导航列表的时候把产品列表也更新掉
     */
    public abstract void downLoadContentImage(ContentBean contentBean, ContentListListener listener);

}
