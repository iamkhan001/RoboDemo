package com.csjbot.blackgaga.router;

import com.csjbot.blackgaga.feature.clothing.ClothingDetailsActivity;
import com.csjbot.blackgaga.feature.clothing.ClothingListActivity;
import com.csjbot.blackgaga.feature.clothing.ClothingTypeActivity;

/**
 * Created by xiasuhuei321 on 2017/12/6.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 * <p>
 * desc:保存了所有 Activity 对应的路径，所有路径至少两级
 * 如：/feature/product
 */

public class BRouterPath {
    // 这个分级表示 功能
    private static final String NEW_RETAIL = "/new_retail";
    // 这个分级表示 信息展示
    private static final String INFO = "/info";

    // 产品
    private static final String PRODUCT = "/product";
    // 导航
    private static final String NAVI = "/navi";
    // 娱乐
    private static final String PLAY = "/play";
    // 人脸
    private static final String FACE = "/face";
    // 周边
    private static final String NEARBY = "/nearby";
    // 设置
    private static final String SETTING = "/setting";

    // 首页
    private static final String MAINPAGE = "/mainpage";

    //内容种类（webview[酒店]）
    private static final String JIUDIAN = "/hotel_type";

    /**
     * 关于我们
     *
     * @see com.csjbot.blackgaga.feature.aboutus.AboutUsActivity
     */
    public static final String ABOUT_US_PATH = NEW_RETAIL + INFO + "/AboutUs";

    /**
     * 产品详情
     *
     * @see com.csjbot.blackgaga.feature.product.productDetail.ProductDetailActivity
     */
    public static final String PRODUCT_DETAIL = NEW_RETAIL + PRODUCT + "/Detail";
    /**
     * 衣服种类
     *
     * @see ClothingTypeActivity
     */
    public static final String PRODUCT_CLOTHING_TYPE = NEW_RETAIL + PRODUCT + "/ClothingType";
    /**
     * 衣服列表
     *
     * @see ClothingListActivity
     */
    public static final String PRODUCT_CLOTHING_LIST = NEW_RETAIL + PRODUCT + "/ClothingList";
    /**
     * 衣服详情
     *
     * @see ClothingDetailsActivity
     */
    public static final String PRODUCT_CLOTHING_DETAIL = NEW_RETAIL + PRODUCT + "/ClothingDetail";
    /**
     * 优惠券
     *
     * @see com.csjbot.blackgaga.feature.coupon.CouponActivity
     */
    public static final String COUPON = NEW_RETAIL + INFO + "/Coupon";

    /**
     * 娱乐 - 跳舞
     *
     * @see com.csjbot.blackgaga.feature.dance.DanceActivity
     */
    public static final String DANCE = NEW_RETAIL + PLAY + "/DANCE";
    /**
     * 娱乐 - 首页
     *
     * @see com.csjbot.blackgaga.feature.entertainment.EntertainmentActivity
     */
    public static final String ENTERTAINMENT = NEW_RETAIL + PLAY + "/Entertainment";
    /**
     * 娱乐 - 播放音乐
     *
     * @see com.csjbot.blackgaga.feature.music.MusicActivity
     */
    public static final String MUSIC = NEW_RETAIL + PLAY + "/Music";
    /**
     * 娱乐 - 讲故事
     *
     * @see com.csjbot.blackgaga.feature.story.StoryActivity
     */
    public static final String STORY = NEW_RETAIL + PLAY + "/Story";


    /**
     * 人脸注册
     *
     * @see com.csjbot.blackgaga.feature.face_register.FaceRegisterActivity
     */
    public static final String FACE_REGISTER = NEW_RETAIL + FACE + "/Register";
    /**
     * 会员中心
     *
     * @see com.csjbot.blackgaga.feature.vipcenter.VipCenterActivity
     */
    public static final String VIP_CENTER = NEW_RETAIL + FACE + "/VipCenter";


    /**
     * @see com.csjbot.blackgaga.TestActivity
     * 测试Activity
     */
    private static final String TEST = "/test";
    public static final String UPBODY_TEST = TEST + "/UpBodyTest";

    /**
     * 导航首页
     *
     * @see com.csjbot.blackgaga.feature.navigation.NaviActivity
     */
    public static final String NAVI_MAIN = NEW_RETAIL + NAVI + "/Main";
    /**
     * 导航首页新
     *
     * @see com.csjbot.blackgaga.feature.navigation.NaviActivityNew
     */
    public static final String NAVI_MAIN_NEW = NEW_RETAIL + NAVI + "/Main_new";
    /**
     * 导航设置
     *
     * @see com.csjbot.blackgaga.feature.navigation.setting.NaviSettingActivity
     */
    public static final String NAVI_SETTING = NEW_RETAIL + NAVI + "/Setting";

