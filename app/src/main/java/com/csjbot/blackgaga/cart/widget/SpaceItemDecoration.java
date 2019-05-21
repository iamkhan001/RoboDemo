package com.csjbot.blackgaga.cart.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/10/18.
 * 添加间距
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildItemId(view) != 0) {
            outRect.left = space;
            outRect.right = space;
        }
    }
}
