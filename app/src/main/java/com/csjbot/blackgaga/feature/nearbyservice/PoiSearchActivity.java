package com.csjbot.blackgaga.feature.nearbyservice;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.cart.adapter.MapListAdapter;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.router.BRouterPath;
import com.csjbot.blackgaga.util.BlackgagaLogger;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.coshandler.log.Csjlogger;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * poi搜索，进入时直接进行周边搜索
 */
@Route(path = BRouterPath.NEAR_BY_SEARCH)
public class PoiSearchActivity extends BaseModuleActivity implements
        OnGetPoiSearchResultListener, OnGetSuggestionResultListener {
    public static final String KEYWORD = "keyword";

    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;
    private BaiduMap mBaiduMap = null;
    private MapListAdapter adapter;
    /**
     * 搜索关键字输入窗口
     */
    private EditText editCity = null;
    private AutoCompleteTextView keyWorldsView = null;
    private ArrayAdapter<String> sugAdapter = null;
    private int loadIndex = 0;

    //    LatLng center = new LatLng(39.92235, 116.380338);
    // 暂时用默认的经纬度，因为
    private LatLng center;
    private int radius = 2000;

    private LatLng theDefault = new LatLng(31.391546, 120.987261);//昆山市中心
    private int searchType = 0;  // 搜索的类型，在显示时区分

    private String text = null;
    private LatLng LatLng_end;
    private TextureMapView mMapView;
    private ListView mMapLstView;
    private List<PoiInfo> dataList;
    private boolean fakeLocation;

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
//        if (TextUtils.isEmpty(Constants.LocationInfo.address)) {
//            finish();
//            CSJToast.showToast(this, R.string.Locate_errors, 100);
//        }

        getTitleView().setBackVisibility(View.VISIBLE);

        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);

        // 初始化建议搜索模块，注册建议搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);

        editCity = (EditText) findViewById(R.id.city);
        keyWorldsView = (AutoCompleteTextView) findViewById(R.id.searchkey);
        sugAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line);
        keyWorldsView.setAdapter(sugAdapter);
        keyWorldsView.setThreshold(1);
        mMapView = (TextureMapView) findViewById(R.id.map);
        mBaiduMap = mMapView.getMap();
        mMapLstView = (ListView) findViewById(R.id.maplistView);
        /*
         * 当输入关键字变化时，动态更新建议列表
         */
        keyWorldsView.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
                if (cs.length() <= 0) {
                    return;
                }

                /*
                 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                 */
                mSuggestionSearch
                        .requestSuggestion((new SuggestionSearchOption())
                                .keyword(cs.toString()).city(editCity.getText().toString()));
            }
        });
        Intent intent = getIntent();
        text = intent.getStringExtra(KEYWORD);

        if (text.equals("快捷酒店")) {
            radius *= 10;
        }else if(text.equals("出租车")){
            radius *= 10;
        }

        String longitude = SharedPreUtil.getString(SharedKey.LOCATION, SharedKey.LONGITUDE);
        String latitude = SharedPreUtil.getString(SharedKey.LOCATION, SharedKey.LATITUDE);
        if (!TextUtils.isEmpty(longitude) && !TextUtils.isEmpty(latitude)) {
            center = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
        } else {
            center = new LatLng(Constants.LocationInfo.latitude, Constants.LocationInfo.longitude);
        }

