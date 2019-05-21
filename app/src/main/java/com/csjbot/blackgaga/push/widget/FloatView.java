/*
 * Copyright (C) 2015 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.csjbot.blackgaga.push.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.utils.TextUtils;
import com.bumptech.glide.Glide;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.model.http.bean.QrCodeBean;
import com.csjbot.blackgaga.model.http.factory.ServerFactory;
import com.csjbot.blackgaga.model.http.product.ProductProxy;
import com.csjbot.blackgaga.push.utils.ResourceUtils;
import com.csjbot.blackgaga.util.CSJToast;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.coshandler.log.Csjlogger;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Desction:悬浮窗
 * Created by  Wql , 2018/2/27 15:32
 */
public class FloatView extends FrameLayout implements OnTouchListener {

    private final int HANDLER_TYPE_HIDE_LOGO = 100;//隐藏LOGO
    private final int HANDLER_TYPE_CANCEL_ANIM = 101;//退出动画

    private WindowManager.LayoutParams mWmParams;
    private WindowManager mWindowManager;
    private Context mContext;

    //private View mRootFloatView;
    private ImageView mIvFloatLogo;
    private ImageView mIvFloatLoader;
    private LinearLayout mLlFloatMenu;
    private ImageView iv_imageview1;
    private ImageView iv_imageview2;
    private FrameLayout mFlFloatLogo;

    private boolean mIsRight;//logo是否在右边
    private boolean mCanHide;//是否允许隐藏
    private float mTouchStartX;
    private float mTouchStartY;
    private int mScreenWidth;
    private int mScreenHeight;
    private boolean mDraging;
    private boolean mShowLoader = true;

    private Timer mTimer;
    private TimerTask mTimerTask;

