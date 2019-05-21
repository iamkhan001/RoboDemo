package com.csjbot.blackgaga.util;

import android.os.Environment;
import android.text.TextUtils;

import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.network.IOCloseUtil;
import com.csjbot.coshandler.listener.OnSNListener;
import com.csjbot.coshandler.log.Csjlogger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Created by xiasuhuei321 on 2017/10/25.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 * <p>
 * desc:用来获取Sn的工具类
 */

public class ConfInfoUtil {
    private static String CONFINFO = getConfInfo();
    private static volatile String SN;

    public static void init() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String path = Environment.getExternalStorageDirectory().getPath();
            path += File.separator + ".robot_info";

            File f = new File(path);
            if (!f.exists()) f.mkdirs();

            String filePath = path + File.separator + ".sys.txt";
            File confFile = new File(filePath);

            if (!confFile.exists()) try {
                confFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        RobotManager.getInstance().addListener(new OnSNListener() {
            @Override
            public void response(String sn) {
                try {
                    JSONObject jo = new JSONObject(sn);
                    String str = jo.optString("sn");
                    if (TextUtils.isEmpty(str) || str.contains("empty")) {
                        // sn 未能获取到
                        BlackgagaLogger.debug("未能获取到SN信息");
                    } else {
                        BlackgagaLogger.debug("获取到SN信息");
                        ConfInfoUtil.putData(Constants.SN, str);
                        BlackgagaLogger.debug("从底层获取 SN 成功，SN 为：" + sn);
                        SN = str;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    BlackgagaLogger.debug("发生异常");
                }
//                putData(CONFINFO, SN);
            }
        });
        RobotManager.getInstance().robot.reqProxy.getSN();
    }

    private static String getConfPath() {
        return Environment.getExternalStorageDirectory().getPath() + File.separator +
                ".robot_info" + File.separator + ".sys.txt";
    }

    /**
     * 获取 SN ，此处仅仅用于从配置文件获取 SN ，中间有 IO 操作
     * 自行考虑是否将这个操作放在线程中
     *
     * @return null 如果没有 SN，否则是 SN 的字符串
     */
    public static String getSN() {
        return SN == null ? (String) getData(CONFINFO, Constants.SN) : SN;
    }

    public static Object getData(String name) {
        try {
            FileInputStream fis = new FileInputStream(getConfPath());
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            StringBuilder sb = new StringBuilder();
            String str;

            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            JSONObject jo;
            if (!TextUtils.isEmpty(sb)) {
                jo = new JSONObject(sb.toString());
                return jo.opt(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Object getData(String confJson, String name) {
        if (!TextUtils.isEmpty(confJson)) {
            try {
                JSONObject jo = new JSONObject(confJson);
                return jo.opt(name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return getData(name);
    }

    public static void putData(String name, String value) {
        JSONObject json = getConfJson();
        try {
            if (json == null) {
                json = new JSONObject();
            }
            json.put(name, value);
            saveConf(json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void putData(String name, int value) {
        JSONObject json = getConfJson();
        try {
            json.put(name, value);
            saveConf(json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void putData(String name, float value) {
        JSONObject json = getConfJson();
        try {
            json.put(name, value);
            saveConf(json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String getConfInfo() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(getConfPath());
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            StringBuilder sb = new StringBuilder();
            String str = null;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            CONFINFO = sb.toString();
            Csjlogger.debug(sb.toString());
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOCloseUtil.closeIO(fis);
        }

        return null;
    }

    public static JSONObject getConfJson() {
        String str = getConfInfo();
        if (TextUtils.isEmpty(str)) return null;
        try {
            return new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void saveConf(String conf) {
        Csjlogger.debug(conf);
        String path = getConfPath();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            fos.write(conf.getBytes());
            fos.flush();
            CONFINFO = conf;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOCloseUtil.closeIO(fos);
        }
    }

}
