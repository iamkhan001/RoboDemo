package com.csjbot.blackgaga.feature.clothing.mvp;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.feature.clothing.ClothingListActivity;
import com.csjbot.blackgaga.feature.clothing.adapter.ClothTypeAdapter;
import com.csjbot.blackgaga.feature.clothing.bean.ClothTypeBean;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.model.http.product.ProductProxy;
import com.csjbot.blackgaga.router.BRouter;
import com.csjbot.blackgaga.router.BRouterPath;
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
 * @date 2018/11/13 9:51
 * @email 714081644@qq.com
 */

public class ClothingTypePresenter implements ClothingTypeContract.Presenter {
    private ClothingTypeContract.View mView;
    private PagerGridLayoutManager mManager;
    private ClothTypeAdapter mAdapter;
    private List<ClothTypeBean.ResultBean> mList;

    public ClothingTypePresenter(ClothingTypeContract.View mView) {
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void initData() {
        mList = new ArrayList<>();
        RecyclerView mRvCloth = mView.getRvCloth();

        //可以根据大小屏动态设置不同的布局
        if (TextUtils.equals(BuildConfig.FLAVOR, BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                || TextUtils.equals(BuildConfig.FLAVOR, BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS)) {
            mAdapter = new ClothTypeAdapter(R.layout.item_cloth_type_vertical, mList);
        } else {
            mAdapter = new ClothTypeAdapter(R.layout.item_cloth_type, mList);
        }

        mManager = new PagerGridLayoutManager(2, 3, PagerGridLayoutManager.HORIZONTAL);
        mRvCloth.setLayoutManager(mManager);
        // 设置滚动辅助工具
        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
        pageSnapHelper.attachToRecyclerView(mRvCloth);
        mRvCloth.setHasFixedSize(true);
        mRvCloth.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) -> BRouter.jumpActivity(BRouterPath.PRODUCT_CLOTHING_LIST,
                ClothingListActivity.SELECT_TYPE, mList.get(position).getSecondLevel()));

        ProductProxy.newProxyInstance().getAllClothType(Robot.SN, new Observer<ClothTypeBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull ClothTypeBean clothTypeBean) {
                if (clothTypeBean == null) {
                    mView.isNoData(true);
                    return;
                }
                if (TextUtils.equals(clothTypeBean.getMessage(), "ok")
                        && TextUtils.equals(clothTypeBean.getStatus(), "200")) {
                    mList.clear();
                    Constants.ClothProduct.clothTypeBean = clothTypeBean;
                    if (clothTypeBean.getResult() != null && !clothTypeBean.getResult().isEmpty()) {
                        mView.isNoData(false);
                        mList.addAll(clothTypeBean.getResult());
                        mView.speakMessage("这是本店商品，请您随意查看");
                        mAdapter.setNewData(mList);
                    } else {
                        mView.isNoData(true);
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mView.isNoData(true);
                CsjlogProxy.getInstance().error("获取服装种类列表失败: " + e.getMessage());
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
}