    /**
     * 周边首页
     *
     * @see com.csjbot.blackgaga.feature.nearbyservice.NearByActivity
     */
    public static final String NEAR_BY_MAIN = NEW_RETAIL + NEARBY + "/Main";
    /**
     * 周边首页(机场)
     *
     * @see com.csjbot.blackgaga.feature.nearbyservice.NearByAirportActivity
     */
    public static final String NEAR_BY_MAIN_AIRPORT = NEW_RETAIL + NEARBY + "/Main_Airport";
    /**
     * 周边搜索结果展示页
     *
     * @see com.csjbot.blackgaga.feature.nearbyservice.PoiSearchActivity
     */
    public static final String NEAR_BY_SEARCH = NEW_RETAIL + NEARBY + "/Search";

    /**
     * 咨询
     *
     * @see com.csjbot.blackgaga.feature.search.SearchActivity
     */
    public static final String INFO_SEARCH = NEW_RETAIL + INFO + "/search";

    /**
     * 机场 咨询服务
     *
     * @see com.csjbot.blackgaga.feature.search.SearchActivity2
     */
    public static final String INFO_SEARCH2 = NEW_RETAIL + INFO + "/search2";

    /**
     * 进入设置界面，需要输入密码
     *
     * @see com.csjbot.blackgaga.feature.settings.SettingsActivity
     */
    public static final String SETTING_CONFIRM = NEW_RETAIL + SETTING + "/setting_input_pwd";

    /**
     * 食品首页
     *
     * @see com.csjbot.blackgaga.home.ShiPinHomeActivity
     */

    public static final String MAINPAGE_SHIPIN = NEW_RETAIL + MAINPAGE + "/ShiPin";

    /**
     * 家电首页
     *
     * @see com.csjbot.blackgaga.home.JiaDianHomeActivity
     */

    public static final String MAINPAGE_JIADIAN = NEW_RETAIL + MAINPAGE + "/JiaDian";


    /**
     * 商厦首页
     *
     * @see com.csjbot.blackgaga.home.ShangShaHomeActivity
     */

    public static final String MAINPAGE_SHANGSHA = NEW_RETAIL + MAINPAGE + "/ShangSha";


    /**
     * 药店首页
     *
     * @see com.csjbot.blackgaga.home.YaoDianHomeActivity
     */
    public static final String MAINPAGE_YAODIAN = NEW_RETAIL + MAINPAGE + "/YaoDian";

    /**
     * 汽车首页
     *
     * @see com.csjbot.blackgaga.home.QiCheHomeActivity
     */
    public static final String MAINPAGE_QICHE = NEW_RETAIL + MAINPAGE + "/QiChe";


    /**
     * 穿山甲首页
     *
     * @see com.csjbot.blackgaga.home.CSJBotHomeActivity
     */
    public static final String MAINPAGE_CSJBOT = NEW_RETAIL + MAINPAGE + "/CSJBot";

    /**
     * 国家电网首页
     *
     * @see com.csjbot.blackgaga.home.StateGridHomeActivity
     */
    public static final String MAINPAGE_STATE_GRID = NEW_RETAIL + MAINPAGE + "/DianWang";

    /**
     * 行政首页
     *
     * @see com.csjbot.blackgaga.home.StateGridHomeActivity
     */
    public static final String MAINPAGE_XINGZHENG = NEW_RETAIL + MAINPAGE + "/XingZheng";


    /**
     * 酒店甲首页
     *
     * @see com.csjbot.blackgaga.home.JiuDianHomeActivity
     */
    public static final String MAINPAGE_JIUDIAN = NEW_RETAIL + MAINPAGE + "/JiuDian";

    /**
     * 超市首页
     *
     * @see com.csjbot.blackgaga.home.ChaoShiHomeActivity
     */
    public static final String MAINPAGE_CHAOSHI = NEW_RETAIL + MAINPAGE + "/ChaoShi";

    /**
     * 银行首页
     *
     * @see com.csjbot.blackgaga.home.YinHangHomeActivity
     */
    public static final String MAINPAGE_YINHANG = NEW_RETAIL + MAINPAGE + "/YinHang";

    /**
     * 车管所
     *
     * @see com.csjbot.blackgaga.home.CheGuanSuoHomeActivity
     */
    public static final String MAINPAGE_CHEGUANSUO = NEW_RETAIL + MAINPAGE + "/Cheguansuo";

    /**
     * 车站首页
     *
     * @see com.csjbot.blackgaga.home.CheZhanHomeActivity
     */
    public static final String MAINPAGE_CHEZHAN = NEW_RETAIL + MAINPAGE + "/CheZhan";
    /**
     * 税务首页
     *
     * @see com.csjbot.blackgaga.home.ShuiWuHomeActivity2
     */
    public static final String MAINPAGE_SHUIWU = NEW_RETAIL + MAINPAGE + "/ShuiWu";

