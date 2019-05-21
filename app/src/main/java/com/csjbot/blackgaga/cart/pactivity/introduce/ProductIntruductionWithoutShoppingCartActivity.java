package com.csjbot.blackgaga.cart.pactivity.introduce;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.ai.ProductIntruductionWithoutShoppingCartAI;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.cart.adapter.ProductBoxAdapter;
import com.csjbot.blackgaga.cart.adapter.RecyclerViewForEmpty;
import com.csjbot.blackgaga.cart.adapter.SimpleItemTouchHelperCallback;
import com.csjbot.blackgaga.cart.entity.RobotSpListBean;
import com.csjbot.blackgaga.cart.widget.CartImageAnmotionTool;
import com.csjbot.blackgaga.cart.widget.HorizontalPageLayoutManager;
import com.csjbot.blackgaga.cart.widget.PagingScrollHelper;
import com.csjbot.blackgaga.cart.widget.SpaceItemDecoration;
import com.csjbot.blackgaga.feature.product.productDetail.ProductDetailWithoutShoppingCartActivity;
import com.csjbot.blackgaga.feature.settings.synchronous_data_setting.SynchronousDataActivity;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.util.BlackgagaLogger;
import com.csjbot.blackgaga.util.ProductUtil;
import com.csjbot.blackgaga.util.RichTextUtil;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.google.gson.Gson;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Ben
 * @date 2018/4/18
 */

public class ProductIntruductionWithoutShoppingCartActivity extends BaseModuleActivity implements ProductIntruductionContract.view, ProductBoxAdapter.OnAddCatListener {

    @BindView(R.id.index_right)
    Button indexRight;
    @BindView(R.id.index_left)
    Button indexLeft;
    @BindView(R.id.recyclerview)
    RecyclerViewForEmpty recyclerview;
    @BindView(R.id.main)
    RelativeLayout btnBody;
    private ProductIntruductionContract.presenter presenter;

    private CartImageAnmotionTool cartImageAnmotionTool;

    private HorizontalPageLayoutManager horizontalPageLayoutManager = null;

    private PagingScrollHelper pagingScrollHelper = null;

    private ProductBoxAdapter myAdapter;

    private static long delayLUNBO = 15000;

    private Timer timer = null;

    ProductIntruductionWithoutShoppingCartAI mAI;

    String parentName = null;

    TimerTask task = null;

    private int tran = 0;

    private int column = 0;

