package com.csjbot.blackgaga.feature.navigation.setting;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.localbean.NaviBean;
import com.csjbot.blackgaga.model.http.bean.NoticeBean;
import com.csjbot.blackgaga.model.http.product.ProductProxy;
import com.csjbot.blackgaga.model.tcp.bean.Position;
import com.csjbot.blackgaga.router.BRouterPath;
import com.csjbot.blackgaga.service.GlobalNoticeService;
import com.csjbot.blackgaga.util.BlackgagaLogger;
import com.csjbot.blackgaga.util.CheckOutUtil;
import com.csjbot.blackgaga.util.FileUtil;
import com.csjbot.blackgaga.util.GsonUtils;
import com.csjbot.blackgaga.util.MD5;
import com.csjbot.blackgaga.util.MaxLengthWatcher;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.blackgaga.util.StrUtil;
import com.csjbot.blackgaga.widget.KeyPointView;
import com.csjbot.blackgaga.widget.NewRetailDialog;
import com.csjbot.blackgaga.widget.OnRecyclerItemClickListener;
import com.csjbot.blackgaga.widget.TitleView;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 孙秀艳 on 2017/11/2.
 * 导航导览设置界面
 */

@Route(path = BRouterPath.NAVI_SETTING)
public class NaviSettingActivity extends BaseModuleActivity implements NaviSettingContract.View, View.OnClickListener {
    NaviSettingContract.Presenter mPresenter;
    List<KeyPointView> keyPointViews;
    NaviType naviType = NaviType.NAVISTATE;
    NaviGuideListAdapter naviGuideListAdapter;
    List<NaviBean> naviBeanLists = new ArrayList<>();
    int index = -1;
    boolean isBack = false;

    @BindView(R.id.et_navi_name)
    EditText etNaviName;//导航点名称
    @BindView(R.id.et_another_name)
    EditText etNaviNickName;//导航点别称
    @BindView(R.id.et_navi_description)
    EditText etNaviDescription;//导航点描述
    @BindView(R.id.btn_plz_explain)
    Button btnExpalinChoose;//到点后讲解‘请选择’按钮
    @BindView(R.id.et_navi_xpoint)
    EditText etNaviXPoint;//x坐标
    @BindView(R.id.et_navi_ypoint)
    EditText etNaviYPoint;//y坐标
    @BindView(R.id.et_navi_zpoint)
    EditText etNaviZPoint;//z坐标
    @BindView(R.id.btn_get_position)
    Button btnGetPosition;//获取坐标点按钮
    @BindView(R.id.btn_yingbin_get_position)
    Button btnGetYingbinPosition;
    @BindView(R.id.et_yingbing_xpoint)
    EditText etYingbinXPoint;//迎宾点X坐标
    @BindView(R.id.et_yingbin_ypoint)
    EditText etYingbinYPoint;//迎宾点Y坐标
    @BindView(R.id.et_yingbin_zpoint)
    EditText etYingbinZPoint;//迎宾点Z坐标
    @BindView(R.id.btn_save)
    Button btnSave;//保存按钮
    @BindView(R.id.btn_delete)
    Button btnDelete;//删除按钮
    @BindView(R.id.rl_root)
    RelativeLayout rl_root;
    @BindView(R.id.btn_add_navi)
    Button btnAddNavi;//新增导航点
    @BindView(R.id.btn_add_yingbin)
    Button btnAddYingBin;//新增迎宾点
    @BindView(R.id.btn_add_guide)
    Button btnAddGuide;//一键导览
    @BindView(R.id.btn_add_navi_selected)
    Button btnAddNaviSelected;//新增导航点
    @BindView(R.id.btn_add_yingbin_selected)
    Button btnAddYingBinSelected;//新增迎宾点
    @BindView(R.id.btn_add_guide_selected)
    Button btnAddGuideSelected;//一键导览
    @BindView(R.id.navi_tab_point)
    LinearLayout llNavi;//导航点数据布局
    @BindView(R.id.view_tab_yingbin)
    RelativeLayout llYingBin;//迎宾点数据布局
    @BindView(R.id.view_tab_guide)
    LinearLayout llGuide;//一键导览数据布局
    @BindView(R.id.ll_save_btn)
    LinearLayout llSaveBtn;//保存按钮布局
    @BindView(R.id.ll_save_delete_btn)
    LinearLayout llSaveDeleteBtn;//保存和删除按钮布局
    @BindView(R.id.recyclerView_guide)
    RecyclerView recyclerView;//一键导览数据列表
    @BindView(R.id.btn_save_yingbin)
    Button btnSaveYingbin;
    @BindView(R.id.btn_map_upload)
    Button btnMapUpload;
    @BindView(R.id.iv_map)
    ImageView ivMap;
    @BindView(R.id.rl_map)
    RelativeLayout rl_map;
    @BindView(R.id.et_navi_music)
    TextView tv_navi_music;//背景音乐
    @BindView(R.id.et_navi_explain)
    TextView tv_navi_explain;//讲解
    @BindView(R.id.et_navi_image_path)
    TextView tv_navi_image_path;//到点图片路径
    @BindView(R.id.btn_plz_choose_music)
    Button btnChooseMusic;//背景音乐 ‘请选择’
    @BindView(R.id.btn_plz_choose_file)
    Button btnChooseFile;//途中讲解 ‘请选择’
    @BindView(R.id.btn_plz_choose_image)
    Button btnChooseImage;//到点后 '请选择'
    @BindView(R.id.et_navi_start)
    EditText etNaviStartTip;//出发前提示
    @BindView(R.id.btn_plz_start)
    Button btnStartChoose;//出发前提示请选择
    @BindView(R.id.et_navi_arrive)
    EditText etNaviArrive;//到点后提示
    @BindView(R.id.btn_plz_arrive)
    Button btnArriveChoose;//到点后提示请选择
    @BindView(R.id.cbMusic)
    ToggleButton cbMusic;
    @BindView(R.id.cbExplain)
    ToggleButton cbExplain;
    @BindView(R.id.cbImage)
    ToggleButton cbImage;
    @BindView(R.id.et_wait_min)
    EditText tvWaitMin;//讲解等待分钟数
    @BindView(R.id.et_wait_sec)
    EditText tvWaitSec;//讲解等待秒数
    @BindView(R.id.btn_map_navi_normal)
    Button btnMapNavi;//地图导航
    @BindView(R.id.btn_map_navi_select)
    Button btnMapNaviSelect;//地图导航
    @BindView(R.id.btn_menu_navi_normal)
    Button btnMenuNavi;//菜单导航
    @BindView(R.id.btn_menu_navi_select)
    Button btnMenuNaviSelect;//菜单导航
    /*中文、英文、数字但不包括下划线等符号*/
    private String REGEX_USERNAME = "^[\\u4E00-\\u9FA5A-Za-z0-9]+$";

    public enum file {DESCRIPTION, MUSIC, EXPLAIN, IMAGE, START, ARRIVE}

