package com.csjbot.blackgaga.model.http.product;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.csjbot.blackgaga.BaseApplication;
import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.cart.entity.RobotMenuListBean;
import com.csjbot.blackgaga.cart.entity.RobotSpBean;
import com.csjbot.blackgaga.cart.entity.RobotSpListBean;
import com.csjbot.blackgaga.cart.pactivity.evaluate.AnswerResponse;
import com.csjbot.blackgaga.feature.clothing.bean.ClothListBean;
import com.csjbot.blackgaga.feature.clothing.bean.ClothTypeBean;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.model.http.bean.AdInfo;
import com.csjbot.blackgaga.model.http.bean.ContentBean;
import com.csjbot.blackgaga.model.http.bean.ContentTypeBean;
import com.csjbot.blackgaga.model.http.bean.LogoBean;
import com.csjbot.blackgaga.model.http.bean.PayResponse;
import com.csjbot.blackgaga.model.http.bean.PayResult;
import com.csjbot.blackgaga.model.http.bean.ProductInfo;
import com.csjbot.blackgaga.model.http.bean.SceneBean;
import com.csjbot.blackgaga.model.http.bean.SceneRepuestBean;
import com.csjbot.blackgaga.model.http.product.listeners.BaseBackstageListener;
import com.csjbot.blackgaga.model.http.product.listeners.ContentListListener;
import com.csjbot.blackgaga.model.http.product.listeners.ContentTypeListListener;
import com.csjbot.blackgaga.model.http.product.listeners.EvalutionListener;
import com.csjbot.blackgaga.model.http.product.listeners.IProduct;
import com.csjbot.blackgaga.model.http.product.listeners.MenuListListener;
import com.csjbot.blackgaga.model.http.product.listeners.ObtainData;
import com.csjbot.blackgaga.model.http.product.listeners.OrderCodeUrlListener;
import com.csjbot.blackgaga.model.http.product.listeners.SpListListener;
import com.csjbot.blackgaga.model.http.workstream.Logo;
import com.csjbot.blackgaga.util.BlackgagaLogger;
import com.csjbot.blackgaga.util.CustomSDCardLoader;
import com.csjbot.blackgaga.util.FileUtil;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.coshandler.log.Csjlogger;
import com.google.gson.Gson;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import skin.support.SkinCompatManager;

/**
 * @author xiasuhuei321
 *         Changed by jingwc on 2017/9/18.
 */

public class ProductProxy extends ProductParent implements IProduct, ObtainData {

    private IProduct iProduct;

    private ObtainData obtainData;

    private String TAG = this.getClass().getSimpleName();

    private Integer downloadCount = 0;

    private Integer alreadyCount = 0;

    public static final String PRODUCT_IMG_PATH = Environment.getExternalStorageDirectory() +
            File.separator + "blackgaga" + File.separator + "product_image" + File.separator;

    public static String SN = "12345678";

    /**
     * 是正常的更新数据还是强制同步数据
     */
    public static boolean ISUPDATA;

    /**
     * 获取当前sp下的图片数量
     */
    private Integer SP_SIZE = 0;

    /**
     * 确保当前不是一个下载地址，多个menu下载对应的sp
     */
    private static Integer NEED_LOAD = 0;


    private Integer isLoadNum = 0;


    /**
     * 已经下载的图片
     */
    private int ALREADY_DOWNLOAD = 0;

    /**
     * 下载图片和视频资源有多少
     */
    private static Integer NEED_LOAD_MEDIA = 0;


    /**
     * 需要更新数据的列表
     */
    private static List<RobotSpBean> updateSp;

    public static ProductProxy newProxyInstance() {
        return ProductHandler.product;
    }

    /**
     * 通过android 内部运行机制创建唯一的一个单例
     */
    public static class ProductHandler {
        static ProductProxy product = new ProductProxy();
    }

    public ProductProxy() {
        obtainData = new ProductImpl();
        iProduct = new ProductImpl();
        if (Robot.SN != null) {
            SN = Robot.SN;
            BlackgagaLogger.debug("获取 SN 成功，SN为：" + Robot.SN);
        } else {
            BlackgagaLogger.debug("获取 SN 失败，使用默认SN");
        }
    }

