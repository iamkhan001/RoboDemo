package com.csjbot.blackgaga.cart.pactivity.introduce_list;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.ai.ProductListAI;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.cart.adapter.ProductMainAdapter;
import com.csjbot.blackgaga.cart.adapter.RecyclerViewForEmpty;
import com.csjbot.blackgaga.cart.entity.RobotMenuListBean;
import com.csjbot.blackgaga.cart.pactivity.introduce.ProductIntruductionActivity;
import com.csjbot.blackgaga.cart.widget.HorizontalPageLayoutManager;
import com.csjbot.blackgaga.cart.widget.PagingScrollHelper;
import com.csjbot.blackgaga.cart.widget.SpaceItemDecoration;
import com.csjbot.blackgaga.feature.product.shopcart.ShoppingCartActivity;
import com.csjbot.blackgaga.feature.settings.synchronous_data_setting.SynchronousDataActivity;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.util.RichTextUtil;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.cameraclient.utils.CameraLogger;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/10/18.
 * 产品列表页面
 */

public class ProductListActivity extends BaseModuleActivity implements ProductContract.view, View.OnClickListener {
    @BindView(R.id.index_right)
    Button indexRight;
    @BindView(R.id.index_left)
    Button indexLeft;
    @BindView(R.id.recyclerview)
    RecyclerViewForEmpty recyclerview;
    @BindView(R.id.main)
    RelativeLayout btnBody;
    private ProductContract.presenter presenter;
    private ProductMainAdapter myAdapter;

    private PagingScrollHelper pagingScrollHelper = null;

    ProductListAI mAI;
    private static long delayLUNBO = 15000;
    private Timer timer = new Timer();
    TimerTask task = null;
    private int tran = 0;
    private int column = 0;

    @Override
    public int getLayoutId() {
        if (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS)) {
            tran = 2;
            column = 2;
            System.out.println("chenqi  =   " + BuildConfig.FLAVOR);
            return R.layout.vertical_activity_product_intruduction_main;
        } else {
            String Industry = SharedPreUtil.getString(SharedKey.MAINPAGE, SharedKey.MAINPAGE_KEY, "");
            if (Industry.equals("yinhang")) {
                tran = 2;
                column = 4;
            } else {
                tran = 2;
                column = 4;
            }
            return R.layout.activity_product_intruduction_main;
        }
    }

