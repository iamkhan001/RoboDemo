package com.csjbot.blackgaga.cart.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/10/16.
 */

public class HorizontalPageLayoutManager extends RecyclerView.LayoutManager{

    private int rows = 0;//item行数
    private int columns = 0;//item列数
    private int pageSize = 0;//item总数
    private int itemWidth = 0;//item
    private int itemHeight = 0;
    private int onePageSize = 0;
    private int itemWidthUsed;
    private int itemHeightUsed;
    private int totalHeight = 0;
    private int totalWidth = 0;
    private int offsetY = 0;
    private int offsetX = 0;

    public HorizontalPageLayoutManager(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.onePageSize = rows * columns;
    }


    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return null;
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }


    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);
        int newX = offsetX + dx;
        int result = dx;
        if (newX > totalWidth) {
            result = totalWidth - offsetX;
        } else if (newX < 0) {
            result = 0 - offsetX;
        }
        offsetX += result;
        offsetChildrenHorizontal(-result);
        recycleAndFillItems(recycler, state);
        return result;
    }
//
//    @Override
//    public boolean canScrollVertically() {
//        //返回true表示可以纵向滑动
//        return true;
//    }
//    @Override
//    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
//        //列表向下滚动dy为正，列表向上滚动dy为负，这点与Android坐标系保持一致。
//        //实际要滑动的距离
//        int travel = dy;
//
//        //如果滑动到最顶部
//        if (offsetY + dy < 0) {
//            travel = -offsetY;
//        } else if (offsetY + dy > totalHeight) {//如果滑动到最底部
//            travel = totalHeight - offsetY;
//        }
//
//        //将竖直方向的偏移量+travel
//        offsetY += travel;
//
//        // 调用该方法通知view在y方向上移动指定距离
//        offsetChildrenVertical(-travel);
//
//        return travel;
//    }

    private SparseArray<Rect> allItemFrames = new SparseArray<>();

    private int getUsableWidth() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int getUsableHeight() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }


    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0) {
            removeAndRecycleAllViews(recycler);
            return;
        }
        if (state.isPreLayout()) {
            return;
        }
        //获取每个Item的平均宽高
        itemWidth = getUsableWidth() / columns;
        itemHeight = getUsableHeight() / rows;

        //计算宽高已经使用的量，主要用于后期测量
        itemWidthUsed = (columns - 1) * itemWidth;
        itemHeightUsed = (rows - 1) * itemHeight;

        //计算总的页数
        pageSize = getItemCount() / onePageSize + (getItemCount() % onePageSize == 0 ? 0 : 1);

        //计算可以横向滚动的最大值
        totalWidth = (pageSize - 1) * getWidth();

        //分离view
        detachAndScrapAttachedViews(recycler);

        int count = getItemCount();
        for (int p = 0; p < pageSize; p++) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < columns; c++) {
                    int index = p * onePageSize + r * columns + c;
                    if (index == count) {
                        //跳出多重循环
                        c = columns;
                        r = rows;
                        p = pageSize;
                        break;
                    }

                    View view = recycler.getViewForPosition(index);
                    addView(view);
                    //测量item
                    measureChildWithMargins(view, itemWidthUsed, itemHeightUsed);

                    int width = getDecoratedMeasuredWidth(view);
                    int height = getDecoratedMeasuredHeight(view);
                    //记录显示范围
                    Rect rect = allItemFrames.get(index);
                    if (rect == null) {
                        rect = new Rect();
                    }
                    int x = p * getUsableWidth() + c * itemWidth;
                    int y = r * itemHeight;
                    rect.set(x, y, width + x, height + y);
                    allItemFrames.put(index, rect);
                }
            }
            //每一页循环以后就回收一页的View用于下一页的使用
            removeAndRecycleAllViews(recycler);
        }
        recycleAndFillItems(recycler, state);
    }

    @Override
    public void onDetachedFromWindow(RecyclerView view, RecyclerView.Recycler recycler) {
        super.onDetachedFromWindow(view, recycler);
        offsetX = 0;
        offsetY = 0;
    }

    private void recycleAndFillItems(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.isPreLayout()) {
            return;
        }

        Rect displayRect = new Rect(getPaddingLeft() + offsetX, getPaddingTop(), getWidth() - getPaddingLeft() - getPaddingRight() + offsetX, getHeight() - getPaddingTop() - getPaddingBottom());
        Rect childRect = new Rect();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            childRect.left = getDecoratedLeft(child);
            childRect.top = getDecoratedTop(child);
            childRect.right = getDecoratedRight(child);
            childRect.bottom = getDecoratedBottom(child);
            if (!Rect.intersects(displayRect, childRect)) {
                removeAndRecycleView(child, recycler);
            }
        }

        for (int i = 0; i < getItemCount(); i++) {
            if (Rect.intersects(displayRect, allItemFrames.get(i))) {
                View view = recycler.getViewForPosition(i);
                addView(view);
                measureChildWithMargins(view, itemWidthUsed, itemHeightUsed);
                Rect rect = allItemFrames.get(i);
                layoutDecorated(view, rect.left - offsetX, rect.top, rect.right - offsetX, rect.bottom);
            }
        }

    }
}
