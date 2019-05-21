package com.csjbot.blackgaga.util;

import android.graphics.Bitmap;

import com.github.sumimakito.awesomeqr.AwesomeQRCode;

/**
 * Created by xiasuhuei321 on 2017/10/25.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class QRCodeUtil {
    public static void getQRCodeAsync(String content, int size, ReadyListener listener) {
        new AwesomeQRCode.Renderer().contents(content).size(size)
                .renderAsync(new AwesomeQRCode.Callback() {
                    @Override
                    public void onRendered(AwesomeQRCode.Renderer renderer, Bitmap bitmap) {
                        listener.codeReady(bitmap);
                    }

                    @Override
                    public void onError(AwesomeQRCode.Renderer renderer, Exception e) {

                    }
                });
    }

    public interface ReadyListener {
        void codeReady(Bitmap bitmap);
    }
}
