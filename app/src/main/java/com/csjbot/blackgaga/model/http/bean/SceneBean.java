package com.csjbot.blackgaga.model.http.bean;

/**
 * @author : chenqi.
 * @e_mail : 1650699704@163.com.
 * @create_time : 2018/1/2.
 * @Package_name: BlackGaGa
 */

public class SceneBean {

    /**
     * message : ok
     *  status : 200
     * result : {"theme":"xiaoqingxin","themename":"小清新","mainPage":{},"check":"ABCDEF12345678",
     * "resUrl":"http://xxxxxx","thumbnail":"http://xxxxxx","industry":"food","industryname":"食品"}
     */

    private String message;
    private String status;
    private ResultBean result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * theme : xiaoqingxin
         * themename : 小清新
         * mainPage : {}
         * check : ABCDEF12345678
         * resUrl : http://xxxxxx
         * thumbnail : http://xxxxxx
         * industry : food
         * industryname : 食品
         */

        private String theme;
        private String themename;
        private MainPageBean mainPage;
        private String check;
        private String resUrl;
        private String thumbnail;
        private String industry;
        private String industryname;

        public String getTheme() {
            return theme;
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }

        public String getThemename() {
            return themename;
        }

        public void setThemename(String themename) {
            this.themename = themename;
        }

        public MainPageBean getMainPage() {
            return mainPage;
        }

        public void setMainPage(MainPageBean mainPage) {
            this.mainPage = mainPage;
        }

        public String getCheck() {
            return check;
        }

        public void setCheck(String check) {
            this.check = check;
        }

        public String getResUrl() {
            return resUrl;
        }

        public void setResUrl(String resUrl) {
            this.resUrl = resUrl;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getIndustry() {
            return industry;
        }

        public void setIndustry(String industry) {
            this.industry = industry;
        }

        public String getIndustryname() {
            return industryname;
        }

        public void setIndustryname(String industryname) {
            this.industryname = industryname;
        }

        public static class MainPageBean {
            private String uri;
            public String getUri() {
                return uri;
            }

            public void setUrl(String uri) {
                this.uri = uri;
            }
        }
    }
}