    @Override
    public int getLayoutId() {
        if (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS)) {
            tran = 1;
            column = 2;
            return R.layout.vertical_activity_product_intruduction;
        } else {
            tran = 1;
            column = 4;
            return R.layout.activity_product_intruduction;
        }
    }

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void init() {
        super.init();
//        String Industry = SharedPreUtil.getString(SharedKey.MAINPAGE, SharedKey.MAINPAGE_KEY, "");
//        if (Industry.equals("yinhang")) {
//            getTitleView().setShoppingCartVisibility(View.GONE);
//        } else {
//            getTitleView().setShoppingCartVisibility(View.VISIBLE);
//            getTitleView().setShoppingCartListener(() -> jumpActivity(ShoppingCartActivity.class));
//        }
        presenter = new ProductIntruductionPresenter(this);
        presenter.initView(this);
        initRecy();

        getTitleView().setSettingsVisibility(View.VISIBLE);

        getTitleView().setCustomerServiceVisibility(View.VISIBLE);

        getTitleView().setLogoVisibility(View.VISIBLE);

        getTitleView().setBackVisibility(View.VISIBLE);
        mAI = ProductIntruductionWithoutShoppingCartAI.newInstance();
        mAI.initAI(this);

        getChatView().clearChatMsg();
        initSpeak();

    }

    private void update() {
        Intent intent = getIntent();
        parentName = intent.getStringExtra("menuName");
        presenter.updateSp(intent.getStringExtra("pma"));
        initLUNBO();
    }

    private void initLUNBO() {
        destroyTimer();
        if (timer == null) {
            timer = new Timer();
        }
        task = new TimerTask() {
            @Override
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
                    pagingScrollHelper.setIndexPage(1);
                    break;
            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        update();
//        if (presenter.getCount() == 0)
//            getTitleView().setShoppingCountVisibility(View.GONE);
//        else {
//            getTitleView().setShoppingCountVisibility(View.VISIBLE);
//            getTitleView().setShoppingCount(presenter.getCount() + "");
//        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void initSpeak() {
        if (Constants.Language.CURRENT_LANGUAGE == Constants.Language.CHINESE) {
            new Handler().postDelayed(() -> {
                SpannableStringBuilder ssb = null;
                if (isHaveRecommentProduct(myAdapter.getProductBean())) {
                    ssb = getCommendStr();
                } else {
                    ssb = getSpeakStr();
                }
                if (ssb != null) {
                    speak(ssb.toString());
                    setRobotChatMsg(ssb);
                }
            }, 500);
        }

    }

    public SpannableStringBuilder getSpeakStr() {
        RichTextUtil richTextUtil = new RichTextUtil();
        List<RobotSpListBean.ResultBean.ProductBean> products = myAdapter.getProductBean();
        if (products != null && products.size() > 0) {
            richTextUtil.append("《" + parentName + "》"+getString(R.string.series_includes)+":").append("    ");
            int color = getResources().getColor(R.color.product_intro_speak_color);
            for (int i = 0; i < products.size(); i++) {
                RobotSpListBean.ResultBean.ProductBean productBean = products.get(i);
                richTextUtil.append("\t" + (i + 1) + "." + products.get(i).getName(), color, 40, v -> {
                    BlackgagaLogger.debug("chenqi 是不是同一个" + productBean.getName());
                    goProductDetailAct(productBean);
                });
            }
            richTextUtil.append("    ");
        }

        return richTextUtil.finish();
    }

    public SpannableStringBuilder getCommendStr() {
        RichTextUtil richTextUtil = new RichTextUtil();
        List<RobotSpListBean.ResultBean.ProductBean> products = myAdapter.getProductBean();
        richTextUtil = ProductUtil.getHotProduct(this, richTextUtil, products);
        richTextUtil = ProductUtil.getNewProduct(this, richTextUtil, products);
        richTextUtil = ProductUtil.getSaleProduct(this, richTextUtil, products);
        return richTextUtil.finish();
    }

    //是否包含需要推荐的商品
    public boolean isHaveRecommentProduct(List<RobotSpListBean.ResultBean.ProductBean> products) {
        boolean flag = false;
        if (products != null && products.size() > 0) {
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getMarketingtype() == 1 || products.get(i).getMarketingtype() == 3 || products.get(i).getMarketingtype() == 5) {//爆款 新品 折扣
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    @Override
    public boolean isOpenChat() {
        return true;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    /**
     * 初始化recycler
     */
    public void initRecy() {
        //加载没有数据的view
        View show_no_data;
        if (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS)) {
            show_no_data = getLayoutInflater().inflate(R.layout.vertical_no_data_show, null);
        } else {
            show_no_data = getLayoutInflater().inflate(R.layout.no_data_show, null);
        }
        recyclerview.setEmptyView(show_no_data).setSynDataSetting(() -> jumpActivity(SynchronousDataActivity.class));
        myAdapter = new ProductBoxAdapter(this);
        recyclerview.setAdapter(myAdapter);
        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(myAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerview);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.dm_20dp);
        recyclerview.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        pagingScrollHelper = new PagingScrollHelper();
        pagingScrollHelper.setUpRecycleView(recyclerview);
        recyclerview.setNestedScrollingEnabled(false);
        myAdapter.setAddCatListener(this);
        myAdapter.setListener(productBean -> {
            Bundle bundle = new Bundle();
            String gson = new Gson().toJson(productBean);
            bundle.putString("productBean", gson);
            Intent intent = new Intent(this, ProductDetailWithoutShoppingCartActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        switchLayout();
    }

    @Override
    protected boolean keywordInterrupt(String text) {
        if (myAdapter != null && myAdapter.getProductBean() != null && myAdapter.getProductBean().size() > 0) {
            for (RobotSpListBean.ResultBean.ProductBean productBean : myAdapter.getProductBean()) {
                if (text.contains(productBean.getName())) {
                    return true;
                }
            }
        }
        return super.keywordInterrupt(text);
    }

    public void goProductDetailAct(int position) {
        Bundle bundle = new Bundle();
        String gson = new Gson().toJson(myAdapter.getProductBean().get(position));
        bundle.putString("productBean", gson);
        Intent intent = new Intent(this, ProductDetailWithoutShoppingCartActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void goProductDetailAct(RobotSpListBean.ResultBean.ProductBean productBean) {
        Bundle bundle = new Bundle();
        String gson = new Gson().toJson(productBean);
        bundle.putString("productBean", gson);
        Intent intent = new Intent(this, ProductDetailWithoutShoppingCartActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }
//        ProductIntruductionWithoutShoppingCartAI.Intent intent = mAI.getIntent(text);
//        if (intent != null) {
//            mAI.handleIntent(intent);
//        } else {
            if (!mAI.dynamicHandle(text, myAdapter.getProductBean())) {
                prattle(answerText);
            }
//        }
        return true;
    }

    private void initRecyInfo(RobotSpListBean menulist) {
        if (menulist != null) {
            myAdapter.updateRey(menulist);
            String Industry = SharedPreUtil.getString(SharedKey.MAINPAGE, SharedKey.MAINPAGE_KEY, "");
            if (Industry.equals("yinhang")) {
                pagingScrollHelper.init(menulist.getResult().getProduct().size(), tran * column);
            } else {
                pagingScrollHelper.init(menulist.getResult().getProduct().size(), tran * column);
            }
        }
    }


    private void switchLayout() {
        String Industry = SharedPreUtil.getString(SharedKey.MAINPAGE, SharedKey.MAINPAGE_KEY, "");
        if (Industry.equals("yinhang")) {
            tran = 1;
            column = 4;
            horizontalPageLayoutManager = new HorizontalPageLayoutManager(tran, column);
        } else {
            tran = 1;
            column = 4;
            horizontalPageLayoutManager = new HorizontalPageLayoutManager(tran, column);
        }
        RecyclerView.LayoutManager layoutManager = horizontalPageLayoutManager;
        if (layoutManager != null) {
            recyclerview.setLayoutManager(layoutManager);
        }
    }

    @OnClick({R.id.index_left, R.id.index_right})
    public void viewOnClick(View v) {
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


    /**
     * 添加购物车
     *
     * @param view
     * @param productBean
     */
    @Override
    public <T> void add(T view, RobotSpListBean.ResultBean.ProductBean productBean) {
//        if (presenter.addCartCount(productBean)) {
//            cartImageAnmotionTool = new CartImageAnmotionTool(this);
//            cartImageAnmotionTool.addAnmotion((View) view,
//                    btnBody,
//                    getTitleView().getShoppingCart(),
//                    ((ImageView) view).getDrawable()).setListener(() -> {
//                getTitleView().setShoppingCountVisibility(View.VISIBLE);
//                getTitleView().setShoppingCount(presenter.getCount() + "");
//            });
//        } else {
//            //没有库存了
//            CSJToast.showToast(context, R.string.shopcart_not_more_product, 1000);
//        }
    }


    @Override
    public void updateInfor(RobotSpListBean product) {
        if (product != null) {
            initRecyInfo(product);
        } else {
            myAdapter.notifyDataSetChanged();
        }
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

}
