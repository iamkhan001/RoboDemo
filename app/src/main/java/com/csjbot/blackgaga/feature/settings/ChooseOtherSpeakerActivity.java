package com.csjbot.blackgaga.feature.settings;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.util.CSJToast;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.coshandler.log.Csjlogger;
import com.csjbot.coshandler.tts.GoogleSpechImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;

public class ChooseOtherSpeakerActivity extends BaseModuleActivity {
    @BindView(R.id.speaker_listView)
    ListView speaker_listView;

    class OtherSpeaker {
        String showName;
        String realName;
        String speakString;
        String voice_country;
        boolean isChoosen;
    }


    class SpeakViewAdapter extends BaseAdapter {
        private LayoutInflater inflater = LayoutInflater.from(ChooseOtherSpeakerActivity.this);

        @Override
        public int getCount() {
            return speakers.size();
        }

        @Override
        public OtherSpeaker getItem(int position) {
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

            OtherSpeaker speaker = getItem(position);

            rollName.setText(speaker.showName);
            speakWord.setText(speaker.speakString);
            checkBox.setChecked(speaker.isChoosen);

            playIcon.setOnClickListener(v -> {

                country = speaker.voice_country;
                voiceSpeaker = speaker.realName;
                CsjlogProxy.getInstance().debug("tempCountry {}", country);
                CsjlogProxy.getInstance().debug("tempVoice {}", voiceSpeaker);
                CsjlogProxy.getInstance().debug("language {}", language);

                Intent intent = new Intent(TextToSpeech.Engine.ACTION_GET_SAMPLE_TEXT);
                intent.putExtra("language", language);
                intent.putExtra("country", country);


                startActivityForResult(intent, 20);
            });

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                for (OtherSpeaker OtherSpeaker : speakers) {
                    OtherSpeaker.isChoosen = false;
                }

                speaker.isChoosen = true;
                notifyDataSetChanged();

                if (SharedPreUtil.putString(SharedKey.SPEAKERVOICE, language_key, Constants.Language.getISOLanguage())
                        && SharedPreUtil.putString(SharedKey.SPEAKERVOICE, country_key, speaker.voice_country)
                        && SharedPreUtil.putString(SharedKey.SPEAKERVOICE, speaker_key, speaker.realName)) {
                    CSJToast.showToast(ChooseOtherSpeakerActivity.this, getString(R.string.save_success));
                }
            });

