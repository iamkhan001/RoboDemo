package com.csjbot.blackgaga.feature.product.productDetail;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.ai.ProductDetailWithoutShoppingCartAI;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.router.BRouterPath;
import com.csjbot.blackgaga.util.ShopcartUtil;

import butterknife.BindView;

/**
 * Created by 孙秀艳 on 2017/10/16.
 */

@Route(path = BRouterPath.PRODUCT_DETAIL)
public class ProductDetailWithoutShoppingCartActivity extends BaseModuleActivity {

    @BindView(R.id.product_detail_main)
    RelativeLayout layoutProductDetailMain;

    ProductDetailFragment productDetailFragment;

    ProductDetailWithoutShoppingCartAI mAI;

    public ProductDetailFragment getProductDetailFragment() {
        return productDetailFragment;
    }

    @Override
    public void init() {
        super.init();
        initView();
        initFragment();
        ShopcartUtil.getOrderList();
//        setShoppingCartListener();
        mAI = ProductDetailWithoutShoppingCartAI.newInstance();
        mAI.initAI(this);

//        String Industry = SharedPreUtil.getString(SharedKey.MAINPAGE, SharedKey.MAINPAGE_KEY, "");
//        if (Industry.equals("yinhang") || Industry.equals("jichang")) {
//            getTitleView().setShoppingCartVisibility(View.GONE);
//        } else {
//            getTitleView().setShoppingCartVisibility(View.VISIBLE);
//            getTitleView().setShoppingCartListener(() -> jumpActivity(ShoppingCartActivity.class));
//        }
        getTitleView().setShoppingCartVisibility(View.GONE);
    }


    @Override
    protected CharSequence initChineseSpeakText() {
//        RobotSpListBean.ResultBean.ProductBean bean = new Gson().fromJson(getIntent().getExtras().getString("productBean"), RobotSpListBean.ResultBean.ProductBean.class);
//        int color = getResources().getColor(R.color.product_list_speak_color);
//        SpannableStringBuilder ssb = new RichTextUtil()
//                .append("请问是")
//                .append("  加入购物车  ", -1, 40, v -> productDetailFragment.addShoppingCart())
//                .append("还是")
//                .append("  立即购买  ", -1, 40, v -> {
//                    productDetailFragment.isCheckSettlement();
//                    productDetailFragment.purchase();
//                })
//                .append("请说出您要的操作?")
//                .finish();
//
//        if (bean.getViewType() == 1) {//视频 只显示话术
//            setRobotChatMsg(ssb.toString());
//            return null;
//        } else {
//            return ssb;
//        }

        return null;
    }

    @Override
    protected CharSequence initEnglishSpeakText() {
//        RobotSpListBean.ResultBean.ProductBean bean = new Gson().fromJson(getIntent().getExtras().getString("productBean"), RobotSpListBean.ResultBean.ProductBean.class);
//        if (bean.getViewType() == 1) {//视频 只显示话术
//            setRobotChatMsg(R.string.product_detail_operation_speek);
//            return null;
//        } else {
//            return getString(R.string.product_detail_operation_speek);
//        }

        return null;
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }
        if(Constants.Scene.CurrentScene.equals(Constants.Scene.YinHangScene)){
            prattle(answerText);
            return true;
        }
//        ProductDetailWithoutShoppingCartAI.Intent intent = mAI.getIntent(text);
//        if (intent != null) {
//            mAI.handleIntent(intent);
//        } else {
//            prattle(answerText);
//        }
        return true;
    }

    private void initFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("productBean", getIntent().getExtras().getString("productBean"));
        bundle.putInt("number", -1);
        productDetailFragment = new ProductDetailFragment();
        productDetailFragment.setArguments(bundle);
        transaction.replace(R.id.fragment_container, productDetailFragment);
        transaction.commit();
    }

    private void initView() {
//        getTitleView().setShoppingCartVisibility(View.VISIBLE);
        getTitleView().setBackVisibility(View.VISIBLE);
//        getTitleView().setShoppingCartListener(() -> jumpActivity(ShoppingCartActivity.class));
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))
                ? R.layout.vertical_activity_product_detail : R.layout.activity_product_detail;
    }

    @Override
    public boolean isOpenChat() {
        return true;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    public View getShopCartView() {
        return getTitleView().getShoppingCart();
    }

    public View getMainView() {
        return layoutProductDetailMain;
    }

    /**
     * 设置购物车图片数量是否显示，显示的数量
     */
    public void setShoppingCartListener() {
        if (ShopcartUtil.getShopcartNum() > 0) {
            getTitleView().setShoppingCountVisibility(View.VISIBLE);
            getTitleView().setShoppingCount(ShopcartUtil.getShopcartNum() + "");
        } else {
            getTitleView().setShoppingCountVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        setShoppingCartListener();
    }
}
