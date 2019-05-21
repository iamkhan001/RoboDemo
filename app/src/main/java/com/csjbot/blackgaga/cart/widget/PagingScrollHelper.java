package com.csjbot.blackgaga.cart.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/10/16.
 * 处理点击滑动
 */

public class PagingScrollHelper {

    private RecyclerView mRecyclerView = null;
    private ValueAnimator mAnimator = null;
    private MyOnScrollListener mOnScrollListener = new MyOnScrollListener();
    private MyOnFlingListener mOnFlingListener = new MyOnFlingListener();
    //当前滑动距离
    private int offsetY = 0;
    private int offsetX = 0;

    //按下屏幕点
    private int startY = 0;
    private int startX = 0;

    //最后一个可见 view 位置
    private int lastItemPosition = -1;
    //第一个可见view的位置
    private int firstItemPosition = -2;
    //总 itemView 数量
    private int totalNum;
    //滑动至耨一页
    private int pageNum = -1;
    //一次滚动 n 页
    private int indexPage;
    //一共多少页
    private int allPage;

    private boolean isScall = false;

    private OnScrollPageListener mListener;

    private enum ORIENTATION {
        HORIZONTAL, VERTICAL, NULL
    }

    private ORIENTATION mOrientation = ORIENTATION.HORIZONTAL;

    public void setUpRecycleView(RecyclerView recycleView) {
        if (recycleView == null) {
            throw new IllegalArgumentException("recycleView must be not null");
        }
        mRecyclerView = recycleView;
        recycleView.setOnFlingListener(mOnFlingListener);
        recycleView.addOnScrollListener(mOnScrollListener);
        recycleView.setOnTouchListener(mOnTouchListener);
        updateLayoutManger();
    }

    public void init(int itemSize, int base) {
        if (itemSize % base == 0) {
            allPage = itemSize / base - 1;
        } else {
            allPage = itemSize / base;
        }
    }

    public void setOnScrollPageListener(OnScrollPageListener listener) {
        mListener = listener;
    }

