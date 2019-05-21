package com.csjbot.blackgaga.feature.Learning;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.cart.widget.HorizontalPageLayoutManager;
import com.csjbot.blackgaga.cart.widget.PagingScrollHelper;
import com.csjbot.blackgaga.cart.widget.SpaceItemDecoration;
import com.csjbot.blackgaga.feature.Learning.service.FloatingWindowBackService;
import com.csjbot.blackgaga.service.BatteryService;
import com.csjbot.blackgaga.util.ShellUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by  Wql , 2018/3/7 9:58
 */
public class LearningEducationActivity extends BaseModuleActivity {

    @BindView(R.id.index_right)
    ImageView indexRight;
    @BindView(R.id.index_left)
    ImageView indexLeft;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;

    @OnClick({R.id.index_left, R.id.index_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.index_left:
                pagingScrollHelper.setIndexPageNotBack(-1);
                break;
            case R.id.index_right:
                pagingScrollHelper.setIndexPageNotBack(1);
                break;
            default:
                break;
        }
    }

    private HorizontalPageLayoutManager horizontalPageLayoutManager = null;
    private PagingScrollHelper pagingScrollHelper = null;
    private LearningEducationAdatper myAdapter;
    private List<LearnBean> mLists;

    private PackageManager pManager;

    private boolean isStartService = false;

    private boolean isResume = false;

    public static String currentStartApp = "";

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void init() {
        super.init();
        getTitleView().setBackVisibility(View.VISIBLE);
        mLists = new ArrayList<>();
        initRecy();
        getAllAlowedApps();
    }

    private void initRecy() {
        //加载没有数据的view
        myAdapter = new LearningEducationAdatper(this);
        mRecyclerview.setAdapter(myAdapter);
        horizontalPageLayoutManager = new HorizontalPageLayoutManager(2, 2);
        mRecyclerview.setLayoutManager(horizontalPageLayoutManager);
        pagingScrollHelper = new PagingScrollHelper();
        pagingScrollHelper.setUpRecycleView(mRecyclerview);
        mRecyclerview.setNestedScrollingEnabled(false);
//        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.dm_20dp);
//        mRecyclerview.addItemDecoration(new SpaceItemDecoration(spacingInPixels));

        myAdapter.setOnItemClickListener(position -> {
            currentStartApp = mLists.get(position).getPakageName();
            Intent i = pManager.getLaunchIntentForPackage(mLists.get(position).getPakageName());
            startActivity(i);

            startService(new Intent(this, FloatingWindowBackService.class));
            isStartService = true;

            stopService(new Intent(this,BatteryService.class));
        });

    }