    /**
     * 展馆首页
     *
     * @see com.csjbot.blackgaga.home.ZhanGuanHomeActivity
     */
    public static final String MAINPAGE_ZHANGUAN = NEW_RETAIL + MAINPAGE + "/ZhanGuan";
    /**
     * 医院首页
     *
     * @see com.csjbot.blackgaga.home.YiYuanHomeActivity
     */
    public static final String MAINPAGE_YIYUAN = NEW_RETAIL + MAINPAGE + "/YiYuan";


    /**
     * 咸阳机场首页
     *
     * @see com.csjbot.blackgaga.home.XianYangAirportHomeActivity
     */
    public static final String MAINPAGE_JICHANG = NEW_RETAIL + MAINPAGE + "/JiChang";

    /**
     * 前台首页
     *
     * @see com.csjbot.blackgaga.home.QianTaiHomeActivity
     */
    public static final String MAINPAGE_QIANTAI = NEW_RETAIL + MAINPAGE + "/QianTai";

    /**
     * 眼镜首页
     *
     * @see com.csjbot.blackgaga.home.YanJingHomeActivity
     */
    public static final String MAINPAGE_YANJING = NEW_RETAIL + MAINPAGE + "/YanJing";

    /**
     * 家居连锁首页
     *
     * @see com.csjbot.blackgaga.home.JiaJuHomeActivity
     */
    public static final String MAINPAGE_JIAJU = NEW_RETAIL + MAINPAGE + "/JiaJu";

    /**
     * 科技馆首页
     *
     * @see com.csjbot.blackgaga.home.KeJiGuanHomeActivity
     */
    public static final String MAINPAGE_KEJIGUAN = NEW_RETAIL + MAINPAGE + "/KeJiGuan";

    /**
     * 新教育首页
     *
     * @see com.csjbot.blackgaga.home.JiaoYu2HomeActivity
     */
    public static final String MAINPAGE_JIAOYU2 = NEW_RETAIL + MAINPAGE + "/JiaoYu2";

    /**
     * 汽车站首页
     *
     * @see com.csjbot.blackgaga.home.QiCheZhanHomeActivity
     */
    public static final String MAINPAGE_QICHEZHAN = NEW_RETAIL + MAINPAGE + "/QiCheZhan";

    /**
     * 科技馆首页
     *
     * @see com.csjbot.blackgaga.home.ShouJiHomeActivity
     */
    public static final String MAINPAGE_SHOUJI = NEW_RETAIL + MAINPAGE + "/ShouJi";

    /**
     * 鞋店首页
     *
     * @see com.csjbot.blackgaga.home.XieDianHomeActivity
     */
    public static final String MAINPAGE_XIEDIAN = NEW_RETAIL + MAINPAGE + "/XieDian";


    /**
     * 旅游首页
     *
     * @see com.csjbot.blackgaga.home.LvYouHomeActivity
     */
    public static final String MAINPAGE_LVYOU = NEW_RETAIL + MAINPAGE + "/LvYou";


    /**
     * 服装首页
     *
     * @see com.csjbot.blackgaga.home.FuZhuangHomeActivity
     */
    public static final String MAINPAGE_FUZHUANG = NEW_RETAIL + MAINPAGE + "/FuZhuang";

    /**
     * 导览评价
     *
     * @see com.csjbot.blackgaga.feature.navigation.NaviGuideCommentActivity
     */
    public static final String NAVI_GUIDE_COMMENT = NEW_RETAIL + NAVI + "/comment";

    /**
     * 默认主页
     *
     * @see com.csjbot.blackgaga.home.HomeActivity
     */
    public static final String HOME_PAGE = NEW_RETAIL + "/home";


    /**
     * 服装首页
     *
     * @see com.csjbot.blackgaga.home.VerticalScreenHomeActivity
     */
    public static final String MAINPAGE_VERTICALSCREEN = NEW_RETAIL + MAINPAGE + "/VerticalScreen";

    /**
     * 博物馆首页
     *
     * @see com.csjbot.blackgaga.home.MuseumHomeActivity
     */
    public static final String MAINPAGE_MUSEUM = NEW_RETAIL + MAINPAGE + "/Museum";

    /**
     * 语言选择页
     *
     * @see com.csjbot.blackgaga.MainActivity
     */
    public static final String MAIN = NEW_RETAIL + "/main";

    /**
     * 三级
     * 开发中
     */
    public static final String THERELEVEL = NEW_RETAIL + JIUDIAN + "/3";

    /**
     * 二级
     *
     * @see
     */
    public static final String TWOLEVEL = NEW_RETAIL + JIUDIAN + "/2";

    /**
     * 一级
     *
     * @see com.csjbot.blackgaga.feature.content.ContentActivity
     */
    public static final String ONELEVEL = NEW_RETAIL + JIUDIAN + "/1";
}