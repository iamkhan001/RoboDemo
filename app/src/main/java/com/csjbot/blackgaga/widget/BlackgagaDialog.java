package com.csjbot.blackgaga.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.StyleRes;

/**
 * Created by xiasuhuei321 on 2017/11/9.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class BlackgagaDialog extends AlertDialog {
    protected BlackgagaDialog(Context context) {
        super(context);
    }

    protected BlackgagaDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected BlackgagaDialog(Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    private void init(){

    }


}
