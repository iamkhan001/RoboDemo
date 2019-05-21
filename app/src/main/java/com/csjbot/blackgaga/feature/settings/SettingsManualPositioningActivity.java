package com.csjbot.blackgaga.feature.settings;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.util.MaxLengthWatcher;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.blackgaga.widget.NewRetailDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 人工定位
 */
public class SettingsManualPositioningActivity extends BaseModuleActivity {

    TextureMapView mMapView;

    BaiduMap mBaiduMap;

    GeoCoder mSearch;

    LatLng mLatLng;

    String mAddress;

    @BindView(R.id.et_city)
    EditText et_city;

    @BindView(R.id.et_address)
    EditText et_address;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)|| BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))                ?  R.layout.vertical_activity_settings_manual_positioning : R.layout.activity_settings_manual_positioning;
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        getTitleView().setSettingsVisibility(View.GONE);
        getTitleView().setBackVisibility(View.VISIBLE);
    }

    @Override
    public void init() {
        super.init();

        mMapView = (TextureMapView) findViewById(R.id.texture_mapview);

        mBaiduMap = mMapView.getMap();

        mSearch = GeoCoder.newInstance();

        mSearch.setOnGetGeoCodeResultListener(listener);

        String address = SharedPreUtil.getString(SharedKey.LOCATION,SharedKey.ADDRESS,mAddress);
        if(TextUtils.isEmpty(address)){
            LatLng latLng = new LatLng(Constants.LocationInfo.latitude,Constants.LocationInfo.longitude);
            mLatLng = latLng;
            et_address.setText(Constants.LocationInfo.address);
            et_city.setText(Constants.LocationInfo.city);
            mAddress = Constants.LocationInfo.address;
            move();
            drawPoint();
        }else{
            double latitude = Double.valueOf(SharedPreUtil.getString(SharedKey.LOCATION,SharedKey.LATITUDE));
            double longitude = Double.valueOf(SharedPreUtil.getString(SharedKey.LOCATION,SharedKey.LONGITUDE));
            String city = SharedPreUtil.getString(SharedKey.LOCATION,SharedKey.CITY);
            LatLng latLng = new LatLng(latitude,longitude);
            mLatLng = latLng;
            mAddress = address;
            et_address.setText(address);
            et_city.setText(city);
            move();
            drawPoint();
        }

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mLatLng = latLng;
                move();
                drawPoint();
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(mLatLng));
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });

        et_city.addTextChangedListener(new MaxLengthWatcher(this, 11, et_city, getString(R.string.city_input_string_length_hint)));
        et_address.addTextChangedListener(new MaxLengthWatcher(this, 30, et_address, getString(R.string.address_input_string_length_hint)));

    }

    @OnClick(R.id.bt_search)
    public void search(View v){
        String city = et_city.getText().toString().trim();
        String address = et_address.getText().toString().trim();
        if(TextUtils.isEmpty(city) || TextUtils.isEmpty(address)){
            speak(R.string.input_success_location_info);
            Toast.makeText(context, getString(R.string.input_success_location_info), Toast.LENGTH_SHORT).show();
            return;
        }
        mSearch.geocode(new GeoCodeOption().city(city).address(address));
    }

    @OnClick(R.id.bt_save)
    public void save(View v){
        showNewRetailDialog(getString(R.string.hint), getString(R.string.save_current_location_info_hint),getString(R.string.ok),getString(R.string.cancel), new NewRetailDialog.OnDialogClickListener() {
            @Override
            public void yes() {
                if(mLatLng == null || TextUtils.isEmpty(mAddress)){
                    dismissNewRetailDialog();
                    speak(R.string.location_is_null);
                    Toast.makeText(context, R.string.location_is_null, Toast.LENGTH_SHORT).show();
                    return;
                }
                dismissNewRetailDialog();
                SharedPreUtil.putString(SharedKey.LOCATION,SharedKey.LATITUDE,mLatLng.latitude);
                SharedPreUtil.putString(SharedKey.LOCATION,SharedKey.LONGITUDE,mLatLng.longitude);
                SharedPreUtil.putString(SharedKey.LOCATION,SharedKey.ADDRESS,mAddress);
                SharedPreUtil.putString(SharedKey.LOCATION,SharedKey.CITY,et_city.getText().toString().trim());
                speak(getString(R.string.save_success));
                Toast.makeText(context, getString(R.string.save_success), Toast.LENGTH_SHORT).show();
                dismissNewRetailDialog();
            }

            @Override
            public void no() {
                dismissNewRetailDialog();
            }
        });

    }

    @OnClick(R.id.bt_clear)
    public void clear(View v){
        showNewRetailDialog(getString(R.string.warn), getString(R.string.location_clear_warn_hint), getString(R.string.clear), getString(R.string.cancel), new NewRetailDialog.OnDialogClickListener() {
            @Override
            public void yes() {
                SharedPreUtil.putString(SharedKey.LOCATION,SharedKey.LATITUDE,"");
                SharedPreUtil.putString(SharedKey.LOCATION,SharedKey.LONGITUDE,"");
                SharedPreUtil.putString(SharedKey.LOCATION,SharedKey.ADDRESS,"");
                speak(R.string.clear_success);
                Toast.makeText(context, getString(R.string.clear_success), Toast.LENGTH_SHORT).show();
                dismissNewRetailDialog();
            }

            @Override
            public void no() {
                dismissNewRetailDialog();
            }
        });
    }

    OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
            if(geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR){
                return;
            }
            getLog().info("getLocation:"+geoCodeResult.getLocation().latitude+","+geoCodeResult.getLocation().longitude);
            getLog().info("getAddress:"+geoCodeResult.getAddress());
            mLatLng = geoCodeResult.getLocation();
            mAddress = geoCodeResult.getAddress();

            move();
            drawPoint();
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
            mLatLng = reverseGeoCodeResult.getLocation();
            mAddress = reverseGeoCodeResult.getAddress();
            et_address.setText(mAddress);
            et_city.setText(reverseGeoCodeResult.getAddressDetail().city);
        }
    };

    private void move(){
        MapStatus mapStatus = new MapStatus.Builder()
                .target(mLatLng)
                .zoom(18)
                .build();
        MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mBaiduMap.setMapStatus(update);
    }

    private void drawPoint(){
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_openmap_focuse_mark);
        OverlayOptions option = new MarkerOptions()
                .position(mLatLng)
                .icon(bitmap);

        if(mBaiduMap != null){
            mBaiduMap.clear();
            mBaiduMap.addOverlay(option);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_MIN_VOICE));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mSearch != null){
            mSearch.destroy();
        }
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }
        return true;
    }



    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public boolean isOpenChat() {
        return false;
    }
}
