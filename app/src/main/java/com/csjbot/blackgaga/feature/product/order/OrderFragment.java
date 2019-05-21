package com.csjbot.blackgaga.feature.product.order;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.base.BaseFragment;
import com.csjbot.blackgaga.cart.pactivity.evaluate.ServiceEvaluationActivity;
import com.csjbot.blackgaga.model.http.bean.PayQrCodeBean;
import com.csjbot.blackgaga.router.BRouter;
import com.csjbot.blackgaga.util.ShopcartUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 孙秀艳 on 2017/10/25.
 */
public class OrderFragment extends BaseFragment implements OrderContract.View {
    private OrderContract.Presenter mPresenter;
    private String startTime;
    private int orderCount = 1;
    private ProgressDialog dialog;

    static OrderFragment newInstance() {
        return new OrderFragment();
    }

//    @BindView(R.id.ivCode)
//    ImageView ivCodel;

    @BindView(R.id.ivCode1)
    ImageView ivCode11;//支付宝二维码
    @BindView(R.id.ivCode2)
    ImageView ivCode12;//支付宝二维码


    @BindView(R.id.btnCancelOrder)
    Button btnCancelOrder;//取消订单按钮

    @BindView(R.id.no_data_view)
    LinearLayout no_data_view;

    @BindView(R.id.in_data_setting)
    Button insetting;

    @OnClick(R.id.btnCancelOrder)
    public void clickCancel() {
        ShopcartUtil.clearShopcart();
        ShopcartUtil.setOrderIsBack(true);
        BRouter.toHome();
//        RobotManager robotManager = RobotManager.getInstance();
//        robotManager.robot.reqProxy.print("测试打印接口");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int resId = getPlusModel()
                ? R.layout.vertical_fragment_pay : R.layout.fragment_pay;
        View rootView = inflater.inflate(resId, container, false);
        ButterKnife.bind(this, rootView);
        initData();
        mPresenter = new OrderPresenter();
        mPresenter.initView(this);
        mPresenter.pullPayQrCodeBean();
        return rootView;
    }

    public boolean getPlusModel(){
        return BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS);
    }

    private void initData() {
        DateFormat sd = new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault());
        startTime = sd.format(new Date());
    }


    @Override
    public void showCode(String codeUrl) {
//        QRCodeUtil.getQRCodeAsync(codeUrl, 800, new QRCodeUtil.ReadyListener() {
//            @Override
//            public void codeReady(Bitmap bitmap) {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        ivCode.setImageBitmap(bitmap);
//                    }
//                });
//            }
//        });
    }

    @Override
    public String getStartTime() {
        return startTime;
    }

    @Override
    public int getOrderCount() {
        return orderCount;
    }

    @Override
    public void showLoading() {
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.order_generating));
        dialog.setTitle(getString(R.string.order_generation));
        dialog.show();
    }

    @Override
    public void hideLoading() {
        dialog.dismiss();
    }

    @Override
    public void paySuccess() {
        Toast.makeText(getActivity(), R.string.order_success, Toast.LENGTH_LONG).show();
        orderCount++;
        ShopcartUtil.clearShopcart();
        startActivity(new Intent(getActivity(), ServiceEvaluationActivity.class));
    }

    @Override
    public void payError(String msg) {
        Toast.makeText(getActivity(), R.string.order_fail, Toast.LENGTH_LONG).show();
    }

    @Override
    public void getPayQrCodeBean(PayQrCodeBean bean) {
        if (bean == null ) {
            no_data_view.setVisibility(View.VISIBLE);
            insetting.setVisibility(View.GONE);
            btnCancelOrder.setVisibility(View.GONE);
            return;
        }
        if (bean.getResult() == null) {
            no_data_view.setVisibility(View.VISIBLE);
            insetting.setVisibility(View.GONE);
            btnCancelOrder.setVisibility(View.GONE);
            return;
        }
        if (bean.getResult().getQrcodes() == null){
            no_data_view.setVisibility(View.VISIBLE);
            insetting.setVisibility(View.GONE);
            btnCancelOrder.setVisibility(View.GONE);
            return;
        }
        no_data_view.setVisibility(View.GONE);
        List<PayQrCodeBean.ResultBean.QrcodesBean> qrcodesBeans = bean.getResult().getQrcodes();
        for (PayQrCodeBean.ResultBean.QrcodesBean body : qrcodesBeans) {
            if (body.getDesc().matches("^.*微信.*")) {
                Glide.with(getContext())
                        .load(body.getImageUrl())
                        .into(ivCode12);
            } else if (body.getDesc().matches("^.*支付宝.*")) {
                Glide.with(getContext())
                        .load(body.getImageUrl())
                        .into(ivCode11);
            }
        }
    }
}

