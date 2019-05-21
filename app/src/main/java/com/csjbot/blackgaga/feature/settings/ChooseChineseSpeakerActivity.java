package com.csjbot.blackgaga.feature.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.util.CSJToast;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.listener.OnSpeakListener;
import com.iflytek.cloud.SpeechError;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ChooseChineseSpeakerActivity extends BaseModuleActivity {

    @BindView(R.id.speaker_listView)
    ListView speaker_listView;

    class ChineseSpeaker {
        String showName;
        String realName;
        String speakString;
        boolean isChoosen;
    }


    class SpeakViewAdapter extends BaseAdapter {
        private LayoutInflater inflater = LayoutInflater.from(ChooseChineseSpeakerActivity.this);

        @Override
        public int getCount() {
            return speakers.size();
        }

        @Override
        public ChineseSpeaker getItem(int position) {
            return speakers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.item_choose_chinese_speaker, parent, false);

            TextView rollName = convertView.findViewById(R.id.rollName);
            TextView speakWord = convertView.findViewById(R.id.speakWord);
            CheckBox checkBox = convertView.findViewById(R.id.chooseSpeakerCheckBox);
            ImageView playIcon = convertView.findViewById(R.id.playSpeaker);

            ChineseSpeaker speaker = getItem(position);

            rollName.setText(speaker.showName);
            speakWord.setText(speaker.speakString);
            checkBox.setChecked(speaker.isChoosen);

            playIcon.setOnClickListener(v -> {
                Robot.getInstance().setSpeakerName(speaker.realName);
                Robot.getInstance().startSpeaking(speaker.speakString, new OnSpeakListener(){

                    @Override
                    public void onSpeakBegin() {

                    }

                    @Override
                    public void onCompleted(SpeechError speechError) {
                        String storedSpeaker = SharedPreUtil.getString(SharedKey.SPEAKERVOICE, SharedKey.SPEAKER_KEY, SharedKey.DEFAULT_SPEAKER);
                        Robot.getInstance().setSpeakerName(storedSpeaker);
                    }
                });

            });

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                for (ChineseSpeaker chineseSpeaker : speakers) {
                    chineseSpeaker.isChoosen = false;
                }

                speaker.isChoosen = true;
                notifyDataSetChanged();

                if (SharedPreUtil.putString(SharedKey.SPEAKERVOICE, SharedKey.SPEAKER_KEY, speaker.realName)) {
                    CSJToast.showToast(ChooseChineseSpeakerActivity.this, "保存成功");
                }
            });

            return convertView;
        }
    }

    private List<ChineseSpeaker> speakers = new ArrayList<>();

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    protected void onPause() {
        Robot.getInstance().stopSpeaking();
        String storedSpeaker = SharedPreUtil.getString(SharedKey.SPEAKERVOICE, SharedKey.SPEAKER_KEY, SharedKey.DEFAULT_SPEAKER);
        Robot.getInstance().setSpeakerName(storedSpeaker);

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_HIDE));
    }

    @Override
    public void onBackPressed() {
        Robot.getInstance().stopSpeaking();
        String storedSpeaker = SharedPreUtil.getString(SharedKey.SPEAKERVOICE, SharedKey.SPEAKER_KEY, SharedKey.DEFAULT_SPEAKER);
        Robot.getInstance().setSpeakerName(storedSpeaker);

        super.onBackPressed();
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);

        String[] showNames = getResources().getStringArray(R.array.array_role_entries);
        String[] realNames = getResources().getStringArray(R.array.array_role_values);
        String[] speakStrings = getResources().getStringArray(R.array.array_role_testStrins);

        String storedSpeaker = SharedPreUtil.getString(SharedKey.SPEAKERVOICE, SharedKey.SPEAKER_KEY, SharedKey.DEFAULT_SPEAKER);

        try {
            for (int i = 0; i < showNames.length; i++) {
                ChineseSpeaker speaker = new ChineseSpeaker();
                speaker.showName = showNames[i];
                speaker.realName = realNames[i];
                speaker.speakString = speakStrings[i];
                if (speaker.realName.equals(storedSpeaker)) {
                    speaker.isChoosen = true;
                }

                speakers.add(speaker);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        getTitleView().setSettingsPageBackVisibility(View.VISIBLE);
        initListView();

        speak("请勾选您需要的TTS发声人");
    }

    private void initListView() {
        SpeakViewAdapter adapter = new SpeakViewAdapter();
        speaker_listView.setAdapter(adapter);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_chinese_speaker;
    }
}
