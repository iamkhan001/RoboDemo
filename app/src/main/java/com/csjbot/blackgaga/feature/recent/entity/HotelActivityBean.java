package com.csjbot.blackgaga.feature.recent.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author : chenqi.
 * @e_mail : 1650699704@163.com.
 * @create_time : 2018/3/1.
 * @Package_name: BlackGaGa
 */

public class HotelActivityBean implements Serializable {
    private static final long serialVersionUID = 5837411594337794229L;
    private String data;//日期
    private List<HotelResultBean> bean;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<HotelResultBean> getBean() {
        return bean;
    }

    public void setBean(List<HotelResultBean> bean) {
        this.bean = bean;
    }

    public static class HotelResultBean{
        private String startData;//开始日期
        private String endData;//结束日期
        private String place;//活动地点
        private String exhPhotos;//活动图片
        private String naviPhoto;//导航图片//
        private String title;//标题
        private String keyWord;//关键字
        private String naviString;//导航语句

        public String getStartData() {
            return startData;
        }

        public void setStartData(String startData) {
            this.startData = startData;
        }

        public String getEndData() {
            return endData;
        }

        public void setEndData(String endData) {
            this.endData = endData;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getExhPhotos() {
            return exhPhotos;
        }

        public void setExhPhotos(String exhPhotos) {
            this.exhPhotos = exhPhotos;
        }

        public String getNaviPhoto() {
            return naviPhoto;
        }

        public void setNaviPhoto(String naviPhoto) {
            this.naviPhoto = naviPhoto;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getKeyWord() {
            return keyWord;
        }

        public void setKeyWord(String keyWord) {
            this.keyWord = keyWord;
        }

        public String getNaviString() {
            return naviString;
        }

        public void setNaviString(String naviString) {
            this.naviString = naviString;
        }

        @Override
        public String toString() {
            return "HotelActivityBean startData='" + startData + '\'' +
                    ", endData='" + endData + '\'' +
                    ", place='" + place + '\'' +
                    ", exhPhotos=" + exhPhotos +
                    ", naviPhoto='" + naviPhoto + '\'' +
                    ", title='" + title + '\'' +
                    ", keyWord='" + keyWord + '\'' +
                    ", naviString='" + naviString + '\'' +
                    '}';
        }
    }
}
