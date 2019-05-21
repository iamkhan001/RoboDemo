package com.csjbot.printer.callback;

import com.csjbot.printer.bean.BluetoothBean;

import java.util.ArrayList;

/**
 * Created by jingwc on 2018/5/8.
 */

public interface BlueSearchCallback {
    void complete(ArrayList<BluetoothBean> bluetoothBeans);
    void empty();
}
