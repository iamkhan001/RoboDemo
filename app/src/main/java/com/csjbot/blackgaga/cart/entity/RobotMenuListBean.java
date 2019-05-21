package com.csjbot.blackgaga.cart.entity;

import java.io.Serializable;
import java.util.List;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/10/24.
 */

public class RobotMenuListBean implements Serializable {
    private static final long serialVersionUID = 1552437009093312803L;
    /**
     * message : ok
     * result : {"menulist":[{"aliasName":"RtSsDMNMa28wr6TFKkA3.jpg","botDataSource":"","botFunction":"","isdefaulthome":"0","language":"cn","level":2,"mImg":"http://csj-robot-test.oss-cn-shanghai.aliyuncs.com/menu/RtSsDMNMa28wr6TFKkA3.jpg","mURL":"http://test.csjbot.com:8080/api/pdt/getRobotSpListNew?sn={sn}&producttype=141&language=cn","menuName":"uuuuyyytt","menuid":"5b02e5fe18e04cb6919ec96feb85fe76","parentid":"1f4b1230f13e42fca6038357ebdd45df","serviceDataSource":"","serviceFunction":"","sn":""},{"aliasName":"eDjrdmGWYskdKZywB8wH.jpg","botDataSource":"","botFunction":"","isdefaulthome":"0","language":"cn","level":2,"mImg":"http://csj-robot-test.oss-cn-shanghai.aliyuncs.com/menu/eDjrdmGWYskdKZywB8wH.jpg","mURL":"http://test.csjbot.com:8080/api/pdt/getRobotSpListNew?sn={sn}&producttype=123&language=cn","menuName":"麻辣味","menuid":"641d4d1a494b4be69e89a16624679c19","parentid":"1f4b1230f13e42fca6038357ebdd45df","serviceDataSource":"","serviceFunction":"","sn":""},{"aliasName":"ecGz6daWwxD5em5YeRGS.jpg","botDataSource":"","botFunction":"","isdefaulthome":"0","language":"cn","level":2,"mImg":"http://csj-robot-test.oss-cn-shanghai.aliyuncs.com/menu/ecGz6daWwxD5em5YeRGS.jpg","mURL":"http://test.csjbot.com:8080/api/pdt/getRobotSpListNew?sn={sn}&producttype=138&language=cn","menuName":"fruit","menuid":"ae7f835901f341919ccb106c4d4da267","parentid":"1f4b1230f13e42fca6038357ebdd45df","serviceDataSource":"","serviceFunction":"","sn":""},{"aliasName":"kF2Ysa35JsikhkpcB235.jpg","botDataSource":"","botFunction":"","isdefaulthome":"0","language":"cn","level":2,"mImg":"http://csj-robot-test.oss-cn-shanghai.aliyuncs.com/menu/kF2Ysa35JsikhkpcB235.jpg","mURL":"http://test.csjbot.com:8080/api/pdt/getRobotSpListNew?sn={sn}&producttype=135&language=cn","menuName":"爆款推荐","menuid":"bb70f212b2344126b2394aa384df9ea6","parentid":"1f4b1230f13e42fca6038357ebdd45df","serviceDataSource":"","serviceFunction":"","sn":""},{"aliasName":"RwzEHWyDkiJA6PyzdQs3.jpg","botDataSource":"","botFunction":"","isdefaulthome":"0","language":"cn","level":2,"mImg":"http://csj-robot-test.oss-cn-shanghai.aliyuncs.com/menu/RwzEHWyDkiJA6PyzdQs3.jpg","mURL":"http://test.csjbot.com:8080/api/pdt/getRobotSpListNew?sn={sn}&producttype=124&language=cn","menuName":"甜味","menuid":"2a69df36a2ea401e8dcef082f69b08cc","parentid":"1f4b1230f13e42fca6038357ebdd45df","serviceDataSource":"","serviceFunction":"","sn":""},{"aliasName":"MfD5ThTM6JtzdQfYZZNP.jpg","botDataSource":"","botFunction":"","isdefaulthome":"0","language":"cn","level":2,"mImg":"http://csj-robot-test.oss-cn-shanghai.aliyuncs.com/menu/MfD5ThTM6JtzdQfYZZNP.jpg","mURL":"http://test.csjbot.com:8080/api/pdt/getRobotSpListNew?sn={sn}&producttype=125&language=cn","menuName":"变态辣","menuid":"62a157ad2282490d963ed83f0f55091f","parentid":"1f4b1230f13e42fca6038357ebdd45df","serviceDataSource":"","serviceFunction":"","sn":""},{"aliasName":"ayXfbRWE3bXnmCA3Qpfz.jpg","botDataSource":"","botFunction":"","isdefaulthome":"0","language":"cn","level":2,"mImg":"http://csj-robot-test.oss-cn-shanghai.aliyuncs.com/menu/ayXfbRWE3bXnmCA3Qpfz.jpg","mURL":"http://test.csjbot.com:8080/api/pdt/getRobotSpListNew?sn={sn}&producttype=134&language=cn","menuName":"Snow Robot","menuid":"786df044341e41d2985eeeb3ee099305","parentid":"1f4b1230f13e42fca6038357ebdd45df","serviceDataSource":"","serviceFunction":"","sn":""}]}
     * status : 200
     * timestamp :
     */

