package com.csjbot.blackgaga.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.csjbot.blackgaga.R;

/**
 * Created by jingwc on 2018/2/27.
 */

public class NoticeView extends FrameLayout {

    Context mContext;

    TextView tv_notice;

    public NoticeView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public NoticeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NoticeView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){

        mContext = context;

        LayoutInflater.from(mContext).inflate(R.layout.layout_notice, this, true);

        tv_notice = findViewById(R.id.tv_notice);
    }

    public void setNotice(String text){
        if(tv_notice != null) {
            tv_notice.setText(text);
        }
    }
}
