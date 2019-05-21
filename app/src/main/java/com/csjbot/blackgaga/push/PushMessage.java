package com.csjbot.blackgaga.push;

/**
 * Created by xiasuhuei321 on 2017/11/17.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class PushMessage {
    // 0 更新产品
    // 1 更新
    public int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * data : {}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
    }
}
