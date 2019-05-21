package com.csjbot.blackgaga.model.http.product.listeners;

import com.csjbot.blackgaga.cart.entity.RobotMenuListBean;
import com.csjbot.blackgaga.cart.entity.RobotSpListBean;
import com.csjbot.blackgaga.feature.clothing.bean.ClothTypeBean;
import com.csjbot.blackgaga.model.http.bean.ContentBean;
import com.csjbot.blackgaga.model.http.bean.ContentTypeBean;
import com.csjbot.blackgaga.model.http.bean.ProductInfo;
import com.csjbot.blackgaga.model.http.bean.SceneBean;

import java.util.List;

/**
 * @author : chenqi.
 * @e_mail : 1650699704@163.com.
 * @create_time : 2018/1/24.
 * @Package_name: BlackGaGa
 */

public interface ObtainData {
    /**
     * 获取本地产品信息
     */
    ProductInfo getLocalProduct();

    /**
     * 获取到本地导航列表信息
     */

    RobotMenuListBean getMenuList();


    /**
     * 获取到本地产品信息
     */

    RobotSpListBean getSpList(String menuid);

    /**
     * 删除到本地产品信息
     */
    boolean removeSpList(String lan);

    /**
     * 删除到本地导航列表信息
     */
    boolean removeMenuList();

    /**
     * 获取到本地scene数据
     *
     * @return
     */
    SceneBean getSceneRes(String industry);

    /**
     * 删除本地所有皮肤数据不包括skin包
     *
     * @return
     */
    boolean removeSceneRes();

    /**
     * 获取本地所有的产品的对象
     */
    List<RobotSpListBean.ResultBean.ProductBean> getAllSpInformation();

    /**
     * 获取内容
     *
     * @return
     */
    ContentBean getContent();

    /**
     * 获取子级菜单
     *
     * @return
     */
    ContentTypeBean getContentType(String parent_id);
}
