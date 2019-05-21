package com.csjbot.blackgaga.util;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.csjbot.blackgaga.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 验证输入内容正确与否
 */

public class CheckOutUtil {
    public static final String REGEX_USERNAME = "^[A-Za-z0-9]+$";
    private static CheckOutUtil mCheckOutUtil = null;
    private String mBeforeTextChangedText;
    private Context mContext;
    private String finalText;
    private int lenth;

    private CheckOutUtil() {
    }

    public static CheckOutUtil getInstance() {
        synchronized (CheckOutUtil.class) {
            if (mCheckOutUtil == null) {
                mCheckOutUtil = new CheckOutUtil();
            }
        }
        return mCheckOutUtil;
    }

    /**
     * 验证EditText输入的用户名
     */
    public void verifyUseName(final EditText editText, Context context, final String REGEX_USERNAME) {
        this.mContext = context;
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                CsjlogProxy.getInstance().warn("afterTextChanged " + s.toString());
                if (!isUseName(s.toString(), REGEX_USERNAME)) {
                    Toast.makeText(mContext, R.string.Input_prompt, Toast.LENGTH_SHORT).show();
                    editText.setText(finalText);
                    editText.setSelection(finalText.length());
                } else {
                    finalText = editText.getText().toString();
                }
            }
        });
    }

    public void verifyUseName(final EditText editText, Context context) {
        this.verifyUseName(editText, context, REGEX_USERNAME);
    }

    private boolean isUseName(String str, String REGEX_USERNAME) {
        if (TextUtils.isEmpty(str)) {
            return true;
        }
        // 将给定的正则表达式编译并赋予给Pattern类
        Pattern pattern = Pattern.compile(REGEX_USERNAME);
        // 对指定输入的字符串创建一个Matcher对象
        Matcher matcher = pattern.matcher(str);
        // 尝试对整个目标字符展开匹配检测,也就是只有整个目标字符串完全匹配时才返回真值.
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }
}