    private void getAllAlowedApps() {
        List<PackageInfo> appList = getAllApps();
        pManager = getPackageManager();
        for (int i = 0; i < appList.size(); i++) {
            PackageInfo pinfo = appList.get(i);
            String appName = pManager.getApplicationLabel(pinfo.applicationInfo).toString().trim();
//            if (!TextUtils.equals(appName, "搜狗输入法") &&
//                    !TextUtils.equals(appName, "应用宝") &&
//                    !TextUtils.equals(appName, "讯飞语记") &&
//                    !TextUtils.equals(appName, "新零售_d") &&
//                    !TextUtils.equals(appName, "ADB WiFi") &&
//                    !TextUtils.equals(appName, "Serial Port API sample")) {
//                LearnBean shareItem = new LearnBean();
//                // 设置应用程序名字
//                shareItem.setAppName(pManager.getApplicationLabel(pinfo.applicationInfo).toString());
//                // 设置应用图片
//                shareItem.setIcon(pManager.getApplicationIcon(pinfo.applicationInfo));
//                //设置应用包名
//                shareItem.setPakageName(pinfo.applicationInfo.packageName);
//                Log.e("程序名", pManager.getApplicationLabel(pinfo.applicationInfo).toString());
//                mLists.add(shareItem);
//            }
            LearnBean shareItem = new LearnBean();
            if(appName.contains("喜马拉雅FM")){
                shareItem.setAppName("喜马拉雅FM");
                shareItem.setIcon(resIdToDrawable(R.drawable.enc_01));
                shareItem.setPakageName(pinfo.applicationInfo.packageName);
                mLists.add(shareItem);
            }else if(appName.equals("巴巴熊成语故事动画")){
                shareItem.setAppName("成语故事");
                shareItem.setIcon(resIdToDrawable(R.drawable.enc_02));
                shareItem.setPakageName(pinfo.applicationInfo.packageName);
                mLists.add(shareItem);
            }else if(appName.equals("儿童游戏弹钢琴")){
                shareItem.setAppName("儿童钢琴");
                shareItem.setIcon(resIdToDrawable(R.drawable.enc_03));
                shareItem.setPakageName(pinfo.applicationInfo.packageName);
                mLists.add(shareItem);
            }else if(appName.equals("儿童教育学数学")){
                shareItem.setAppName("儿童数学");
                shareItem.setIcon(resIdToDrawable(R.drawable.enc_04));
                shareItem.setPakageName(pinfo.applicationInfo.packageName);
                mLists.add(shareItem);
            }else if(appName.equals("看图识字")){
                shareItem.setAppName("看图识字");
                shareItem.setIcon(resIdToDrawable(R.drawable.enc_05));
                shareItem.setPakageName(pinfo.applicationInfo.packageName);
                mLists.add(shareItem);
            }else if(appName.equals("儿童学拼音游戏")){
                shareItem.setAppName("拼音游戏");
                shareItem.setIcon(resIdToDrawable(R.drawable.enc_06));
                shareItem.setPakageName(pinfo.applicationInfo.packageName);
                mLists.add(shareItem);
            }else if(appName.equals("小学英语助手")){
                shareItem.setAppName("小学英语");
                shareItem.setIcon(resIdToDrawable(R.drawable.enc_07));
                shareItem.setPakageName(pinfo.applicationInfo.packageName);
                mLists.add(shareItem);
            }else if(appName.equals("FingerPen Coloring Book")){
                shareItem.setAppName("颜色屋");
                shareItem.setIcon(resIdToDrawable(R.drawable.enc_08));
                shareItem.setPakageName(pinfo.applicationInfo.packageName);
                mLists.add(shareItem);
            }else if(appName.equals("FingerPen Coloring Book")){
                shareItem.setAppName("颜色屋");
                shareItem.setIcon(resIdToDrawable(R.drawable.enc_09));
                shareItem.setPakageName(pinfo.applicationInfo.packageName);
                mLists.add(shareItem);
            }else if(appName.equals("Princess Pea")){
                shareItem.setAppName("童话书屋");
                shareItem.setIcon(resIdToDrawable(R.drawable.enc_10));
                shareItem.setPakageName(pinfo.applicationInfo.packageName);
                mLists.add(shareItem);
            }
        }

        if (!mLists.isEmpty()) {
            myAdapter.setData(mLists);
            pagingScrollHelper.init(mLists.size(), 2 * 4);
        }
    }

    private Drawable resIdToDrawable(@DrawableRes int resId){
        return getBaseContext().getResources().getDrawable(resId);
    }

    private List<PackageInfo> getAllApps() {
        List<PackageInfo> apps = new ArrayList<>();
        PackageManager pManager = getPackageManager();
        // 获取手机内所有应用
        List<PackageInfo> packlist = pManager.getInstalledPackages(0);
        for (int i = 0; i < packlist.size(); i++) {
            PackageInfo pak = packlist.get(i);
            // if()里的值如果<=0则为自己装的程序，否则为系统工程自带
            if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                // 添加自己已经安装的应用程序
                apps.add(pak);
            }
        }
        return apps;
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if(isResume) {
            if (super.onSpeechMessage(text, answerText)) {
                return false;
            }
        }

        Log.e("教育模块，学习百科", "onSpeechMessage: " + text);
        if (mLists.isEmpty()) {
            return false;
        }

        if (text.contains(getString(R.string.back)) ||
                text.contains(getString(R.string.quit)) ||
                text.contains(getString(R.string.colse))) {
            if (isStartService) {
                ShellUtils.execCommand("am force-stop " + currentStartApp, true, false);
                return true;
            }
        }


        for (int i = 0; i < mLists.size(); i++) {
            if (text.contains(mLists.get(i).getAppName())) {
                Intent intent = pManager.getLaunchIntentForPackage(mLists.get(i).getPakageName());
                startActivity(intent);

                startService(new Intent(this, FloatingWindowBackService.class));
                isStartService = true;
            }
        }
        return true;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_learningeducation,R.layout.vertical_activity_learningeducation);
    }

    @Override
    public boolean isOpenChat() {
        return true;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
        startService(new Intent(this,BatteryService.class));
        stopService(new Intent(this, FloatingWindowBackService.class));
        isStartService = false;
        isResume = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResume = false;
    }
}
