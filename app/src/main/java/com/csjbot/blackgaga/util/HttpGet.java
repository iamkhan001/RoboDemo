package com.csjbot.blackgaga.util;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;

/**
 * Copyright (c) 2018, SuZhou CsjBot. All Rights Reserved.
 * www.csjbot.com
 * <p>
 * Created by 浦耀宗 at 2018/05/30 0030-11:13.
 * Email: puyz@csjbot.com
 */

public final class HttpGet extends HttpEntityEnclosingRequestBase {
    public static final String METHOD_NAME = "GET";

    public HttpGet() {
    }

    public HttpGet(URI uri) {
        this.setURI(uri);
    }

    public HttpGet(String uri) {
        this.setURI(URI.create(uri));
    }

    public String getMethod() {
        return "GET";
    }
}