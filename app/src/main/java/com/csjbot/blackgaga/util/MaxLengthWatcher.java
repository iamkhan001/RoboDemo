package com.csjbot.blackgaga.util;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by 孙秀艳 on 2018/3/9.
 */

/*
 * 监听输入内容是否超出最大长度，并设置光标位置
 * */
public class MaxLengthWatcher implements TextWatcher {
    private int editStart;
    private int editEnd;
    private int maxLen = 0;
    private EditText editText = null;
    private Context mContext;
    private String msg;

    public MaxLengthWatcher(Context context, int maxLen, EditText editText, String message) {
        this.mContext = context;
        this.maxLen = maxLen;
        this.editText = editText;
        this.msg = message;
    }

    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub
    }

    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
        // TODO Auto-generated method stub

    }

    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
        Editable editable = editText.getText();
        int len = editable.length();
        if(len > maxLen){
            int selEndIndex = Selection.getSelectionEnd(editable);
            String str = editable.toString();
            //截取新字符串
            String newStr = str.substring(0,maxLen);
            editText.setText(newStr);
            editable = editText.getText();

            //新字符串的长度
            int newLen = editable.length();
            //旧光标位置超过字符串长度
            if(selEndIndex > newLen)
            {
                selEndIndex = editable.length();
            }
            //设置新光标所在的位置
            Selection.setSelection(editable, selEndIndex);
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        }
    }

    private int calculateLength(String etstring) {
        char[] ch = etstring.toCharArray();

        int varlength = 0;
        for (int i = 0; i < ch.length; i++) {
            // changed by zyf 0825 , bug 6918，增加中文标点范围 ， TODO 标点范围有待详细化
            if ((ch[i] >= 0x2E80 && ch[i] <= 0xFE4F) || (ch[i] >= 0xA13F && ch[i] <= 0xAA40) || ch[i] >= 0x80) { // 中文字符范围0x4e00 0x9fbb
                varlength = varlength + 2;
            } else {
                varlength++;
            }
        }
        Log.d("TextChanged", "varlength = " + varlength);
        // 这里也能够使用getBytes,更准确嘛
        // varlength = etstring.getBytes(CharSet.forName("GBK")).lenght;// 编码依据自己的需求，注意u8中文占3个字节...
        return varlength;
    }

}
