package com.csjbot.blackgaga.feature.settings;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.bean.RobotStateBean;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;
import com.csjbot.blackgaga.widget.TableView;
import com.csjbot.coshandler.listener.OnWarningCheckSelfListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by jingwc on 2018/4/12.
 */
public class SettingsRobotStateActivity extends BaseModuleActivity {

    @BindView(R.id.table_view)
    TableView table_view;

    @BindView(R.id.table_layout)
    TableLayout table_layout;

    @BindView(R.id.tv_header1)
    TextView tv_header1;
    @BindView(R.id.tv_header2)
    TextView tv_header2;
    @BindView(R.id.tv_header3)
    TextView tv_header3;
    @BindView(R.id.tv_header4)
    TextView tv_header4;
    @BindView(R.id.tv_header5)
    TextView tv_header5;

    private List<RobotStateBean> mRobotStates;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)|| BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))                ?  R.layout.vertical_activity_settings_robot_state : R.layout.activity_settings_robot_state;
    }

    private Map<String, String> englishToChinese = new HashMap<>();

    @Override
    public void init() {
        super.init();
        initMap();
        ServerFactory.getRobotState().checkSelf();
        RobotManager.getInstance().addListener((OnWarningCheckSelfListener) json -> {
            getLog().info("robot_state:" + json);
            String listJson = "";
            try {
                listJson = new JSONObject(json).getJSONArray("list").toString();
            } catch (JSONException e) {
                getLog().error("e:" + e.toString());
            }
            getLog().info("listJson:" + listJson);
            mRobotStates = new Gson().fromJson(listJson, new TypeToken<List<RobotStateBean>>() {
            }.getType());
            runOnUiThread(this::setData);
        });


    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        getTitleView().setSettingsVisibility(View.GONE);
        getTitleView().setBackVisibility(View.VISIBLE);
    }

    private void initMap() {
        englishToChinese.put("mainboard", "主控板");
        englishToChinese.put("dcboard", "直流电机驱动板");
        englishToChinese.put("steboard", "步进电机驱动板");
        englishToChinese.put("powerboard", "电源管理板");
        englishToChinese.put("expboard", "表情控制板");
        englishToChinese.put("ampboard", "功放板");
        englishToChinese.put("micboard", "5/6MIC板");
        englishToChinese.put("ultsensor", "超声波传感器");
        englishToChinese.put("antsensor", "防跌落传感器");
        englishToChinese.put("tousensor", "触摸传感器");
        englishToChinese.put("ioasensor", "餐盘红外避障传感器");
        englishToChinese.put("pycsensor", "热释电传感器");
        englishToChinese.put("stopswitch", "急停开关");
        englishToChinese.put("leftmotor", "左轮电机");
        englishToChinese.put("rightmotor", "右轮电机");
        englishToChinese.put("lefthandmotor", "左手肘电机");
        englishToChinese.put("righthandmotor", "右手肘电机");
        englishToChinese.put("nodmotor", "点头电机");
        englishToChinese.put("shakemotor", "摇头电机");
        englishToChinese.put("lwingmotor", "左翅膀电机");
        englishToChinese.put("rwingmotor", "右翅膀电机");
        englishToChinese.put("lwmencoder", "左轮电机编码器");
        englishToChinese.put("rwmencoder", "右轮电机编码器");
        englishToChinese.put("battery", "电池");
        englishToChinese.put("navmodule", "导航模块");
    }

    public void setData() {
        tv_header1.setText(getString(R.string.title));
        tv_header2.setText(getString(R.string.standard));
        tv_header3.setText(getString(R.string.serial_number));
        tv_header4.setText(getString(R.string.running_state));
        tv_header5.setText(getString(R.string.firmware_version));
//        table_view.clearTableContents()
//                .setHeader(getString(R.string.title), getString(R.string.standard), getString(R.string.serial_number), getString(R.string.running_state), getString(R.string.firmware_version));
         if (mRobotStates != null && mRobotStates.size() > 0) {
            List<String[]> contents = new ArrayList<>();
            getLog().info("listJson:" + mRobotStates.size());
            for (RobotStateBean robotStateBean : mRobotStates) {
                String[] strs = new String[5];
                if (!TextUtils.isEmpty(robotStateBean.getType())) {
                    if (Constants.Language.isChinese()) {
                        String type = englishToChinese.get(robotStateBean.getType());
                        if (type == null) {
                            type = robotStateBean.getType();
                        }
                        robotStateBean.setType(type);
                    }
                } else {
                    robotStateBean.setType("N/A");
                }

                strs[0] = robotStateBean.getType();
                strs[1] = robotStateBean.getModel() != null ? robotStateBean.getModel() : "N/A";
                strs[2] = robotStateBean.getSerialnumber() != null ? robotStateBean.getSerialnumber() : "N/A";
                strs[3] = robotStateBean.getState() != null ? robotStateBean.getState() : "N/A";
                strs[4] = robotStateBean.getFirmwareversion() != null ? robotStateBean.getFirmwareversion() : "N/A";

//                if(strs[2].length() > 20){
//                    String str = strs[2];
//                    strs[2] = str.substring(0,21)+"\r\n"+str.substring(20);
//                }
//                if(strs[4].length() > 20){
//                    String str = strs[4];
//                    strs[4] = str.substring(0,21)+"\r\n"+str.substring(20);
//                }

                contents.add(strs);
            }
//            table_view.addContents(contents);
             setTableLayoutData(contents);
        }
//        table_view.addContent("主控板","数据","数据","OKor NG","数据");
//        table_view.addContent("直流电机驱动板","数据","数据","OKor NG","数据");
//        table_view.addContent("步进电机驱动板","数据","数据","OKor NG","数据");
//        table_view.addContent("电源管理版","数据","数据","OKor NG","数据");
//        table_view.addContent("表情控制板","数据","数据","OKor NG","数据");
//        table_view.addContent("功放版","数据","数据","OKor NG","数据");
//        table_view.refreshTable();
    }

    private void setTableLayoutData(List<String[]> datas) {
        for (String[] strings : datas){
            TableRow tableRow = new TableRow(this);
            tableRow.setBackgroundColor(Color.WHITE);
            tableRow.setOrientation(LinearLayout.HORIZONTAL);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT,1.0f);
            lp.setMargins(0,20,0,20);
            lp.gravity = Gravity.CENTER_VERTICAL;
            for (String str : strings){
                TextView textView = new TextView(this);
                textView.setText(str);
                textView.setGravity(Gravity.CENTER);
                textView.setLayoutParams(lp);
                textView.setTextSize(18);
                tableRow.addView(textView);
            }
            lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,2);
            TextView line = new TextView(this);
            line.setBackgroundColor(Color.RED);
            line.setLayoutParams(lp);
            table_layout.addView(tableRow);
            table_layout.addView(line);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
    }
}