    private String message;
    private ResultBean result;
    private String status;
    private String timestamp;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public static class ResultBean {
        private List<MenulistBean> menulist;

        public List<MenulistBean> getMenulist() {
            return menulist;
        }

        public void setMenulist(List<MenulistBean> menulist) {
            this.menulist = menulist;
        }

        public static class MenulistBean {
            /**
             * aliasName : RtSsDMNMa28wr6TFKkA3.jpg
             * botDataSource :
             * botFunction :
             * isdefaulthome : 0
             * language : cn
             * level : 2
             * mImg : http://csj-robot-test.oss-cn-shanghai.aliyuncs.com/menu/RtSsDMNMa28wr6TFKkA3.jpg
             * mURL : http://test.csjbot.com:8080/api/pdt/getRobotSpListNew?sn={sn}&producttype=141&language=cn
             * menuName : uuuuyyytt
             * menuid : 5b02e5fe18e04cb6919ec96feb85fe76
             * parentid : 1f4b1230f13e42fca6038357ebdd45df
             * serviceDataSource :
             * serviceFunction :
             * sn :
             */

            private String aliasName;
            private String botDataSource;
            private String botFunction;
            private String isdefaulthome;
            private String language;
            private int level;
            private String mImg;
            private String mURL;
            private String menuName;
            private String menuid;
            private String parentid;
            private String serviceDataSource;
            private String serviceFunction;
            private String sn;

            public String getAliasName() {
                return aliasName;
            }

            public void setAliasName(String aliasName) {
                this.aliasName = aliasName;
            }

            public String getBotDataSource() {
                return botDataSource;
            }

            public void setBotDataSource(String botDataSource) {
                this.botDataSource = botDataSource;
            }

            public String getBotFunction() {
                return botFunction;
            }

            public void setBotFunction(String botFunction) {
                this.botFunction = botFunction;
            }

            public String getIsdefaulthome() {
                return isdefaulthome;
            }

            public void setIsdefaulthome(String isdefaulthome) {
                this.isdefaulthome = isdefaulthome;
            }

            public String getLanguage() {
                return language;
            }

            public void setLanguage(String language) {
                this.language = language;
            }

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }

            public String getMImg() {
                return mImg;
            }

            public void setMImg(String mImg) {
                this.mImg = mImg;
            }

            public String getMURL() {
                return mURL;
            }

            public void setMURL(String mURL) {
                this.mURL = mURL;
            }

            public String getMenuName() {
                return menuName;
            }

            public void setMenuName(String menuName) {
                this.menuName = menuName;
            }

            public String getMenuid() {
                return menuid;
            }

            public void setMenuid(String menuid) {
                this.menuid = menuid;
            }

            public String getParentid() {
                return parentid;
            }

            public void setParentid(String parentid) {
                this.parentid = parentid;
            }

            public String getServiceDataSource() {
                return serviceDataSource;
            }

            public void setServiceDataSource(String serviceDataSource) {
                this.serviceDataSource = serviceDataSource;
            }

            public String getServiceFunction() {
                return serviceFunction;
            }

            public void setServiceFunction(String serviceFunction) {
                this.serviceFunction = serviceFunction;
            }

            public String getSn() {
                return sn;
            }

            public void setSn(String sn) {
                this.sn = sn;
            }
        }
    }
}