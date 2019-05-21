package com.csjbot.blackgaga.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by 孙秀艳 on 2018/8/20.
 */

public class BaseSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int top;
    private int bottom;
    private int left;
    private int right;
    private int column;

    public BaseSpaceItemDecoration(int top, int bottom, int left, int right, int column) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
        this.column = column;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
//        if (outRect.top == top) {
//            outRect.top = 0;
//        }
//        if (outRect.left == (left / 2)) {
//            outRect.left = 0;
//        }
//        if (parent.getChildLayoutPosition(view) % column != 0) {
//            //在左边
//            outRect.right = right / 2;
//        }
//        if (parent.getChildLayoutPosition(view) >= column) {
//            outRect.top = top;
//        }

        if (parent.getChildItemId(view) != 0) {
            outRect.left = left;
            outRect.right = right;
            outRect.top = top;
            outRect.bottom = bottom;
        }
    }
}