//    private void initTranAndColumn(){
//
//    }

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void init() {
        super.init();
        presenter = new ProductPresenter(this);
        presenter.initView(this);
        initRecy();
        String Industry = SharedPreUtil.getString(SharedKey.MAINPAGE, SharedKey.MAINPAGE_KEY, "");
        if (Industry.equals("yinhang") || Industry.equals("bowuguan")) {
            getTitleView().setShoppingCartVisibility(View.GONE);
            getTitleView().setSettingsVisibility(View.VISIBLE);
        } else {
            getTitleView().setSettingsVisibility(View.VISIBLE);
            getTitleView().setShoppingCartVisibility(View.VISIBLE);
            getTitleView().setShoppingCartListener(() -> jumpActivity(ShoppingCartActivity.class));
        }
//        getTitleView().setCustomerServiceVisibility(View.VISIBLE);
        getTitleView().setLogoVisibility(View.VISIBLE);
        getTitleView().setBackVisibility(View.VISIBLE);
        mAI = ProductListAI.newInstance();
        mAI.initAI(this);
    }

    private void initLUNBO() {
        destroyTimer();
        if (timer == null) {
            timer = new Timer();
        }
        task = new TimerTask() {
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };
        timer.schedule(task, delayLUNBO, delayLUNBO);
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    runOnUiThread(() -> pagingScrollHelper.setIndexPage(1));
                    break;
            }
            super.handleMessage(msg);
        }

    };


    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }
        ProductListAI.Intent intent = mAI.getIntent(text);
        if (intent != null) {
            mAI.handleIntent(intent);
        } else {
            if (!mAI.dynamicHandle(text, myAdapter.getLoadData())) {
                prattle(answerText);
            }
        }
        return true;
    }

    public void goProductIntruductionAct(String menuid, String menuName) {
        //选择跳转页面
        Intent intent = new Intent(ProductListActivity.this, ProductIntruductionActivity.class);
        intent.putExtra("pma", menuid);
        intent.putExtra("menuName", menuName);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.initListInfo();
        if (presenter.getCount() == 0)
            getTitleView().setShoppingCountVisibility(View.GONE);
        else {
            getTitleView().setShoppingCountVisibility(View.VISIBLE);
            getTitleView().setShoppingCount(presenter.getCount() + "");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        destroyTimer();
    }

    /**
     * destory上次使用的 Timer
     */
    public void destroyTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    @Override
    public boolean isOpenChat() {
        return true;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }


    private void initRecy() {
        //加载没有数据的view
        View show_no_data;
        if (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS)) {
            show_no_data = getLayoutInflater().inflate(R.layout.vertical_no_data_show, null);
        } else {
            show_no_data = getLayoutInflater().inflate(R.layout.no_data_show, null);
        }
        recyclerview.setEmptyView(show_no_data).setSynDataSetting(() -> {
            jumpActivity(SynchronousDataActivity.class);
        });
        myAdapter = new ProductMainAdapter(this);
        recyclerview.setAdapter(myAdapter);
        pagingScrollHelper = new PagingScrollHelper();
        pagingScrollHelper.setUpRecycleView(recyclerview);
        recyclerview.setNestedScrollingEnabled(false);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.dm_10dp);
        recyclerview.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        CameraLogger.debug(myAdapter.getSortName().toString());
        myAdapter.setImgItemClickListener(this::toProductIntroductionActivity);
        getChatView().clearChatMsg();
        new Handler().postDelayed(() -> {
            if (Constants.Language.CURRENT_LANGUAGE == Constants.Language.CHINESE) {
                SpannableStringBuilder ssb = getInitText();
                if (ssb != null) {
                    speak(ssb.toString());
                    setRobotChatMsg(ssb);
                }
            }
        }, 500);
        System.out.println("chenqi  =   tran = " + tran + " column = " + column);
        RecyclerView.LayoutManager layoutManager = new HorizontalPageLayoutManager(tran, column);
        if (layoutManager != null) {
            recyclerview.setLayoutManager(layoutManager);
        }
    }

    @Override
    protected boolean keywordInterrupt(String text) {
        if (myAdapter != null && myAdapter.getLoadData() != null && myAdapter.getLoadData().size() > 0) {
            for (RobotMenuListBean.ResultBean.MenulistBean menulistBean : myAdapter.getLoadData()) {
                if (text.contains(menulistBean.getMenuName())) {
                    return true;
                }
            }
        }
        return super.keywordInterrupt(text);
    }

    private void toProductIntroductionActivity(RobotMenuListBean.ResultBean.MenulistBean menulistBean) {
        //选择跳转页面
        Intent intent = new Intent(ProductListActivity.this, ProductIntruductionActivity.class);
        intent.putExtra("pma", menulistBean.getMenuid());
        intent.putExtra("menuName", menulistBean.getMenuName());
        startActivity(intent);
    }

    public SpannableStringBuilder getInitText() {
        RichTextUtil richTextUtil = new RichTextUtil();

        if (myAdapter.loadData != null && myAdapter.loadData.size() > 0) {
            int color = getResources().getColor(R.color.product_list_speak_color);
            richTextUtil.append(getString(R.string.shop_products_is)).append("    ");
            for (int i = 0; i < myAdapter.loadData.size(); i++) {
                RobotMenuListBean.ResultBean.MenulistBean bean = myAdapter.loadData.get(i);
                richTextUtil.append("\t" + (i + 1) + "." + bean.getMenuName(), color, 40, v -> {
                    toProductIntroductionActivity(bean);
                });
            }
            richTextUtil.append("    ").append(getString(R.string.say_need_product));
        }
        return richTextUtil.finish();
    }

    public void initRecyInfo(RobotMenuListBean robotMenuListBean) {
        myAdapter.updateRey(robotMenuListBean);
        pagingScrollHelper.init(myAdapter.getItemCount(), column * tran);
        if (myAdapter.getItemCount() >= 0) {
            initLUNBO();
        }
    }

    @OnClick({R.id.index_left, R.id.index_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.index_left:
                pagingScrollHelper.setIndexPage(-1);
                break;
            case R.id.index_right:
                pagingScrollHelper.setIndexPage(1);
                break;
            default:
                break;
        }
    }

    @Override
    public void error() {
    }

    @Override
    public void updateInfor(RobotMenuListBean robotMenuListBean) {
        initRecyInfo(robotMenuListBean);
    }
}