    /**
     * 主动加载
     * 一级加载次加载列表数据
     * 包括主列表和产品列表
     */
    public void getRobotMenuList(MenuListListener listener) {
        RobotMenuListBean productInfo = getMenuList();
        if (ISUPDATA || productInfo == null) {
            //清空所有产品信息缓存
            removeMenuList();
            removeSpList(Constants.Language.getLanguageStr());
            getRobotMenuList(SN, Constants.Language.getLanguageStr(), new Observer<RobotMenuListBean>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                }

                @Override
                public void onNext(@NonNull RobotMenuListBean robotMenuListBean) {
                    if (robotMenuListBean.getStatus().equals("500")) {
                        if (listener != null) {
                            listener.loadError500();
                        }
                    } else {
                        saveList(robotMenuListBean, listener, "");
                    }
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    if (listener != null) {
                        listener.onMenuError(e);
                        listener.onLocaleMenuList(getMenuList());
                    }
                }

                @Override
                public void onComplete() {

                }
            });
        } else if (!ISUPDATA && productInfo != null) {
            //本地如果有了数据就下载一下图片
            if (listener != null) {
                listener.getMenuList(productInfo);
            }
            downLoadMenuImage(productInfo, listener);
        }
    }


    public synchronized void robotSpList(String menuid, Context context, SpListListener listener) {
        updateSp = new ArrayList<>();
        isLoadNum = 0;
        SP_SIZE = 0;
        RobotSpListBean productInfo = getSpList(menuid);
        if (getMenuList() == null) {
            return;
        }
        //如果menuid == "" 那么就要重新下载数据并更新本地的
        if (ISUPDATA) {
            RobotMenuListBean.ResultBean resultBean = getMenuList().getResult();
            List<RobotMenuListBean.ResultBean.MenulistBean> list = resultBean.getMenulist();
            removeSpList(SharedKey.SP_LAN.getLanguageStr(Constants.Language.getLanguageStr()));
            NEED_LOAD = 0;
            NEED_LOAD_MEDIA = list.size();
            if (NEED_LOAD_MEDIA == 0) {
                if (listener != null)
                    listener.loadAllSuccess();
                return;
            }
            for (int i = 0; i < NEED_LOAD_MEDIA; i++) {
                getRobotSpListByUrl(list.get(i).getMURL().replace("{sn}", SN), list.get(i).getMenuid(), listener);
            }
        } else if (!ISUPDATA && !menuid.equals("")) {
            //获取当前的id
            String url = DownLoadResTools.getOneSpURL(getMenuList(), menuid);
            if (!url.equals("")) {
                getRobotSpListByUrl(url.replace("{sn}", SN),
                        menuid,
                        listener);
            }
        } else if (!menuid.equals("") && (!ISUPDATA && productInfo != null)) {
            //本地如果有了数据就下载图片
            downLoadSpImage(getSpList(menuid), listener);
            return;
        } else {
            return;
        }
    }

    /**
     * @param url            下载地址
     * @param menuid         导航id 为了利用id来保存数据
     * @param spListListener 状态监听
     */
    public synchronized void getRobotSpListByUrl(String url, String menuid, SpListListener spListListener) {
        fullDynamicGetRobotSpList(url, new Observer<RobotSpListBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull RobotSpListBean robotSpListBean) {
                //接口返回下载的图片数量
                if (robotSpListBean.getStatus().equals("200")) {
                    RobotSpBean r = new RobotSpBean(robotSpListBean, menuid);
                    updateSp.add(r);
                }
                NEED_LOAD_MEDIA--;
                //确定需要下载的图片数量F
                if (NEED_LOAD_MEDIA <= 0) {
                    for (int i = 0; i < updateSp.size(); i++) {
                        getResQuantity(updateSp.get(i).getBean());
                    }
                    if (spListListener != null)
                        spListListener.ImageSize(SP_SIZE);
                    if (updateSp.size() != 0) {
                        for (int i = 0; i < updateSp.size(); i++) {
                            saveList(updateSp.get(i).getBean(), spListListener, updateSp.get(i).getMenuID());
                        }
                    } else {
                        if (spListListener != null)
                            spListListener.loadAllSuccess();
                    }
                    updateSp.clear();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                if (spListListener != null) {
                    spListListener.onSpError(e);
                    spListListener.getSpList(getSpList(menuid));
                }
            }

            @Override
            public void onComplete() {
            }
        });
    }

    /**
     * 获取到所有sp子类下面的照片或者video
     *
     * @param robotSpListBean
     * @return
     */
    private int getResQuantity(RobotSpListBean robotSpListBean) {
        int size = robotSpListBean.getResult().getProduct().size();
        List<RobotSpListBean.ResultBean.ProductBean> beam = robotSpListBean.getResult().getProduct();
        SP_SIZE = SP_SIZE + size;
        for (int i = 0; i < size; i++) {
            SP_SIZE = SP_SIZE + beam.get(i).getViewUrl().size();
        }
        return SP_SIZE;
    }

    public <T> void saveList(T bean, T listener, String parent_id) {
        if (bean instanceof RobotMenuListBean) {
            RobotMenuListBean robotMenuListBean = (RobotMenuListBean) bean;
            // 将json字符串存入到本地
            if (SharedPreUtil.putString(SharedKey.PRODUCT_MENU_LIST,
                    SharedKey.MENU_LAN.getLanguageStr(Constants.Language.getLanguageStr()), robotMenuListBean)) {
                System.out.println("chenqi  我保存成功了");
                if (listener != null) {
                    ((MenuListListener) listener).getMenuList(getMenuList());
                }
                downLoadMenuImage(robotMenuListBean, (MenuListListener) listener);
            }
        } else if (bean instanceof RobotSpListBean) {
            RobotSpListBean robotSpListBean = (RobotSpListBean) bean;
            // 将json字符串存入到本地
            if (SharedPreUtil.putString(SharedKey.SP_LAN.getLanguageStr(Constants.Language.getLanguageStr()),
                    parent_id, robotSpListBean)) {
                if (listener != null) {
                    ((SpListListener) listener).getSpList(getSpList(parent_id));
                }
                downLoadSpImage(robotSpListBean, (SpListListener) listener);
            }
        } else if (bean instanceof SceneBean) {
            SceneBean sceneBean = (SceneBean) bean;
            String tempURL = ((SceneBean) bean).getResult().getIndustry();
            //保存当前的scene，不需要删除了根据当前
            if (SharedPreUtil.putString(SharedKey.SCENE_LIST,
                    tempURL, sceneBean)) {
                //把当前的跳转的页面删除
                SharedPreUtil.removeString(SharedKey.MAINPAGE, SharedKey.MAINPAGE_KEY);
                SharedPreUtil.putString(SharedKey.MAINPAGE, SharedKey.MAINPAGE_KEY, tempURL);
                downLoadScene(sceneBean, (BaseBackstageListener) listener);
            }
        } else if (bean instanceof ContentBean) {
            ContentBean contentBean = (ContentBean) bean;
            // 将json字符串存入到本地
            if (SharedPreUtil.putString(SharedKey.CONTENT_NAME,
                    SharedKey.CONTENT_LAN.getLanguageStr(Constants.Language.getLanguageStr()), contentBean)) {
                if (listener != null) ((ContentListListener) listener).getContentList(contentBean);
                //下载数据
                if (contentBean != null && listener == null) {
                    List<ContentBean.ResultBean.ContentMessageBean> list = contentBean.getResult().getContentType();
                    for (ContentBean.ResultBean.ContentMessageBean d : list) {
                        getContentTypeListener(d.getResUrl(), null);
                    }
                }
            }
        } else if (bean instanceof ContentTypeBean) {
            ContentTypeBean contentTypeBean = (ContentTypeBean) bean;
            if (listener != null)
                ((ContentTypeListListener) listener).getContentTypeList(contentTypeBean);
            // 将json字符串存入到本地
            if (SharedPreUtil.putString(SharedKey.CONTENT_LAN.getLanguageStr(Constants.Language.getLanguageStr()),
                    parent_id, contentTypeBean)) {
            }
        }
    }

    public synchronized void downLoadScene(SceneBean bean, BaseBackstageListener listener) {
        String url = bean.getResult().getResUrl();
        String nailUrl = bean.getResult().getThumbnail();
        String thumbName = bean.getResult().getThemename();
        String[] field = url.split("/");
        String skinName = field[field.length - 1];
        File file = new File(Constants.SKIN_PATH + thumbName);
        getSkin(url, new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull ResponseBody responseBody) {
                BaseApplication.isPushSkinEnd = true;
                if (responseBody == null) {
                    BlackgagaLogger.error("skin资源下载失败！");
                    if (listener != null)
                        listener.loadFailed("");
                } else {
                    if (FileUtil.writeResponseBodyToDisk(responseBody, skinName)) {
                        if (listener != null)
                            listener.loadSuccess("");
                        SkinCompatManager.getInstance().loadSkin(skinName, new SkinCompatManager.SkinLoaderListener() {
                            @Override
                            public void onStart() {
                                CsjlogProxy.getInstance().info("换肤开始");
                            }

                            @Override
                            public void onSuccess() {
                                BaseApplication.getAppContext().sendBroadcast(new Intent(Constants.UPDATE_LOGO));
                                Constants.HomePage.isHomePageLoadSuccess = true;
                            }

                            @Override
                            public void onFailed(String s) {
                                CsjlogProxy.getInstance().info("换肤失败" + s);
                                Constants.HomePage.isHomePageLoadSuccess = true;
                            }
                        }, CustomSDCardLoader.SKIN_LOADER_STRATEGY_SDCARD);
                        DownLoadResTools.downLoadSceneResByGildeApp(nailUrl, file, listener);
                    } else {
                        if (listener != null) {
                            listener.loadFailed("");
                        }
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                BaseApplication.isPushSkinEnd = true;
                if (listener != null)
                    listener.loadError500();
                BlackgagaLogger.debug("chenqi", "皮肤下载失败" + e);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public synchronized void downLoadSpImage(RobotSpListBean productInfo, SpListListener listener) {
        downloadCount = 0;
        alreadyCount = 0;
        List<RobotSpListBean.ResultBean.ProductBean> productList =
                productInfo.getResult().getProduct();
        if (productList == null) {
            BlackgagaLogger.debug(TAG + "下载图片数量不能为空！");
            return;
        }
        for (RobotSpListBean.ResultBean.ProductBean productBean : productList) {
            File pathFile = new File(PRODUCT_IMG_PATH);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
            String imgName = productBean.getImgName();
            String toFilePath = PRODUCT_IMG_PATH + imgName;
            pathFile = new File(toFilePath);
            //检查本地数据是否已经存在了，如果没有存在就是用网络
            if (!ISUPDATA && pathFile.exists()) {
                isLoadNum++;
                alreadyCount++;
                int all = downloadCount + alreadyCount;
                downLoadSpViewContent(productBean, listener);
                listener.getSpList(productInfo);//判断是否产品页下载完毕
                if (listener != null && all < productList.size() && isLoadNum < SP_SIZE) {
                    listener.loadSuccess(imgName);//判断是否下载完毕
                } else if (listener != null && all >= productList.size() && isLoadNum >= SP_SIZE) {
                    listener.loadAllSuccess();//判断数据更新是否完成
                    isLoadNum = 0;
                }
                continue;
            }

            Glide.with(BaseApplication.getAppContext())
                    .asFile()
                    .load(productBean.getUrl())
                    .listener(new RequestListener<File>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                            // 拷贝失败 第几张图失败了
                            if (listener != null)
                                listener.loadFailed(imgName);
                            downloadCount++;
                            isLoadNum++;
                            //下载这列中的view
                            int all = downloadCount + alreadyCount;
                            downLoadSpViewContent(productBean, listener);
                            if (listener != null) {
                                listener.getSpList(productInfo);
                            }
                            if (NEED_LOAD == 0 && listener != null && all < productList.size() && isLoadNum < SP_SIZE) {
                                if (listener != null) {
                                    listener.loadSuccess(imgName);
                                }
                            } else if (NEED_LOAD == 0 && listener != null && all == productList.size() && isLoadNum >= SP_SIZE) {
                                if (listener != null) {
                                    listener.loadAllSuccess();
                                }
                                isLoadNum = 0;
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(new SimpleTarget<File>() {
                        @Override
                        public void onResourceReady(File resource, Transition<? super File> transition) {
                            Observable.just(FileUtil.copy(resource, new File(toFilePath)))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(b -> {
                                        if (b) {
                                            if (listener != null)
                                                listener.loadSuccess(imgName);
                                            // 拷贝成功 第几张图
                                        } else {
                                            if (listener != null) {
                                                listener.loadFailed(imgName);
                                            }
                                        }
                                        isLoadNum++;
                                        downloadCount++;
                                        downLoadSpViewContent(productBean, listener);
                                        if (listener != null)
                                            listener.getSpList(productInfo);
                                        int all = downloadCount + alreadyCount;
                                        if (NEED_LOAD == 0 && listener != null && all < productList.size() && isLoadNum < SP_SIZE) {
                                            if (listener != null)
                                                listener.loadSuccess(imgName);
                                        } else if (NEED_LOAD == 0 && listener != null && all == productList.size() && isLoadNum >= SP_SIZE) {
                                            if (listener != null)
                                                listener.loadAllSuccess();
                                        }
                                    }, e -> {
                                        Csjlogger.error(e);
                                        if (listener != null) {
                                            listener.cacheToMore();
                                        }
                                    });
                        }
                    });
        }

    }

    public synchronized void downLoadMenuImage(RobotMenuListBean productInfo, MenuListListener listener) {
        ALREADY_DOWNLOAD = 0;
        downloadCount = 0;
        alreadyCount = 0;
        RobotMenuListBean.ResultBean resultBean = productInfo.getResult();
        List<RobotMenuListBean.ResultBean.MenulistBean> productList =
                resultBean.getMenulist();
        if (productList == null || productList.size() == 0) {
            BlackgagaLogger.debug("下载图片数量不能为空！");
            return;
        }
        if (listener != null) {
            listener.ImageSize(productList.size());
        }

        for (RobotMenuListBean.ResultBean.MenulistBean productBean : productList) {
            File pathFile = new File(PRODUCT_IMG_PATH);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
            String imgName = DownLoadResTools.getMenuImageName(productBean.getMImg());
            String toFilePath = PRODUCT_IMG_PATH + imgName;
            pathFile = new File(toFilePath);
            //检查本地数据是否已经存在了，如果没有存在就是用网络
            if (!ISUPDATA && pathFile.exists()) {
                alreadyCount++;
                ALREADY_DOWNLOAD = downloadCount + alreadyCount;
                // 拷贝成功
                if (listener != null) {
                    listener.loadSuccess(imgName);
                }
                if (ALREADY_DOWNLOAD >= productList.size()) {
                    if (listener != null) {
                        listener.loadAllSuccess();
                    }
                }
                continue;
            }
            Glide.with(BaseApplication.getAppContext())
                    .asFile()
                    .load(productBean.getMImg())
                    .listener(new RequestListener<File>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                            // 拷贝失败
                            if (listener != null) {
                                listener.loadFailed(imgName);
                            }
                            downloadCount++;
                            ALREADY_DOWNLOAD = downloadCount + alreadyCount;
                            if (ALREADY_DOWNLOAD >= productList.size()) {
                                if (listener != null) {
                                    listener.loadAllSuccess();
                                }
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(new SimpleTarget<File>() {
                        @Override
                        public void onResourceReady(File resource, Transition<? super File> transition) {
                            Observable.just(FileUtil.copy(resource, new File(toFilePath)))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(b -> {
                                        if (b) {
                                            // 拷贝成功
                                            if (listener != null) {
                                                listener.loadSuccess(imgName);
                                            }
                                        } else {
                                            // 拷贝成功
                                            if (listener != null) {
                                                listener.loadFailed(imgName);
                                            }
                                        }
                                        downloadCount++;
                                        ALREADY_DOWNLOAD = downloadCount + alreadyCount;
                                        if (ALREADY_DOWNLOAD >= productList.size()) {
                                            if (listener != null) {
                                                listener.loadAllSuccess();
                                            }
                                        }
                                    }, e -> {
                                        e.printStackTrace();
                                        if (listener != null) {
                                            listener.cacheToMore();
                                        }
                                        // 可能引发错误的原因是什么呢？
                                        // 1.内存不足
                                        // 2.io异常
                                        // 3.文件地址错误
                                    });
                        }
                    });
        }
    }

    public synchronized void downLoadSpViewContent(RobotSpListBean.ResultBean.ProductBean productInfo, SpListListener listener) {
        if (productInfo == null || productInfo.getViewUrl().size() == 0) {
            BlackgagaLogger.debug(TAG + "下载轮播图片或者视频资源数量不能为空！");
            return;
        }
        if (productInfo.getViewType() == 2) {//轮播图片
            List<String> imageLists = productInfo.getViewUrl();
            downLoadLoopViewContent(imageLists, listener);
        } else if (productInfo.getViewType() == 1) {//视频资源
            downLoadVideoViewContent(productInfo.getViewUrl().get(0), listener);
        }
    }

    public synchronized void downLoadLoopViewContent(List<String> imageList, SpListListener listener) {
        File pathFile = new File(Constants.PRODUCT_IMG_PATH);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        for (String imageName : imageList) {
            String imgName = FileUtil.getFileName(imageName);
            String filepath = Constants.PRODUCT_IMG_PATH + imgName;
            File file = new File(filepath);
            if (!ISUPDATA && file.exists()) {
                isLoadNum++;
                if (listener != null)
                    listener.loadSuccess(imageName);
                if (listener != null && isLoadNum >= SP_SIZE) {
                    listener.loadAllSuccess();
                }
                continue;
            }
            Glide.with(BaseApplication.getAppContext())
                    .asFile()
                    .load(imageName)
                    .listener(new RequestListener<File>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                            isLoadNum++;
                            if (listener != null)
                                listener.loadFailed(imageName);
                            if (listener != null && isLoadNum >= SP_SIZE) {
                                listener.loadAllSuccess();
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(new SimpleTarget<File>() {
                        @Override
                        public void onResourceReady(File resource, Transition<? super File> transition) {
                            Observable.just(FileUtil.copy(resource, new File(filepath)))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(b -> {
                                        if (b) {
                                            if (listener != null)
                                                listener.loadSuccess(imageName);
                                        } else {
                                            if (listener != null)
                                                listener.loadFailed(imageName);
                                        }
                                        isLoadNum++;
                                        if (listener != null && isLoadNum >= SP_SIZE) {
                                            listener.loadAllSuccess();
                                        }
                                    }, e -> {
                                        if (listener != null) {
                                            listener.cacheToMore();
                                        }
                                        BlackgagaLogger.error(e);
                                    });
                        }
                    });
        }
    }

    public synchronized void downLoadVideoViewContent(String videoUrl, SpListListener listener) {
        File pathFile = new File(Constants.PRODUCT_VIDEO_PATH);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        String videoName = FileUtil.getFileName(videoUrl);
        String videopath = Constants.PRODUCT_VIDEO_PATH + videoName;
        File file = new File(videopath);
        if (!ISUPDATA && file.exists()) {
            isLoadNum++;
            if (listener != null)
                listener.loadSuccess(videoName);
            if (listener != null && isLoadNum >= SP_SIZE) {
                listener.loadAllSuccess();
            }
            return;
        }
        Glide.with(BaseApplication.getAppContext())
                .asFile()
                .load(videoUrl)
                .listener(new RequestListener<File>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                        isLoadNum++;
                        if (listener != null) {
                            if (isLoadNum >= SP_SIZE) {
                                listener.loadAllSuccess();
                                isLoadNum = 0;
                            } else
                                listener.loadFailed(videoName);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(new SimpleTarget<File>() {
                    @Override
                    public void onResourceReady(File resource, Transition<? super File> transition) {
                        Observable.just(FileUtil.copy(resource, new File(videopath)))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(b -> {
                                    if (b) {
                                        if (listener != null)
                                            listener.loadSuccess(videoName);
                                    } else {
                                        if (listener != null)
                                            listener.loadFailed(videoName);
                                    }
                                    isLoadNum++;
                                    if (listener != null && isLoadNum >= SP_SIZE) {
                                        listener.loadAllSuccess();
                                    }
                                }, e -> {
                                    if (listener != null) {
                                        listener.cacheToMore();
                                    }
                                    BlackgagaLogger.error(e);
                                });
                    }
                });
    }


    @Override
    public RobotSpListBean getSpList(String parent_id) {
        return obtainData.getSpList(parent_id);
    }

    @Override
    public boolean removeSpList(String lan) {
        return obtainData.removeSpList(lan);
    }

    @Override
    public boolean removeMenuList() {
        return obtainData.removeMenuList();
    }

    @Override
    public SceneBean getSceneRes(String industry) {
        return obtainData.getSceneRes(industry);
    }

    @Override
    public boolean removeSceneRes() {
        return obtainData.removeSceneRes();
    }

    @Override
    public List<RobotSpListBean.ResultBean.ProductBean> getAllSpInformation() {
        return obtainData.getAllSpInformation();
    }

    @Override
    public ContentBean getContent() {
        return obtainData.getContent();
    }

    @Override
    public ContentTypeBean getContentType(String parent_id) {
        return obtainData.getContentType(parent_id);
    }


    public void getRobotMenuList(boolean b) {
        ISUPDATA = b;
        getRobotMenuList(SN, Constants.Language.getLanguageStr(), new Observer<RobotMenuListBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull RobotMenuListBean robotMenuListBean) {
                saveList(robotMenuListBean, null, "");
            }

            @Override
            public void onError(@NonNull Throwable e) {
                BlackgagaLogger.debug(" getRobotMenuList 服务器炸了 " + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public ProductInfo getLocalProduct() {
        return obtainData.getLocalProduct();
    }

    @Override
    public RobotMenuListBean getMenuList() {
        return obtainData.getMenuList();
    }

    @Override
    public void getAdInfo(Observer<AdInfo> observer) {
        iProduct.getAdInfo(observer);
    }

    @Override
    public void generateOrder(String json, Observer<PayResponse> observer) {
        iProduct.generateOrder(json, observer);
    }

    @Override
    public void sendEvalution(String json, Observer<AnswerResponse> observer) {
        iProduct.sendEvalution(json, observer);
    }

    public void sendEvalution(String json, EvalutionListener listener) {
        sendEvalution(json, new Observer<AnswerResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull AnswerResponse evaluationAnswer) {
                if (evaluationAnswer.getStatus().equals("200")) {
                    listener.sendEvalutionSuccess();
                } else if (evaluationAnswer.getStatus().equals("500")) {
                    listener.sendEvalutionFailed();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                listener.sendEvalutionFailed();
            }

            @Override
            public void onComplete() {
            }
        });
    }

    public void generateOrder(String json, OrderCodeUrlListener listener) {
        generateOrder(json, new Observer<PayResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull PayResponse payResponse) {
                listener.getOrderCodeUrl(payResponse);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                listener.onOrderCodeError();
            }

            @Override
            public void onComplete() {
                listener.onOrderCodeComplete();
            }
        });
    }


    public void getScene(BaseBackstageListener listener) {
        SceneRepuestBean sceneBean = new SceneRepuestBean();
        sceneBean.setSn(SN);
        sceneBean.setVersion(BuildConfig.VERSION_CODE);
        String body = new Gson().toJson(sceneBean);
        getScene(body, new Observer<SceneBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull SceneBean sceneBean) {
                if (sceneBean.getStatus().equals("200")) {
                    //进入下载
                    saveList(sceneBean, listener, "");
                } else {
                    if (listener != null) {
                        listener.loadError500();
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                if (listener != null) {
                    listener.loadFailed("");
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void getContent(ContentListListener listener) {
        String lan = Constants.Language.getLanguageStr2();
        getContent(lan, SN, new Observer<ContentBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull ContentBean contentBean) {
                if (contentBean.getStatus().equals("500")) {
                    if (listener != null) {
                        listener.loadError500();
                    }
                } else {
                    saveList(contentBean, listener, "");
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                CsjlogProxy.getInstance().error("getContent:e:" + e.toString());
                if (listener != null) {
                    listener.onContentError(e);
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void getContentTypeListener(String url, ContentTypeListListener listener) {
        getContentType(URLDecoder.decode(url), new Observer<ContentTypeBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull ContentTypeBean contentTypeBean) {
                if (contentTypeBean.getStatus().equals("500")) {
                    if (listener != null) {
                        listener.loadError500();
                    }
                } else {
                    saveList(contentTypeBean, listener, contentTypeBean.getResult().getContentMessage().getParentId());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                CsjlogProxy.getInstance().error("getContentType:e" + e.toString());
                if (listener != null) {
                    listener.onContentTypeError(e);
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void downLoadContentImage(ContentBean contentBean, ContentListListener listener) {
        ALREADY_DOWNLOAD = 0;
        downloadCount = 0;
        alreadyCount = 0;
        List<ContentBean.ResultBean.ContentMessageBean> productList =
                contentBean.getResult().getContentType();
        if (productList == null || productList.size() == 0) {
            BlackgagaLogger.debug("下载图片数量不能为空！");
            return;
        }
        if (listener != null) {
            listener.ImageSize(productList.size());
        }

        for (ContentBean.ResultBean.ContentMessageBean productBean : productList) {
            File pathFile = new File(PRODUCT_IMG_PATH);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
            String imgName = DownLoadResTools.getMenuImageName("null");
            String toFilePath = PRODUCT_IMG_PATH + imgName;
            pathFile = new File(toFilePath);
            //检查本地数据是否已经存在了，如果没有存在就是用网络
            if (!ISUPDATA && pathFile.exists()) {
                alreadyCount++;
                ALREADY_DOWNLOAD = downloadCount + alreadyCount;
                // 拷贝成功
                if (listener != null) {
                    listener.loadSuccess(imgName);
                }
                if (ALREADY_DOWNLOAD >= productList.size()) {
                    if (listener != null) {
                        listener.loadAllSuccess();
                    }
                }
                continue;
            }
            Glide.with(BaseApplication.getAppContext())
                    .asFile()
                    .load("null")
                    .listener(new RequestListener<File>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                            // 拷贝失败
                            if (listener != null) {
                                listener.loadFailed(imgName);
                            }
                            downloadCount++;
                            ALREADY_DOWNLOAD = downloadCount + alreadyCount;
                            if (ALREADY_DOWNLOAD >= productList.size()) {
                                if (listener != null) {
                                    listener.loadAllSuccess();
                                }
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(new SimpleTarget<File>() {
                        @Override
                        public void onResourceReady(File resource, Transition<? super File> transition) {
                            Observable.just(FileUtil.copy(resource, new File(toFilePath)))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(b -> {
                                        if (b) {
                                            // 拷贝成功
                                            if (listener != null) {
                                                listener.loadSuccess(imgName);
                                            }
                                        } else {
                                            // 拷贝成功
                                            if (listener != null) {
                                                listener.loadFailed(imgName);
                                            }
                                        }
                                        downloadCount++;
                                        ALREADY_DOWNLOAD = downloadCount + alreadyCount;
                                        if (ALREADY_DOWNLOAD >= productList.size()) {
                                            if (listener != null) {
                                                listener.loadAllSuccess();
                                            }
                                        }
                                    }, e -> {
                                        e.printStackTrace();
                                        if (listener != null) {
                                            listener.cacheToMore();
                                        }
                                        // 可能引发错误的原因是什么呢？
                                        // 1.内存不足
                                        // 2.io异常
                                        // 3.文件地址错误
                                    });
                        }
                    });
        }
    }

    public void downLoadLogo() {
        if (new File(Constants.LOGO_PATH + "logo.jpg").exists()) {
            new File(Constants.LOGO_PATH + "logo.jpg").delete();
        }
        Logo.getLogo(SN, new Observer<LogoBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull LogoBean logoBean) {
                Logo.setLogoBean(logoBean);
                if (logoBean != null && logoBean.getResult() != null) {
                    BlackgagaLogger.debug("sunxy后台获取logo");
                    SharedPreUtil.putString(SharedKey.LOGO, SharedKey.LOGOTYPE, "net");
                    CsjlogProxy.getInstance().info("logoUrl:" + logoBean.getResult().get(0).getLogourl());
                    String logoUrl = null;
                    try {
                        logoUrl = URLDecoder.decode(logoBean.getResult().get(0).getLogourl(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                    }
                    Glide.with(BaseApplication.getAppContext())
                            .asFile()
                            .load(logoUrl)
                            .listener(new RequestListener<File>() {

                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                                    BlackgagaLogger.debug("sunxy后台获取logo Failed:e" + e.toString());
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                                    BlackgagaLogger.debug("sunxy后台获取logo ResourceReady");
                                    return false;
                                }
                            })
                            .into(new SimpleTarget<File>() {
                                @Override
                                public void onResourceReady(File resource, Transition<? super File> transition) {
                                    File pathFile = new File(Constants.LOGO_PATH);
                                    if (!pathFile.exists()) {
                                        pathFile.mkdirs();
                                    }
                                    Observable.just(FileUtil.copy(resource, new File(Constants.LOGO_PATH + "logo.jpg")))
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(b -> {
                                                if (b) {
                                                    BlackgagaLogger.debug("sunxy后台获取logo 下载成功");
                                                } else {
                                                    BlackgagaLogger.debug("sunxy后台获取logo 下载失败 本地图片");
                                                }
                                            });
                                }
                            });
                } else {
                    BlackgagaLogger.debug("sunxy后台获取logo 本地图片");
                    if (new File(Constants.LOGO_PATH + "logo.jpg").exists()) {
                        new File(Constants.LOGO_PATH + "logo.jpg").delete();
                    }
                    SharedPreUtil.putString(SharedKey.LOGO, SharedKey.LOGOTYPE, "local");
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void searchPayResult(String orderid, Observer<PayResult> observer) {
        iProduct.searchPayResult(orderid, observer);
    }

    @Override
    public void getRobotMenuList(String sn, String language, Observer<RobotMenuListBean> observer) {
        iProduct.getRobotMenuList(sn, language, observer);
    }

    @Override
    public void dynamicGetRobotSpList(String url, String sn, Observer<RobotSpListBean> observer) {
        iProduct.dynamicGetRobotSpList(url, sn, observer);
    }

    @Override
    public void fullDynamicGetRobotSpList(String url, Observer<RobotSpListBean> observer) {
        iProduct.fullDynamicGetRobotSpList(url, observer);
    }

    @Override
    public void getProductImage(String url, Observer<ResponseBody> observer) {
        iProduct.getProductImage(url, observer);
    }

    @Override
    public void getScene(String json, Observer<SceneBean> observer) {
        iProduct.getScene(json, observer);
    }

    @Override
    public void getSkin(String url, Observer<ResponseBody> observer) {
        iProduct.getSkin(url, observer);
    }

    @Override
    public void getContent(String lan, String sn, Observer<ContentBean> observer) {
        iProduct.getContent(lan, sn, observer);
    }

    @Override
    public void getContentType(String url, Observer<ContentTypeBean> observer) {
        iProduct.getContentType(url, observer);
    }

    @Override
    public void getAllClothType(String sn, Observer<ClothTypeBean> observer) {
        iProduct.getAllClothType(sn, observer);
    }

    @Override
    public void getClothingList(String sn, String secondLevel, String season, double minPrice, double maxPrice, Observer<ClothListBean> observer) {
        iProduct.getClothingList(sn, secondLevel, season, minPrice, maxPrice, observer);
    }
}
