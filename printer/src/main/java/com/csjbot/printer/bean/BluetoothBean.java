package com.csjbot.printer.bean;

import android.bluetooth.BluetoothDevice;

import java.io.Serializable;

/**
 * Created by jingwc on 2018/5/7.
 */

public class BluetoothBean implements Serializable{

    public String mBluetoothName;
    public String mBluetoothAddress;
    public BluetoothDevice mBluetoothDevice;

}
