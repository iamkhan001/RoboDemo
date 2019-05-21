package com.csjbot.blackgaga.feature.clothing;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.blackgaga.GlideApp;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.feature.clothing.bean.ClothListBean;
import com.csjbot.blackgaga.feature.clothing.bean.ClothTypeBean;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.router.BRouter;
import com.csjbot.blackgaga.router.BRouterPath;
import com.csjbot.blackgaga.util.GsonUtils;

import butterknife.BindView;

/**
 * 展示服装详情界面
 *
 * @author ShenBen
 * @date 2018/11/12 13:20
 * @email 714081644@qq.com
 */

@Route(path = BRouterPath.PRODUCT_CLOTHING_DETAIL)
public class ClothingDetailsActivity extends BaseModuleActivity {

    public static final String CLOTH_DETAIL = "CLOTH_DETAIL";
    @BindView(R.id.iv_cloth)
    ImageView ivCloth;
    @BindView(R.id.tv_cloth_name)
    TextView tvClothName;
    @BindView(R.id.tv_original_price)
    TextView tvOriginalPrice;
    @BindView(R.id.tv_present_price)
    TextView tvPresentPrice;
    @BindView(R.id.tv_cloth_type)
    TextView tvClothType;
    @BindView(R.id.tv_cloth_specifications)
    TextView tvClothSpecifications;
    @BindView(R.id.tv_cloth_year_season)
    TextView tvClothYearSeason;
    @BindView(R.id.tv_cloth_style)
    TextView tvClothStyle;
    @BindView(R.id.tv_cloth_color)
    TextView tvClothColor;
    @BindView(R.id.tv_cloth_age)
    TextView tvClothAge;
    @BindView(R.id.tv_cloth_size)
    TextView tvClothSize;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_clothing_details, R.layout.vertical_activity_clothing_details);
    }

    @Override
    public boolean isOpenChat() {
        return true;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public void init() {
        super.init();
        Intent intent = getIntent();
        if (intent != null) {
            String json = intent.getStringExtra(CLOTH_DETAIL);
            if (!TextUtils.isEmpty(json)) {
                ClothListBean.ResultBean.GoodsListBean bean = GsonUtils.jsonToObject(json,
                        ClothListBean.ResultBean.GoodsListBean.class);
                if (bean != null) {
                    GlideApp.with(this).load(bean.getGoodsPicture())
                            .placeholder(R.drawable.iv_load_clothing_failed)
                            .error(R.drawable.iv_load_clothing_failed)
                            .into(ivCloth);
                    tvClothName.setText(bean.getGoodsName());
                    tvOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    tvOriginalPrice.setText("￥" + bean.getOriginalPrice());
                    tvPresentPrice.setText("￥" + bean.getPresentPrice());
                    tvClothType.setText("服装版型: ");
                    tvClothSpecifications.setText("规格: ");
                    tvClothYearSeason.setText("年份/季节: " + bean.getYear() + bean.getSeason());
                    tvClothStyle.setText("风格: ");
                    tvClothColor.setText("颜色分类: " + bean.getColor());
                    tvClothAge.setText("适穿年龄: ");
                    tvClothSize.setText("尺码: " + bean.getSize());
                }
            }
        }
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
        getTitleView().setBackVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_MIN_VOICE));
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }
        if (Constants.ClothProduct.clothTypeBean != null && Constants.ClothProduct.clothTypeBean.getResult() != null
                && !Constants.ClothProduct.clothTypeBean.getResult().isEmpty()) {
            for (ClothTypeBean.ResultBean resultBean : Constants.ClothProduct.clothTypeBean.getResult()) {
                if (text.contains(resultBean.getSecondLevel())) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("GoodsType", resultBean.getSecondLevel());
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                    return true;
                }
            }
        }
        prattle(answerText);
        return true;
    }
}
