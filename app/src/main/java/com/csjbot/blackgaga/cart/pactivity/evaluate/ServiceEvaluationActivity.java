package com.csjbot.blackgaga.cart.pactivity.evaluate;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;

import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.ai.ServiceEvaluationAI;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.cart.pactivity.introduce_list.ProductListActivity;
import com.csjbot.blackgaga.model.http.factory.ServerFactory;
import com.csjbot.blackgaga.model.http.product.ProductProxy;
import com.csjbot.blackgaga.model.http.product.listeners.EvalutionListener;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.blackgaga.util.ShopcartUtil;
import com.csjbot.coshandler.listener.OnSpeakListener;
import com.csjbot.coshandler.log.Csjlogger;
import com.google.gson.Gson;
import com.iflytek.cloud.SpeechError;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/10/19.
 * 评价页面
 */

public class ServiceEvaluationActivity extends BaseModuleActivity implements ServiceEvaluationContract.view {

    @BindView(R.id.buttonPanel1)
    Button buttonPanel1;
    @BindView(R.id.buttonPanel2)
    Button buttonPanel2;
    @BindView(R.id.buttonPanel3)
    Button buttonPanel3;
    private ServiceEvaluationContract.presenter presenter;
    private CountDownTimer cat;

    int time;

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public int getLayoutId() {
        return  (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))
                ? R.layout.vertical_activity_service_evaluation : R.layout.activity_service_evaluation;

    }

    private ServiceEvaluationAI mAI;

    @Override
    public void init() {
        super.init();

        boolean isChecked = SharedPreUtil.getBoolean(SharedKey.EVALUATE, SharedKey.EVALUATE_SWITCH, true);
        time = SharedPreUtil.getInt(SharedKey.EVALUATE, SharedKey.EVALUATE_TIME, 4);
        if (!isChecked) {
            this.finish();
            return;
        }

        presenter = new ServiceEvaluationPresenter(this);
        presenter.initView(this);
        mAI = ServiceEvaluationAI.newInstance();
        mAI.initAI(this);
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }
        ServiceEvaluationAI.Intent intent = mAI.getIntent(text);
        if (intent != null) {
            mAI.handleIntent(intent);
        } else {
            prattle(answerText);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTitleView().setBackVisibility(View.VISIBLE);
        getChatView().clearChatMsg();
        getChatView().addChatMsg(0, getString(R.string.service_evaluation));
        speak(getString(R.string.service_evaluation), new OnSpeakListener() {
            @Override
            public void onSpeakBegin() {

            }

            @Override
            public void onCompleted(SpeechError speechError) {
                //做一个定时器
                startCat();
            }
        });

    }

    public void startCat() {
        stopCat();
        cat = new CountDownTimer((time * 1000), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                toFinish();
            }
        };
        cat.start();
    }

    public void stopCat() {
        if (cat != null) {
            cat.onFinish();
            cat.cancel();
            cat = null;
        }
    }

    @Override
    protected void onDestroy() {
        stopCat();
        super.onDestroy();
    }

    @Override
    public boolean isOpenChat() {
        return true;
    }


    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @OnClick({R.id.buttonPanel1, R.id.buttonPanel2, R.id.buttonPanel3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonPanel1:
                sendToHttp(getString(R.string.satisfied));
                break;
            case R.id.buttonPanel2:
                sendToHttp(getString(R.string.very_satisfied));
                break;
            case R.id.buttonPanel3:
                sendToHttp(getString(R.string.dissatisfied));
                break;
        }
    }


    public void sendToHttp(String maY) {
        stopCat();
        EvaluationBean evaluationBean = new EvaluationBean(ProductProxy.SN, maY, "abc", "chenqi", "23121321322", "nifdsafdsafasd");
        Gson gson = new Gson();
        String ss = gson.toJson(evaluationBean);
        ProductProxy productProxy = ServerFactory.createProduct();
        productProxy.sendEvalution(ss, new EvalutionListener() {
            @Override
            public void sendEvalutionSuccess() {
                Csjlogger.debug("chenqi  {}", "发送成功");
                toFinish();
            }


            @Override
            public void sendEvalutionFailed() {
                Csjlogger.debug("chenqi  {}", "发送失败");
                toFinish();
            }
        });
    }

    private void toFinish() {
        speak(getString(R.string.thankyou), new OnSpeakListener() {
            @Override
            public void onSpeakBegin() {

            }

            @Override
            public void onCompleted(SpeechError speechError) {
                ShopcartUtil.clearShopcart();
                jumpActivity(ProductListActivity.class);
            }
        });
    }

    public static class EvaluationBean {

        public EvaluationBean(String robotsn, String serviceeva, String usersaccount, String usersname, String usersmobile, String proposal) {
            this.robotsn = robotsn;
            this.serviceeva = serviceeva;
            this.usersaccount = usersaccount;
            this.usersname = usersname;
            this.usersmobile = usersmobile;
            this.proposal = proposal;
        }

        /**
         * robotsn : 12345678
         * serviceeva : 满意
         * usersaccount : abc
         * usersname : 陈奇
         * usersmobile : 13588978895
         * proposal : 有嗯哦是杰旁边呢
         */


        private String robotsn;
        private String serviceeva;
        private String usersaccount;
        private String usersname;
        private String usersmobile;
        private String proposal;
        private int type = 0;

        public String getRobotsn() {
            return robotsn;
        }

        public void setRobotsn(String robotsn) {
            this.robotsn = robotsn;
        }

        public String getServiceeva() {
            return serviceeva;
        }

        public void setServiceeva(String serviceeva) {
            this.serviceeva = serviceeva;
        }

        public String getUsersaccount() {
            return usersaccount;
        }

        public void setUsersaccount(String usersaccount) {
            this.usersaccount = usersaccount;
        }

        public String getUsersname() {
            return usersname;
        }

        public void setUsersname(String usersname) {
            this.usersname = usersname;
        }

        public String getUsersmobile() {
            return usersmobile;
        }

        public void setUsersmobile(String usersmobile) {
            this.usersmobile = usersmobile;
        }

        public String getProposal() {
            return proposal;
        }

        public void setProposal(String proposal) {
            this.proposal = proposal;
        }
    }
}
