package com.csjbot.blackgaga.router;

import android.app.Application;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.csjbot.blackgaga.home.AdminHomeActivity;
import com.csjbot.blackgaga.home.CSJBotHomeActivity;
import com.csjbot.blackgaga.home.ChaoShiHomeActivity;
import com.csjbot.blackgaga.home.CheGuanSuoHomeActivity;
import com.csjbot.blackgaga.home.FuZhuangHomeActivity;
import com.csjbot.blackgaga.home.JiaDianHomeActivity;
import com.csjbot.blackgaga.home.JiaJuHomeActivity;
import com.csjbot.blackgaga.home.JiaoYu2HomeActivity;
import com.csjbot.blackgaga.home.JiuDianHomeActivity;
import com.csjbot.blackgaga.home.KeJiGuanHomeActivity;
import com.csjbot.blackgaga.home.LvYouHomeActivity;
import com.csjbot.blackgaga.home.MuseumHomeActivity;
import com.csjbot.blackgaga.home.QiCheHomeActivity;
import com.csjbot.blackgaga.home.QiCheZhanHomeActivity;
import com.csjbot.blackgaga.home.QianTaiHomeActivity;
import com.csjbot.blackgaga.home.ShangShaHomeActivity;
import com.csjbot.blackgaga.home.ShiPinHomeActivity;
import com.csjbot.blackgaga.home.ShouJiHomeActivity;
import com.csjbot.blackgaga.home.ShuiWuHomeActivity3;
import com.csjbot.blackgaga.home.StateGridHomeActivity;
import com.csjbot.blackgaga.home.XianYangAirportHomeActivity;
import com.csjbot.blackgaga.home.XieDianHomeActivity;
import com.csjbot.blackgaga.home.YanJingHomeActivity;
import com.csjbot.blackgaga.home.YaoDianHomeActivity;
import com.csjbot.blackgaga.home.YiYuanHomeActivity;
import com.csjbot.blackgaga.home.YinHangHomeActivity;
import com.csjbot.blackgaga.home.ZhanGuanHomeActivity;
import com.csjbot.blackgaga.model.http.bean.ContentBean;
import com.csjbot.blackgaga.model.http.product.ProductProxy;
import com.csjbot.blackgaga.model.http.product.listeners.ContentListListener;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.coshandler.log.Csjlogger;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

import skin.support.SkinCompatManager;

