package com.csjbot.blackgaga.guide;

import android.content.Context;

import com.csjbot.blackgaga.cart.entity.RobotMenuListBean;
import com.csjbot.blackgaga.cart.entity.RobotSpListBean;
import com.csjbot.blackgaga.guide.runnable.DelayIntoActivityRunnable;
import com.csjbot.blackgaga.guide.runnable.DelayRunnable;
import com.csjbot.blackgaga.guide.splash_thread_model.CsjSplashPlatform;
import com.csjbot.blackgaga.model.http.factory.ServerFactory;
import com.csjbot.blackgaga.model.http.product.ProductProxy;
import com.csjbot.blackgaga.model.http.product.listeners.MenuListListener;
import com.csjbot.blackgaga.model.http.product.listeners.SpListListener;
import com.csjbot.blackgaga.util.BlackgagaLogger;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/12/13.
 */

public class GuidePresenter implements GuideContract.presenter {
    private GuideContract.view view;

    private ProductProxy productProxy;

    private CsjSplashPlatform CsjSplashPlatform;

    private Context mContext;

    public GuidePresenter(Context context) {
        this.mContext = context;
        CsjSplashPlatform = CsjSplashPlatform.getInstants();
        productProxy = ServerFactory.createProduct();
        productProxy.ISUPDATA = true;
        CsjSplashPlatform.addRunnable(DelayRunnable.class, "延迟线程");
        CsjSplashPlatform.addRunnable(DelayIntoActivityRunnable.class, "进入到activity");
    }

    @Override
    public GuideContract.view getView() {
        return view;
    }

    @Override
    public void initView(GuideContract.view view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        if (view != null) {
            view = null;
        }
    }

    @Override
    public void startTh() {
        CsjSplashPlatform.openCsjSplashThreadDelayLoop("进入到activity", 0, 1000, 5, new CsjSplashPlatform.ThreadOpenListener() {
            @Override
            public void openSuccess() {
                BlackgagaLogger.debug("chenqi 进入到activity 我打开成功！");
            }

            @Override
            public void openFailed() {
                BlackgagaLogger.debug("chenqi 进入到activity 我打开失败！");
            }
        }).setMessage(message -> {
            if (message.obj.toString().equals("DelayIntoActivityRunnable")) {
                stopDown();
                view.showButton();
            }
        });
    }


    @Override
    public void delayTh(Type type) {
        CsjSplashPlatform.openCsjSplashThread("延迟线程", 3000, new CsjSplashPlatform.ThreadOpenListener() {
            @Override
            public void openSuccess() {
                BlackgagaLogger.debug("chenqi 延迟线程 我打开成功！");
            }

            @Override
            public void openFailed() {
                BlackgagaLogger.debug("chenqi 延迟线程 我打开失败！");
            }
        }).setMessage(message -> {
            if (message.obj.toString().equals("DelayRunnable")) {
                stopDelay();
                view.timeOut(type);
            }
        });
    }


    @Override
    public void releaseHandler() {
        CsjSplashPlatform.removeRunnableAll();
    }

    @Override
    public void stopDown() {
        CsjSplashPlatform.closeCsjSplashThread("进入到activity", new CsjSplashPlatform.ThreadCloseListener() {
            @Override
            public void closeSuccess() {
                BlackgagaLogger.debug("chenqi  进入到activity 关闭成功");
            }

            @Override
            public void closeFailed() {
                BlackgagaLogger.debug("chenqi  进入到activity 关闭失败");
            }
        });
    }

    @Override
    public void stopDelay() {
        CsjSplashPlatform.closeCsjSplashThread("延迟线程", new CsjSplashPlatform.ThreadCloseListener() {
            @Override
            public void closeSuccess() {
                BlackgagaLogger.debug("chenqi  延迟线程 关闭成功");
            }

            @Override
            public void closeFailed() {
                BlackgagaLogger.debug("chenqi  延迟线程 关闭失败");
            }
        });
    }

    @Override
    public void actionSpLoad() {
        productProxy.getRobotMenuList(new MenuListListener() {
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
                getSp();
            }

            @Override
            public void loadError500() {
                view.showButton();
            }

            @Override
            public void getMenuList(RobotMenuListBean bean) {
            }

            @Override
            public void onMenuError(Throwable e) {
            }


            @Override
            public void onLocaleMenuList(RobotMenuListBean bean) {
            }

            @Override
            public void ImageSize(int num) {
            }
        });
    }


    private void getSp() {
        productProxy.robotSpList("", mContext, new SpListListener() {
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
                view.shutDown();
            }

            @Override
            public void loadError500() {

            }

            @Override
            public void getSpList(RobotSpListBean bean) {
            }

            @Override
            public void onSpError(Throwable e) {
            }

            @Override
            public void onLocaleSpList(RobotSpListBean bean) {

            }

            @Override
            public void ImageSize(int num) {
            }
        });
    }

    public enum Type {
        LINUX, NET, DATA
    }
}
