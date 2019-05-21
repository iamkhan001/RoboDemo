package com.csjbot.nuance;

import android.net.Uri;

import com.nuance.speechkit.PcmFormat;

/**
 * All Nuance Developers configuration parameters can be set here.
 *
 * Copyright (c) 2015 Nuance Communications. All rights reserved.
 */
public class Configuration {

    //All fields are required.
    //Your credentials can be found in your Nuance Developers portal, under "Manage My Apps".
    static final String APP_KEY = "0bcead59a847bbac8bc32e8f153904b227edbd5c1f3781cc5fc7bffefc" +
            "6fb7083e6461e6ac1c9237dfc0c6f5f9caa720b8647dd79bfea3fac662f9c2d2b0ca10";
    static final String APP_ID = "NMDPPRODUCTION_csjbot_csjAudioTest_20180717022201";
    static final String SERVER_HOST = "gni.nmdp.nuancemobility.net";
    static final String SERVER_PORT = "443";

    static final Uri SERVER_URI = Uri.parse("nmsps://" + APP_ID + "@" + SERVER_HOST + ":" + SERVER_PORT);

}



