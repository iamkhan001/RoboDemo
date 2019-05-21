package com.csjbot.blackgaga.cart.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.csjbot.blackgaga.R;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/10/27.
 */

public class RecyclerViewForEmpty extends RecyclerView {
    private Context context;
    private View emptyView;
    private Button button;


    private GoSynDataSetting synDataSetting;
    private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {//设置空view原理都一样，没有数据显示空view，有数据隐藏空view
            Adapter adapter = getAdapter();
            if (adapter.getItemCount() == 0) {
                emptyView.setVisibility(VISIBLE);
                RecyclerViewForEmpty.this.setVisibility(GONE);
            } else {
                emptyView.setVisibility(GONE);
                RecyclerViewForEmpty.this.setVisibility(VISIBLE);
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            onChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            onChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            onChanged();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            onChanged();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            onChanged();
        }
    };

    public RecyclerViewForEmpty(Context context) {
        super(context);
        this.context = context;
    }

    public RecyclerViewForEmpty(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public RecyclerViewForEmpty setEmptyView(View view) {
        this.emptyView = view;
        button = (Button) view.findViewById(R.id.in_data_setting);
        ((ViewGroup) this.getRootView()).addView(view);
        button.setOnClickListener(v -> {
            if (synDataSetting != null) {
                synDataSetting.onClickListener();
            }
        });
        return this;
    }


    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        adapter.registerAdapterDataObserver(observer);
        observer.onChanged();
    }

    public void setSynDataSetting(GoSynDataSetting synDataSetting) {
        this.synDataSetting = synDataSetting;
    }

    public interface GoSynDataSetting {
        void onClickListener();
    }

}
