package com.csjbot.blackgaga.feature.settings;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.listener.OnSNListener;
import com.github.sumimakito.awesomeqr.AwesomeQRCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xiasuhuei321 on 2017/10/24.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class ProductSNActivity extends BaseModuleActivity {
    @BindView(R.id.vp_container)
    ViewPager container;
    @BindView(R.id.tv_check_sn)
    TextView tv_check_sn;

    private List<String> data;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_product_sn;
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        getTitleView().setSettingsVisibility(View.GONE);
        getTitleView().setBackVisibility(View.VISIBLE);
        data = new ArrayList<>();
        Intent intent = getIntent();
        data.add(intent.getStringExtra(Constants.UP_PLATE));
        data.add(intent.getStringExtra(Constants.DOWN_PLATE));
        data.add(intent.getStringExtra(Constants.UP_COMPUTER));
        data.add(intent.getStringExtra(Constants.NAV));
        SnAdapter adapter = new SnAdapter();
        container.setAdapter(adapter);

        RobotManager.getInstance().addListener(new OnSNListener() {
            @Override
            public void response(String sn) {
                try {
                    JSONObject jo = new JSONObject(sn);
                    String str = jo.optString("sn");
                    if (TextUtils.isEmpty(str) || str.contains("empty")) {
                        inRunning = false;
                        checkSn();
                    } else {
                        if (dialog != null) dialog.dismiss();
                        runOnUiThread(() -> {
                            new AlertDialog.Builder(ProductSNActivity.this)
                                    .setMessage(getString(R.string.get_sn_is) + str)
                                    .setTitle(R.string.get_success)
                                    .setPositiveButton(getString(R.string.sure), (dialog1, which) -> dialog1.dismiss()).show();
                            Robot.setSN(str);
                            inRunning = false;
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
    }

    class SnAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = View.inflate(ProductSNActivity.this, R.layout.frag_sn_qr, null);
            container.addView(v);
            ImageView sn_qr_imageView = (ImageView) v.findViewById(R.id.sn_qr_imageView);
            new AwesomeQRCode.Renderer().contents(data.get(position)).size(400)
                    .renderAsync(new AwesomeQRCode.Callback() {
                        @Override
                        public void onRendered(AwesomeQRCode.Renderer renderer, Bitmap bitmap) {
                            runOnUiThread(() -> sn_qr_imageView.setImageBitmap(bitmap));
                        }

                        @Override
                        public void onError(AwesomeQRCode.Renderer renderer, Exception e) {

                        }
                    });
            TextView sn_qr_textView = (TextView) v.findViewById(R.id.sn_qr_textView);
            TextView sn_qr_textViewCode = (TextView) v.findViewById(R.id.sn_qr_textViewCode);
            sn_qr_textViewCode.setText(data.get(position));
            if (position == 0) {
                sn_qr_textView.setText(getString(R.string.up_sn));
            } else if (position == 1) {
                sn_qr_textView.setText(R.string.down_sn);
            } else if (position == 2) {
                sn_qr_textView.setText(R.string.upper_computer_sn);
            } else if (position == 3) {
                sn_qr_textView.setText(R.string.navigation_board);
            }
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private boolean runFlag = true;
    private boolean inRunning = false;
    private ProgressDialog dialog;

    @OnClick(R.id.tv_check_sn)
    public void checkSn() {
        if (!inRunning) {
            if (dialog == null) {
                dialog = new ProgressDialog(this);
                dialog.setTitle(getString(R.string.getting_sn));
                dialog.setMessage(getString(R.string.getting_sn));
            }
            if (!dialog.isShowing()) dialog.show();
            inRunning = true;
            RobotManager.getInstance().robot.reqProxy.getSN();
        }
    }
}