    private file whichFile = file.DESCRIPTION;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutId() {
        return (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS) || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS)) ? R.layout.vertical_activity_navi_setting : R.layout.activity_navi_setting;

    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
        boolean isGuideAll = getIntent().getBooleanExtra(Constants.GUIDE_ALL, false);
        if (isGuideAll) {
            onClick(btnAddGuide);
        }
    }

    @Override
    protected void onDestroy() {
        if (Constants.Scene.CurrentScene.equals(Constants.Scene.XianYangJiChang)) {
            startGlobalNotice();
        }
        super.onDestroy();
    }

    @Override
    public void init() {
        super.init();
        CheckOutUtil.getInstance().verifyUseName(etNaviName, this, REGEX_USERNAME);
//        CheckOutUtil.getInstance().verifyUseName(etNaviDescription, this,REGEX_USERNAME);
        getTitleView().setBackVisibility(View.VISIBLE);
        mPresenter = new NaviSettingPresenter(this);
        mPresenter.initView(this);
        keyPointViews = new ArrayList<>();
        mPresenter.getNaviData();
        initListener();
        initRecycleView();
        back();
        initMap();
        initCheckBox();
        speak(getString(R.string.navi_setting_guide_tip));
        if (SharedPreUtil.getBoolean(SharedKey.NAVIMODE_NAME, SharedKey.NAVIMODE_KEY, true)) {
            btnMapNavi.setVisibility(View.GONE);
            btnMapNaviSelect.setVisibility(View.VISIBLE);
            btnMenuNavi.setVisibility(View.VISIBLE);
            btnMenuNaviSelect.setVisibility(View.GONE);
        } else {
            btnMapNavi.setVisibility(View.VISIBLE);
            btnMapNaviSelect.setVisibility(View.GONE);
            btnMenuNavi.setVisibility(View.GONE);
            btnMenuNaviSelect.setVisibility(View.VISIBLE);
        }
    }

    private void initCheckBox() {
        cbMusic.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b && StrUtil.isNotBlank(tv_navi_music.getText().toString())) {
                    cbMusic.setButtonDrawable(R.drawable.navi2_on_ico);
                } else {
                    cbMusic.setButtonDrawable(R.drawable.navi2_off_ico);
                }
            }
        });
        cbExplain.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b && StrUtil.isNotBlank(tv_navi_explain.getText().toString())) {
                    cbExplain.setButtonDrawable(R.drawable.navi2_on_ico);
                } else {
                    cbExplain.setButtonDrawable(R.drawable.navi2_off_ico);
                }
            }
        });
        cbImage.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b && StrUtil.isNotBlank(tv_navi_image_path.getText().toString())) {
                    cbImage.setButtonDrawable(R.drawable.navi2_on_ico);
                } else {
                    cbImage.setButtonDrawable(R.drawable.navi2_off_ico);
                }
            }
        });
    }

    @Override
    public void onResume() {
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_MIN_VOICE));
        if (Constants.Scene.CurrentScene != Constants.Scene.JiChangScene) {
            btnAddGuide.setEnabled(true);
            btnAddGuideSelected.setEnabled(true);
        } else {
            btnAddGuide.setEnabled(false);
            btnAddGuideSelected.setEnabled(false);
        }
        if (Constants.Scene.CurrentScene.equals(Constants.Scene.XianYangJiChang)) {
            stopGlobalNotice();
        }
        super.onResume();
    }

    private void initMap() {
        String mapPath = SharedPreUtil.getString(SharedKey.NAVI_NAME, SharedKey.MAP_PATH);
        if (TextUtils.isEmpty(mapPath)) {// 获取不到地图，提示用户添加地图
//            mPresenter.uploadMap();
        } else {// 有地图，但是没设置
            Glide.with(this).load(mapPath).into(ivMap);
        }
    }

    private void initListener() {
        btnAddNavi.setOnClickListener(this);
        btnAddNaviSelected.setOnClickListener(this);
        btnGetPosition.setOnClickListener(this);
        btnGetYingbinPosition.setOnClickListener(this);
        btnAddYingBin.setOnClickListener(this);
        btnAddYingBinSelected.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnAddGuide.setOnClickListener(this);
        btnAddGuideSelected.setOnClickListener(this);
        btnSaveYingbin.setOnClickListener(this);
        btnMapUpload.setOnClickListener(this);
        btnExpalinChoose.setOnClickListener(this);
        btnChooseMusic.setOnClickListener(this);
        btnChooseFile.setOnClickListener(this);
        cbMusic.setOnClickListener(this);
        cbExplain.setOnClickListener(this);
        cbImage.setOnClickListener(this);
        btnMapNavi.setOnClickListener(this);
        btnMapNaviSelect.setOnClickListener(this);
        btnMenuNavi.setOnClickListener(this);
        btnMenuNaviSelect.setOnClickListener(this);
        btnChooseImage.setOnClickListener(this);
        btnStartChoose.setOnClickListener(this);
        btnArriveChoose.setOnClickListener(this);
        etNaviName.addTextChangedListener(new MaxLengthWatcher(this, 8, etNaviName, getString(R.string.navi_name_length_limit)));
        etNaviNickName.addTextChangedListener(new MaxLengthWatcher(this, 32, etNaviNickName, getString(R.string.navi_nick_length_limit_2)));
        etNaviDescription.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setEditEnable(etNaviDescription, true);
                etNaviDescription.setText("");
                etNaviDescription.removeTextChangedListener(watcher);
                etNaviDescription.addTextChangedListener(textChangedWatch(etNaviDescription, 16, getString(R.string.navi_arrive_length_limit)));
                return true;
            }
        });
        etNaviStartTip.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setEditEnable(etNaviStartTip, true);
                etNaviStartTip.setText("");
                etNaviStartTip.removeTextChangedListener(naviStartWatcher);
                etNaviStartTip.addTextChangedListener(naviStartWatch(etNaviStartTip, 16, getString(R.string.navi_start_length_limit)));
                return true;
            }
        });
        etNaviArrive.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setEditEnable(etNaviArrive, true);
                etNaviArrive.setText("");
                etNaviArrive.removeTextChangedListener(naviArriveWatcher);
                etNaviArrive.addTextChangedListener(naviArriveWatch(etNaviArrive, 16, getString(R.string.navi_arrive_tip_length_limit)));
                return true;
            }
        });
    }

    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        naviGuideListAdapter = new NaviGuideListAdapter(this);
        recyclerView.setAdapter(naviGuideListAdapter);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
//                Toast.makeText(NaviSettingActivity.this, "click", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {
                mItemTouchHelper.startDrag(vh);
            }
        });
        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {

            /**
             * 是否处理滑动事件 以及拖拽和滑动的方向 如果是列表类型的RecyclerView的只存在UP和DOWN，如果是网格类RecyclerView则还应该多有LEFT和RIGHT
             * @param recyclerView
             * @param viewHolder
             * @return
             */
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                    final int swipeFlags = 0;
                    return makeMovementFlags(dragFlags, swipeFlags);
                } else {
                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
//                    final int swipeFlags = 0;
                    final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                    return makeMovementFlags(dragFlags, swipeFlags);
                }
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //得到当拖拽的viewHolder的Position
                int fromPosition = viewHolder.getAdapterPosition();
                //拿到当前拖拽到的item的viewHolder
                int toPosition = target.getAdapterPosition();
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(naviBeanLists, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(naviBeanLists, i, i - 1);
                    }
                }
                naviGuideListAdapter.notifyItemMoved(fromPosition, toPosition);
