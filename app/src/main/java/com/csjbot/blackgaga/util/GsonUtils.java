package com.csjbot.blackgaga.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by jingwc on 2017/8/14.
 */

public class GsonUtils {

    public static <T> T jsonToObject(String json, Class<T> c){
        return new Gson().fromJson(json,c);
    }
    public static <T> T jsonToObject(String json,Type type){
        return new Gson().fromJson(json,type);
    }

    public static String objectToJson(Object src){
        return new Gson().toJson(src);
    }
    public static String objectToJson(Object src,String... filterNames){
        return new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                for (String filterName : filterNames){
                    if(f.getName().contains(filterName)){
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        }).create().toJson(src);
    }
}
