package com.csjbot.blackgaga.feature.coupon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.ai.CouponAI;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.feature.coupon.adapter.CouponListAdapter;
import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;
import com.csjbot.blackgaga.model.tcp.print.IPrint;
import com.csjbot.blackgaga.router.BRouterPath;
import com.github.sumimakito.awesomeqr.AwesomeQRCode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by xiasuhuei321 on 2017/10/19.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

@Route(path = BRouterPath.COUPON)
public class CouponActivity extends BaseModuleActivity {

    @BindView(R.id.iv_qrcode)
    ImageView iv_qrcode;
    @BindView(R.id.rv_couponList)
    RecyclerView couponList;

    CouponAI mAI;

    IPrint mPrint;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_coupon;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public boolean isOpenChat() {
        return true;
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        getTitleView().setBackVisibility(View.VISIBLE);
        new AwesomeQRCode.Renderer()
                .size(800).margin(20)
                .contents(getString(R.string.this_is_test_qr_code))
                .renderAsync(new AwesomeQRCode.Callback() {
                    @Override
                    public void onRendered(AwesomeQRCode.Renderer renderer, Bitmap bitmap) {
                        runOnUiThread(() -> iv_qrcode.setImageBitmap(bitmap));
                    }

                    @Override
                    public void onError(AwesomeQRCode.Renderer renderer, Exception e) {
                        e.printStackTrace();
                    }
                });

        CouponListAdapter adapter = new CouponListAdapter();
        couponList.setAdapter(adapter);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        couponList.setLayoutManager(manager);
    }

    @Override
    public void init() {
        super.init();
        mAI = CouponAI.newInstance();
        mAI.initAI(this);

        mPrint = ServerFactory.getPrintInstance();

        mPrint.openPrinter();
    }

    @Override
    protected CharSequence initChineseSpeakText() {
        return "您好，请选择要领取的优惠券使用微信扫描右侧二维码进行领取。";
    }

    @Override
    protected CharSequence initEnglishSpeakText() {
        return "Hello, please choose the coupon firstly and then scan the QR code by WeChat to get it.";
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }
        CouponAI.Intent intent = mAI.getIntent(text);
        if (intent != null) {
            mAI.handleIntent(intent);
        } else {
            prattle(answerText);
        }
        return true;
    }

    boolean inPrint;

    @OnClick(R.id.bt_print)
    public void print() {
        if (inPrint) {
            return;
        }
        inPrint = true;
        iv_qrcode.postDelayed(() -> {
            inPrint = false;
        }, 10000);
        mPrint.printQRCode("www.csjbot.com");
    }


    void printImage() {
        String result = null;
        Bitmap bitmap = drawableToBitmap(iv_qrcode.getDrawable());
        if (bitmap != null) {
            ByteArrayOutputStream baos = null;
            try {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                baos.flush();
                baos.close();
                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (baos != null) {
                    try {
                        baos.flush();
                        baos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            if (result != null) {
                ServerFactory.getPrintInstance().printImage(result);
            }
        }
    }

    Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }
}
