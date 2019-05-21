package com.csjbot.blackgaga.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;

/**
 * Created by jingwc on 2017/11/10.
 */

public class NewRetailDialog extends Dialog {

    private TextView tv_content;
    private TextView tv_title;
    private Button bt_yes;
    private Button bt_no;

    private OnDialogClickListener mListener;

    public NewRetailDialog(@NonNull Context context) {
        super(context, R.style.NewRetailDialog);
        initView();
    }


    private void initView() {

        setContentView(R.layout.dialog_new_retail);
        setCanceledOnTouchOutside(false);

        tv_content = findViewById(R.id.tv_content);
        tv_title = findViewById(R.id.tv_title);
        bt_no = findViewById(R.id.bt_no);
        bt_yes = findViewById(R.id.bt_yes);

        bt_no.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.no();
            }
        });
        bt_yes.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.yes();
            }
        });

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = getContext().getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        if (!(BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))) {
            lp.width = (int) (d.widthPixels * 0.4);
            lp.height = (int) (d.heightPixels * 0.5);
        } else {
            lp.width = (int) (d.widthPixels * 0.8);
            lp.height = (int) (d.heightPixels * 0.3);
        }

        dialogWindow.setAttributes(lp);

    }

    public void setListener(OnDialogClickListener listener) {
        mListener = listener;
    }

    public void setContent(String text) {
        tv_content.setText(text);
    }

    public void setContent(int resId) {
        tv_content.setText(resId);
    }

    public void setTitle(String text) {
        tv_title.setText(text);
    }

    public void setTitle(int resId) {
        tv_title.setText(resId);
    }

    public void setYes(String text) {
        bt_yes.setText(text);
    }

    public void setYes(int resId) {
        bt_yes.setText(resId);
    }

    public void setNo(String text) {
        bt_no.setText(text);
    }

    public void setNo(int resId) {
        bt_no.setText(resId);
    }

    public interface OnDialogClickListener {
        void yes();

        void no();
    }
}
