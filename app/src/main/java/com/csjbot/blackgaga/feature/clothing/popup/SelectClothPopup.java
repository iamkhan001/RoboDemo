package com.csjbot.blackgaga.feature.clothing.popup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.feature.clothing.adapter.SelectClothAdapter;
import com.csjbot.blackgaga.feature.clothing.bean.SelectClothBean;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

/**
 * @author ShenBen
 * @date 2018/11/12 18:20
 * @email 714081644@qq.com
 */

public class SelectClothPopup extends BasePopupWindow implements View.OnClickListener {
    private RecyclerView rvVersionType;
    private RecyclerView rvSeason;
    private EditText etMinPrice;
    private EditText etMaxPrice;
    private Button btnReset;
    private Button btnSure;

    private List<SelectClothBean> mTypeList;
    private List<SelectClothBean> mSeasonList;
    private SelectClothAdapter mTypeAdapter;
    private SelectClothAdapter mSeasonAdapter;

    private Context mContext;
    private OnSelectClothDetailListener mListener;
    private String mVersionType = "";
    private String mSeason = "";
    private double mMinPrice = 0.0;
    private double mMaxPrice = Integer.MAX_VALUE;

    private int mCurrentTypePosition = -1;
    private int mCurrentSeasonPosition = -1;

    public SelectClothPopup(Context context) {
        super(context, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setAlignBackground(true);
        mContext = context;
        rvVersionType = findViewById(R.id.rv_version_type);
        rvSeason = findViewById(R.id.rv_season);
        etMinPrice = findViewById(R.id.et_min_price);
        etMaxPrice = findViewById(R.id.et_max_price);
        btnReset = findViewById(R.id.btn_reset);
        btnSure = findViewById(R.id.btn_sure);

        btnReset.setOnClickListener(this);
        btnSure.setOnClickListener(this);

        init();
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_select_cloth);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -1f,
                Animation.RELATIVE_TO_SELF, 0f);
        animation.setDuration(300);
        animation.setInterpolator(new DecelerateInterpolator());
        return animation;
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, -1f);
        animation.setDuration(300);
        animation.setInterpolator(new DecelerateInterpolator());
        return animation;
    }

    private void init() {
        mTypeList = new ArrayList<>();
        mSeasonList = new ArrayList<>();

        //可以根据大小屏动态设置不同的布局
        if (TextUtils.equals(BuildConfig.FLAVOR, BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                || TextUtils.equals(BuildConfig.FLAVOR, BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS)) {
            mTypeAdapter = new SelectClothAdapter(R.layout.item_select_cloth_vertical, mTypeList);
            mSeasonAdapter = new SelectClothAdapter(R.layout.item_select_cloth_vertical, mSeasonList);
        } else {
            mTypeAdapter = new SelectClothAdapter(R.layout.item_select_cloth, mTypeList);
            mSeasonAdapter = new SelectClothAdapter(R.layout.item_select_cloth, mSeasonList);
        }

        GridLayoutManager typeManager = new GridLayoutManager(mContext, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        GridLayoutManager seasonManager = new GridLayoutManager(mContext, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        rvVersionType.setLayoutManager(typeManager);
        rvSeason.setLayoutManager(seasonManager);
        rvVersionType.setHasFixedSize(true);
        rvSeason.setHasFixedSize(true);

        rvVersionType.setAdapter(mTypeAdapter);
        rvSeason.setAdapter(mSeasonAdapter);

        mTypeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });

        mSeasonAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mCurrentSeasonPosition == position) {
                return;
            }
            mSeason = mSeasonList.get(position).getType();
            mSeasonList.get(position).setChecked(true);
            mSeasonAdapter.notifyItemChanged(position, "season");

            if (mCurrentSeasonPosition != -1) {
                mSeasonList.get(mCurrentSeasonPosition).setChecked(false);
                mSeasonAdapter.notifyItemChanged(mCurrentSeasonPosition, "season");
            }
            mCurrentSeasonPosition = position;
        });

    }

    /**
     * 设置版型集合
     *
     * @param list
     */
    public void setTypeList(@NonNull List<SelectClothBean> list) {
        mTypeList.clear();
        mTypeList.addAll(list);
        mTypeAdapter.setNewData(mTypeList);
    }

    /**
     * 设置版型集合
     *
     * @param list
     */
    public void setSeasonList(@NonNull List<SelectClothBean> list) {
        mSeasonList.clear();
        mSeasonList.addAll(list);
        mSeasonAdapter.setNewData(mSeasonList);
    }

    public void setOnSelectClothDetailListener(OnSelectClothDetailListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reset:
                if (mCurrentTypePosition != -1) {
                    mTypeList.get(mCurrentTypePosition).setChecked(false);
                    mTypeAdapter.notifyItemChanged(mCurrentTypePosition, "type");
                }
                if (mCurrentSeasonPosition != -1) {
                    mSeasonList.get(mCurrentSeasonPosition).setChecked(false);
                    mSeasonAdapter.notifyItemChanged(mCurrentSeasonPosition, "season");
                }
                etMinPrice.setText(null);
                etMaxPrice.setText(null);
                mCurrentTypePosition = -1;
                mCurrentSeasonPosition = -1;
                mVersionType = "";
                mSeason = "";
                break;
            case R.id.btn_sure:
                String minPrice = etMinPrice.getText().toString().trim();
                String maxPrice = etMaxPrice.getText().toString().trim();
                mMinPrice = 0.0;
                mMaxPrice = Integer.MAX_VALUE;
                if (!TextUtils.isEmpty(minPrice)) {
                    mMinPrice = Double.parseDouble(minPrice);
                }
                if (!TextUtils.isEmpty(maxPrice)) {
                    mMaxPrice = Double.parseDouble(maxPrice);
                }
                if (mListener != null) {
                    mListener.selectCloth(mVersionType, mSeason, mMinPrice, mMaxPrice);
                }
                dismiss();
                break;
        }
    }

    public interface OnSelectClothDetailListener {
        /**
         * 传递筛选条件信息
         *
         * @param versionType 版型
         * @param season      季节
         * @param minPrice    最低价
         * @param maxPrice    最高价
         */
        void selectCloth(String versionType, String season, double minPrice, double maxPrice);
    }
}
