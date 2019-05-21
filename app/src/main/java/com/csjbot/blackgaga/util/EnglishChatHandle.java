package com.csjbot.blackgaga.util;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jingwc on 2017/11/3.
 */

public class EnglishChatHandle {

    public static EnglishChatHandle newInstance(){
        return new EnglishChatHandle();
    }

    EnglishChatHandle(){
        readDefaultCustomData();
    }

    private static final double MAX_THREASHOLD = 1f;
    private static final double MIN_THREASHOLD = 0.8f;
    private static final double EACH_STAGE = 0.04f;

    Map<String,String> customData = new HashMap<>();

    private void readDefaultCustomData() {
        customData = new HashMap<>();
        customData.put("How are you", "I’m pretty good.");
        customData.put("How are you today", "Not bad, and you?");
        customData.put("How do you do", "How do you do");
        customData.put("How are you doing", "I am doing great!");
        customData.put("Good morning", "Good morning");
        customData.put("Good afternoon", "Good afternoon");
        customData.put("Good evening", "evening, you look good!");
        customData.put("What’s your name", "This is Snow baby, what can I do for you?");
        customData.put(" What’s the weather like today", "It’s sunny");
        customData.put("could you tell me how’s the weather today", "It’s cloudy");
        customData.put("What can you do", "I am a genius good at many things such as singing, dancing, eating and sleeping.");
        customData.put("Where do you come from", "I am a pure Chinese");
        customData.put("When were you born", "China!");
        customData.put("What did you know about China before you came here", "China is a great country that is developing very rapidly, I am a pure Chinese girl and I love my country.");
        customData.put("Are you a boy or girl", "Who cares?");
        customData.put("How old are you", "You will never guess.");
        customData.put("What are you doing", "In the middle of chatting with you.");
        customData.put("What are you up to", "Leave me alone, I am meditating.");
        customData.put("Are you busy", "In the middle of chatting with you.");
        customData.put("Thank you, you are cute", "You are welcome.");
        customData.put("Thank you, you are sweet", "It’s my pleasure");
        customData.put("Can you go home with me", "No thanks, I am loyal to my owner.");
        customData.put("Can I take you home", "No thanks, I am loyal to my owner.");
        customData.put("Who are you", "You will never guess who I am");
        customData.put("Nice to meet you", "Nice to meet you too.");
        customData.put("Are you single", "No, I crushed on a boy robot though I am only 2 years old.");
        customData.put("Do you speak English", "Yes, a little.");
        customData.put("Can  you speak English", "Yes, a little.");
        customData.put("You speak English pretty well.", "Oh, thank you");
        customData.put("Your English is very good.", "Oh, thank you");
        customData.put("Are you a native English speaker?", "No, my native language is Chinese. I speak English with strong robot accent, hope you like it.");
        customData.put("Are you a native speaker of English", "No, my native language is Chinese. I speak English with strong robot accent, hope you like it.");

    }

    public String getAnswer(String content) {
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        double threashold = MAX_THREASHOLD;

        String text = content.toUpperCase().replace(" ", "");

        // 匹配度从高到低依次匹配，直到＜0.8
        while (threashold > MIN_THREASHOLD) {
            for (String key : customData.keySet()) {
                double similarity = SimilarityUtil.sim(key.toUpperCase().replace(" ", ""), text);

                if (similarity >= threashold) {
                    return customData.get(key);
                }
            }

            threashold -= EACH_STAGE;
        }
        return null;
    }
}
