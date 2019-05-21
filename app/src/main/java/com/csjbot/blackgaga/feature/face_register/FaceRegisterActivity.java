package com.csjbot.blackgaga.feature.face_register;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.router.BRouterPath;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 人脸注册页面
 * Created by jingwc on 2017/8/15.
 */

@Route(path = BRouterPath.FACE_REGISTER)
public class FaceRegisterActivity extends BaseActivity implements FaceRegisterContract.View {


    FaceRegisterContract.Presenter mPresenter;

    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_phone_num)
    EditText et_phone_num;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @OnClick(R.id.bt_register)
    public void register(){
        String name = et_name.getText().toString().trim();
        String phoneNum = et_phone_num.getText().toString().trim();
        if(!TextUtils.isEmpty(name) && TextUtils.isEmpty(phoneNum)) {
            mPresenter.faceReg(name,phoneNum);
        }
    }

    @Override
    public void init() {
        setContentView(R.layout.activity_face_register);
        ButterKnife.bind(this);
        mPresenter = new FaceRegisterPresenter();
        mPresenter.initView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
    }

    @Override
    public void faceRegEnd() {
        this.finish();
    }
}
