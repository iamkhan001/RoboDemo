package com.csjbot.blackgaga.util;

import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by 孙秀艳 on 2018/1/29.
 */

public class WebSettingUtil {

    public static WebView initWebViewSetting(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

//缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

//其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        return webView;
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        webSettings.setLoadWithOverviewMode(true);
//        /*支持缩放*/
//        webSettings.setSupportZoom(true);
//        webSettings.setBuiltInZoomControls(true);
//        //        webSettings.setDisplayZoomControls(false);
//        //扩大比例的缩放
//        webSettings.setUseWideViewPort(true);
//        /*自适应*/
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        webSettings.setLoadWithOverviewMode(true);
//
//        webSettings.setLoadsImagesAutomatically(true);


//        //设置渲染的优先级
//        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
//        // 开启 DOM storage API 功能
//        webSettings.setDomStorageEnabled(true);
//        //开启 database storage API 功能
//        webSettings.setDatabaseEnabled(true);
//        String cacheDirPath = Constants.WEB_CACHE_PATH;
//        //设置数据库缓存路径
//        webSettings.setDatabasePath(cacheDirPath);
//        //设置  Application Caches 缓存目录
//        webSettings.setAppCachePath(cacheDirPath);
//        //开启 Application Caches 功能
//        webSettings.setAppCacheEnabled(true);
//
//        webSettings.setLoadWithOverviewMode(true);
//        //设置WebView支持JavaScript
//        webSettings.setJavaScriptEnabled(true);
//        //设置可以访问文件
//        webSettings.setAllowFileAccess(true);
//        //设置支持缩放
//        webSettings.setBuiltInZoomControls(true);
//        webSettings.setDefaultTextEncodingName("UTF-8");
//        webSettings.setBlockNetworkImage(false);
//        if (isNetworkAvailable(BaseApplication.getAppContext())) {
//            //有网络连接，设置默认缓存模式
//            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
//        } else {
//            //无网络连接，设置本地缓存模式
//            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        }
//    }
//
//    public static boolean isNetworkAvailable(Context context) {
//        ConnectivityManager connectivity = (ConnectivityManager) context
//                .getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connectivity != null) {
//            NetworkInfo info = connectivity.getActiveNetworkInfo();
//            if (info != null && info.isConnected()) {
//                // 当前网络是连接的
//                if (info.getState() == NetworkInfo.State.CONNECTED) {
//                    // 当前所连接的网络可用
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
    }

}
