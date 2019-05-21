package com.csjbot.blackgaga.feature.clothing.mvp;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.feature.clothing.ClothingDetailsActivity;
import com.csjbot.blackgaga.feature.clothing.adapter.ClothListAdapter;
import com.csjbot.blackgaga.feature.clothing.bean.ClothListBean;
import com.csjbot.blackgaga.feature.clothing.bean.SelectClothBean;
import com.csjbot.blackgaga.feature.clothing.popup.SelectClothPopup;
import com.csjbot.blackgaga.model.http.product.ProductProxy;
import com.csjbot.blackgaga.router.BRouterPath;
import com.csjbot.blackgaga.util.GsonUtils;
import com.csjbot.blackgaga.widget.pagergrid.PagerGridLayoutManager;
import com.csjbot.blackgaga.widget.pagergrid.PagerGridSnapHelper;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.log.CsjlogProxy;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * @author ShenBen
 * @date 2018/11/12 11:13
 * @email 714081644@qq.com
 */

public class ClothingListPresenter implements ClothingListContract.Presenter, PagerGridLayoutManager.PageListener {
    private Activity mActivity;
    private ClothingListContract.View mView;
    private ClothListAdapter mAdapter;
    private PagerGridLayoutManager mManager;
    private List<ClothListBean.ResultBean.GoodsListBean> mList;
    private SelectClothPopup mPopup;

    private List<SelectClothBean> mTypeList;
    private List<SelectClothBean> mSeasonList;

    private String mGoodsType;

    public ClothingListPresenter(Activity mActivity, ClothingListContract.View mView) {
        this.mActivity = mActivity;
        this.mView = mView;
        mView.setPresenter(this);

    }

    @Override
    public void init() {
        mList = new ArrayList<>();
        mTypeList = new ArrayList<>();
        mSeasonList = new ArrayList<>();

        mSeasonList.add(new SelectClothBean("春"));
        mSeasonList.add(new SelectClothBean("夏"));
        mSeasonList.add(new SelectClothBean("秋"));
        mSeasonList.add(new SelectClothBean("冬"));

        //可以根据大小屏动态设置不同的布局
        if (TextUtils.equals(BuildConfig.FLAVOR, BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                || TextUtils.equals(BuildConfig.FLAVOR, BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS)) {
            mAdapter = new ClothListAdapter(R.layout.item_cloth_list_vertical, mList);
        } else {
            mAdapter = new ClothListAdapter(R.layout.item_cloth_list, mList);
        }

        RecyclerView mRvCloth = mView.getRvCloth();
        mManager = new PagerGridLayoutManager(2, 3, PagerGridLayoutManager.HORIZONTAL);
        mManager.setPageListener(this);
        mRvCloth.setLayoutManager(mManager);
        // 设置滚动辅助工具
        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
        pageSnapHelper.attachToRecyclerView(mRvCloth);
        mRvCloth.setHasFixedSize(true);
        mRvCloth.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) ->
                ARouter.getInstance()
                .build(BRouterPath.PRODUCT_CLOTHING_DETAIL)
                .withString(ClothingDetailsActivity.CLOTH_DETAIL, GsonUtils.objectToJson(mList.get(position)))
                .navigation(mActivity, 99));
    }

    @Override
    public void setGoodStyyle(String goodsStyle) {
        mGoodsType = goodsStyle;
    }

    @Override
    public void loadData(String season, double minPrice, double maxPrice) {
        ProductProxy.newProxyInstance().getClothingList(
                Robot.SN,
                mGoodsType, //衣服类型
                season, //季节
                minPrice,//最低价
                maxPrice,//最高价
                new Observer<ClothListBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ClothListBean clothListBean) {
                        if (clothListBean == null) {
                            mView.isNoData(true);
                            return;
                        }
                        if (TextUtils.equals("ok", clothListBean.getMessage())
                                && TextUtils.equals("200", clothListBean.getStatus())) {
                            mList.clear();
                            if (clothListBean.getResult().getGoodsList() != null
                                    && !clothListBean.getResult().getGoodsList().isEmpty()) {
                                mView.isNoData(false);
                                mList.addAll(clothListBean.getResult().getGoodsList());
                                mAdapter.setNewData(mList);
                            } else {
                                mView.isNoData(true);
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.isNoData(true);
                        CsjlogProxy.getInstance().error("服装列表筛选查询失败: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void previousPage() {
        if (mManager != null) {
            mManager.smoothPrePage();
        }
    }

    @Override
    public void nextPage() {
        if (mManager != null) {
            mManager.smoothNextPage();
        }
    }

    @Override
    public void showSelectPopup() {
        if (mPopup == null) {
            mPopup = new SelectClothPopup(mActivity);
            mPopup.setOnSelectClothDetailListener((versionType, season, minPrice, maxPrice) ->
                    loadData(season, minPrice, maxPrice));
            //mPopup.setTypeList(mTypeList);
            mPopup.setSeasonList(mSeasonList);
        }

        if (!mPopup.isShowing()) {
            mPopup.showPopupWindow(mView.getSelectCloth());
        }
    }

    @Override
    public void onPageSizeChanged(int pageSize) {

    }

    @Override
    public void onPageSelect(int pageIndex) {

    }
}
