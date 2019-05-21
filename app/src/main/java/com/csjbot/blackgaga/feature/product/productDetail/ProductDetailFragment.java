package com.csjbot.blackgaga.feature.product.productDetail;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bumptech.glide.Glide;
import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.base.BaseFragment;
import com.csjbot.blackgaga.cart.entity.RobotSpListBean;
import com.csjbot.blackgaga.cart.widget.CartImageAnmotionTool;
import com.csjbot.blackgaga.feature.product.shopcart.ShoppingCartActivity;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.util.CSJToast;
import com.csjbot.blackgaga.util.FileUtil;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.blackgaga.util.ShopcartUtil;
import com.csjbot.blackgaga.widget.CommonVideoView;
import com.csjbot.blackgaga.widget.LoopImageHolderView;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 孙秀艳 on 2017/10/16.
 * 产品明细Fragment
 */

public class ProductDetailFragment extends BaseFragment implements View.OnClickListener {
    private CartImageAnmotionTool cartImageAnmotionTool;
    private RobotSpListBean.ResultBean.ProductBean productBean;

    @BindView(R.id.cbLoop)
    ConvenientBanner convenientBanner;//轮播图片
    @BindView(R.id.videoView)
    CommonVideoView videoView;//视频播放
    @BindView(R.id.tvProductName)
    TextView tvProductName;//产品名称
    @BindView(R.id.tv_old_price_value)
    TextView tvOldPriceValue;//产品原价价格
    @BindView(R.id.tv_current_price)
    TextView tvCurrentPrice;//产品现价字样
    @BindView(R.id.tv_current_price_value)
    TextView tvCurrentPriceValue;//产品现在价格
    @BindView(R.id.tvSalesNum)
    TextView tvSalesNum;//产品售出数量
    @BindView(R.id.layoutShoppingCart)
    LinearLayout layoutShoppingCart;//加入购物车
    @BindView(R.id.layoutPhurse)
    LinearLayout layoutPhurse;//购买
    @BindView(R.id.layout_cart_phurse)
    LinearLayout layoutCartPhuse;
    @BindView(R.id.ivProductThumb)
    ImageView ivProductThumb;//产品缩略图
    @BindView(R.id.product_market_type)
    ImageView ivProductType;//营销类型
    @BindView(R.id.product_info)
    TextView tvProductInfo;//产品介绍
    @BindView(R.id.detail_bottom)
    RelativeLayout tvdetailbottom;
    @BindView(R.id.mid)
    RelativeLayout mid;
    @BindView(R.id.lllll)
    LinearLayout lll;

    private ArrayList<String> localImages = new ArrayList<>();

