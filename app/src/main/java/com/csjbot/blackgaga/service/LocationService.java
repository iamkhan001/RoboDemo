package com.csjbot.blackgaga.service;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.text.TextUtils;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.model.http.factory.ServerFactory;
import com.csjbot.blackgaga.model.http.product.ProductProxy;
import com.csjbot.coshandler.log.CsjlogProxy;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public class LocationService extends BaseService {

    public LocationClient mLocationClient = null;

    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        checkWifi();
        initConfig();
        startQuery();
    }

    private void startQuery() {
        mLocationClient.start();
    }

    private void initConfig() {

        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                switch (bdLocation.getLocType()){
                    case BDLocation.TypeNetWorkLocation:
                    case BDLocation.TypeOffLineLocation:
                        CsjlogProxy.getInstance().info("定位地址:"+bdLocation.getAddrStr());
                        if(!TextUtils.isEmpty(bdLocation.getAddrStr())){
                            Constants.LocationInfo.latitude = bdLocation.getLatitude();
                            Constants.LocationInfo.longitude = bdLocation.getLongitude();
                            Constants.LocationInfo.radius = bdLocation.getRadius();
                            Constants.LocationInfo.address = bdLocation.getAddrStr();
                            Constants.LocationInfo.city = bdLocation.getCity();
                            // 将位置上传至服务器
                            String json = "{\"sn\":\""+ ProductProxy.SN+"\",\"address\":\""+Constants.LocationInfo.address+"\",\"latitude\":\""+Constants.LocationInfo.latitude+"\"," +
                                    "\"longitude\":\""+Constants.LocationInfo.longitude+"\",\"coordinates\":\"bd09ll\"}";
                            CsjlogProxy.getInstance().info("location json:"+json);
                            ServerFactory.createPosition().uploadPosition(json, new Observer<ResponseBody>() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {

                                }

                                @Override
                                public void onNext(@NonNull ResponseBody responseBody) {
                                    CsjlogProxy.getInstance().info("位置上传服务器!");
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    CsjlogProxy.getInstance().info("位置上传服务器失败!"+e.toString());
                                }

                                @Override
                                public void onComplete() {
                                    CsjlogProxy.getInstance().info("位置上传服务器成功!");
                                }
                            });
                            mLocationClient.stop();
                            LocationService.this.stopSelf();
                        }
                        break;
                    default:
                        break;
                }
            }
        });


        LocationClientOption option = new LocationClientOption();

        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

        //可选，设置返回经纬度坐标类型，默认gcj02
        //gcj02：国测局坐标；
        //bd09ll：百度经纬度坐标；
        //bd09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标
        option.setCoorType("bd09ll");

        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效
        option.setScanSpan(5000);

        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
        option.setIgnoreKillProcess(false);

        //可选，设置是否收集Crash信息，默认收集，即参数为false
        option.SetIgnoreCacheException(false);

        mLocationClient.setLocOption(option);

    }

    /**
     * 检查Wifi如果是关闭状态则开启Wifi
     */
    public void checkWifi() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int state = wifiManager.getWifiState();
        if (state == WifiManager.WIFI_STATE_DISABLED) {
            wifiManager.setWifiEnabled(true);
        }
    }
}