/**
 * Created by xiasuhuei321 on 2017/11/28.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class BRouter {
    /**
     * 获取到现在的主页是哪个
     */
    public static String CURRENTCLASSNAME = "";

    /**
     * 初始化 路由
     */
    public static void init(Application app, boolean isDebug) {
        if (isDebug) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(app);
    }

    private static class BRouterHolder {
        private static final BRouter INSTANCE = new BRouter();
    }

    public static BRouter getInstance() {
        return BRouterHolder.INSTANCE;
    }

    // 应用内跳转
    public BRouterDataCarry build(String url) {
        return new BRouterDataCarry(url);
    }

    /**
     * 根据传入的path进行activity的跳转
     *
     * @param path activity 路径，可见 BRouterPath
     */
    public static void jumpActivity(@Nullable String path) {
        BRouter.getInstance()
                .build(path)
                .navigation();
    }

    /**
     * 根据传入的path进行activity的跳转
     * 带参数跳转
     *
     * @param path activity 路径，可见 BRouterPath
     */
    public static void jumpActivity(@Nullable String path, @Nullable String key, @Nullable Serializable bundle) {
        BRouter.getInstance()
                .build(path)
                .withSerializable(key, bundle)
                .navigation();
    }

    /**
     * 根据传入的path进行activity的跳转
     * 带参数跳转
     *
     * @param path activity 路径，可见 BRouterPath
     */
    public static void jumpActivity(@Nullable String path, @Nullable String key, @Nullable String bundle) {
        BRouter.getInstance()
                .build(path)
                .withString(key, bundle)
                .navigation();
    }

    /**
     * 根据传入的path进行activity的跳转
     * 带参数跳转
     *
     * @param path activity 路径，可见 BRouterPath
     */
    public static void jumpActivity(@Nullable String path, @Nullable String key, @Nullable Parcelable bundle) {
        BRouter.getInstance()
                .build(path)
                .withParcelable(key, bundle)
                .navigation();
    }

    /**
     * 根据传入的path进行activity的跳转
     * 带参数跳转
     */
    public static void jumpActivityByContent(String name) {
        if (TextUtils.isEmpty(name)) {
            return;
        }
        ProductProxy product = ProductProxy.newProxyInstance();
        ContentBean c = product.getContent();
        if (c != null) {
            ContentBean.ResultBean bean = c.getResult();
            List<ContentBean.ResultBean.ContentMessageBean> b = bean.getContentType();
            for (int i = 0; i < b.size(); i++) {
                String loName = b.get(i).getName();
                if (loName.equals(name)) {
                    String uri = b.get(i).getUri();
                    String id = b.get(i).getId();
                    String resUrl = b.get(i).getResUrl();
                    jumpActivity(uri, BRouterKey.CONTENT_KEY,
                            new Gson().toJson(new ContentUrlBody(id, resUrl, b.get(i).getWords())));
                    break;
                }
            }
        }
    }


    /**
     * 实时刷新
     *
     * @param name
     */
    public static void sync(String name) {
        ProductProxy.newProxyInstance().getContent(new ContentListListener() {
            @Override
            public void getContentList(ContentBean bean) {
                BRouter.jumpActivityByContent(name);
            }

            @Override
            public void onContentError(Throwable e) {

            }

            @Override
            public void onLocaleContentList(ContentBean bean) {

            }

            @Override
            public void ImageSize(int num) {

            }

            @Override
            public void loadSuccess(String name) {

            }

            @Override
            public void cacheToMore() {

            }

            @Override
            public void loadFailed(String name) {

            }

            @Override
            public void loadAllSuccess() {
                BRouter.jumpActivityByContent(name);
            }

            @Override
            public void loadError500() {

            }
        });
    }

    /**
     * 跳转到主界面
     */
    public static void toHome() {
        String industry = SharedPreUtil.getString(SharedKey.MAINPAGE, SharedKey.MAINPAGE_KEY, "");
        //如果本地存储的为空 那么就用网络上的
        Csjlogger.error("场景: " + industry);
        jumpActivity(getCheckIndustry(industry), BRouterKey.CLASS_NAME, industry);
    }

    /**
     * 跳转到测试主界面
     */
    public static void toTestHome(String industry) {
        jumpActivity(getCheckIndustry(industry), BRouterKey.CLASS_NAME, industry);
        SkinCompatManager.getInstance().loadSkin(industry + ".skins", new SkinCompatManager.SkinLoaderListener() {

            @Override
            public void onStart() {
                CsjlogProxy.getInstance().warn("开始换肤");
            }

            @Override
            public void onSuccess() {
                CsjlogProxy.getInstance().warn("换肤成功");
            }

            @Override
            public void onFailed(String s) {
                CsjlogProxy.getInstance().warn("换肤失败  " + s);

            }
        }, SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
    }


    /**
     * 通过industry判断是哪个主页
     *
     * @param industry
     * @return
     */
    private static String getCheckIndustry(String industry) {
        String tempURL;
        switch (industry) {
            case "shangsha":
                CURRENTCLASSNAME = ShangShaHomeActivity.class.getSimpleName();
                tempURL = BRouterPath.MAINPAGE_SHANGSHA;
                break;
            case "jiadian":
                CURRENTCLASSNAME = JiaDianHomeActivity.class.getSimpleName();
                tempURL = BRouterPath.MAINPAGE_JIADIAN;
                break;
            case "yaodian":
                CURRENTCLASSNAME = YaoDianHomeActivity.class.getSimpleName();
                tempURL = BRouterPath.MAINPAGE_YAODIAN;
                break;
            case "fuzhuang":
                CURRENTCLASSNAME = FuZhuangHomeActivity.class.getSimpleName();
                tempURL = BRouterPath.MAINPAGE_FUZHUANG;
                break;
            case "chaoshi":
                CURRENTCLASSNAME = ChaoShiHomeActivity.class.getSimpleName();
                tempURL = BRouterPath.MAINPAGE_CHAOSHI;
                break;
            case "qiche":
                CURRENTCLASSNAME = QiCheHomeActivity.class.getSimpleName();
                tempURL = BRouterPath.MAINPAGE_QICHE;
                break;
            case "shipin":
                CURRENTCLASSNAME = ShiPinHomeActivity.class.getSimpleName();
                tempURL = BRouterPath.MAINPAGE_SHIPIN;
                break;
            case "shuiwu":
                CURRENTCLASSNAME = ShuiWuHomeActivity3.class.getSimpleName();
                tempURL = BRouterPath.MAINPAGE_SHUIWU;
                break;
            case "fayuan":
                CURRENTCLASSNAME = ShuiWuHomeActivity3.class.getSimpleName();
                tempURL = BRouterPath.MAINPAGE_SHUIWU;
                break;
            case "jianchayuan":
                CURRENTCLASSNAME = ShuiWuHomeActivity3.class.getSimpleName();
                tempURL = BRouterPath.MAINPAGE_SHUIWU;
                break;
            case "gongan":
                CURRENTCLASSNAME = ShuiWuHomeActivity3.class.getSimpleName();
                tempURL = BRouterPath.MAINPAGE_SHUIWU;
                break;
            case "gongshang":
                CURRENTCLASSNAME = ShuiWuHomeActivity3.class.getSimpleName();
                tempURL = BRouterPath.MAINPAGE_SHUIWU;
                break;
            case "xingzheng":
                CURRENTCLASSNAME = AdminHomeActivity.class.getSimpleName();
                tempURL = BRouterPath.MAINPAGE_XINGZHENG;
                break;
            case "jichang":
                CURRENTCLASSNAME = XianYangAirportHomeActivity.class.getSimpleName();
                tempURL = BRouterPath.MAINPAGE_JICHANG;
                break;
            case "jiudian":
                CURRENTCLASSNAME = JiuDianHomeActivity.class.getSimpleName();
                tempURL = BRouterPath.MAINPAGE_JIUDIAN;
                break;
            case "yinhang":
                tempURL = BRouterPath.MAINPAGE_YINHANG;
                CURRENTCLASSNAME = YinHangHomeActivity.class.getSimpleName();
                break;
            case "chezhan":
                tempURL = BRouterPath.MAINPAGE_QICHEZHAN;
                CURRENTCLASSNAME = QiCheZhanHomeActivity.class.getSimpleName();
                break;
            case "jiaoyu":
                tempURL = BRouterPath.MAINPAGE_JIAOYU2;
                CURRENTCLASSNAME = JiaoYu2HomeActivity.class.getSimpleName();
                break;
            case "zhanguan":
                tempURL = BRouterPath.MAINPAGE_ZHANGUAN;
                CURRENTCLASSNAME = ZhanGuanHomeActivity.class.getSimpleName();
                break;
            case "yiyuan":
                tempURL = BRouterPath.MAINPAGE_YIYUAN;
                CURRENTCLASSNAME = YiYuanHomeActivity.class.getSimpleName();
                break;
            case "lvyou":
                tempURL = BRouterPath.MAINPAGE_LVYOU;
                CURRENTCLASSNAME = LvYouHomeActivity.class.getSimpleName();
                break;
            case "qiantai":
                tempURL = BRouterPath.MAINPAGE_QIANTAI;
                CURRENTCLASSNAME = QianTaiHomeActivity.class.getSimpleName();
                break;
            case "yanjing":
                tempURL = BRouterPath.MAINPAGE_YANJING;
                CURRENTCLASSNAME = YanJingHomeActivity.class.getSimpleName();
                break;
            case "jiaju":
                tempURL = BRouterPath.MAINPAGE_JIAJU;
                CURRENTCLASSNAME = JiaJuHomeActivity.class.getSimpleName();
                break;
            case "kejiguan":
                tempURL = BRouterPath.MAINPAGE_KEJIGUAN;
                CURRENTCLASSNAME = KeJiGuanHomeActivity.class.getSimpleName();
                break;
            case "xiedian":
                tempURL = BRouterPath.MAINPAGE_XIEDIAN;
                CURRENTCLASSNAME = XieDianHomeActivity.class.getSimpleName();
                break;
            case "shouji":
                tempURL = BRouterPath.MAINPAGE_SHOUJI;
                CURRENTCLASSNAME = ShouJiHomeActivity.class.getSimpleName();
                break;
            case "dianwang":
                tempURL = BRouterPath.MAINPAGE_STATE_GRID;
                CURRENTCLASSNAME = StateGridHomeActivity.class.getSimpleName();
                break;
            case "cheguansuo":
                tempURL = BRouterPath.MAINPAGE_CHEGUANSUO;
                CURRENTCLASSNAME = CheGuanSuoHomeActivity.class.getSimpleName();
                break;
            case "bowuguan":
                tempURL = BRouterPath.MAINPAGE_MUSEUM;
                CURRENTCLASSNAME = MuseumHomeActivity.class.getSimpleName();
                break;
            default:
                //默认是home页面
                CsjlogProxy.getInstance().info("-->走了default.跳转默认主页");
                CURRENTCLASSNAME = CSJBotHomeActivity.class.getSimpleName();
                tempURL = BRouterPath.MAINPAGE_CSJBOT;
                break;
        }
        CsjlogProxy.getInstance().info("皮肤industry:-->" + industry);
        return tempURL;
    }

    public static class ContentUrlBody {
        String parentId;
        String resId;
        String words;

        public ContentUrlBody(String parentId, String resId, String words) {
            this.parentId = parentId;
            this.resId = resId;
            this.words = words;
        }

        @Override
        public String toString() {
            return "ContentUrlBody{" +
                    "brouterId='" + parentId + '\'' +
                    ", resId='" + resId + '\'' +
                    '}';
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getResId() {
            return resId;
        }

        public void setResId(String resId) {
            this.resId = resId;
        }

        public void setWords(String words) {
            this.words = words;
        }

        public String getWords() {
            return words;
        }
    }

}
