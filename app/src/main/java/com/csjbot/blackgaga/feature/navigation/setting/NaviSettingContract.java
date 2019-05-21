package com.csjbot.blackgaga.feature.navigation.setting;

import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.BaseView;
import com.csjbot.blackgaga.localbean.NaviBean;
import com.csjbot.blackgaga.model.tcp.bean.Position;

import java.io.File;
import java.util.List;

/**
 * Created by 孙秀艳 on 2017/11/2.
 */

public class NaviSettingContract {
    interface Presenter extends BasePresenter<View> {
        void addNaviPoint();//新增导航点
        void saveNaviPointByPosition(NaviBean naviBean, int position);//保存导航点
        void removeNaviPointByPosition(int position);//移除导航点
        void getNaviData();//获取导航数据
        void getYingBinData();//获取迎宾点数据
        void saveYingBinPoint(Position position);//设置迎宾点数据
        void getGuideData();//获取一键导览的数据
        void saveGuideData(List<NaviBean> naviBeanList);//保存一键导览数据
        void getPosition();//获取当前位置
        void uploadMap();
        void saveNaviData();//重新检测一下导航点的数据是否有无效数据
        boolean isGuideData(int postion);//该导航点是否在一键导引中
        void getMusicPath();
        void getTxtPath();
        void getPicPath();
    }

    interface View extends BaseView {
        void addNaviPointItem(NaviBean naviBean);//新增导航点View
        void showNaviMap(List<NaviBean> naviBeanList);//显示地图
        void removeNaviItem(int position);//移除导航点
        void showYingbinData(Position position);
        void showGuideData(List<NaviBean> naviBeanList);
        void setNaviPosition(Position position);
        void setYingbinPosition(Position position);
        void setMapFile(String path);
        void addNotNaviPoint();
        void saveSuccess();
    }
}
