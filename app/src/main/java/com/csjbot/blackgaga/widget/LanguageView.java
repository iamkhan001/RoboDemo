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
 * Created by jingwc on 2018/4/4.
 */

public class LanguageView extends FrameLayout {

    Context mContext;

    private TextView tv_chinese;
    private TextView tv_english_us;
    private TextView tv_english_uk;
    private TextView tv_japanese;
    private TextView tv_korean;
    private TextView tv_french_france;
    private TextView tv_spanish_spain;
    private TextView tv_portuguese_portugal;
    private TextView tv_indonesia;
    private TextView tv_russian;

    LanguageClickListener mLanguageClickLlistener;

    public LanguageView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public LanguageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LanguageView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        mContext = context;

        LayoutInflater.from(mContext).inflate(R.layout.layout_language, this, true);

        tv_chinese = findViewById(R.id.tv_chinese);
        tv_english_us = findViewById(R.id.tv_english_us);
        tv_english_uk = findViewById(R.id.tv_english_uk);
        tv_japanese = findViewById(R.id.tv_japanese);
        tv_korean = findViewById(R.id.tv_korean);
        tv_french_france = findViewById(R.id.tv_french_france);
        tv_spanish_spain = findViewById(R.id.tv_spanish_spain);
        tv_portuguese_portugal = findViewById(R.id.tv_portuguese_portugal);
        tv_indonesia = findViewById(R.id.tv_indonesia);
        tv_russian = findViewById(R.id.tv_russian);

        tv_chinese.setOnClickListener(v -> {
            if (mLanguageClickLlistener != null) {
                mLanguageClickLlistener.onChineseClick();
            }
        });

        tv_english_us.setOnClickListener(v -> {
            if (mLanguageClickLlistener != null) {
                mLanguageClickLlistener.onEnglishUSClick();
            }
        });

        tv_english_uk.setOnClickListener(v -> {
            if (mLanguageClickLlistener != null) {
                mLanguageClickLlistener.onEnglishUKClick();
            }
        });

        tv_japanese.setOnClickListener(v -> {
            if (mLanguageClickLlistener != null) {
                mLanguageClickLlistener.onJapaneseClick();
            }
        });

        tv_korean.setOnClickListener(v -> {
            if (mLanguageClickLlistener != null) {
                mLanguageClickLlistener.onKoreanClick();
            }
        });

        tv_french_france.setOnClickListener(v -> {
            if (mLanguageClickLlistener != null) {
                mLanguageClickLlistener.onFranchFranceClick();
            }
        });

        tv_spanish_spain.setOnClickListener(v -> {
            if (mLanguageClickLlistener != null) {
                mLanguageClickLlistener.onSpanishSpainClick();
            }
        });

        tv_portuguese_portugal.setOnClickListener(v -> {
            if (mLanguageClickLlistener != null) {
                mLanguageClickLlistener.onPortuguesePortugalClick();
            }
        });

        tv_indonesia.setOnClickListener(v -> {
            if (mLanguageClickLlistener != null) {
                mLanguageClickLlistener.onIndonesiaClick();
            }
        });

        tv_russian.setOnClickListener(v -> {
            if (mLanguageClickLlistener != null) {
                mLanguageClickLlistener.onRussianClick();
            }
        });
    }

    public void setLanguageClickListener(LanguageClickListener listener) {
        this.mLanguageClickLlistener = listener;
    }

    public interface LanguageClickListener {
        void onChineseClick();

        void onEnglishUSClick();
        void onEnglishUKClick();
        void onEnglishCanadaClick();
        void onEnglishIndiaClick();

        void onJapaneseClick();

        void onKoreanClick();

        void onFranchFranceClick();
        void onFranchCanadaClick();

        void onSpanishSpainClick();
        void onSpanishLatAmClick();

        void onPortuguesePortugalClick();
        void onPortugueseBrazilClick();

        void onIndonesiaClick();

        void onRussianClick();
    }
}
