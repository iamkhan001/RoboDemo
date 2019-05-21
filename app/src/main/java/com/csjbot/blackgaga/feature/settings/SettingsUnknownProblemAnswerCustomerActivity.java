package com.csjbot.blackgaga.feature.settings;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by 荆为成 on 2018/8/6.
 */

public class SettingsUnknownProblemAnswerCustomerActivity extends BaseModuleActivity {


    @BindView(R.id.listview)
    ListView listview;

    @BindView(R.id.et_answer)
    EditText et_answer;

    List<String> answers;

    MyAdapter myAdapter;

    int currentEditIndex = -1;

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);

    }

    @Override
    public void init() {
        super.init();
        getTitleView().setSettingsVisibility(View.GONE);
        getTitleView().setSettingsPageBackVisibility(View.VISIBLE);

        answers = new ArrayList<>();

        String customerAnswerJson = SharedPreUtil.getString(SharedKey.UNKNOWN_PROBLEM_ANSWER,SharedKey.UNKNOWN_PROBLEM_ANSWER_CUSTOMER_ANSWER);
        getLog().info("CstomerAnswerJson:"+customerAnswerJson);
        if(!TextUtils.isEmpty(customerAnswerJson)){
            answers = new Gson().fromJson(customerAnswerJson,new TypeToken<List<String>>(){}.getType());
        }

        myAdapter = new MyAdapter();
        listview.setAdapter(myAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                et_answer.setText(answers.get(i));
                currentEditIndex = i;
            }
        });
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))
                ? R.layout.vertical_activity_settings_unknown_problem_answer_customer : R.layout.activity_settings_unknown_problem_answer_customer;

    }

    @Override
    public boolean isOpenChat() {
        return false;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_HIDE));
    }

    @OnClick(R.id.bt_save)
    public void bt_save(){
        SharedPreUtil.putString(SharedKey.UNKNOWN_PROBLEM_ANSWER,SharedKey.UNKNOWN_PROBLEM_ANSWER_CUSTOMER_ANSWER,new Gson().toJson(answers));
        Constants.UnknownProblemAnswer.refreshCustomerAnswers(answers);
        Toast.makeText(context, getString(R.string.save_success), Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.bt_add)
    public void bt_add(){
        String answer = et_answer.getText().toString().trim();
        if(!TextUtils.isEmpty(answer)){
            answers.add(answer);
            myAdapter.notifyDataSetChanged();
            currentEditIndex = answers.size() - 1;
            Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.bt_update)
    public void bt_update(){
        String answer = et_answer.getText().toString().trim();
        if(!TextUtils.isEmpty(answer)){
            answers.set(currentEditIndex,answer);
            myAdapter.notifyDataSetChanged();
            Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
        }
    }


    @OnClick(R.id.bt_remove)
    public void bt_remove(){
        if(currentEditIndex > -1){
            answers.remove(currentEditIndex);
            myAdapter.notifyDataSetChanged();
            et_answer.setText("");
            currentEditIndex = -1;
            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
        }
    }

    class MyAdapter extends BaseAdapter {
        private LayoutInflater inflater = LayoutInflater.from(SettingsUnknownProblemAnswerCustomerActivity.this);

        @Override
        public int getCount() {
            return answers.size();
        }

        @Override
        public String getItem(int position) {
            return answers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            View view;
            if(viewHolder == null){
                view = inflater.inflate(R.layout.item_list_unknow_problem_answer_customer, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.tv_answer = view.findViewById(R.id.tv_answer);
                view.setTag(viewHolder);
            }else{
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.tv_answer.setText(answers.get(position));
            return view;
        }

        class ViewHolder{
            TextView tv_answer;
        }
    }

}
