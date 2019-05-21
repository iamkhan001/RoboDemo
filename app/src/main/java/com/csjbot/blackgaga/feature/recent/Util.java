package com.csjbot.blackgaga.feature.recent;

import android.os.Environment;

import com.csjbot.blackgaga.feature.recent.entity.HotelActivityBean;
import com.csjbot.blackgaga.util.FileUtils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author : chenqi.
 * @e_mail : 1650699704@163.com.
 * @create_time : 2018/3/1.
 * @Package_name: BlackGaGa
 */

public class Util {

    /*图片下载地址*/
    public static final String HOTELLOADURL = Environment.getExternalStorageDirectory() +
            File.separator + "csjbot" + File.separator + "hotel" + File.separator;

    public static String BASEPATH = HOTELLOADURL;
    /*U盘地址*/
    public static final String UPANPATH = "/mnt/usb_storage/csjbot_data/";

    public static List<HotelActivityBean> getData() {
        int type = FileUtils.copyUToSdCard(UPANPATH, HOTELLOADURL);
        List<HotelActivityBean> beanList = new ArrayList<>();
        List<String> beanString;
        switch (type) {
            case 1:
                BASEPATH = HOTELLOADURL;
                beanString = FileUtils.readDirByTxt(BASEPATH + "hotel_activities.txt");
                beanList = getDataList(beanString);
                break;
            case 0:
                BASEPATH = UPANPATH;
                beanString = FileUtils.readDirByTxt(BASEPATH + "hotel_activities.txt");
                beanList = getDataList(beanString);
                break;
            case -1:
                BASEPATH = HOTELLOADURL;
                beanString = FileUtils.readDirByTxt(BASEPATH + "hotel_activities.txt");
                beanList = getDataList(beanString);
                break;
        }
        return beanList;
    }

    public static List<HotelActivityBean> getDataList(List<String> listOriginal) {
        String split = "\t";
        List<String> list = new ArrayList<>();
        List<HotelActivityBean.HotelResultBean> body;
        List<HotelActivityBean> result = new ArrayList<>();

        if (listOriginal.size() == 0) {
            System.out.println("没有数据 谢谢！");
            return new ArrayList<>();
        }

        for (int i = 0; i < listOriginal.size(); i++) {
            String halfToString = BCConvert.toHalf(listOriginal.get(i));
            list.add(halfToString);
        }

        list = updateFormatAndSortByDate(list);

        String index;
        for (int i = 0; i < list.size(); i++) {
            String[] arrays = list.get(i).split(split);
            if (arrays.length == 9 && isEffectiveDate(arrays[0] + " " + arrays[2])) {
                index = arrays[0];
                body = new ArrayList<>();
                for (int j = 0; j < list.size(); j++) {
                    String[] _data = list.get(j).split(split);
                    if (index.equals(_data[0]) && isEffectiveDate(_data[0] + " " + _data[2])) {
                        body.add(addHotelActivity(_data));
                    }
                }
                //排序
                Collections.sort(body, (o1, o2) -> {
                    int i1 = dateToStamp(o1.getStartData(), null) - dateToStamp(o2.getStartData(), null);
                    if (i1 == 0) {
                        return dateToStamp(o1.getEndData(), null) - dateToStamp(o2.getEndData(), null);
                    }
                    return i1;
                });
                HotelActivityBean bean = addHotelBean(arrays[0], body);
                if (result.size() == 0) {
                    result.add(bean);
                } else {
                    for (int j = 0; j < result.size(); j++) {
                        if (result.get(j).getData().equals(arrays[0])) {
                            break;
                        } else if (j == result.size() - 1) {
                            result.add(bean);
                            break;
                        }
                    }
                }
            }
        }

        return result;
    }

    public static HotelActivityBean addHotelBean(String data, List<HotelActivityBean.HotelResultBean> re) {
        HotelActivityBean bean = new HotelActivityBean();
        bean.setBean(re);
        bean.setData(data);
        return bean;
    }

    /*
   * 将时间转换为时间戳
   */
    public static int dateToStamp(String s, String format) {
        String more = "HH:mm";
        if (format != null) {
            more = format;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(more);
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (int) date.getTime();
    }


    /**
     * 把活动内容添加进去
     *
     * @param arrays
     * @return
     */
    public static HotelActivityBean.HotelResultBean addHotelActivity(String[] arrays) {
        HotelActivityBean.HotelResultBean hotelActivitiesBean = new HotelActivityBean.HotelResultBean();
        hotelActivitiesBean.setStartData(arrays[1].replace("\"", ""));
        hotelActivitiesBean.setEndData(arrays[2].replace("\"", ""));
        hotelActivitiesBean.setPlace(arrays[3].replace("\"", ""));
        hotelActivitiesBean.setExhPhotos(arrays[4].replace("\"", ""));
        hotelActivitiesBean.setNaviPhoto(arrays[5].replace("\"", ""));
        hotelActivitiesBean.setTitle(arrays[6].replace("\"", ""));
        hotelActivitiesBean.setKeyWord(arrays[7].replace("\"", ""));
        hotelActivitiesBean.setNaviString(arrays[8].replace("\"", ""));
        return hotelActivitiesBean;
    }

    /**
     * 过滤活动时间已经过了的数据
     *
     * @param data
     * @return
     */
    public static boolean isEffectiveDate(String data) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String now = simpleDateFormat.format(System.currentTimeMillis());
        try {
            if (simpleDateFormat.parse(data).getTime() > simpleDateFormat.parse(now).getTime()) {
                return true;
            } else return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 切换时间格式
     *
     * @param list
     * @return
     */
    public static List<String> updateFormatAndSortByDate(List<String> list) {
        List<String> newResult = new ArrayList<>();
        for (int i = 1; i < list.size(); i++) {
            System.out.println("index 错误数据是" + list.get(i));
            System.out.println("index 错误数据是" + list.get(i).trim().length());
            String result = list.get(i);
            if (result.trim().length() != 0) {
                String date = result.split("\t")[0];
                newResult.add(result.replace(date, replaceDate(date)));
            } else {
                continue;
            }
        }
        return newResult;
    }

    /**
     * 将时间格式转换
     *
     * @return
     */
    public static String replaceDate(String msg) {
        String date1 = "\\d{4}-\\d{1,2}-\\d{1,2}";//2018-01-02
        String date2 = "\\d{4}[年]\\d{1,2}[月]\\d{1,2}[日]";//2018年01月01日
        String date3 = "\\d{4}[\\s+]\\d{1,2}[\\s+]\\d{1,2}";//2018 01 02
        String date4 = "\\d{4}[/]\\d{1,2}[/]\\d{1,2}";//2018/01/02
        String result;
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (Pattern.compile(date1).matcher(msg).matches()) {
                return msg;
            } else if (Pattern.compile(date2).matcher(msg).matches()) {
                result = msg.replace("年", "-").replace("月", "-").replace("日", "");
                result = sdf3.format(sdf3.parse(result));
                return result;
            } else if (Pattern.compile(date3).matcher(msg).matches()) {
                result = msg.replace("[\\s]", "-");
                result = sdf3.format(sdf3.parse(result));
                return result;
            } else if (Pattern.compile(date4).matcher(msg).matches()) {
                result = msg.replace("/", "-");
                result = sdf3.format(sdf3.parse(result));
                return result;
            } else {
                return msg;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return msg;
        }
    }

}