    static ProductDetailFragment newInstance() {
        return new ProductDetailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                        || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))
                        ? R.layout.vertical_fragment_product_detail : R.layout.fragment_product_detail, container, false);
        ButterKnife.bind(this, rootView);
        productBean = new Gson().fromJson(this.getArguments().getString("productBean"), RobotSpListBean.ResultBean.ProductBean.class);
        showProduct(productBean);
        layoutShoppingCart.setOnClickListener(this);
        layoutPhurse.setOnClickListener(this);
        cartImageAnmotionTool = new CartImageAnmotionTool(getActivity());
        String Industry = SharedPreUtil.getString(SharedKey.MAINPAGE, SharedKey.MAINPAGE_KEY, "");
        if (Industry.equals("yinhang") || Industry.equals("bowuguan")) {
            lll.setVisibility(View.GONE);
            tvdetailbottom.setVisibility(View.GONE);
        }
        int number = this.getArguments().getInt("number");
        CsjlogProxy.getInstance().info("number:" + number);
        if (number > 0) {
            ShopcartUtil.addShopcartProduct(productBean, number);
        }
        return rootView;
    }

    private void initShowLayout() {
        if (productBean.isIsshowbtnshopcatr()) {
            layoutShoppingCart.setVisibility(View.VISIBLE);
        } else {
            layoutShoppingCart.setVisibility(View.GONE);
        }

        if (productBean.isIsshowbtnpay()) {
            layoutPhurse.setVisibility(View.VISIBLE);
        } else {
            layoutPhurse.setVisibility(View.GONE);
        }

        if (!productBean.isIsshowbtnshopcatr() && !productBean.isIsshowbtnpay()) {
            layoutCartPhuse.setVisibility(View.GONE);
        } else {
            layoutCartPhuse.setVisibility(View.VISIBLE);
        }
    }

    public void showProduct(RobotSpListBean.ResultBean.ProductBean productBean) {
        if (productBean.getViewType() == 1) {//视频
            videoView.setVisibility(View.VISIBLE);
            convenientBanner.setVisibility(View.GONE);
            startPlay(productBean.getViewUrl().get(0));
        } else if (productBean.getViewType() == 2) {
            videoView.setVisibility(View.GONE);
            convenientBanner.setVisibility(View.VISIBLE);
            setLoopData(productBean.getViewUrl());
        }
        setProductName(productBean.getName());
        if (productBean.getCurrentprice() < productBean.getOriginalprice()) {
            setProductPrice(productBean.getCurrentprice() + "", productBean.getOriginalprice() + "");
        } else {
            setProductPrice(productBean.getCurrentprice() + "");
        }
        setSalesNum(productBean.getSell());
        setProductThumb(productBean.getImgName());
        tvProductInfo.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvProductInfo.setText(productBean.getIntroduction());
    }

    /**
     * 设置产品缩略图
     *
     * @param imgName
     */
    private void setProductThumb(String imgName) {
        String path = Constants.PRODUCT_IMG_PATH + imgName;
        File file = new File(path);
        if (file.exists()) {
            Glide.with(getActivity())
                    .load(path)
                    .into(ivProductThumb);
        } else {
            ivProductThumb.setBackgroundResource(R.drawable.no_product);
        }
        setProductMarketType();
    }

    //设置产品营销类型
    private void setProductMarketType() {
        if (productBean.getMarketingtype() == 1) {//促销
            ivProductType.setBackgroundResource(R.drawable.cx);
        } else if (productBean.getMarketingtype() == 2) {//热卖
            ivProductType.setBackgroundResource(R.drawable.rm);
        } else if (productBean.getMarketingtype() == 3) {//新品
            ivProductType.setBackgroundResource(R.drawable.xp);
        } else if (productBean.getMarketingtype() == 4) {//满赠
            ivProductType.setBackgroundResource(R.drawable.mz);
        } else if (productBean.getMarketingtype() == 5) {//爆款
            ivProductType.setBackgroundResource(R.drawable.bk);
        }
    }

    /**
     * 使用ViewVideo播放视频
     * 先判断本地有没有数据
     */
    private void startPlay(String url) {
        String videoPath = Constants.PRODUCT_VIDEO_PATH + FileUtil.getFileName(url);
        File file = new File(videoPath);
        if (!file.exists()) {
            videoPath = url;
        }
        Uri uri = Uri.parse(videoPath);
        videoView.startPlay(uri);
    }

    @Override
    public void onPause() {
        videoView.pausePlay();
        super.onPause();
    }

    @Override
    public void onResume() {
        videoView.continuePlay();
        initShowLayout();
        super.onResume();
    }

    /**
     * 轮播图片
     */
    private void setLoopData(List<String> viewUrls) {
        //获取轮播图片本地是否存在
        boolean isExist = true;
        ArrayList<String> imageNames = new ArrayList<>();
        for (int i = 0; i < viewUrls.size(); i++) {
            String path = Constants.PRODUCT_IMG_PATH + FileUtil.getFileName(viewUrls.get(i));
            imageNames.add(path);
            File file = new File(path);
            if (!file.exists()) {
                isExist = false;
                break;
            }
        }
        if (isExist) {
            for (int i = 0; i < imageNames.size(); i++) {
                localImages.add(i, imageNames.get(i));
            }
        } else {
            for (int i = 0; i < viewUrls.size(); i++) {
                localImages.add(i, viewUrls.get(i));
            }
        }
        convenientBanner.setPages(LoopImageHolderView::new, localImages)
                .setPointViewVisible(true)//设置指示器是否可见
                .startTurning(2000)//设置自动切换（同时设置了切换时间间隔）
//                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);//设置指示器的方向（左、中、右）
    }

    /**
     * 设置产品名称
     */
    private void setProductName(String productName) {
        tvProductName.setText(productName);
    }

    /**
     * 设置产品价格
     */
    private void setProductPrice(String currentprice, String originalprice) {
        tvOldPriceValue.setText(getString(R.string.yuan) + originalprice);
        tvOldPriceValue.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中间横线
        tvCurrentPrice.setVisibility(View.VISIBLE);
        tvCurrentPriceValue.setVisibility(View.VISIBLE);
        tvCurrentPriceValue.setText(getString(R.string.yuan) + currentprice);
    }

    private void setProductPrice(String price) {
        tvOldPriceValue.setText(getString(R.string.yuan) + price);
        tvCurrentPrice.setVisibility(View.GONE);
        tvCurrentPriceValue.setVisibility(View.GONE);
    }

    /**
     * 设置已售出的产品数量
     */
    private void setSalesNum(int num) {
        String str = getString(R.string.product_sell_num) + num;
        int length = getString(R.string.product_sell_num).length();
        SpannableString spanStr = new SpannableString(str);
        spanStr.setSpan(new AbsoluteSizeSpan(24), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//字体大小
        spanStr.setSpan(new ForegroundColorSpan(Color.RED), length, spanStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//字体颜色
        spanStr.setSpan(new AbsoluteSizeSpan(36), length, spanStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvSalesNum.setText(spanStr);
        tvSalesNum.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutShoppingCart:
                addShoppingCart();
                break;
            case R.id.layoutPhurse:
                isCheckSettlement();
                purchase();
                break;
            default:
                break;
        }
    }

    /**
     * 加入购物车
     */
    public void addShoppingCart() {
        //判断是否没有存货了
        if (ShopcartUtil.isAddShopcart(productBean)) {
            ShopcartUtil.addShopcartProduct(productBean);
            ProductDetailActivity activity = (ProductDetailActivity) getActivity();
            activity.setShoppingCartListener();
//            cartImageAnmotionTool.addAnmotion(ivProductThumb, (RelativeLayout) activity.getMainView(),
//                    (RelativeLayout) activity.getShopCartView(),
//                    ivProductThumb.getDrawable()).setListener(() -> activity.setShoppingCartListener());
            layoutPhurse.setEnabled(true);
        } else {
            CSJToast.showToast(getActivity(), getString(R.string.shopcart_not_more_product), 1000);
        }
    }

    /**
     * 立即购买
     */
    public void purchase() {
        if (ShopcartUtil.isAddShopcart(productBean)) {//库存充足，添加一件商品至购物车，跳转到购物车界面
            ShopcartUtil.addShopcartProduct(productBean);
            layoutPhurse.setEnabled(true);
        }
        startActivity(new Intent(getActivity(), ShoppingCartActivity.class));
    }

    public void isCheckSettlement() {
        if (ShopcartUtil.getShopcartNum() > 0) {
            layoutPhurse.setEnabled(true);
        } else {
            layoutPhurse.setEnabled(false);
        }
    }
}
