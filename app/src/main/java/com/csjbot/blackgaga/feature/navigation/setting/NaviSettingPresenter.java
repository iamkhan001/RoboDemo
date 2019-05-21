package com.csjbot.blackgaga.feature.navigation.setting;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.dialog.MapSelectDialog;
import com.csjbot.blackgaga.feature.navigation.adapter.FileListAdapter;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.localbean.NaviBean;
import com.csjbot.blackgaga.model.tcp.bean.Position;
import com.csjbot.blackgaga.model.tcp.chassis.IChassis;
import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;
import com.csjbot.blackgaga.util.BlackgagaLogger;
import com.csjbot.blackgaga.util.FileUtil;
import com.csjbot.blackgaga.util.FileUtils;
import com.csjbot.blackgaga.util.GsonUtils;
import com.csjbot.blackgaga.util.MD5;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.blackgaga.util.StrUtil;
import com.csjbot.blackgaga.widget.MoveView;
import com.csjbot.coshandler.listener.OnPositionListener;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 孙秀艳 on 2017/11/2.
 */

public class NaviSettingPresenter implements NaviSettingContract.Presenter {
    private NaviSettingContract.View view;
    private IChassis chassis;
    private String name;
    private List<NaviBean> navis;
    private List<MoveView> moveViews;
    private FileListAdapter adapter;
    private Context mContext;

    public NaviSettingPresenter(Context context) {
        mContext = context;
        chassis = ServerFactory.getChassisInstance();
        navis = new ArrayList<>();
        /* 设置位置回调事件 */
        RobotManager.getInstance().addListener(new NaviSettingPresenter.MyOnPositionListener(this));
    }

    @Override
    public NaviSettingContract.View getView() {
        return view;
    }

    @Override
    public void initView(NaviSettingContract.View view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        if (view != null) {
            view = null;
        }
    }

    @Override
    public void getPosition() {
        chassis.getPosition();
    }

