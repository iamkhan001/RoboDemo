package com.csjbot.blackgaga.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.csjbot.blackgaga.R;

/**
 * Created by jingwc on 2017/11/10.
 */

public class NewRetailOneBtnDialog extends Dialog {

    private TextView tv_content;
    private TextView tv_title;
    private Button button;

    private OnDialogClickListener mListener;

    public NewRetailOneBtnDialog(@NonNull Context context) {
        super(context,R.style.NewRetailDialog);
        initView();
    }


    private void initView() {

        setContentView(R.layout.dialog_new_retail_onebtn);
        setCanceledOnTouchOutside(false);

        tv_content = findViewById(R.id.tv_content);
        tv_title = findViewById(R.id.tv_title);

        button.setOnClickListener(view -> {
            if(mListener != null){
                mListener.onClick();
            }
        });

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = getContext().getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.4);
        lp.height = (int) (d.heightPixels * 0.5);
        dialogWindow.setAttributes(lp);

    }

    public void setListener(OnDialogClickListener listener){
        mListener = listener;
    }

    public void setContent(String text){
        tv_content.setText(text);
    }
    public void setContent(int resId){
        tv_content.setText(resId);
    }

    public void setTitle(String text){
        tv_title.setText(text);
    }
    public void setTitle(int resId){
        tv_title.setText(resId);
    }

    public void setYes(String text){
        button.setText(text);
    }
    public void setYes(int resId){
        button.setText(resId);
    }

    public interface OnDialogClickListener{
        void onClick();
    }
}