//        fakeLocation = SharedPreUtil.getBoolean(SharedKey.POISEARCH_FAKELOCATION, SharedKey.POISEARCH_FAKELOCATION);
//        Csjlogger.debug("fakeLocation======>" + fakeLocation);
//        if (!fakeLocation) {
//            center = new LatLng(Constants.LocationInfo.latitude, Constants.LocationInfo.longitude);
//        } else {
//            center = new LatLng(34.442079, 108.768576);//咸阳机场
//        }
        findViewById(R.id.bt_food).setOnClickListener(view -> text = "美食");

        findViewById(R.id.bt_scenic).setOnClickListener(view -> text = "景点");

        findViewById(R.id.bt_hotel).setOnClickListener(view -> text = "酒店");
        // 直接进行周边搜索
        searchNearbyProcess(text, center);
        CsjlogProxy.getInstance().info("定位地址:" + Constants.LocationInfo.address);
    }

    //    @Override
    //    public void onCreate(Bundle savedInstanceState) {
    //        super.onCreate(savedInstanceState);
    //        setContentView(R.layout.activity_poisearch);
    //        // 初始化搜索模块，注册搜索事件监听
    //        mPoiSearch = PoiSearch.newInstance();
    //        mPoiSearch.setOnGetPoiSearchResultListener(this);
    //
    //        // 初始化建议搜索模块，注册建议搜索事件监听
    //        mSuggestionSearch = SuggestionSearch.newInstance();
    //        mSuggestionSearch.setOnGetSuggestionResultListener(this);
    //
    //        editCity = (EditText) findViewById(R.id.city);
    //        keyWorldsView = (AutoCompleteTextView) findViewById(R.id.searchkey);
    //        sugAdapter = new ArrayAdapter<String>(this,
    //                android.R.layout.simple_dropdown_item_1line);
    //        keyWorldsView.setAdapter(sugAdapter);
    //        keyWorldsView.setThreshold(1);
    //        mBaiduMap = ((SupportMapFragment) (getSupportFragmentManager()
    //                .findFragmentById(R.id.map))).getBaiduMap();
    //        /**
    //         * 当输入关键字变化时，动态更新建议列表
    //         */
    //        keyWorldsView.addTextChangedListener(new TextWatcher() {
    //
    //            @Override
    //            public void afterTextChanged(Editable arg0) {
    //
    //            }
    //
    //            @Override
    //            public void beforeTextChanged(CharSequence arg0, int arg1,
    //                                          int arg2, int arg3) {
    //
    //            }
    //
    //            @Override
    //            public void onTextChanged(CharSequence cs, int arg1, int arg2,
    //                                      int arg3) {
    //                if (cs.length() <= 0) {
    //                    return;
    //                }
    //
    //                /**
    //                 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
    //                 */
    //                mSuggestionSearch
    //                        .requestSuggestion((new SuggestionSearchOption())
    //                                .keyword(cs.toString()).city(editCity.getText().toString()));
    //            }
    //        });
    //        Intent intent = getIntent();
    //        text = intent.getStringExtra(KEYWORD);
    //        if (text != null) {
    //            Toast.makeText(this, "text=" + text, Toast.LENGTH_SHORT).show();
    //        }
    //        findViewById(R.id.bt_food).setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View view) {
    //                text = "美食";
    //                Toast.makeText(PoiSearchActivity.this, "text=" + text, Toast.LENGTH_SHORT).show();
    //            }
    //        });
    //
    //        findViewById(R.id.bt_scenic).setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View view) {
    //                text = "景点";
    //                Toast.makeText(PoiSearchActivity.this, "text=" + text, Toast.LENGTH_SHORT).show();
    //            }
    //        });
    //
    //        findViewById(R.id.bt_hotel).setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View view) {
    //                text = "酒店";
    //                Toast.makeText(PoiSearchActivity.this, "text=" + text, Toast.LENGTH_SHORT).show();
    //            }
    //        });
    //
    //    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_poisearch;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_MIN_VOICE));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        mPoiSearch.destroy();
        mSuggestionSearch.destroy();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * 响应城市内搜索按钮点击事件
     *
     * @param v
     */
    public void searchButtonProcess(View v) {
        searchType = 1;
        String citystr = editCity.getText().toString();
        //        String keystr = keyWorldsView.getText().toString();
        String keystr = text;
        mPoiSearch.searchInCity((new PoiCitySearchOption())
                .city(citystr).keyword(keystr).pageNum(loadIndex));
    }

    /**
     * 响应周边搜索按钮点击事件
     *
     * @param v
     */
    public void searchNearbyProcess(View v) {
        //        searchType = 2;
        //        PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption().keyword(keyWorldsView.getText()
        //                .toString()).sortType(PoiSortType.distance_from_near_to_far).location(center)
        //                .radius(radius).pageNum(loadIndex);
        //        mPoiSearch.searchNearby(nearbySearchOption);
        searchNearbyProcess(text, new LatLng(34.442079, 108.768576));
    }

    public void Process(View v) {
        EditText edittext = (EditText) findViewById(R.id.edittext);
        String et = edittext.getText().toString();

        searchNearbyProcess(et, center);
    }

    public void searchNearbyProcess(String text, LatLng latLng) {
        searchType = 2;
        PoiNearbySearchOption nearbySearchOption;
        if (text == null) {
            nearbySearchOption = new PoiNearbySearchOption().keyword(keyWorldsView.getText()
                    .toString()).sortType(PoiSortType.distance_from_near_to_far).location(latLng)
                    .radius(radius).pageNum(loadIndex);
        } else {
            nearbySearchOption = new PoiNearbySearchOption().keyword(text)
                    .sortType(PoiSortType.distance_from_near_to_far).location(latLng)
                    .radius(radius).pageNum(loadIndex);
        }
        mPoiSearch.searchNearby(nearbySearchOption);
    }

    public void goToNextPage(View v) {
        loadIndex++;
        searchButtonProcess(null);
    }


    /**
     * 获取POI搜索结果，包括searchInCity，searchNearby，searchInBound返回的搜索结果
     *
     * @param result
     */
    public void onGetPoiResult(PoiResult result) {
        //更改地图比例尺的大小
        //        {"10m", "20m", "50m", "100m", "200m", "500m", "1km", "2km", "5km", "10km", "20km", "25km", "50km", "100km", "200km", "500km", "1000km", "2000km"}
        //        Level依次为：20、19、18、17、16、15、14、13、12、11、10、9、8、7、6、5、4、3
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(15).build()));
        //当附近搜索不到时,定位到市政府
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            mBaiduMap.clear();
            //            searchNearbyProcess(text, theDefault);
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(theDefault);
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            switch (searchType) {
                case 2:
                    showNearbyArea(theDefault, radius);
                    searchNearbyProcess(text, theDefault);
                    break;
                default:
                    break;
            }
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            mapLise(result);
            mBaiduMap.clear();
            PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result);
            overlay.addToMap();
            overlay.zoomToSpan();

            switch (searchType) {
                case 2:
                    showNearbyArea(center, radius);
                    break;
                default:
                    break;
            }
            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            StringBuilder strInfo = new StringBuilder("在");
            for (CityInfo cityInfo : result.getSuggestCityList()) {
                strInfo.append(cityInfo.city);
                strInfo.append(",");
            }
            strInfo.append("找到结果");
            Toast.makeText(PoiSearchActivity.this, strInfo.toString(), Toast.LENGTH_LONG)
                    .show();
        }
    }

    /**
     * 添加地图信息列表
     *
     * @param result
     */
    private void mapLise(PoiResult result) {
        dataList = new ArrayList<>();
        List<PoiInfo> allPoi = result.getAllPoi();
        for (PoiInfo poiInfo : allPoi) {
            PoiInfo info = new PoiInfo();
            info.name = poiInfo.name;
            info.address = poiInfo.address;
            info.location = poiInfo.location;
            info.postCode = getLatLngDistance(center, poiInfo.location);
            dataList.add(info);
        }
        adapter = new MapListAdapter(0, dataList);
        mMapLstView.setAdapter(adapter);
        initEvent();
    }

    /**
     * 计算两点之间距离
     *
     * @param start
     * @param end
     * @return String  多少m ,  多少km
     */
    public String getLatLngDistance(LatLng start, LatLng end) {

        double lat1 = (Math.PI / 180) * start.latitude;
        double lat2 = (Math.PI / 180) * end.latitude;

        double lon1 = (Math.PI / 180) * start.longitude;
        double lon2 = (Math.PI / 180) * end.longitude;

        //地球半径
        double R = 6371.004;

        //两点间距离 m，如果想要米的话，结果*1000就可以了
        double dis = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;
        NumberFormat nFormat = NumberFormat.getNumberInstance();  //数字格式化对象
        if (dis < 1) {               //当小于1千米的时候用,用米做单位保留一位小数

            nFormat.setMaximumFractionDigits(1);    //已可以设置为0，这样跟百度地图APP中计算的一样
            dis *= 1000;
            return nFormat.format(dis) + "m";
        } else {
            nFormat.setMaximumFractionDigits(2);
            return nFormat.format(dis) + "km";
        }


    }

    /**
     * 条目点击
     */
    private void initEvent() {
        mMapLstView.setOnItemClickListener((parent, view, position, id) -> {
            adapter.setCheckposition(position);
            adapter.notifyDataSetChanged();
            PoiInfo poiInfo = dataList.get(position);
            LatLng address = poiInfo.location;
            PlanNode enNode = PlanNode.withLocation(address);
            SearchProcess(enNode);
            if (center != null) {
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(center));//点击条目回到起始点
            }
        });
    }

    /**
     * 获取POI详情搜索结果，得到searchPoiDetail返回的搜索结果
     *
     * @param result
     */
    @Override
    public void onGetPoiDetailResult(PoiDetailResult result) {
        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(PoiSearchActivity.this, R.string.sorry_no_find_result, Toast.LENGTH_SHORT)
                    .show();
            switch (result.error) {
                case RESULT_NOT_FOUND:
                    Log.e("error", "RESULT_NOT_FOUND");
                    break;
                case AMBIGUOUS_KEYWORD:
                    Log.e("error", "AMBIGUOUS_KEYWORD");
                    break;
                case AMBIGUOUS_ROURE_ADDR:
                    Log.e("error", "AMBIGUOUS_ROURE_ADDR");
                    break;
                case NOT_SUPPORT_BUS:
                    Log.e("error", "NOT_SUPPORT_BUS");
                    break;
                case NOT_SUPPORT_BUS_2CITY:
                    Log.e("error", "NOT_SUPPORT_BUS_2CITY");
                    break;
                case ST_EN_TOO_NEAR:
                    Log.e("error", "ST_EN_TOO_NEAR");
                    break;
                case KEY_ERROR:
                    Log.e("error", "KEY_ERROR");
                    break;
                case PERMISSION_UNFINISHED:
                    Log.e("error", "PERMISSION_UNFINISHED");
                    break;
                case NETWORK_TIME_OUT:
                    Log.e("error", "NETWORK_TIME_OUT");
                    break;
                case NETWORK_ERROR:
                    Log.e("error", "NETWORK_ERROR");
                    break;
                case POIINDOOR_BID_ERROR:
                    Log.e("error", "POIINDOOR_BID_ERROR");
                    break;
                case POIINDOOR_FLOOR_ERROR:
                    Log.e("error", "POIINDOOR_FLOOR_ERROR");
                    break;
                case POIINDOOR_SERVER_ERROR:
                    Log.e("error", "POIINDOOR_SERVER_ERROR");
                    break;
                case INDOOR_ROUTE_NO_IN_SAME_BUILDING:
                    Log.e("error", "INDOOR_ROUTE_NO_IN_SAME_BUILDING");
                    break;
                case MASS_TRANSIT_SERVER_ERROR:
                    Log.e("error", "MASS_TRANSIT_SERVER_ERROR");
                    break;
                case MASS_TRANSIT_OPTION_ERROR:
                    Log.e("error", "MASS_TRANSIT_OPTION_ERROR");
                    break;
                case MASS_TRANSIT_NO_POI_ERROR:
                    Log.e("error", "MASS_TRANSIT_NO_POI_ERROR");
                    break;
                case SEARCH_SERVER_INTERNAL_ERROR:
                    Log.e("error", "SEARCH_SERVER_INTERNAL_ERROR");
                    break;
                case SEARCH_OPTION_ERROR:
                    Log.e("error", "SEARCH_OPTION_ERROR");
                    break;
                case REQUEST_ERROR:
                    Log.e("error", "REQUEST_ERROR");
                    break;


            }
        } else {
            //            Toast.makeText(PoiSearchActivity.this, result.getName() + ": " + result.getAddress(), Toast.LENGTH_SHORT)
            //                    .show();
            TextView tv = new TextView(PoiSearchActivity.this);
            tv.setText(result.getName());
            tv.setOnClickListener(view -> {
                mBaiduMap.hideInfoWindow();
                //                    mBaiduMap.hideInfoWindow();
                //                    Toast.makeText(PoiSearchActivity.this, "弹框被点击了", Toast.LENGTH_SHORT).show();
            });
            InfoWindow infoWindow = new InfoWindow(tv, result.location, -80);
            mBaiduMap.showInfoWindow(infoWindow);
            //            mBaiduMap.hideInfoWindow();
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    /**
     * 获取在线建议搜索结果，得到requestSuggestion返回的搜索结果
     *
     * @param res
     */
    @Override
    public void onGetSuggestionResult(SuggestionResult res) {
        if (res == null || res.getAllSuggestions() == null) {
            return;
        }
        List<String> suggest = new ArrayList<>();
        for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
            if (info.key != null) {
                suggest.add(info.key);
            }
        }
        sugAdapter = new ArrayAdapter<>(PoiSearchActivity.this, android.R.layout.simple_dropdown_item_1line, suggest);
        keyWorldsView.setAdapter(sugAdapter);
        sugAdapter.notifyDataSetChanged();
    }

    /**
     * 点击定位图标移动到按当前位置
     *
     * @param v
     */
    public void mobile(View v) {
        if (center != null) {
            //带平移的动画
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(center));
        }
    }

    private class MyPoiOverlay extends PoiOverlay {

        MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            PoiInfo poi = getPoiResult().getAllPoi().get(index);
            // if (poi.hasCaterDetails) {
            mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
                    .poiUid(poi.uid));
            // }
            LatLng_end = poi.location;
            Log.e("POI", "uid=" + poi.uid);
            Log.e("POI", "----" + poi.location.latitude + "---" + poi.location.longitude);
            PlanNode enNode = PlanNode.withLocation(LatLng_end);
            SearchProcess(enNode);
            return true;
        }
    }

    /**
     * 路径规划
     */
    private void SearchProcess(PlanNode enNode) {
        mBaiduMap.clear();
        PlanNode stNode = PlanNode.withLocation(center);
        if (enNode != null) {
            Csjlogger.debug("终点坐标：" + enNode.getLocation());
        } else {
            BlackgagaLogger.debug("终点异常！");
        }
        if (center != null) {
            Csjlogger.debug("起点坐标：" + center);
        } else {
            BlackgagaLogger.debug("起点异常！");
        }
        // 注册 路径规划 服务
        RoutePlanSearch search = RoutePlanSearch.newInstance();
        MyGetRoutePlanResultListener getRoutePlanResultListener = new MyGetRoutePlanResultListener();
        search.setOnGetRoutePlanResultListener(getRoutePlanResultListener);

        search.walkingSearch((new WalkingRoutePlanOption()).from(stNode).to(
                enNode));
    }

    private class MyGetRoutePlanResultListener implements
            OnGetRoutePlanResultListener {

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult arg0) {

        }

        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

        }

        @Override
        public void onGetTransitRouteResult(TransitRouteResult arg0) {

        }

        @Override
        public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

        }

        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(PoiSearchActivity.this, R.string.no_results, Toast.LENGTH_SHORT).show();
            }
            assert result != null;
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {

                Toast.makeText(PoiSearchActivity.this, R.string.no_address_wrong, Toast.LENGTH_SHORT).show();
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {

                WalkingRouteLine route = result.getRouteLines().get(0);
                WalkingRouteOverlay overlay = new WalkingRouteOverlay(mBaiduMap);
                mBaiduMap.setOnMarkerClickListener(overlay);

                overlay.setData(route);
                overlay.addToMap();
                overlay.zoomToSpan();

                MapStatus mapStatus = new MapStatus.Builder().zoom(15).build();
                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                        .newMapStatus(mapStatus);
                // 改变地图状态
                mBaiduMap.setMapStatus(mMapStatusUpdate);

                Toast.makeText(PoiSearchActivity.this, R.string.map_path_planning, Toast.LENGTH_SHORT).show();


            }
        }
    }


    /**
     * 对周边检索的范围进行绘制
     *
     * @param center
     * @param radius
     */
    public void showNearbyArea(LatLng center, int radius) {
        BitmapDescriptor centerBitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_openmap_focuse_mark);
        MarkerOptions ooMarker = new MarkerOptions().position(center).icon(centerBitmap);
        mBaiduMap.addOverlay(ooMarker);
        //添加定位中心点颜色
        OverlayOptions ooDot = new DotOptions().center(center).radius(5)
                .color(0xFF346BDA);
        mBaiduMap.addOverlay(ooDot);
        //添加圆圈颜色
        OverlayOptions ooCircle = new CircleOptions().fillColor(0x55346BDA)
                .center(center).stroke(new Stroke(3, 0x55346BDA))
                .radius(radius);
        mBaiduMap.addOverlay(ooCircle);
    }

    /**
     * 对区域检索的范围进行绘制
     *
     * @param bounds
     */
    public void showBound(LatLngBounds bounds) {
        BitmapDescriptor bdGround = BitmapDescriptorFactory
                .fromResource(R.drawable.ground_overlay);

        OverlayOptions ooGround = new GroundOverlayOptions()
                .positionFromBounds(bounds).image(bdGround).transparency(0.8f);
        mBaiduMap.addOverlay(ooGround);

        MapStatusUpdate u = MapStatusUpdateFactory
                .newLatLng(bounds.getCenter());
        mBaiduMap.setMapStatus(u);

        bdGround.recycle();
    }

    @Override
    public boolean isOpenChat() {
        return false;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public void init() {
        super.init();
        getTitleView().setBackVisibility(View.VISIBLE);
    }
}