    @Override
    public void getNaviData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String json = SharedPreUtil.getString(SharedKey.NAVI_NAME, SharedKey.NAVI_KEY);
                navis = GsonUtils.jsonToObject(json, new TypeToken<List<NaviBean>>() {
                }.getType());
                /* 读取完毕,发送handler处理 */
                if (navis != null && navis.size() > 0) {
                    BlackgagaLogger.debug("navis:" + navis.size());
                    BlackgagaLogger.debug("json:----->" + json);
                }
                mHandle.obtainMessage(0).sendToTarget();
            }
        }).start();
    }

    @Override
    public void getYingBinData() {
        String json = SharedPreUtil.getString(SharedKey.YINGBIN_NAME, SharedKey.YINGBIN_KEY);
        List<Position> positionList = GsonUtils.jsonToObject(json, new TypeToken<List<Position>>() {
        }.getType());
        if (positionList == null || positionList.size() <= 0) {
            BlackgagaLogger.debug("json:----->" + json);
        } else {
            view.showYingbinData(positionList.get(0));
        }
    }

    @Override
    public void saveYingBinPoint(Position position) {
        List<Position> positionList = new ArrayList<Position>();
        positionList.add(position);
        SharedPreUtil.putString(SharedKey.YINGBIN_NAME, SharedKey.YINGBIN_KEY, GsonUtils.objectToJson(positionList));
        view.saveSuccess();
        BlackgagaLogger.debug("保存成功:" + position.getX());
    }

    @Override
    public void getGuideData() {
        String json = SharedPreUtil.getString(SharedKey.GUIDE_NAME, SharedKey.GUIDE_KEY);
        List<NaviBean> naviBeanList = GsonUtils.jsonToObject(json, new TypeToken<List<NaviBean>>() {
        }.getType());
        view.showGuideData(naviBeanList);
    }

    @Override
    public void saveGuideData(List<NaviBean> naviBeanList) {
        SharedPreUtil.putString(SharedKey.GUIDE_NAME, SharedKey.GUIDE_KEY, GsonUtils.objectToJson(naviBeanList));
        view.saveSuccess();
        BlackgagaLogger.debug("保存成功:" + naviBeanList.size());
    }

    Handler mHandle = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            /* 显示地图 */
            view.showNaviMap(navis);
        }
    };

    @Override
    public void addNaviPoint() {
        if (navis == null) {
            navis = new ArrayList<NaviBean>();
        }
        boolean flag = true;
        if (navis.size() > 0) {
            for (int i = 0; i < navis.size(); i++) {
                if (StrUtil.isBlank(navis.get(i).getName())) {
                    flag = false;
                    break;
                }
            }
        }
        if (flag) {
            NaviBean naviBean = new NaviBean();
            naviBean.setId(MD5.getRandomString(10));
            navis.add(naviBean);
            view.addNaviPointItem(naviBean);
        } else {
            view.addNotNaviPoint();
        }
    }

    @Override
    public void removeNaviPointByPosition(int position) {
        if (isGuideData(position)) {
            removeGuideData(navis.get(position));
        }
//        removeMediaRes(navis.get(position));
        navis.remove(position);
        SharedPreUtil.putString(SharedKey.NAVI_NAME, SharedKey.NAVI_KEY, GsonUtils.objectToJson(navis));
        view.removeNaviItem(position);
    }

    /**
     * 删除对应导航点的数据
     */
    private void removeMediaRes(NaviBean naviBean) {
        if (naviBean != null) {
            FileUtil.deleteFile(Constants.NAVI_PATH+naviBean.getImagePath());//视频图片动画
            FileUtil.deleteFile(Constants.NAVI_PATH+naviBean.getMusicPath());//背景音乐
            FileUtil.deleteFile(Constants.NAVI_PATH+naviBean.getDescInRunning());//途中讲解
        }
    }

    @Override
    public void saveNaviPointByPosition(NaviBean naviBean, int position) {
        try {
            navis.set(position, naviBean);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, "保存失败", Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreUtil.putString(SharedKey.NAVI_NAME, SharedKey.NAVI_KEY, GsonUtils.objectToJson(navis));
        BlackgagaLogger.debug("保存成功:savePosition" + GsonUtils.objectToJson(navis));
        view.saveSuccess();
        updateGuideData(naviBean);
        addNaviPoint();
    }

    /**
     * 此事件为获取到当前位置而触发
     * 由于此事件需要设置到RobotManager,而RobotManager是静态对象,为避免释放不掉Presenter造成内存泄露
     * 所以将Presenter对象以弱引用的方式传入
     */
    private static final class MyOnPositionListener implements OnPositionListener {

        WeakReference<NaviSettingPresenter> reference;

        public MyOnPositionListener(NaviSettingPresenter value) {
            this.reference = new WeakReference<>(value);
        }

        @Override
        public void positionInfo(String json) {
            BlackgagaLogger.debug(json);
            reference.get().view.setNaviPosition(GsonUtils.jsonToObject(json, Position.class));
            reference.get().view.setYingbinPosition(GsonUtils.jsonToObject(json, Position.class));
        }

        @Override
        public void moveResult(String json) {

        }

        @Override
        public void moveToResult(String json) {

        }

        @Override
        public void cancelResult(String json) {

        }
    }

    @Override
    public void uploadMap() {
        File root = new File(Constants.USB_PATH);
        if (!root.exists() || root.listFiles() == null || root.listFiles().length == 0) {
            Toast.makeText(mContext, mContext.getString(R.string.no_usb), Toast.LENGTH_SHORT).show();
            return;
        }
        MapSelectDialog dialog = new MapSelectDialog(mContext);
        dialog.setListener(new MapSelectDialog.OnDialogClickListener() {
            @Override
            public void onItemClicked(String path) {
                setMap(path);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void setMap(String path) {
        view.setMapFile(path);
    }

    @Override
    public void saveNaviData() {
        if (navis == null) {
            navis = new ArrayList<NaviBean>();
        }
        if (navis.size() > 0) {
            for (int i = 0; i < navis.size(); i++) {
                if (StrUtil.isBlank(navis.get(i).getName()) || StrUtil.isBlank(navis.get(i).getDescription())
                        || StrUtil.isBlank(navis.get(i).getPos().getX()) || StrUtil.isBlank(navis.get(i).getPos().getY())
                        || StrUtil.isBlank(navis.get(i).getPos().getRotation())) {
                    navis.remove(i);
                    view.removeNaviItem(i);
                    break;
                }
            }
        }
        SharedPreUtil.putString(SharedKey.NAVI_NAME, SharedKey.NAVI_KEY, GsonUtils.objectToJson(navis));
    }

    /**
     * 当保存导航数据的时候，需要更新一键导引数据
     */
    private void updateGuideData(NaviBean naviBean) {
        String json = SharedPreUtil.getString(SharedKey.GUIDE_NAME, SharedKey.GUIDE_KEY);
        List<NaviBean> naviBeanList = GsonUtils.jsonToObject(json, new TypeToken<List<NaviBean>>() {
        }.getType());
        boolean isUpdate = false;
        if (naviBeanList != null && naviBeanList.size() > 0) {
            for (int i = 0; i < naviBeanList.size(); i++) {
                if (naviBeanList.get(i).getId().equals(naviBean.getId())) {
                    naviBeanList.set(i, naviBean);
                    isUpdate = true;
                }
            }
            if (isUpdate) {
                SharedPreUtil.putString(SharedKey.GUIDE_NAME, SharedKey.GUIDE_KEY, GsonUtils.objectToJson(naviBeanList));
            }
        }
    }

    private void removeGuideData(NaviBean naviBean) {
        String json = SharedPreUtil.getString(SharedKey.GUIDE_NAME, SharedKey.GUIDE_KEY);
        List<NaviBean> naviBeanList = GsonUtils.jsonToObject(json, new TypeToken<List<NaviBean>>() {
        }.getType());
        if (naviBeanList != null && naviBeanList.size() > 0) {
            for (int i = naviBeanList.size() - 1; i >= 0; i--) {
                if (naviBeanList.get(i).getId().equals(naviBean.getId())) {
                    naviBeanList.remove(i);
                }
            }
        }
        SharedPreUtil.putString(SharedKey.GUIDE_NAME, SharedKey.GUIDE_KEY, GsonUtils.objectToJson(naviBeanList));
    }

    @Override
    public boolean isGuideData(int position) {
        String json = SharedPreUtil.getString(SharedKey.GUIDE_NAME, SharedKey.GUIDE_KEY);
        List<NaviBean> naviBeanList = GsonUtils.jsonToObject(json, new TypeToken<List<NaviBean>>() {
        }.getType());
        boolean isExist = false;
        if (naviBeanList != null && naviBeanList.size() > 0 && navis != null && navis.size() > position) {
            NaviBean naviBean = navis.get(position);
            for (int i = 0; i < naviBeanList.size(); i++) {
                if (naviBeanList.get(i).getId().equals(naviBean.getId())) {
                    isExist = true;
                }
            }
        }
        BlackgagaLogger.debug("sunxy isGuideData isExist" + isExist);
        return isExist;
    }

    @Override
    public void getMusicPath() {
        openAssignFolder("audio/*");
    }

    @Override
    public void getTxtPath() {
        openAssignFolder("text/plain");
    }

    @Override
    public void getPicPath() {
        openAssignFolder("*/*");
    }//image/*, video/*

    private void openAssignFolder(String mime){
        File file = new File(Constants.NAVI_PATH);
        if(null==file){
            return;
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(Constants.NAVI_PATH), mime);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            ((NaviSettingActivity)mContext).startActivityForResult(intent, 1);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
