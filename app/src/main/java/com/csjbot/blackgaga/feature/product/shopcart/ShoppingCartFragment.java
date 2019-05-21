package com.csjbot.blackgaga.feature.product.shopcart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.base.BaseFragment;
import com.csjbot.blackgaga.feature.product.order.OrderActivity;
import com.csjbot.blackgaga.util.ShopcartUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 孙秀艳 on 2017/10/16.
 * 确认订单
 */

public class ShoppingCartFragment extends BaseFragment implements View.OnClickListener, ShoppingCartListAdapter.DeleteDetailListener {

    @BindView(R.id.orderList)
    RecyclerView recyclerView;
    @BindView(R.id.tvTotalPrice)
    TextView tvTotalPrice;//订单总价
    @BindView(R.id.tvProductSelNum)
    TextView tvProductSelNum;//订单商品数量
    @BindView(R.id.btnAccount)
    Button btnAccount;//结算

    static ShoppingCartFragment newInstance() {
        return new ShoppingCartFragment();
    }

    View show_no_data;

    ShoppingCartListAdapter orderListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_order, container, false);
        ButterKnife.bind(this, rootView);
        initRecycleView();
        btnAccount.setOnClickListener(this);
        isCheckSettlement();
        return rootView;
    }


    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        orderListAdapter = new ShoppingCartListAdapter(getActivity(), ShopcartUtil.getOrderList());
        showNoDataView();
        recyclerView.setAdapter(orderListAdapter);
        orderListAdapter.setDeleteDetailListener(this);
        orderListAdapter.registerAdapterDataObserver(observer);
        observer.onChanged();
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        setAllInfo();
        orderListAdapter.setOnRecyclerViewItemClickListener(new ShoppingCartListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemSubClick(View view, int position) {
                orderListAdapter.updateItem(position, false);
                setAllInfo();
            }

            @Override
            public void onItemAddClick(View view, int position) {
                orderListAdapter.updateItem(position, true);
                setAllInfo();
            }

            @Override
            public void onItemDeleteClick(View view, int position) {
                orderListAdapter.removeItem(position);
                setAllInfo();
            }

            @Override
            public void onItemCheckboxClick(View view, int position) {
                setAllInfo();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setAllInfo() {
        setTotalProductNum(ShopcartUtil.getShopcartNum() + "");
        setTvTotalPrice(ShopcartUtil.getShopcartPrice() + "");
        isCheckSettlement();
        if (getActivity() instanceof ShoppingCartActivity && ShopcartUtil.getShopcartNum() > 0) {
            ((ShoppingCartActivity) getActivity()).speak(ShopcartUtil.getShopcartChatContent(getActivity()).toString());
            ((ShoppingCartActivity) getActivity()).setRobotChatMsg(ShopcartUtil.getShopcartChatContent(getActivity()));
        } else {
            ((ShoppingCartActivity) getActivity()).stopSpeak();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            orderListAdapter.unregisterAdapterDataObserver(observer);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置已选商品数量
     */
    private void setTotalProductNum(String num) {
        tvProductSelNum.setText(num + "");
        //        tvProductSelNum.setText(Html.fromHtml(getString(R.string.shopcart_selected_product) + "<font color=red>" + num + "</font>" + getString(R.string.shopcart_product_item)));
    }

    /**
     * 设置已选商品总金额
     */
    private void setTvTotalPrice(String price) {
        tvTotalPrice.setText(getString(R.string.yuan) + price);
        //        tvTotalPrice.setText(Html.fromHtml(getString(R.string.shopcart_total_price) + "<font color=red>" + getString(R.string.￥) + price + "</font>"));
    }

    @Override
    public void onClick(View v) {
        settlement();
    }


    public void settlement() {
        if (ShopcartUtil.getShopcartNum() > 0) {
            startActivity(new Intent(getActivity(), OrderActivity.class));
        } else {
            Toast.makeText(getActivity(), getString(R.string.product_no_choose), Toast.LENGTH_SHORT).show();
        }
    }

    public void isCheckSettlement() {
        if (ShopcartUtil.getShopcartNum() > 0) {
            btnAccount.setBackgroundResource(R.drawable.order_confirm);
            btnAccount.setEnabled(true);
        } else {
            btnAccount.setBackgroundResource(R.drawable.gray);
            btnAccount.setEnabled(false);
        }
    }

    public void showNoDataView() {
        //构建子view想要显示的参数
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        //加载没有数据的view
        show_no_data = getActivity().getLayoutInflater().inflate(R.layout.no_data_show, null);
        Button button = show_no_data.findViewById(R.id.in_data_setting);
        TextView textView = show_no_data.findViewById(R.id.body_no_data);
        textView.setText(R.string.no_product);
        button.setVisibility(View.GONE);

        ((ViewGroup) recyclerView.getRootView()).addView(show_no_data);
    }

    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {//设置空view原理都一样，没有数据显示空view，有数据隐藏空view
            if (orderListAdapter.getItemCount() == 0) {
                show_no_data.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                show_no_data.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
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

    @Override
    public void del() {
        observer.onChanged();
        isCheckSettlement();
    }
}

