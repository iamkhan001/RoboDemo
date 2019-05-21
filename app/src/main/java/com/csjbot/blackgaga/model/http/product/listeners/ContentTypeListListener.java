package com.csjbot.blackgaga.model.http.product.listeners;

import com.csjbot.blackgaga.model.http.bean.ContentTypeBean;

/**
 * @author : chenqi.
 * @e_mail : 1650699704@163.com.
 * @create_time : 2018/1/23.
 * @Package_name: BlackGaGa
 */

public interface ContentTypeListListener extends BaseBackstageListener{
    /**
     * 通知更新内容
     * @param bean
     */
    void getContentTypeList(ContentTypeBean bean);

    /**
     * 异常上报
     * @param e
     */
    void onContentTypeError(Throwable e);

    /**
     * 更新本地内容
     * @param bean
     */
    void onLocaleContentTypeList(ContentTypeBean bean);

    /**
     * 图片数量
     * @param num
     */
    void ImageSize(int num);
}
