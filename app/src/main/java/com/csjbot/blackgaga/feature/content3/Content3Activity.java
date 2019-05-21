package com.csjbot.blackgaga.feature.content3;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.ai.Content3AI;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.feature.clothing.ClothingListActivity;
import com.csjbot.blackgaga.feature.clothing.bean.ClothTypeBean;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.model.http.bean.ContentTypeBean;
import com.csjbot.blackgaga.model.http.product.ProductProxy;
import com.csjbot.blackgaga.model.http.product.listeners.ContentTypeListListener;
import com.csjbot.blackgaga.router.BRouter;
import com.csjbot.blackgaga.router.BRouterKey;
import com.csjbot.blackgaga.router.BRouterPath;
import com.csjbot.blackgaga.util.BlackgagaLogger;
import com.google.gson.Gson;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;

/**
 * Created by 孙秀艳 on 2018/2/1.
 */
@Route(path = BRouterPath.THERELEVEL)
public class Content3Activity extends BaseModuleActivity implements Content3Contract.View {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.layout_tab_data)//内容顶层tab部分
            LinearLayout layoutTab;
    @BindView(R.id.layout_no_data)//内容项内容为空
            LinearLayout layoutNoData;
    @BindView(R.id.layout_content)//内容部分
            LinearLayout layoutContent;
    @BindView(R.id.layout_recyclerview)//内容项部分
            LinearLayout layoutRecyclerview;
    @BindView(R.id.pd_all)
    ProgressBar progressBar;


    private Content3Contract.Presenter mPresenter;
    private Content3Adapter mAdapter;
    List<ContentTypeBean.ResultBean.ContentTypeMessageBean.MessageListBean> mLists = new ArrayList<>();//今日活动列表有效数据
    List<ContentTypeBean.ResultBean.ContentTypeMessageBean.MessageListBean> totalLists = new ArrayList<>();//今日活动列表所有数据
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private int mLeftPos = 0;//左侧导航栏选中的位置
    private int mTopPos = 0;//顶部导航栏选中的位置
    private Content3AI mAI;
    private static ContentTypeBean contentTypeBean = null;
    private BRouter.ContentUrlBody url = null;

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutId() {
        //获取当前场景
        if (Constants.Scene.CurrentScene.equals(Constants.Scene.XingZheng)
                || Constants.Scene.CurrentScene.equals(Constants.Scene.CheGuanSuo)) {
            if ((BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                    || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))) {
                return R.layout.vertical_activity_content3;
            } else {
                return R.layout.activity_content3_xingzheng;
            }
        } else {
            return getCorrectLayoutId(R.layout.activity_content3, R.layout.vertical_activity_content3);
        }

    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
    }

    @Override
    public void init() {
        super.init();
        getTitleView().setBackVisibility(View.VISIBLE);
        getChatView().clearChatMsg();
//        initSpeak();
        mPresenter = new Content3Presenter();
        mPresenter.initView(this);
        mAI = new Content3AI();
        mAI.initAI(this);
        initData();
    }

    private void initView() {
        if (mLists == null || mLists.size() <= 0) {
            layoutContent.setVisibility(View.GONE);
        } else {
            layoutContent.setVisibility(View.VISIBLE);
        }
    }

    private void initTab() {
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTopPos = position;
                speak(mLists.get(mLeftPos).getMessages().get(mTopPos).getSpeechScript(), true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mAdapter = new Content3Adapter(this, mLists);
        recyclerView.setAdapter(mAdapter);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mAdapter.setOnItemClickListener((view, position, speak) -> {
            mLeftPos = position;
            mTopPos = 0;
            onClickItem(position, mTopPos);
        });
        onClickItem(mLeftPos, mTopPos);
    }


    private void initData() {
        url = new Gson().fromJson(getIntent().getStringExtra(BRouterKey.CONTENT_KEY), BRouter.ContentUrlBody.class);
        progressBar.setVisibility(View.VISIBLE);
        if (url == null) {
            progressBar.setVisibility(View.GONE);
            return;
        }
        ProductProxy.newProxyInstance().getContentTypeListener(url.getResId(), new ContentTypeListListener() {
            @Override
            public void getContentTypeList(ContentTypeBean bean) {
                contentTypeBean = bean;
                progressBar.setVisibility(View.GONE);
                initDataView();
            }

            @Override
            public void onContentTypeError(Throwable e) {
                contentTypeBean = ProductProxy.newProxyInstance().getContentType(url.getParentId());
                initDataView();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLocaleContentTypeList(ContentTypeBean bean) {

            }

            @Override
            public void ImageSize(int num) {

            }

            @Override
            public void loadSuccess(String name) {
                progressBar.setProgress(View.GONE);
            }

            @Override
            public void cacheToMore() {

            }

            @Override
            public void loadFailed(String name) {
            }

            @Override
            public void loadAllSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void loadError500() {
                contentTypeBean = ProductProxy.newProxyInstance().getContentType(url.getParentId());
                initDataView();
                progressBar.setProgress(View.GONE);
            }
        });
    }

    private void initDataView() {
        totalLists.clear();
        mLists.clear();
        if (contentTypeBean != null && contentTypeBean.getResult() != null && contentTypeBean.getResult().getContentMessage() != null) {
            totalLists = contentTypeBean.getResult().getContentMessage().getMessageList();
        }
        if (totalLists != null && totalLists.size() > 0) {
            for (int i = 0; i < totalLists.size(); i++) {
                if (totalLists.get(i).isEffective()) {
                    mLists.add(totalLists.get(i));
                }
            }
        }
        if (mLists != null && mLists.size() > 0) {
            sortList();
            for (int i = 0; i < mLists.size(); i++) {
                List<ContentTypeBean.ResultBean.ContentTypeMessageBean.MessageListBean.MessagesBean> lists = mLists.get(i).getMessages();
                if (lists != null && lists.size() > 0) {
                    sortItem(lists);
                }
            }
        }
        initView();
        initTab();
        initRecycleView();
    }

    private void sortList() {
        Comparator<ContentTypeBean.ResultBean.ContentTypeMessageBean.MessageListBean> comparator = new Comparator<ContentTypeBean.ResultBean.ContentTypeMessageBean.MessageListBean>() {
            @Override
            public int compare(ContentTypeBean.ResultBean.ContentTypeMessageBean.MessageListBean l1, ContentTypeBean.ResultBean.ContentTypeMessageBean.MessageListBean l2) {
                // 先排内容项
                if (l1.getOrder() != l2.getOrder()) {
                    return l1.getOrder() - l2.getOrder();
                }
                return 0;
            }
        };
        Collections.sort(mLists, comparator);
    }

    private void sortItem(List<ContentTypeBean.ResultBean.ContentTypeMessageBean.MessageListBean.MessagesBean> messageLists) {
        Comparator<ContentTypeBean.ResultBean.ContentTypeMessageBean.MessageListBean.MessagesBean> comparator = (o1, o2) -> {
            if (o1.getOrder() != o2.getOrder()) {
                return o1.getOrder() - o2.getOrder();
            }
            return 0;
        };
        Collections.sort(messageLists, comparator);
    }

    public void onClickItem(int leftPos, int topPos) {
        mLeftPos = leftPos;
        mTopPos = topPos;
        List<String> strLists = new ArrayList<>();
        List<String> urls = new ArrayList<>();
        if (mLists != null && mLists.size() > leftPos) {
            List<ContentTypeBean.ResultBean.ContentTypeMessageBean.MessageListBean.MessagesBean> messageLists = mLists.get(leftPos).getMessages();
            if (messageLists != null && messageLists.size() > 0) {
                layoutTab.setVisibility(View.VISIBLE);
                layoutNoData.setVisibility(View.GONE);
                if (topPos == 0) {
                    speak(mLists.get(leftPos).getMessages().get(topPos).getSpeechScript(), true);
                }
                for (int i = 0; i < messageLists.size(); i++) {
                    strLists.add(messageLists.get(i).getName());
                    urls.add(URLDecoder.decode(messageLists.get(i).getCcontentUrl()));
                }
                viewPager.setAdapter(new Content3FragmentAdapter(urls, strLists, getSupportFragmentManager(), this));
                viewPager.setCurrentItem(topPos);
            } else {
                layoutTab.setVisibility(View.GONE);
                layoutNoData.setVisibility(View.VISIBLE);
                speak(mLists.get(leftPos).getSpeechScript(), true);
                BlackgagaLogger.debug("tab" + "暂无数据");
            }
            mAdapter.setItemClickBg(leftPos);
        }
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }
        if (Constants.ClothProduct.clothTypeBean != null && Constants.ClothProduct.clothTypeBean.getResult() != null
                && !Constants.ClothProduct.clothTypeBean.getResult().isEmpty()) {
            for (ClothTypeBean.ResultBean resultBean : Constants.ClothProduct.clothTypeBean.getResult()) {
                if (text.contains(resultBean.getSecondLevel())) {
                    BRouter.jumpActivity(BRouterPath.PRODUCT_CLOTHING_LIST,
                            ClothingListActivity.SELECT_TYPE, resultBean.getSecondLevel());
                    return true;
                }
            }
        }
        if (!mAI.dynamicHandle(text, mLists)) {
            prattle(answerText);
        }
        return true;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public boolean isOpenChat() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_MIN_MUTE));
    }

    private void initSpeak() {
        //        if(Constants.Language.CURRENT_LANGUAGE == Constants.Language.CHINESE) {
        new Handler().postDelayed(() -> {
            if (url == null) {
                return;
            }

            String speakStr = url.getWords();
            Log.e("Content3Activity", "initSpeak: " + speakStr);
            speak(speakStr);
            setRobotChatMsg(speakStr);
        }, 500);
        //        }
    }

    @Override
    protected void onDestroy() {
        contentTypeBean = null;
        super.onDestroy();
    }
}

