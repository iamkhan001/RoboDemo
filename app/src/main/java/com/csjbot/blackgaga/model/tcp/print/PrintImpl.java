package com.csjbot.blackgaga.model.tcp.print;

import com.csjbot.blackgaga.model.tcp.base.BaseImpl;

/**
 * Created by jingwc on 2017/9/26.
 */

public class PrintImpl extends BaseImpl implements IPrint{

    @Override
    public void openPrinter() {
        robotManager.robot.reqProxy.openPrinter();
    }

    @Override
    public void printerCut() {
        robotManager.robot.reqProxy.printerCut();
    }

    @Override
    public void printText(String text) {
        robotManager.robot.reqProxy.printText(text);
    }

    @Override
    public void printQRCode(String qrcode) {
        robotManager.robot.reqProxy.printQRCode(qrcode);
    }

    @Override
    public void printImage(String image) {
        robotManager.robot.reqProxy.printImage(image);
    }
}
