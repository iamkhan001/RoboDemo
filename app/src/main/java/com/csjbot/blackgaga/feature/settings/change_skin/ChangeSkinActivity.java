package com.csjbot.blackgaga.feature.settings.change_skin;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.util.CustomSDCardLoader;

import java.util.ArrayList;

import butterknife.BindView;
import skin.support.SkinCompatManager;

import static com.baidu.mapapi.BMapManager.getContext;

/**
 * Created by 孙秀艳 on 2017/12/19.
 */

public class ChangeSkinActivity extends BaseModuleActivity {

    @BindView(R.id.skin_recycler_view)
    RecyclerView recyclerView;

    ArrayList<SkinBean> skinList = new ArrayList<>();
    ChangeSkinAdapter changeSkinAdapter;
    View show_no_data;
    boolean isDefault = false;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)|| BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))                ?  R.layout.vertical_activity_setting_change_skin : R.layout.activity_setting_change_skin;

    }

    @Override
    public void init() {
        super.init();
        initSkinData();
        showSkinIndicator();
        initRecycleView();
    }

    /**
     * 初始化皮肤数据
     */
    private void initSkinData() {
        SkinBean bean = new SkinBean();
        bean.setSkinName("新零售");
        bean.setSkinPackage("default");
        bean.setDrawableId(R.drawable.add_shop_cart_ico);
        SkinBean bean1 = new SkinBean();
        skinList.add(bean);
        bean1.setSkinName("检察院");
        bean1.setSkinPackage("white.skin");
        bean1.setDrawableId(R.drawable.settings_about);
        skinList.add(bean1);
    }

    private void initRecycleView() {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setHasFixedSize(true);
        changeSkinAdapter = new ChangeSkinAdapter(this, skinList);
        showNoDataView();
        recyclerView.setAdapter(changeSkinAdapter);
        changeSkinAdapter.registerAdapterDataObserver(observer);
        observer.onChanged();
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        changeSkinAdapter.setOnRecyclerViewItemClickListener(new ChangeSkinAdapter.OnRecyclerViewItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                Log.e("sunxy", position+"");
                if (skinList !=null && skinList.size() > position) {
                    if (skinList.get(position).getSkinName().equals("新零售")) {
                        SkinCompatManager.getInstance().restoreDefaultTheme();
                        isDefault = true;
                        Toast.makeText(ChangeSkinActivity.this, "换肤成功", Toast.LENGTH_SHORT).show();
                        showSkinIndicator();
//                        jumpActivity(HomeActivity.class);
                        changeSkinAdapter.notifyDataSetChanged();
                    } else if (skinList.get(position).getSkinName().equals("检察院")) {
                        SkinCompatManager.getInstance().loadSkin("white.skin", new SkinCompatManager.SkinLoaderListener() {
                            @Override
                            public void onStart() {
                                Toast.makeText(ChangeSkinActivity.this, "换肤开始", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess() {
                                isDefault =false;
                                Toast.makeText(ChangeSkinActivity.this, "换肤成功", Toast.LENGTH_SHORT).show();
                                showSkinIndicator();
//                                jumpActivity(HomeActivity.class);
                                changeSkinAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onFailed(String s) {
                                Toast.makeText(ChangeSkinActivity.this, "换肤失败"+s, Toast.LENGTH_SHORT).show();
                            }
                        }, CustomSDCardLoader.SKIN_LOADER_STRATEGY_SDCARD);
                    }
                }
            }
        });
    }

    private void showSkinIndicator() {
        String curSkinName = SkinCompatManager.getInstance().getCurSkinName();
        if (curSkinName.equals("") || isDefault) {
            curSkinName = "default";
        }
        if (skinList != null && skinList.size() > 0) {
            for (int i=0; i< skinList.size(); i++) {
                skinList.get(i).setChecked(false);
                if (skinList.get(i).getSkinPackage().equals(curSkinName)) {
                    skinList.get(i).setChecked(true);
                }
            }
        }
    }

    @Override
    protected CharSequence initChineseSpeakText() {
        return "请选择你需要的换肤主题";
    }

    @Override
    protected CharSequence initEnglishSpeakText() {
        return "Please choose the skin change subject you need";
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
    public void onStop() {
        super.onStop();
        try {
            changeSkinAdapter.unregisterAdapterDataObserver(observer);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void showNoDataView() {
        //构建子view想要显示的参数
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        //加载没有数据的view
        show_no_data = getLayoutInflater().inflate(R.layout.no_data_show, null);
        Button button = (Button) show_no_data.findViewById(R.id.in_data_setting);
        TextView textView = (TextView) show_no_data.findViewById(R.id.body_no_data);
        textView.setText(R.string.no_product);
        button.setVisibility(View.GONE);
        ((ViewGroup) recyclerView.getRootView()).addView(show_no_data);
    }

    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {//设置空view原理都一样，没有数据显示空view，有数据隐藏空view
            if (changeSkinAdapter.getItemCount() == 0) {
                show_no_data.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                show_no_data.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            onChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            onChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            onChanged();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            onChanged();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            onChanged();
        }
    };
}