    public void updateLayoutManger() {
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager != null) {
            if (layoutManager.canScrollVertically()) {
                mOrientation = ORIENTATION.VERTICAL;
            } else if (layoutManager.canScrollHorizontally()) {
                mOrientation = ORIENTATION.HORIZONTAL;
            } else {
                mOrientation = ORIENTATION.NULL;
            }
            if (mAnimator != null) {
                mAnimator.cancel();
            }
            startX = 0;
            startY = 0;
            offsetX = 0;
            offsetY = 0;
        }
    }

    private class MyOnFlingListener extends RecyclerView.OnFlingListener {
        @Override
        public boolean onFling(int velocityX, int velocityY) {
            if (mOrientation == ORIENTATION.NULL) {
                return false;
            }
            //获取开始滚动时所在页面的index
            int page = getStartPageIndex();
            //记录滚动开始和结束的位置
            int endPoint;
            int startPoint;

            //如果是垂直方向
            if (mOrientation == ORIENTATION.VERTICAL) {
                //开始滚动位置，当前开始执行 scrollBy 位置
                startPoint = offsetY;
                if (velocityY == Integer.MAX_VALUE) {
                    page += indexPage;
                } else if (velocityY < 0) {
                    page--;
                } else if (velocityY > 0) {
                    page++;
                } else if (pageNum != -1) {
                    startPoint = 0;
                    page = pageNum - 1;
                }
                //根据不同的速度判断需要滚动的方向
                //一次滚动一个 mRecyclerView 高度
                endPoint = page * mRecyclerView.getHeight();
            } else {
                startPoint = offsetX;
                if (velocityX == Integer.MAX_VALUE) {
                    page += indexPage;
                } else if (velocityX < 0) {
                    page--;
                } else if (velocityX > 0) {
                    page++;
                } else if (pageNum != -1) {
                    startPoint = 0;
                    page = pageNum - 1;
                }
                endPoint = page * mRecyclerView.getWidth();
            }

            //使用动画处理滚动
            mAnimator = ValueAnimator.ofInt(startPoint, endPoint);
            mAnimator.setDuration(300);
            isScall = true;
            mAnimator.addUpdateListener(animation -> {
                int nowPoint = (int) animation.getAnimatedValue();
                if (mOrientation == ORIENTATION.VERTICAL) {
                    int dy = nowPoint - offsetY;
                    if (dy == 0) return;
                    //这里通过RecyclerView的scrollBy方法实现滚动。
                    mRecyclerView.scrollBy(0, dy);
                } else {
                    int dx = nowPoint - offsetX;
                    if (dx == 0) return;
                    mRecyclerView.scrollBy(dx, 0);
                }
            });
            mAnimator.addListener(new AnimatorListenerAdapter() {
                //动画结束
                @Override
                public void onAnimationEnd(Animator animation) {
                    startY = offsetY;
                    startX = offsetX;
                    isScall = false;
                    //滚动完成，进行判断是否滚到头了或者滚到尾部了
                    RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
                    //判断是当前layoutManager是否为LinearLayoutManager
                    // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                    if (layoutManager instanceof LinearLayoutManager) {
                        LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                        //获取最后一个可见view的位置
                        lastItemPosition = linearManager.findLastVisibleItemPosition();
                        //获取第一个可见view的位置
                        firstItemPosition = linearManager.findFirstVisibleItemPosition();

                    }
                    totalNum = mRecyclerView.getAdapter().getItemCount();
                    if (totalNum == lastItemPosition + 1) {
                        updateLayoutManger();
                    }
                    if (firstItemPosition == 0) {
                        updateLayoutManger();
                    }
                    if (mListener != null) {
                        mListener.onScrollIndex(getPageIndex());
                    }
                }
            });
            mAnimator.start();
            return true;
        }
    }

    private class MyOnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE && mOrientation != ORIENTATION.NULL) {
                boolean move;
                int vX = 0;
                int vY = 0;
                if (mOrientation == ORIENTATION.VERTICAL) {
                    int absY = Math.abs(offsetY - startY);
                    //如果滑动的距离超过屏幕的一半表示需要滑动到下一页
                    move = absY > (recyclerView.getHeight() / 2);
                    if (move) {
                        vY = offsetY - startY < 0 ? -1000 : 1000;
                    }
                } else {
                    int absX = Math.abs(offsetX - startX);
                    move = absX > (recyclerView.getWidth() / 2);
                    if (move) {
                        vX = offsetX - startX < 100 ? -1000 : 1000;
                    }
                }
                mOnFlingListener.onFling(vX, vY);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            //滚动结束记录滚动的偏移量
            //记录当前滚动到的位置
            offsetY += dy;
            offsetX += dx;
        }
    }


    private MyOnTouchListener mOnTouchListener = new MyOnTouchListener();

    private class MyOnTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (mAnimator != null) {
                    mAnimator.cancel();
                }
            }
            return isScall;
        }
    }

    //获取当前滚动的页数
    private int getPageIndex() {
        int p;
        if (mOrientation == ORIENTATION.VERTICAL) {
            //当前滚动到的位置除以屏幕高度的整数就是当前滚动的位置
            if (mRecyclerView.getHeight() != 0) {
                p = offsetY / mRecyclerView.getHeight();
            } else p = 0;
        } else {
            if (mRecyclerView.getWidth() != 0) {
                p = offsetX / mRecyclerView.getWidth();
            } else p = 0;
        }
        return p;
    }

    private int getStartPageIndex() {
        int p = 0;
        if (mOrientation == ORIENTATION.VERTICAL) {
            //当前按下坐标大对应的页数
            if (mRecyclerView.getHeight() != 0) {
                p = startY / mRecyclerView.getHeight();
            } else p = 0;
        } else {
            if (mRecyclerView.getWidth() != 0) {
                p = startX / mRecyclerView.getWidth();
            } else p = 0;
        }
        return p;
    }

    /**
     * 滑动指定指针
     *
     * @param indexPage
     */
    public void setIndexPage(int indexPage) {
        if (getPageIndex() >= allPage) {
            this.indexPage = -(allPage);
        } else {
            this.indexPage = indexPage;
        }
        mOnFlingListener.onFling(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    /**
     * 滑动指定指针,到最右侧时不返回
     *
     * @param indexPage
     */
    public void setIndexPageNotBack(int indexPage) {
        this.indexPage = indexPage;
        mOnFlingListener.onFling(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public int getAllPage() {
        return allPage;
    }

    public interface OnScrollPageListener {
        void onScrollIndex(int index);
    }

}