            return convertView;
        }
    }

    private List<OtherSpeaker> speakers = new ArrayList<>();

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    protected void onPause() {
        Robot.getInstance().stopSpeaking();
        Robot.getInstance().setLanguage(new Locale(language, country));
        Robot.getInstance().setSpeakerName(voiceSpeaker);

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

    private String language;
    private String country;
    private String voiceSpeaker;
    private GoogleSpechImpl googleTTS;
    private String language_key;
    private String country_key;
    private String speaker_key;

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
        googleTTS = GoogleSpechImpl.newInstance(this);


        language_key = Constants.Language.getISOLanguage() + GoogleSpechImpl.TTS_LANGUAGE_NAME;
        country_key = Constants.Language.getISOLanguage() + GoogleSpechImpl.TTS_COUNTRY_NAME;
        speaker_key = Constants.Language.getISOLanguage() + GoogleSpechImpl.TTS_VOICE_NAME;
        language = SharedPreUtil.getString(SharedKey.SPEAKERVOICE, language_key, Constants.Language.getISOLanguage());
        country = SharedPreUtil.getString(SharedKey.SPEAKERVOICE, country_key, "");
        voiceSpeaker = SharedPreUtil.getString(SharedKey.SPEAKERVOICE, speaker_key, "");

        OtherSpeaker speaker;

        CsjlogProxy.getInstance().debug("language {}", language);
        CsjlogProxy.getInstance().debug("country {}", country);
        CsjlogProxy.getInstance().debug("voiceSpeaker {}", voiceSpeaker);


        String[] counrtysInLanguage = getVoiceCountryByLan(language);
        CsjlogProxy.getInstance().debug("counrtysInLanguage {}", Arrays.asList(counrtysInLanguage).toString());


        for (String s : counrtysInLanguage) {
            ArrayList<String> languageSpeaker;
            try{
                languageSpeaker = Robot.getInstance().getSpeakerNames(language, s);
            }catch (Exception e){
                this.finish();
                return;
            }
            for (int i = 0; i < languageSpeaker.size(); i++) {
                speaker = new OtherSpeaker();
                speaker.showName = languageSpeaker.get(i);
                speaker.realName = languageSpeaker.get(i);
                speaker.voice_country = s;

                Csjlogger.debug("speaker.showName = {}", speaker.showName);

                if (speaker.realName.equals(voiceSpeaker)) {
                    speaker.isChoosen = true;
                }

                speakers.add(speaker);
            }

        }

        getTitleView().setSettingsPageBackVisibility(View.VISIBLE);
        initListView();

//        speak("请勾选您需要的TTS发声人");
    }

    //realName = de-de-x-nfh#female_1-local
    //realName = it-it-x-kda#female_3-local
    private String[] getVoiceCountryByLan(String language) {

        Map<String, String> counrtyForLanguage = new HashMap<>();
        counrtyForLanguage.put("da", "DK");
        counrtyForLanguage.put("uk", "UA");
        counrtyForLanguage.put("ru", "RU");
        counrtyForLanguage.put("si", "LK");
        counrtyForLanguage.put("hu", "HU");
        counrtyForLanguage.put("hi", "IN");
        counrtyForLanguage.put("in", "ID");
        counrtyForLanguage.put("tr", "TR");
        counrtyForLanguage.put("ne", "NP");
        counrtyForLanguage.put("el", "GR");
        counrtyForLanguage.put("de", "DE");
        counrtyForLanguage.put("it", "IT");
        counrtyForLanguage.put("nb", "NO");
        counrtyForLanguage.put("cs", "CZ");
        counrtyForLanguage.put("sk", "SK");
        counrtyForLanguage.put("ja", "JP");
        counrtyForLanguage.put("fr", "FR");
        counrtyForLanguage.put("pl", "PL");
        counrtyForLanguage.put("th", "TH");
        counrtyForLanguage.put("et", "EE");
        counrtyForLanguage.put("sv", "SE");
        counrtyForLanguage.put("ro", "RO");
        counrtyForLanguage.put("fi", "FI");
        counrtyForLanguage.put("nl", "NL");
        counrtyForLanguage.put("tl", "PH");
        counrtyForLanguage.put("pt", "BR");
        counrtyForLanguage.put("vi", "VN");
        counrtyForLanguage.put("ko", "KR");
        counrtyForLanguage.put("km", "KH");

        if (language.equalsIgnoreCase("en")) {
            return new String[]{"IN",
                    "US",
                    "AU",
                    "GB"};
        } else if (language.equalsIgnoreCase("es")) {
            return new String[]{"ES",
                    "US"};
        } else if (language.equalsIgnoreCase("bn")) {
            return new String[]{"IN",
                    "BD"};
        }

        return new String[]{counrtyForLanguage.get(language)};
    }

    private void initListView() {
        SpeakViewAdapter adapter = new SpeakViewAdapter();
        speaker_listView.setAdapter(adapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data == null){
            return;
        }
        Bundle bundle = data.getExtras();
        String text = null;

        if (bundle != null) {
            text = bundle.getString(TextToSpeech.Engine.EXTRA_SAMPLE_TEXT, "");
        }

        googleTTS.setLanguage(new Locale(language, country));
        googleTTS.setSpeakerName(voiceSpeaker);
        googleTTS.startSpeaking(text, null);


//        tts.setLanguage(defalutLocale);
//        tts.setVoice(defalutLocale, localeVoiceMap.get(defalutLocale) == null ? "" : localeVoiceMap.get(defalutLocale));


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