//                naviGuideListAdapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                naviBeanLists.remove(position);
                naviGuideListAdapter.setGuideList(naviBeanLists);
//                naviGuideListAdapter.notifyItemRemoved(position);
            }

            /**
             * 重写拖拽可用
             * @return
             */
            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }

            /**
             * 长按选中Item的时候开始调用
             *
             * @param viewHolder
             * @param actionState
             */
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
                }
                super.onSelectedChanged(viewHolder, actionState);
            }

            /**
             * 手指松开的时候还原
             * @param recyclerView
             * @param viewHolder
             */
            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setBackgroundColor(0);
            }
        });

        mItemTouchHelper.attachToRecyclerView(recyclerView);
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
    public void showNaviMap(List<NaviBean> naviBeanList) {
        if (naviBeanList == null) {
            btnSaveYingbin.postDelayed(() -> {
                mPresenter.addNaviPoint();
            }, 500);
            return;
        }
        for (int i = 0; i < naviBeanList.size(); i++) {
            NaviBean naviBean = naviBeanList.get(i);
            BlackgagaLogger.debug("name:" + naviBean.getName());
            KeyPointView keyPointView = new KeyPointView(this);
            keyPointView.setIsTouch(true);//标注点可以触摸
            keyPointView.setIsBound(true);//标注点需要设置边界
            if ((BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS) || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))) {
                keyPointView.setBound(500, 400);
            } else {
                keyPointView.setBound(900, 450);
            }
            keyPointView.layout(naviBean.left, naviBean.top, naviBean.right, naviBean.bottom);
            keyPointView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (naviType == NaviType.NAVISTATE) {
                        index = getIndex(naviBean);
                        showNaviData(naviBean);
                    } else if (naviType == NaviType.GUIDESTATE) {
                        if (StrUtil.isNotBlank(naviBean.getName())) {
                            naviBeanLists.add(naviBean);
                            naviGuideListAdapter.setGuideList(naviBeanLists);
                        } else {
                            Toast.makeText(NaviSettingActivity.this, R.string.navi_point_not_setting, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            keyPointView.setName(subString(naviBean.getName()));
//            moveView.setTextColor(Color.BLUE);
            keyPointView.setTag(naviBean);
            /* 设置平移坐标 */
            keyPointView.setTranslationX(naviBean.getTranslationX());
            keyPointView.setTranslationY(naviBean.getTranslationY());
            rl_map.addView(keyPointView);
            keyPointViews.add(keyPointView);
        }
        mPresenter.addNaviPoint();
    }

    private void setNaviDescription(NaviBean naviBean) {
        if (naviBean.isInputOrFile()) {
            etNaviDescription.removeTextChangedListener(watcher);
            etNaviDescription.addTextChangedListener(textChangedWatch(etNaviDescription, 16, getString(R.string.navi_arrive_length_limit)));
        } else {
            etNaviDescription.removeTextChangedListener(watcher);
            etNaviDescription.addTextChangedListener(textChangedWatch(etNaviDescription, 1024, "etNaviDescription"));
        }
    }

    private void setNaviStart(NaviBean naviBean) {
        if (naviBean.isInputOrFileStart()) {
            etNaviStartTip.removeTextChangedListener(naviStartWatcher);
            etNaviStartTip.addTextChangedListener(naviStartWatch(etNaviStartTip, 16, getString(R.string.navi_start_length_limit)));
        } else {
            etNaviStartTip.removeTextChangedListener(naviStartWatcher);
            etNaviStartTip.addTextChangedListener(naviStartWatch(etNaviStartTip, 1024, "etNaviStartTip"));
        }
    }

    private void setNaviArrive(NaviBean naviBean) {
        if (naviBean.isInputOrFileArrive()) {
            etNaviArrive.removeTextChangedListener(naviArriveWatcher);
            etNaviArrive.addTextChangedListener(naviArriveWatch(etNaviArrive, 16, getString(R.string.navi_arrive_tip_length_limit)));
        } else {
            etNaviArrive.removeTextChangedListener(naviArriveWatcher);
            etNaviArrive.addTextChangedListener(naviArriveWatch(etNaviArrive, 1024, "etNaviArrive"));
        }
    }

    private MaxLengthWatcher watcher;

    private MaxLengthWatcher textChangedWatch(EditText et, int length, String msg) {
        watcher = new MaxLengthWatcher(this, length, et, msg);
        return watcher;
    }

    private MaxLengthWatcher naviArriveWatcher;

    private MaxLengthWatcher naviArriveWatch(EditText et, int length, String msg) {
        naviArriveWatcher = new MaxLengthWatcher(this, length, et, msg);
        return naviArriveWatcher;
    }

    private MaxLengthWatcher naviStartWatcher;

    private MaxLengthWatcher naviStartWatch(EditText et, int length, String msg) {
        naviStartWatcher = new MaxLengthWatcher(this, length, et, msg);
        return naviStartWatcher;
    }

    private void showNaviData(NaviBean naviBean) {
        if (StrUtil.isBlank(naviBean.getName()) || StrUtil.isBlank(naviBean.getDescription())
                || naviBean.getPos() == null || StrUtil.isBlank(naviBean.getPos().getX())
                || StrUtil.isBlank(naviBean.getPos().getY()) || StrUtil.isBlank(naviBean.getPos().getRotation())) {
            clearNaviData();
            return;
        }
        etNaviName.setText(naviBean.getName());
        etNaviNickName.setText(naviBean.getNickName());
        setNaviDescription(naviBean);
        etNaviDescription.setText(naviBean.getDescription());
        setNaviStart(naviBean);
        etNaviStartTip.setText(naviBean.getStartTip());
        setNaviArrive(naviBean);
        etNaviArrive.setText(naviBean.getArriveTip());
        etNaviXPoint.setText(naviBean.getPos().getX());
        etNaviYPoint.setText(naviBean.getPos().getY());
        etNaviZPoint.setText(naviBean.getPos().getRotation());
        tv_navi_music.setText(naviBean.getMusicPath());
        tv_navi_explain.setText(naviBean.getDescInRunning());
        tv_navi_image_path.setText(naviBean.getImagePath());
        int waitTime = naviBean.getWaitTime() / 1000;
        if (waitTime != 0 && waitTime >= 60) {
            tvWaitMin.setText(waitTime / 60 + "");
            tvWaitSec.setText(waitTime % 60 + "");
        } else if (waitTime != 0) {
            tvWaitMin.setText("");
            tvWaitSec.setText(waitTime % 60 + "");
        } else {
            tvWaitMin.setText("");
            tvWaitSec.setText("");
        }
        if (StrUtil.isBlank(naviBean.getDescription()) || (!StrUtil.isBlank(naviBean.getDescription()) && !naviBean.getDescription().contains(".txt"))) {
            setEditEnable(etNaviDescription, true);
        } else {//到点后讲解有内容，并且是文件的话，长按才可编辑，否则不可修改
            setEditEnable(etNaviDescription, false);
        }
        if (StrUtil.isBlank(naviBean.getStartTip()) || (!StrUtil.isBlank(naviBean.getStartTip()) && !naviBean.getStartTip().contains(".txt"))) {
            setEditEnable(etNaviStartTip, true);
        } else {//到点后讲解有内容，并且是文件的话，长按才可编辑，否则不可修改
            setEditEnable(etNaviStartTip, false);
        }
        if (StrUtil.isBlank(naviBean.getArriveTip()) || (!StrUtil.isBlank(naviBean.getArriveTip()) && !naviBean.getArriveTip().contains(".txt"))) {
            setEditEnable(etNaviArrive, true);
        } else {//到点后讲解有内容，并且是文件的话，长按才可编辑，否则不可修改
            setEditEnable(etNaviArrive, false);
        }
        if (naviBean.isOpenMusic()) {
            cbMusic.setChecked(true);
        } else {
            cbMusic.setChecked(false);
        }
        if (naviBean.isOpenSpeak()) {
            cbExplain.setChecked(true);
        } else {
            cbExplain.setChecked(false);
        }
        if (naviBean.isOpenImage() || naviBean.isPlayVideo()) {
            cbImage.setChecked(true);
        } else {
            cbImage.setChecked(false);
        }
    }

    @Override
    public void addNaviPointItem(NaviBean naviBean) {
        KeyPointView keyPointView = new KeyPointView(this);
        keyPointView.setTag(naviBean);
        keyPointView.setName("");
        rl_map.addView(keyPointView);
        keyPointViews.add(keyPointView);
        keyPointView.setIsTouch(true);
        keyPointView.setIsBound(true);
        if ((BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS) || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))) {
            keyPointView.setBound(500, 400);
        } else {
            keyPointView.setBound(900, 450);
        }
        index = keyPointViews.size() - 1;
        keyPointView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = keyPointViews.size() - 1;
                clearNaviData();
            }
        });
        clearNaviData();
        if (isBack) {
            finish();
        }
    }

    @Override
    public void removeNaviItem(int position) {
        rl_map.removeView(keyPointViews.get(position));
        keyPointViews.remove(position);
        index = -1;
        clearNaviData();
    }

    @Override
    public void showYingbinData(Position position) {
        if (position != null) {
            etYingbinXPoint.setText(position.getX());
            etYingbinYPoint.setText(position.getY());
            etYingbinZPoint.setText(position.getRotation());
        }
    }

    @Override
    public void showGuideData(List<NaviBean> naviBeanList) {
        naviBeanLists.clear();
        if (naviBeanList != null && naviBeanList.size() > 0) {
            naviBeanLists.addAll(naviBeanList);
        }
        naviGuideListAdapter.setGuideList(naviBeanList);
    }

    @Override
    public void setNaviPosition(Position position) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (position != null && naviType == NaviType.NAVISTATE) {
                    etNaviXPoint.setText(position.getX());
                    etNaviYPoint.setText(position.getY());
                    etNaviZPoint.setText(position.getRotation());
                }
            }
        });
    }

    @Override
    public void setYingbinPosition(Position position) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (position != null && naviType == NaviType.YINGBINSTATE) {
                    etYingbinXPoint.setText(position.getX());
                    etYingbinYPoint.setText(position.getY());
                    etYingbinZPoint.setText(position.getRotation());
                }
            }
        });
    }

    @Override
    public void setMapFile(String path) {
        Glide.with(NaviSettingActivity.this).load(path).into(ivMap);
    }

    @Override
    public void addNotNaviPoint() {
        index = keyPointViews.size() - 1;
        clearNaviData();
        if (isBack) {
            finish();
        }
    }

    @Override
    public void saveSuccess() {
        Toast.makeText(this, R.string.save_success, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_navi://添加导航点
            case R.id.btn_add_navi_selected:
                setTabTextAndBG(R.id.btn_add_navi);
                mPresenter.addNaviPoint();
                break;
            case R.id.btn_yingbin_get_position:
            case R.id.btn_get_position://获取坐标点
                if (RobotManager.getInstance().getConnectState()) {
                    mPresenter.getPosition();
                } else {
                    Toast.makeText(context, R.string.not_connect_slam, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_add_yingbin://设置迎宾点
            case R.id.btn_add_yingbin_selected:
                setTabTextAndBG(R.id.btn_add_yingbin);
                mPresenter.getYingBinData();
                break;
            case R.id.btn_save://保存导航点数据
                if (naviType == NaviType.NAVISTATE) {
                    saveNaviData();
                } else if (naviType == NaviType.GUIDESTATE) {
                    saveGuideData();
                }
                break;
            case R.id.btn_delete://删除导航点数据
                if (naviType == NaviType.NAVISTATE) {
                    removeNaviData();
                } else if (naviType == NaviType.GUIDESTATE) {
                    removeGuideData();
                }
                break;
            case R.id.btn_add_guide://一键导览
            case R.id.btn_add_guide_selected:
                naviBeanLists.clear();
                setTabTextAndBG(R.id.btn_add_guide);
                mPresenter.getGuideData();
                break;
            case R.id.btn_save_yingbin:
            case R.id.ll_save_btn:
                saveYingBinData();
                break;
            case R.id.btn_map_upload://上传地图
                mPresenter.uploadMap();
                break;
            case R.id.btn_plz_explain:
                etNaviDescription.removeTextChangedListener(watcher);
                etNaviDescription.addTextChangedListener(textChangedWatch(etNaviDescription, 1024, "etNaviDescription"));
                whichFile = file.DESCRIPTION;
                mPresenter.getTxtPath();
                break;
            case R.id.btn_plz_choose_music:
                whichFile = file.MUSIC;
                mPresenter.getMusicPath();//获取背景音乐路径
                break;
            case R.id.btn_plz_start:
                etNaviStartTip.removeTextChangedListener(naviStartWatcher);
                etNaviStartTip.addTextChangedListener(naviStartWatch(etNaviStartTip, 1024, "etNaviStartTip"));
                whichFile = file.START;
                mPresenter.getTxtPath();
                break;
            case R.id.btn_plz_arrive:
                etNaviArrive.removeTextChangedListener(naviArriveWatcher);
                etNaviArrive.addTextChangedListener(naviArriveWatch(etNaviArrive, 1024, "etNaviArrive"));
                whichFile = file.ARRIVE;
                mPresenter.getTxtPath();
                break;
            case R.id.btn_plz_choose_file:
                whichFile = file.EXPLAIN;
                mPresenter.getTxtPath();//获取背景音乐路径
                break;
            case R.id.btn_plz_choose_image:
                whichFile = file.IMAGE;
                mPresenter.getPicPath();
                break;
            case R.id.cbMusic:
                if (StrUtil.isBlank(tv_navi_music.getText().toString())) {//没有选择背景音乐文件的话，开关无效
                    Toast.makeText(this, R.string.choose_bg_music_tips, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cbExplain:
                if (StrUtil.isBlank(tv_navi_explain.getText().toString())) {//没有选择途中讲解文件的话，开关无效
                    Toast.makeText(this, R.string.choose_desc_inrunning_file, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cbImage:
                if (StrUtil.isBlank(tv_navi_image_path.getText().toString())) {//没有选择背景音乐文件的话，开关无效
                    Toast.makeText(this, R.string.choose_image_gif, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_map_navi_normal://地图导航
            case R.id.btn_map_navi_select:
                btnMapNaviSelect.setVisibility(View.VISIBLE);
                btnMapNavi.setVisibility(View.GONE);
                btnMenuNavi.setVisibility(View.VISIBLE);
                btnMenuNaviSelect.setVisibility(View.GONE);
                SharedPreUtil.putBoolean(SharedKey.NAVIMODE_NAME, SharedKey.NAVIMODE_KEY, true);//地图导航设置为true
                break;
            case R.id.btn_menu_navi_normal://菜单导航
            case R.id.btn_menu_navi_select:
                btnMapNaviSelect.setVisibility(View.GONE);
                btnMapNavi.setVisibility(View.VISIBLE);
                btnMenuNavi.setVisibility(View.GONE);
                btnMenuNaviSelect.setVisibility(View.VISIBLE);
                SharedPreUtil.putBoolean(SharedKey.NAVIMODE_NAME, SharedKey.NAVIMODE_KEY, false);//菜单导航设置为false
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String path = "";
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                path = FileUtil.getPath(this, uri);
            } else {//4.4以下下系统调用方法
                path = FileUtil.getFilePathFromUri(this, uri);
            }
            if (whichFile == file.MUSIC) {
                if (path != null && path.contains(".mp3")) {
                    if (!FileUtil.isExceedSize(path, Constants.musicLimit, Constants.MB)) {
                        copyFile(path, Constants.NAVI_PATH + FileUtil.getFileName(path));//拷贝背景音文件到指定路径
                        tv_navi_music.setText(FileUtil.getFileName(path));//设置内容
                    } else {
                        Toast.makeText(NaviSettingActivity.this, R.string.file_too_large, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(NaviSettingActivity.this, R.string.music_file_not_support, Toast.LENGTH_SHORT).show();
                }
            } else if (whichFile == file.EXPLAIN) {
                if (path != null && path.contains(".txt")) {
                    if (!FileUtil.isExceedSize(path, Constants.inRunningLimit, Constants.KB)) {//判断大小是否超过规定大小
                        if (StrUtil.isNotBlank(FileUtil.readToString(path).trim())) {//判断所选文件内容是否为空
                            copyFile(path, Constants.NAVI_PATH + FileUtil.getFileName(path));
                            tv_navi_explain.setText(FileUtil.getFileName(path));//设置内容
                        } else {
                            Toast.makeText(NaviSettingActivity.this, R.string.plz_select_file_null, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(NaviSettingActivity.this, R.string.file_too_large, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(NaviSettingActivity.this, R.string.txt_file_not_support, Toast.LENGTH_SHORT).show();
                }
            } else if (whichFile == file.DESCRIPTION) {
                if (path != null && path.contains(".txt")) {
                    if (!FileUtil.isExceedSize(path, Constants.descLimit, Constants.KB)) {
                        if (StrUtil.isNotBlank(FileUtil.readToString(path).trim())) {//判断所选文件内容是否为空
                            copyFile(path, Constants.NAVI_PATH + FileUtil.getFileName(path));
                            etNaviDescription.setText(FileUtil.getFileName(path));//设置内容
                            setEditEnable(etNaviDescription, false);
                        } else {
                            Toast.makeText(NaviSettingActivity.this, R.string.plz_select_file_null, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(NaviSettingActivity.this, R.string.file_too_large, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(NaviSettingActivity.this, R.string.txt_file_not_support, Toast.LENGTH_SHORT).show();
                }
            } else if (whichFile == file.START) {
                if (path != null && path.contains(".txt")) {
                    if (!FileUtil.isExceedSize(path, Constants.descLimit, Constants.KB)) {
                        if (StrUtil.isNotBlank(FileUtil.readToString(path).trim())) {//判断所选文件内容是否为空
                            copyFile(path, Constants.NAVI_PATH + FileUtil.getFileName(path));
                            etNaviStartTip.setText(FileUtil.getFileName(path));//设置内容
                            setEditEnable(etNaviStartTip, false);
                        } else {
                            Toast.makeText(NaviSettingActivity.this, R.string.plz_select_file_null, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(NaviSettingActivity.this, R.string.file_too_large, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(NaviSettingActivity.this, R.string.txt_file_not_support, Toast.LENGTH_SHORT).show();
                }
            } else if (whichFile == file.ARRIVE) {
                if (path != null && path.contains(".txt")) {
                    if (!FileUtil.isExceedSize(path, Constants.descLimit, Constants.KB)) {
                        if (StrUtil.isNotBlank(FileUtil.readToString(path))) {//判断所选文件内容是否为空
                            copyFile(path, Constants.NAVI_PATH + FileUtil.getFileName(path));
                            etNaviArrive.setText(FileUtil.getFileName(path));//设置内容
                            setEditEnable(etNaviArrive, false);
                        } else {
                            Toast.makeText(NaviSettingActivity.this, R.string.plz_select_file_null, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(NaviSettingActivity.this, R.string.file_too_large, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(NaviSettingActivity.this, R.string.txt_file_not_support, Toast.LENGTH_SHORT).show();
                }
            } else if (whichFile == file.IMAGE) {
                boolean isImageFile = false;
                for (int i = 0; i < Constants.suffixs.length; i++) {
                    if (path != null && path.contains(Constants.suffixs[i])) {
                        isImageFile = true;
                        break;
                    }
                }
                if (isImageFile) {
                    if (!FileUtil.isExceedSize(path, Constants.videoLimit, Constants.MB)) {
                        copyFile(path, Constants.NAVI_PATH + FileUtil.getFileName(path));
                        tv_navi_image_path.setText(FileUtil.getFileName(path));//设置内容
                    } else {
                        Toast.makeText(NaviSettingActivity.this, R.string.file_too_large, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(NaviSettingActivity.this, R.string.pic_file_not_support, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void copyFile(String oldPath, String newPath) {
        if (StrUtil.isNotBlank(oldPath) && StrUtil.isNotBlank(newPath) && oldPath.equals(newPath)) {
            return;
        }
        Observable.just(1)
                .map(integer -> FileUtil.copy(new File(oldPath), new File(newPath)))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull Boolean b) {
                        if (b) {
                            BlackgagaLogger.debug("拷贝导航文件成功");
                        } else {
                            BlackgagaLogger.debug("拷贝导航文件失败");
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        BlackgagaLogger.error(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void saveGuideData() {
        if (naviBeanLists == null || naviBeanLists.size() <= 0) {
            isBack = false;
            Toast.makeText(this, R.string.plz_choose_guide_data, Toast.LENGTH_LONG).show();
            return;
        }
        mPresenter.saveGuideData(naviBeanLists);
        if (isBack) {
            finish();
        }
    }

    /**
     * 保存迎宾数据
     */
    private void saveYingBinData() {
        String msg = yingBinValidate();
        if (msg != null) {
            isBack = false;
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            return;
        }
        Position position = new Position();
        position.setX(etYingbinXPoint.getText().toString());
        position.setY(etYingbinYPoint.getText().toString());
        position.setRotation(etYingbinZPoint.getText().toString());
        mPresenter.saveYingBinPoint(position);
        if (isBack) {
            finish();
        }
    }

    /**
     * 迎宾数据校验
     */
    private String yingBinValidate() {
        String msg = null;
        if (StrUtil.isBlank(etYingbinXPoint.getText().toString())) {
            msg = getString(R.string.plz_input_x_point);
        } else if (StrUtil.isBlank(etYingbinYPoint.getText().toString())) {
            msg = getString(R.string.plz_input_y_point);
        } else if (StrUtil.isBlank(etYingbinZPoint.getText().toString())) {
            msg = getString(R.string.plz_input_z_point);
        }
        return msg;
    }

    /**
     * 保存导航点数据
     */
    private void saveNaviData() {
        String msg = naviValidate();
        if (msg != null) {
            isBack = false;
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            return;
        }
        if (index == -1) {
            Toast.makeText(this, R.string.plz_choose_save_navi_data, Toast.LENGTH_LONG).show();
            return;
        }
        if (keyPointViews != null && keyPointViews.size() > 0) {
            KeyPointView keyPointView = keyPointViews.get(index);
            NaviBean naviBeam = null;
            if (keyPointView.getTag() == null) {
                naviBeam = new NaviBean();
                naviBeam.setId(MD5.getRandomString(10));
            } else {
                naviBeam = (NaviBean) keyPointView.getTag();
            }
            naviBeam.setName(etNaviName.getText().toString());//非空判断
            naviBeam.setNickName(etNaviNickName.getText().toString());
            naviBeam.setDescription(etNaviDescription.getText().toString());
            naviBeam.setDescContent(FileUtil.readToString(Constants.NAVI_PATH, etNaviDescription.getText().toString()));
            if (FileUtil.isExistFile(Constants.NAVI_PATH + etNaviDescription.getText().toString(), ".txt")) {
                naviBeam.setInputOrFile(false);
            } else {
                naviBeam.setInputOrFile(true);
            }
            naviBeam.setStartTip(etNaviStartTip.getText().toString());
            naviBeam.setStartTipContent(FileUtil.readToString(Constants.NAVI_PATH, etNaviStartTip.getText().toString()));
            if (FileUtil.isExistFile(Constants.NAVI_PATH + etNaviStartTip.getText().toString(), ".txt")) {
                naviBeam.setInputOrFileStart(false);
            } else {
                naviBeam.setInputOrFileStart(true);
            }
            naviBeam.setArriveTip(etNaviArrive.getText().toString());
            naviBeam.setArriveTipContent(FileUtil.readToString(Constants.NAVI_PATH, etNaviArrive.getText().toString()));
            if (FileUtil.isExistFile(Constants.NAVI_PATH + etNaviArrive.getText().toString(), ".txt")) {
                naviBeam.setInputOrFileArrive(false);
            } else {
                naviBeam.setInputOrFileArrive(true);
            }
            naviBeam.setWaitTime(getWaitTime());
            naviBeam.setImagePath(tv_navi_image_path.getText().toString());
            Position position = new Position();
            position.setX(etNaviXPoint.getText().toString());
            position.setY(etNaviYPoint.getText().toString());
            position.setRotation(etNaviZPoint.getText().toString());
            naviBeam.setPos(position);
            naviBeam.setTranslationX(keyPointView.getTranslationX());
            naviBeam.setTranslationY(keyPointView.getTranslationY());
            naviBeam.left = keyPointView.getLeft();
            naviBeam.right = keyPointView.getRight();
            naviBeam.top = keyPointView.getTop();
            naviBeam.bottom = keyPointView.getBottom();
            if (cbMusic.isChecked()) {
                naviBeam.setOpenMusic(true);
            } else {
                naviBeam.setOpenMusic(false);
            }
            if (cbExplain.isChecked()) {
                naviBeam.setOpenSpeak(true);
            } else {
                naviBeam.setOpenSpeak(false);
            }
            if (cbImage.isChecked()) {
                if (FileUtil.isNaviImage(tv_navi_image_path.getText().toString())) {
                    naviBeam.setOpenImage(true);
                    naviBeam.setPlayVideo(false);
                } else {
                    naviBeam.setPlayVideo(true);
                    naviBeam.setOpenImage(false);
                }
            } else {
                naviBeam.setPlayVideo(false);
                naviBeam.setOpenImage(false);
            }
            naviBeam.setMusicPath(tv_navi_music.getText().toString());
            naviBeam.setDescInRunning(tv_navi_explain.getText().toString());
            naviBeam.setDescInRunningContent(FileUtil.readToString(Constants.NAVI_PATH, tv_navi_explain.getText().toString()));//途中讲解内容
            BlackgagaLogger.debug("left=" + keyPointView.getLeft() + " right=" + keyPointView.getRight() + " " +
                    "top=" + keyPointView.getTop() + " bottom=" + keyPointView.getBottom());
            keyPointView.setTag(naviBeam);
            keyPointView.setName(subString(etNaviName.getText().toString()));
            keyPointView.setIsTouch(true);
            keyPointView.setIsBound(true);
            if ((BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS) || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))) {
                keyPointView.setBound(500, 400);
            } else {
                keyPointView.setBound(900, 450);
            }
            keyPointView.layout(naviBeam.left, naviBeam.top, naviBeam.right, naviBeam.bottom);
            final NaviBean naviBean = naviBeam;
            keyPointView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (naviType == NaviType.NAVISTATE) {
                        index = getIndex(naviBean);
                        showNaviData(naviBean);
                    } else if (naviType == NaviType.GUIDESTATE) {
                        if (StrUtil.isNotBlank(naviBean.getName())) {
                            naviBeanLists.add(naviBean);
                            naviGuideListAdapter.setGuideList(naviBeanLists);
                        } else {
                            Toast.makeText(NaviSettingActivity.this, R.string.navi_point_not_setting, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            mPresenter.saveNaviPointByPosition(naviBean, index);
        }
    }

    private int getIndex(NaviBean naviBean) {
        int position = 0;
        String json = SharedPreUtil.getString(SharedKey.NAVI_NAME, SharedKey.NAVI_KEY);
        List<NaviBean> naviList = GsonUtils.jsonToObject(json, new TypeToken<List<NaviBean>>() {
        }.getType());
        if (naviList != null && naviList.size() > 0) {
            for (int i = 0; i < naviList.size(); i++) {
                NaviBean bean = naviList.get(i);
                if (StrUtil.isNotBlank(naviBean.getName()) && naviBean.getName().equals(bean.getName()) &&
                        bean.getTranslationX() == naviBean.getTranslationX() && bean.getTranslationY() == naviBean.getTranslationY()) {
                    position = i;
                }
            }
        }
        return position;
    }

    private int getWaitTime() {
        int min = 0, seconds = 0, time;
        if (StrUtil.isNotBlank(tvWaitMin.getText().toString()))
            min = Integer.parseInt(tvWaitMin.getText().toString().trim());
        if (StrUtil.isNotBlank(tvWaitSec.getText().toString()))
            seconds = Integer.parseInt(tvWaitSec.getText().toString().trim());
        time = min * 60 * 1000 + seconds * 1000;
        return time;
    }

    private String naviValidate() {
        String msg = null;
        if (StrUtil.isBlank(etNaviName.getText().toString())) {
            msg = getString(R.string.plz_input_nave_name);
        } else if (StrUtil.isBlank(etNaviNickName.getText().toString())) {
            msg = getString(R.string.plz_input_navi_aname);
        } else if (StrUtil.isBlank(etNaviDescription.getText().toString())) {
            msg = getString(R.string.plz_input_arrive_explain);
        } else if (StrUtil.isBlank(etNaviXPoint.getText().toString())) {
            msg = getString(R.string.plz_input_x_point);
        } else if (StrUtil.isBlank(etNaviYPoint.getText().toString())) {
            msg = getString(R.string.plz_input_y_point);
        } else if (StrUtil.isBlank(etNaviZPoint.getText().toString())) {
            msg = getString(R.string.plz_input_z_point);
        } else if (cbMusic.isChecked() && StrUtil.isBlank(tv_navi_music.getText().toString())) {
            msg = getString(R.string.plz_export_music);
        } else if (cbExplain.isChecked() && StrUtil.isBlank(tv_navi_explain.getText().toString())) {
            msg = getString(R.string.plz_export_explain);
        } else if (cbImage.isChecked() && StrUtil.isBlank(tv_navi_image_path.getText().toString())) {
            msg = getString(R.string.plz_export_gif);
        } else if (StrUtil.isNotBlank(tvWaitMin.getText().toString()) && Integer.parseInt(tvWaitMin.getText().toString()) >= 10) {
            msg = getString(R.string.wait_min_limit);
        } else if (StrUtil.isNotBlank(tvWaitSec.getText().toString()) && Integer.parseInt(tvWaitSec.getText().toString()) >= 60) {
            msg = getString(R.string.plz_input_wait_sec);
        }
        return msg;
    }

    private void removeGuideData() {
        if (naviBeanLists == null || naviBeanLists.size() <= 0) {
            Toast.makeText(this, R.string.no_navi_data_delete, Toast.LENGTH_LONG).show();
            return;
        }
        showNewRetailDialog(getString(R.string.delete), getString(R.string.is_delete_navi_data), new NewRetailDialog.OnDialogClickListener() {
            @Override
            public void yes() {
                refreshGuideView();
                dismissNewRetailDialog();
            }

            @Override
            public void no() {
                dismissNewRetailDialog();
            }
        });
    }

    private void refreshGuideView() {
        naviBeanLists.clear();
        mPresenter.saveGuideData(naviBeanLists);
        naviGuideListAdapter.setGuideList(naviBeanLists);
    }

    /**
     * 删除导航点数据
     */
    private void removeNaviData() {
        if (keyPointViews == null || keyPointViews.size() <= 0) {
            Toast.makeText(this, R.string.no_navi_data_delete, Toast.LENGTH_LONG).show();
            return;
        }
        if (index == -1) {
            Toast.makeText(this, R.string.plz_choose_navi_data, Toast.LENGTH_LONG).show();
            return;
        }
        int msdId = R.string.is_delete_navi_data;
        if (mPresenter.isGuideData(index)) {
            msdId = R.string.is_delete_using_navi_data;
        }
        showNewRetailDialog(getString(R.string.delete), getString(msdId), new NewRetailDialog.OnDialogClickListener() {
            @Override
            public void yes() {
                mPresenter.removeNaviPointByPosition(index);
                dismissNewRetailDialog();
            }

            @Override
            public void no() {
                dismissNewRetailDialog();
            }
        });
    }

    private void clearNaviData() {
        etNaviName.setText("");
        etNaviNickName.setText("");
        etNaviDescription.setText("");
        setEditEnable(etNaviDescription, true);
        etNaviDescription.removeTextChangedListener(watcher);
        etNaviDescription.addTextChangedListener(textChangedWatch(etNaviDescription, 16, getString(R.string.navi_arrive_length_limit)));
        etNaviStartTip.setText("");
        setEditEnable(etNaviStartTip, true);
        etNaviStartTip.removeTextChangedListener(naviStartWatcher);
        etNaviStartTip.addTextChangedListener(naviStartWatch(etNaviStartTip, 16, getString(R.string.navi_start_length_limit)));
        etNaviArrive.setText("");
        setEditEnable(etNaviArrive, true);
        etNaviArrive.removeTextChangedListener(naviArriveWatcher);
        etNaviArrive.addTextChangedListener(naviArriveWatch(etNaviArrive, 16, getString(R.string.navi_arrive_tip_length_limit)));
        etNaviXPoint.setText("");
        etNaviYPoint.setText("");
        etNaviZPoint.setText("");
        cbMusic.setChecked(false);
        cbExplain.setChecked(false);
        cbImage.setChecked(false);
        tv_navi_music.setText("");
        tv_navi_explain.setText("");
        tv_navi_image_path.setText("");
        tvWaitMin.setText("");
        tvWaitSec.setText("");
    }

    private void setTabTextAndBG(int id) {
        if (id == R.id.btn_add_navi) {
            naviType = NaviType.NAVISTATE;
            btnAddNavi.setVisibility(View.GONE);
            btnAddNaviSelected.setVisibility(View.VISIBLE);
            btnAddYingBin.setVisibility(View.VISIBLE);
            btnAddYingBinSelected.setVisibility(View.GONE);
            btnAddGuide.setVisibility(View.VISIBLE);
            btnAddGuideSelected.setVisibility(View.GONE);
            llNavi.setVisibility(View.VISIBLE);
            llYingBin.setVisibility(View.GONE);
            llGuide.setVisibility(View.GONE);
            llSaveBtn.setVisibility(View.GONE);
            llSaveDeleteBtn.setVisibility(View.VISIBLE);
        } else if (id == R.id.btn_add_yingbin) {
            naviType = NaviType.YINGBINSTATE;
            btnAddNavi.setVisibility(View.VISIBLE);
            btnAddNaviSelected.setVisibility(View.GONE);
            btnAddYingBin.setVisibility(View.GONE);
            btnAddYingBinSelected.setVisibility(View.VISIBLE);
            btnAddGuide.setVisibility(View.VISIBLE);
            btnAddGuideSelected.setVisibility(View.GONE);
            llNavi.setVisibility(View.GONE);
            llYingBin.setVisibility(View.VISIBLE);
            llGuide.setVisibility(View.GONE);
            llSaveBtn.setVisibility(View.VISIBLE);
            llSaveDeleteBtn.setVisibility(View.GONE);
        } else if (id == R.id.btn_add_guide) {
            naviType = NaviType.GUIDESTATE;
            btnAddNavi.setVisibility(View.VISIBLE);
            btnAddNaviSelected.setVisibility(View.GONE);
            btnAddYingBin.setVisibility(View.VISIBLE);
            btnAddYingBinSelected.setVisibility(View.GONE);
            btnAddGuide.setVisibility(View.GONE);
            btnAddGuideSelected.setVisibility(View.VISIBLE);
            llNavi.setVisibility(View.GONE);
            llYingBin.setVisibility(View.GONE);
            llGuide.setVisibility(View.VISIBLE);
            llSaveBtn.setVisibility(View.GONE);
            llSaveDeleteBtn.setVisibility(View.VISIBLE);
        }
    }

    private void back() {
        getTitleView().setBackListener(new TitleView.OnBackListener() {
            @Override
            public void onClick() {
                onClickBack();
            }
        });
    }

    private void onClickBack() {
        isBack = true;
        if (naviType == NaviType.NAVISTATE) {
            if (StrUtil.isNotBlank(etNaviName.getText().toString()) || StrUtil.isNotBlank(etNaviDescription.getText().toString()) || StrUtil.isNotBlank(etNaviNickName.getText().toString())
                    || StrUtil.isNotBlank(etNaviXPoint.getText().toString()) || StrUtil.isNotBlank(etNaviYPoint.getText().toString()) || StrUtil.isNotBlank(etNaviStartTip.getText().toString())
                    || StrUtil.isNotBlank(etNaviZPoint.getText().toString()) || cbMusic.isChecked() || cbExplain.isChecked() || cbImage.isChecked()
                    || StrUtil.isNotBlank(tv_navi_music.getText().toString()) || StrUtil.isNotBlank(tv_navi_explain.getText().toString()) || StrUtil.isNotBlank(etNaviArrive.getText().toString())
                    || StrUtil.isNotBlank(tv_navi_image_path.getText().toString()) || StrUtil.isNotBlank(tvWaitMin.getText().toString()) || StrUtil.isNotBlank(tvWaitSec.getText().toString())) {
                SaveNaviDialog();
            } else {
                finish();
            }
        } else if (naviType == NaviType.YINGBINSTATE) {
            String json = SharedPreUtil.getString(SharedKey.YINGBIN_NAME, SharedKey.YINGBIN_KEY);
            List<Position> positionList = GsonUtils.jsonToObject(json, new TypeToken<List<Position>>() {
            }.getType());

            if ((StrUtil.isNotBlank(etYingbinXPoint.getText().toString()) || StrUtil.isNotBlank(etYingbinYPoint.getText().toString())
                    || StrUtil.isNotBlank(etYingbinZPoint.getText().toString()))
                    && (positionList == null || !positionList.get(0).getX().equals(etYingbinXPoint.getText().toString())
                    || !positionList.get(0).getY().equals(etYingbinYPoint.getText().toString())
                    || !positionList.get(0).getRotation().equals(etYingbinZPoint.getText().toString()))) {
                saveYingBinDialog();
            } else {
                finish();
            }
        } else if (naviType == NaviType.GUIDESTATE) {
            String json = SharedPreUtil.getString(SharedKey.GUIDE_NAME, SharedKey.GUIDE_KEY);
            List<NaviBean> guideLists = GsonUtils.jsonToObject(json, new TypeToken<List<NaviBean>>() {
            }.getType());
            if (naviBeanLists != null && naviBeanLists.size() > 0 && (guideLists == null || guideLists.size() != naviBeanLists.size())) {
                saveGuideDialog();
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    private void saveGuideDialog() {
        showNewRetailDialog(getString(R.string.save), getString(R.string.save_guide_tip), new NewRetailDialog.OnDialogClickListener() {
            @Override
            public void yes() {
                saveGuideData();
                dismissNewRetailDialog();
            }

            @Override
            public void no() {
                finish();
                dismissNewRetailDialog();
            }
        });
    }

    private void saveYingBinDialog() {
        showNewRetailDialog(getString(R.string.save), getString(R.string.save_yingbin_tip), new NewRetailDialog.OnDialogClickListener() {
            @Override
            public void yes() {
                saveYingBinData();
                dismissNewRetailDialog();
            }

            @Override
            public void no() {
                finish();
                dismissNewRetailDialog();
            }
        });
    }

    private void SaveNaviDialog() {
        showNewRetailDialog(getString(R.string.save), getString(R.string.save_navi_tip), new NewRetailDialog.OnDialogClickListener() {
            @Override
            public void yes() {
                saveNaviData();
                dismissNewRetailDialog();
            }

            @Override
            public void no() {
                finish();
                dismissNewRetailDialog();
            }
        });
    }

    private String subString(String name) {
        String strName;
        if (name != null && !name.trim().equals("")) {
            if (name.length() > 5) {
                strName = name.substring(0, 4) + "...";
            } else {
                strName = name;
            }
        } else {
            strName = "";
        }
        return strName;
    }

    @Override
    protected void onPause() {
        if (isBack) {
            mPresenter.saveNaviData();
        }
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                onClickBack();
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setEditEnable(View view, boolean isEnable) {
        view.setFocusable(isEnable);
        view.setFocusableInTouchMode(isEnable);
    }

    //发送广播
    private void showBroadcast() {
        Intent intent = new Intent();
        intent.setAction(GlobalNoticeService.ACTION_CMD);
        intent.putExtra(GlobalNoticeService.ACTION_CMD, GlobalNoticeService.ACTION_SHOW);
        sendBroadcast(intent);
    }

    private void hideBroadcast() {
        Intent intent = new Intent();
        intent.setAction(GlobalNoticeService.ACTION_CMD);
        intent.putExtra(GlobalNoticeService.ACTION_CMD, GlobalNoticeService.ACTION_HIDE);
        sendBroadcast(intent);
    }

    private void stopGlobalNotice() {
        stopService(new Intent(NaviSettingActivity.this, GlobalNoticeService.class));
    }

    private void startGlobalNotice() {
        com.csjbot.blackgaga.model.http.factory.ServerFactory.createNoticeSerivce().getGlobalAnnouncement(ProductProxy.SN, new Observer<NoticeBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull NoticeBean noticeBean) {
                CsjlogProxy.getInstance().info("onNext:" + new Gson().toJson(noticeBean));
                if (noticeBean != null && noticeBean.getResult() != null) {
                    if (noticeBean.getResult().isEnabled()) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Date date = null;
                        try {
                            date = format.parse(noticeBean.getResult().getEndTime());
                        } catch (ParseException e) {
                            CsjlogProxy.getInstance().error("e:" + e.toString());
                        }
                        long endTime = date.getTime();
                        if (System.currentTimeMillis() < endTime) {
                            long showTime = endTime - System.currentTimeMillis();
                            Intent intent = new Intent(NaviSettingActivity.this, GlobalNoticeService.class);
                            intent.putExtra("noticeText", noticeBean.getResult().getText());
                            intent.putExtra("showTime", showTime);
                            startService(intent);
                        }
                    }
                }
                startService(new Intent(NaviSettingActivity.this, GlobalNoticeService.class));
            }

            @Override
            public void onError(@NonNull Throwable e) {
                CsjlogProxy.getInstance().error("e:" + e.toString());
            }

            @Override
            public void onComplete() {

            }
        });
    }


}
