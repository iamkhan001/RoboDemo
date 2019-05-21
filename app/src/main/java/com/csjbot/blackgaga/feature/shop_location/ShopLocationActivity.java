package com.csjbot.blackgaga.feature.shop_location;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiDetailInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.coshandler.log.CsjlogProxy;

import java.util.List;

import butterknife.BindView;

/**
 * 店铺介绍
 *
 * @author ShenBen
 * @date 2018/11/27 15:31
 * @email 714081644@qq.com
 */

public class ShopLocationActivity extends BaseModuleActivity implements
        OnGetPoiSearchResultListener, OnGetSuggestionResultListener {

    @BindView(R.id.rg)
    RadioGroup rg;

    private SupportMapFragment mMap;
    private BaiduMap mBaiduMap;
    private PoiSearch mPoiSearch;
    private SuggestionSearch mSuggestionSearch;
    private String mKeyWord = "MALABATA专柜";

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.vertical_activity_shop_location;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public void init() {
        super.init();
        rg.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_malabata:
                    mKeyWord = "MALABATA专柜";
                    break;
                case R.id.rb_souad:
                    mKeyWord = "SOUAD专柜";
                    break;
                default:
                    mKeyWord = "SOUAD专柜";
                    break;

            }
            search(mKeyWord);
        });
        mMap = (SupportMapFragment) (getSupportFragmentManager().findFragmentById(R.id.map));
        mBaiduMap = mMap.getBaiduMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        // 初始化建议搜索模块，注册建议搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);

        search(mKeyWord);
    }

    @Override
    protected CharSequence initChineseSpeakText() {
        return "这是我们当前城市专柜分布情况，您可以切换查看!";
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        getTitleView().setBackVisibility(View.VISIBLE);
    }

    @Override
    public boolean isOpenChat() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMap.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mMap.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPoiSearch.destroy();
        mSuggestionSearch.destroy();
    }

    private void search(String keyWord) {
        mPoiSearch.searchInCity(new PoiCitySearchOption()
                .city(TextUtils.isEmpty(Constants.LocationInfo.city) ? "北京" : Constants.LocationInfo.city)
                .keyword(keyWord)
                .pageNum(0)
                .scope(1));
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(ShopLocationActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
            CsjlogProxy.getInstance().error("onGetPoiResult：SearchResult.ERRORNO.RESULT_NOT_FOUND");
            return;
        }

        if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
            CsjlogProxy.getInstance().error("onGetPoiResult：SearchResult.ERRORNO.NO_ERROR: " + poiResult.getAllPoi().size());
            mBaiduMap.clear();
            PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(poiResult);
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        if (poiDetailResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(ShopLocationActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        } else {
            CsjlogProxy.getInstance().error("onGetPoiDetailResult1：SearchResult.ERRORNO.NO_ERROR");
            Toast.makeText(ShopLocationActivity.this,
                    poiDetailResult.getName() + ": " + poiDetailResult.getAddress(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {
        if (poiDetailSearchResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(ShopLocationActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        } else {
            CsjlogProxy.getInstance().error("onGetPoiDetailResult2：SearchResult.ERRORNO.NO_ERROR");
            List<PoiDetailInfo> poiDetailInfoList = poiDetailSearchResult.getPoiDetailInfoList();
            if (null == poiDetailInfoList || poiDetailInfoList.isEmpty()) {
                Toast.makeText(ShopLocationActivity.this, "抱歉，检索结果为空", Toast.LENGTH_SHORT).show();
                return;
            }

            for (int i = 0; i < poiDetailInfoList.size(); i++) {
                PoiDetailInfo poiDetailInfo = poiDetailInfoList.get(i);
                if (null != poiDetailInfo) {
                    Toast.makeText(ShopLocationActivity.this,
                            poiDetailInfo.getName() + ": " + poiDetailInfo.getAddress(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {

    }

    private class MyPoiOverlay extends PoiOverlay {
        MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            PoiInfo poi = getPoiResult().getAllPoi().get(index);
            mPoiSearch.searchPoiDetail((new PoiDetailSearchOption()).poiUid(poi.uid));
            return true;
        }
    }
}