    final Handler mTimerHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == HANDLER_TYPE_HIDE_LOGO) {
                // 比如隐藏悬浮框
                if (mCanHide) {
                    mCanHide = false;
                    if (mIsRight) {
                        mIvFloatLogo.setImageResource(ResourceUtils.getDrawableId(mContext, "pj_image_float_right"));
                    } else {
                        mIvFloatLogo.setImageResource(ResourceUtils.getDrawableId(mContext, "pj_image_float_left"));
                    }
                    mWmParams.alpha = 0.7f;
                    mWindowManager.updateViewLayout(FloatView.this, mWmParams);
                    refreshFloatMenu(mIsRight);
                    mLlFloatMenu.setVisibility(View.GONE);
                }
            } else if (msg.what == HANDLER_TYPE_CANCEL_ANIM) {
                mIvFloatLoader.clearAnimation();
                mIvFloatLoader.setVisibility(View.GONE);
                mShowLoader = false;
            }
            super.handleMessage(msg);
        }
    };
    private String mCompanyName;
    private String mIndex1Url;
    private String mIndex2Url;
    private String mDesc2;
    private String mIndex3Url;
    private String mDesc3;
    private String mDesc1;

    private ProductProxy proxy = new ProductProxy();
    private TextView textview1;
    private TextView textview2;
    private HashMap<String, String> hm = new HashMap<>();
    private LinearLayout ll_qr2;
    private LinearLayout ll_qr1;
    private TextView textview3;
    private ImageView iv_imageview3;
    private LinearLayout ll_qr3;
    private String mStatus;

    public FloatView(Context context) {
        super(context);
        init(context);

    }

    public void getQrCodeShow() {
        ServerFactory.createQrCode().getQrCode(ProductProxy.SN, new Observer<QrCodeBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull QrCodeBean qrCodeBean) {
                mStatus = qrCodeBean.getStatus();
                if (qrCodeBean.getStatus().equals("200")) {
                    List<QrCodeBean.ResultEntity.QrcodesEntity> index = qrCodeBean.getResult().getQrcodes();
                    if (index.size() >= 0) {
                        if (index.size() == 1) {
                            mIndex1Url = index.get(0).getImageUrl();
                            mDesc1 = index.get(0).getDesc();

                            textview1.setText(mDesc1);
                            Glide.with(mContext).load(mIndex1Url).into(iv_imageview1);

                            ll_qr1.setVisibility(VISIBLE);
                            ll_qr2.setVisibility(GONE);
                            ll_qr3.setVisibility(GONE);

                        } else if (index.size() == 2) {
                            int index1 = qrCodeBean.getResult().getQrcodes().get(0).getIndex();
                            int index2 = qrCodeBean.getResult().getQrcodes().get(1).getIndex();

                            mIndex1Url = index.get(0).getImageUrl();
                            mDesc1 = index.get(0).getDesc();

                            mIndex2Url = index.get(1).getImageUrl();
                            mDesc2 = index.get(1).getDesc();

                            if (index1 == 0) {
                                textview1.setText(mDesc1);
                                Glide.with(mContext).load(mIndex1Url).into(iv_imageview1);
                                textview2.setText(mDesc2);

                                Glide.with(mContext).load(mIndex2Url).into(iv_imageview2);
                                ll_qr1.setVisibility(VISIBLE);
                                ll_qr2.setVisibility(VISIBLE);
                            } else {
                                textview1.setText(mDesc2);
                                Glide.with(mContext).load(mIndex2Url).into(iv_imageview1);
                                textview2.setText(mDesc1);
                                Glide.with(mContext).load(mIndex1Url).into(iv_imageview2);
                                ll_qr1.setVisibility(VISIBLE);
                                ll_qr2.setVisibility(VISIBLE);
                            }
//                            if (index2 == 0) {
//                                textview1.setText(mDesc2);
//                                Glide.with(mContext).load(mIndex2Url).into(iv_imageview1);
//                                ll_qr1.setVisibility(VISIBLE);
//                            } else if (index2 == 1) {
//                                textview2.setText(mDesc2);
//                                Glide.with(mContext).load(mIndex2Url).into(iv_imageview2);
//                                ll_qr2.setVisibility(VISIBLE);
//                            }
                            ll_qr3.setVisibility(GONE);
                        } else if (index.size() == 3) {
                            int index1 = qrCodeBean.getResult().getQrcodes().get(0).getIndex();
                            int index2 = qrCodeBean.getResult().getQrcodes().get(1).getIndex();
                            int index3 = qrCodeBean.getResult().getQrcodes().get(2).getIndex();

                            mIndex1Url = index.get(0).getImageUrl();
                            mDesc1 = index.get(0).getDesc();

                            mIndex2Url = index.get(1).getImageUrl();
                            mDesc2 = index.get(1).getDesc();

                            mDesc3 = index.get(2).getDesc();
                            mIndex3Url = index.get(2).getImageUrl();

                            if (index1 == 0) {
                                textview1.setText(mDesc1);
                                Glide.with(mContext).load(mIndex1Url).into(iv_imageview1);
                                ll_qr1.setVisibility(VISIBLE);
                            } else if (index1 == 1) {
                                textview2.setText(mDesc1);
                                Glide.with(mContext).load(mIndex1Url).into(iv_imageview2);
                                ll_qr2.setVisibility(VISIBLE);
                            } else if (index1 == 2) {
                                textview3.setText(mDesc1);
                                Glide.with(mContext).load(mIndex1Url).into(iv_imageview3);
                                ll_qr3.setVisibility(VISIBLE);
                            }

                            if (index2 == 0) {
                                textview1.setText(mDesc2);
                                Glide.with(mContext).load(mIndex2Url).into(iv_imageview1);
                                ll_qr1.setVisibility(VISIBLE);
                            } else if (index2 == 1) {
                                textview2.setText(mDesc2);
                                Glide.with(mContext).load(mIndex2Url).into(iv_imageview2);
                                ll_qr2.setVisibility(VISIBLE);
                            } else if (index2 == 2) {
                                textview3.setText(mDesc2);
                                Glide.with(mContext).load(mIndex2Url).into(iv_imageview3);
                                ll_qr3.setVisibility(VISIBLE);
                            }

                            if (index3 == 0) {
                                textview1.setText(mDesc3);
                                Glide.with(mContext).load(mIndex3Url).into(iv_imageview1);
                                ll_qr1.setVisibility(VISIBLE);
                            } else if (index3 == 1) {
                                textview2.setText(mDesc3);
                                Glide.with(mContext).load(mIndex3Url).into(iv_imageview2);
                                ll_qr2.setVisibility(VISIBLE);
                            } else if (index3 == 2) {
                                textview3.setText(mDesc3);
                                Glide.with(mContext).load(mIndex3Url).into(iv_imageview3);
                                ll_qr3.setVisibility(VISIBLE);
                            }
                        }
                    }
                }


            }

            @Override
            public void onError(@NonNull Throwable e) {
                CsjlogProxy.getInstance().error("二维码获取失败" + e.toString());
            }

            @Override
            public void onComplete() {
                mTimerHandler.sendEmptyMessage(HANDLER_TYPE_CANCEL_ANIM);
                CsjlogProxy.getInstance().error("二维码获取成功");
            }
        });
    }

    private void init(Context mContext) {
        this.mContext = mContext;

        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        // 更新浮动窗口位置参数 靠边
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        this.mWmParams = new WindowManager.LayoutParams();
        // 设置window type
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        } else {
            mWmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        // 设置图片格式，效果为背景透明
        mWmParams.format = PixelFormat.RGBA_8888;
        // 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        mWmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 调整悬浮窗显示的停靠位置为左侧置?
        mWmParams.gravity = Gravity.LEFT | Gravity.TOP;

        mScreenHeight = mWindowManager.getDefaultDisplay().getHeight();

        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        mWmParams.x = 0;
        mWmParams.y = mScreenHeight / 2;

        // 设置悬浮窗口长宽数据
        mWmParams.width = LayoutParams.WRAP_CONTENT;
        mWmParams.height = LayoutParams.WRAP_CONTENT;
        addView(createView(mContext));
        mWindowManager.addView(this, mWmParams);

        mTimer = new Timer();
        hide();
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // 更新浮动窗口位置参数 靠边
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        int oldX = mWmParams.x;
        int oldY = mWmParams.y;
        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE://横屏
                if (mIsRight) {
                    mWmParams.x = mScreenWidth;
                    mWmParams.y = oldY;
                } else {
                    mWmParams.x = oldX;
                    mWmParams.y = oldY;
                }
                break;
            case Configuration.ORIENTATION_PORTRAIT://竖屏
                if (mIsRight) {
                    mWmParams.x = mScreenWidth;
                    mWmParams.y = oldY;
                } else {
                    mWmParams.x = oldX;
                    mWmParams.y = oldY;
                }
                break;
        }
        mWindowManager.updateViewLayout(this, mWmParams);
    }

    /**
     * 创建Float view
     *
     * @param context
     * @return
     */
    private View createView(final Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        // 从布局文件获取浮动窗口视图
        View rootFloatView = inflater.inflate(ResourceUtils.getLayoutId(context, "pj_widget_float_view"), null);
        mFlFloatLogo = (FrameLayout) rootFloatView.findViewById(ResourceUtils.getId(context, "pj_float_view"));

        mIvFloatLogo = (ImageView) rootFloatView.findViewById(ResourceUtils.getId(context,
                "pj_float_view_icon_imageView"));
        textview1 = (TextView) rootFloatView.findViewById(ResourceUtils.getId(context,
                "textview1"));
        textview2 = (TextView) rootFloatView.findViewById(ResourceUtils.getId(context,
                "textview2"));
        textview3 = (TextView) rootFloatView.findViewById(ResourceUtils.getId(context,
                "textview3"));

        mIvFloatLoader = (ImageView) rootFloatView.findViewById(ResourceUtils.getId(
                context, "pj_float_view_icon_notify"));
        mLlFloatMenu = (LinearLayout) rootFloatView.findViewById(ResourceUtils.getId(
                context, "ll_menu"));

        iv_imageview1 = (ImageView) rootFloatView.findViewById(ResourceUtils.getId(
                context, "iv_imageview1"));
        iv_imageview1.setOnClickListener(arg0 -> mLlFloatMenu.setVisibility(View.GONE));

        iv_imageview3 = (ImageView) rootFloatView.findViewById(ResourceUtils.getId(
                context, "iv_imageview3"));
        iv_imageview3.setOnClickListener(arg0 -> mLlFloatMenu.setVisibility(View.GONE));
        iv_imageview2 = (ImageView) rootFloatView.findViewById(ResourceUtils.getId(
                context, "iv_imageview2"));
        ll_qr2 = (LinearLayout) rootFloatView.findViewById(ResourceUtils.getId(
                context, "ll_qr2"));
        ll_qr3 = (LinearLayout) rootFloatView.findViewById(ResourceUtils.getId(
                context, "ll_qr3"));
        ll_qr1 = (LinearLayout) rootFloatView.findViewById(ResourceUtils.getId(
                context, "ll_qr1"));

        iv_imageview2.setOnClickListener(arg0 -> mLlFloatMenu.setVisibility(View.GONE));
        rootFloatView.setOnTouchListener(this);
        rootFloatView.setOnClickListener(v -> {
            if (!mDraging) {
                getQrCodeShow();
                Csjlogger.debug("wql-------------->" + mStatus);
                if (mLlFloatMenu.getVisibility() == View.VISIBLE) {
                    mLlFloatMenu.setVisibility(View.GONE);
                } else {
                    if (!TextUtils.isEmpty(mStatus)) {
                        if (mStatus.equals("200")) {
                            mLlFloatMenu.setVisibility(View.VISIBLE);
                        } else if (!mStatus.equals("200")) {
                            CSJToast.showToast(mContext, getResources().getString(R.string.is_no_qrcode), 1000);
                            mLlFloatMenu.setVisibility(View.GONE);
                        }
                    }

                }
            }
        });
        rootFloatView.measure(MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.UNSPECIFIED), MeasureSpec
                .makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));


        return rootFloatView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        removeTimerTask();
        // 获取相对屏幕的坐标，即以屏幕左上角为原点
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
                mIvFloatLogo.setImageResource(ResourceUtils.getDrawableId(
                        mContext, "pj_image_float_logo"));
                mWmParams.alpha = 1f;
                mWindowManager.updateViewLayout(this, mWmParams);
                mDraging = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float mMoveStartX = event.getX();
                float mMoveStartY = event.getY();
                // 如果移动量大于3才移动
                if (Math.abs(mTouchStartX - mMoveStartX) > 3
                        && Math.abs(mTouchStartY - mMoveStartY) > 3) {
                    mDraging = true;
                    // 更新浮动窗口位置参数
                    mWmParams.x = (int) (x - mTouchStartX);
                    mWmParams.y = (int) (y - mTouchStartY);
                    mWindowManager.updateViewLayout(this, mWmParams);
                    mLlFloatMenu.setVisibility(View.GONE);
                    return false;
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                if (mWmParams.x >= mScreenWidth / 2) {
                    mWmParams.x = mScreenWidth;
                    mIsRight = true;
                } else if (mWmParams.x < mScreenWidth / 2) {
                    mIsRight = false;
                    mWmParams.x = 0;
                }
                mIvFloatLogo.setImageResource(ResourceUtils.getDrawableId(
                        mContext, "pj_image_float_logo"));
                refreshFloatMenu(mIsRight);
                timerForHide();
                mWindowManager.updateViewLayout(this, mWmParams);
                // 初始化
                mTouchStartX = mTouchStartY = 0;
                break;
        }
        return false;
    }

    private void removeTimerTask() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    private void removeFloatView() {
        try {
            mWindowManager.removeView(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 隐藏悬浮窗
     */
    public void hide() {
        setVisibility(View.GONE);
        Message message = mTimerHandler.obtainMessage();
        message.what = HANDLER_TYPE_HIDE_LOGO;
        mTimerHandler.sendMessage(message);
        removeTimerTask();
    }

    /**
     * 显示悬浮窗
     */
    public void show() {
        if (getVisibility() != View.VISIBLE) {
            setVisibility(View.VISIBLE);
            if (mShowLoader) {
                mIvFloatLogo.setImageResource(ResourceUtils.getDrawableId(
                        mContext, "pj_image_float_logo"));
                mWmParams.alpha = 1f;
                mWindowManager.updateViewLayout(this, mWmParams);

                timerForHide();

                mShowLoader = false;
                Animation rotaAnimation = AnimationUtils.loadAnimation(mContext,
                        ResourceUtils.getAnimId(mContext, "pj_loading_anim"));
                rotaAnimation.setInterpolator(new LinearInterpolator());
                mIvFloatLoader.startAnimation(rotaAnimation);
                mTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        mTimerHandler.sendEmptyMessage(HANDLER_TYPE_CANCEL_ANIM);
                    }
                }, 500);
            }
        }
    }


    /**
     * 刷新float view menu
     *
     * @param right
     */
    private void refreshFloatMenu(boolean right) {
        if (right) {
            LayoutParams paramsFloatImage = (LayoutParams) mIvFloatLogo.getLayoutParams();
            paramsFloatImage.gravity = Gravity.RIGHT;
            mIvFloatLogo.setLayoutParams(paramsFloatImage);
            LayoutParams paramsFlFloat = (LayoutParams) mFlFloatLogo.getLayoutParams();
            paramsFlFloat.gravity = Gravity.RIGHT;
            mFlFloatLogo.setLayoutParams(paramsFlFloat);

            int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, mContext.getResources().getDisplayMetrics());
            int padding52 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 52, mContext.getResources().getDisplayMetrics());
            LinearLayout.LayoutParams paramsMenuAccount = (LinearLayout.LayoutParams) iv_imageview1.getLayoutParams();
            paramsMenuAccount.rightMargin = padding;
            paramsMenuAccount.leftMargin = padding;
            iv_imageview1.setLayoutParams(paramsMenuAccount);

            LinearLayout.LayoutParams paramsMenuFb = (LinearLayout.LayoutParams) iv_imageview2.getLayoutParams();
            paramsMenuFb.rightMargin = padding52;
            paramsMenuFb.leftMargin = padding;
            iv_imageview2.setLayoutParams(paramsMenuFb);

            LinearLayout.LayoutParams paramsMenuFb3 = (LinearLayout.LayoutParams) iv_imageview3.getLayoutParams();
            paramsMenuFb3.rightMargin = padding52;
            paramsMenuFb3.leftMargin = padding;
            iv_imageview3.setLayoutParams(paramsMenuFb3);

            LinearLayout.LayoutParams textviewparamsMenuAccount = (LinearLayout.LayoutParams) textview1.getLayoutParams();
            textviewparamsMenuAccount.rightMargin = padding;
            textviewparamsMenuAccount.leftMargin = padding;
            textview1.setLayoutParams(textviewparamsMenuAccount);

            LinearLayout.LayoutParams textvparamsMenuFb = (LinearLayout.LayoutParams) textview2.getLayoutParams();
            textvparamsMenuFb.rightMargin = padding52;
            textvparamsMenuFb.leftMargin = padding;
            textview2.setLayoutParams(textvparamsMenuFb);

            LinearLayout.LayoutParams textvparamsMenuFb3 = (LinearLayout.LayoutParams) textview3.getLayoutParams();
            textvparamsMenuFb3.rightMargin = padding52;
            textvparamsMenuFb3.leftMargin = padding;
            textview3.setLayoutParams(textvparamsMenuFb3);
        } else {
            LayoutParams params = (LayoutParams) mIvFloatLogo.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            params.gravity = Gravity.LEFT;
            mIvFloatLogo.setLayoutParams(params);
            LayoutParams paramsFlFloat = (LayoutParams) mFlFloatLogo.getLayoutParams();
            paramsFlFloat.gravity = Gravity.LEFT;
            mFlFloatLogo.setLayoutParams(paramsFlFloat);

            int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, mContext.getResources().getDisplayMetrics());
            int padding52 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 52, mContext.getResources().getDisplayMetrics());

            LinearLayout.LayoutParams paramsMenuAccount = (LinearLayout.LayoutParams) iv_imageview1.getLayoutParams();
            paramsMenuAccount.rightMargin = padding;
            paramsMenuAccount.leftMargin = padding52;
            iv_imageview1.setLayoutParams(paramsMenuAccount);

            LinearLayout.LayoutParams paramsMenuFb = (LinearLayout.LayoutParams) iv_imageview2.getLayoutParams();
            paramsMenuFb.rightMargin = padding;
            paramsMenuFb.leftMargin = padding;
            iv_imageview2.setLayoutParams(paramsMenuFb);

            LinearLayout.LayoutParams paramsMenuFb3 = (LinearLayout.LayoutParams) iv_imageview3.getLayoutParams();
            paramsMenuFb3.rightMargin = padding52;
            paramsMenuFb3.leftMargin = padding;
            iv_imageview3.setLayoutParams(paramsMenuFb3);

            LinearLayout.LayoutParams textviewparamsMenuAccount = (LinearLayout.LayoutParams) textview1.getLayoutParams();
            textviewparamsMenuAccount.rightMargin = padding;
            textviewparamsMenuAccount.leftMargin = padding52;
            textview1.setLayoutParams(textviewparamsMenuAccount);

            LinearLayout.LayoutParams textviewparamsMenuFb = (LinearLayout.LayoutParams) textview2.getLayoutParams();
            textviewparamsMenuFb.rightMargin = padding;
            textviewparamsMenuFb.leftMargin = padding;
            textview2.setLayoutParams(textviewparamsMenuFb);


            LinearLayout.LayoutParams textvparamsMenuFb3 = (LinearLayout.LayoutParams) textview3.getLayoutParams();
            textvparamsMenuFb3.rightMargin = padding52;
            textvparamsMenuFb3.leftMargin = padding;
            textview3.setLayoutParams(textvparamsMenuFb3);


        }
    }

    /**
     * 定时隐藏float view
     */
    private void timerForHide() {
        mCanHide = true;

        //结束任务
        if (mTimerTask != null) {
            try {
                mTimerTask.cancel();
                mTimerTask = null;
            } catch (Exception e) {
            }

        }
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = mTimerHandler.obtainMessage();
                message.what = HANDLER_TYPE_HIDE_LOGO;
                mTimerHandler.sendMessage(message);
            }
        };
        if (mCanHide) {
            mTimer.schedule(mTimerTask, 6000, 3000);
        }
    }


    /**
     * 是否Float view
     */
    public void destroy() {
        hide();
        removeFloatView();
        removeTimerTask();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        try {
            mTimerHandler.removeMessages(1);
        } catch (Exception e) {
        }
    }
}
