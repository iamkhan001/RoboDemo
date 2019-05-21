package com.csjbot.blackgaga.feature.clothing.mvp;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

/**
 * @author ShenBen
 * @date 2018/11/12 11:09
 * @email 714081644@qq.com
 */

public interface ClothingListContract {

    interface View {
        void setPresenter(Presenter presenter);

        RecyclerView getRvCloth();

        TextView getSelectCloth();

        void isNoData(boolean isNodata);
    }

    interface Presenter {
        /**
         * 初始化操作
         */
        void init();

        /**
         * 设置商品类型
         *
         * @param goodsStyle
         */
        void setGoodStyyle(String goodsStyle);

        /**
         * 加载数据
         *
         * @param season   季节
         * @param minPrice 最低价
         * @param maxPrice 最高价
         */
        void loadData(String season, double minPrice, double maxPrice);

        /**
         * 上一页
         */
        void previousPage();

        /**
         * 下一页
         */
        void nextPage();

        /**
         * 显示筛选popup
         */
        void showSelectPopup();
    }

}
