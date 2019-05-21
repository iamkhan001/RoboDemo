package com.csjbot.blackgaga.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.csjbot.blackgaga.R;

/**
 * Created by sunxiuyan on 2017/11/15.
 */

public class ProgressDialog extends Dialog {

    private ProgressBar progressBar;
    private TextView tv_title;
    private Button bt_no;

    private OnDialogClickListener mListener;

    public ProgressDialog(@NonNull Context context) {
        super(context,R.style.NewRetailDialog);
        initView();
    }


    private void initView() {

        setContentView(R.layout.dialog_progress);
        setCanceledOnTouchOutside(false);

        progressBar = findViewById(R.id.progress);
        tv_title = findViewById(R.id.tv_title);
        bt_no = findViewById(R.id.bt_no);

        bt_no.setOnClickListener(view -> {
            if(mListener != null){
                mListener.no();
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

    public void setTitle(String text){
        tv_title.setText(text);
    }
    public void setTitle(int resId){
        tv_title.setText(resId);
    }

    public void setNo(String text){
        bt_no.setText(text);
    }
    public void setNo(int resId){
        bt_no.setText(resId);
    }

    public ProgressBar getProgressId() {
        return progressBar;
    }

    public void setButtonState(int visible){
        bt_no.setVisibility(visible);
    }

    public void setTitleShow(String text,int visible){
        tv_title.setText(text);
        tv_title.setVisibility(visible);
    }

    public interface OnDialogClickListener{
        void no();
    }
}
