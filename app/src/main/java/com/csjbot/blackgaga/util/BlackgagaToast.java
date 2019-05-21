//package com.csjbot.blackgaga.util;
//
//import android.content.Context;
//import android.os.Handler;
//import android.view.Gravity;
//import android.widget.Toast;
//
///**
// * Created by 孙秀艳 on 2017/10/24.
// */
//
//public class BlackgagaToast {
//    private static Toast mToast;
//    private static Handler mHandler = new Handler();
//    private static Runnable r = new Runnable() {
//        public void run() {
//            mToast.cancel();
//            mToast = null;
//        }
//    };
//
//    public static void showToast(Context mContext, String text, int duration) {
//        mHandler.removeCallbacks(r);
//        if (mToast != null) {
//            mToast.setText(text);
//        } else {
//            mToast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
//        }
//        mHandler.postDelayed(r, duration);
//        mToast.setGravity(Gravity.BOTTOM, 0, 200);
//        mToast.show();
//    }
//
//    public static void showToast(Context mContext, int resId, int duration) {
//        showToast(mContext, mContext.getResources().getString(resId), duration);
//    }
//
//    public static void showToast(Context context, String text) {
//        showToast(context, text, Toast.LENGTH_SHORT);
//    }
//}
