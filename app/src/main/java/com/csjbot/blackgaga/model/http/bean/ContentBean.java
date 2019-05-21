package com.csjbot.blackgaga.model.http.bean;

import java.util.List;

/**
 * @author : chenqi.
 * @e_mail : 1650699704@163.com.
 * @create_time : 2018/1/26.
 * @Package_name: BlackGaGa
 */

public class ContentBean {
    /**
     * status : 200
     * message : ok
     * result : {"contentType":[{"effective":true,"id":"asdfwefw23xdf2","isLastLevel":false,"name":"酒店介绍","order":1,"layout":1,"resUrl":"http%3A%2F%2Fdev.csjbot.com%3A8080%2Fcsjbotservice%2Fapi%2FcontentMessage%3Fid%3Dasdfwefw23xdf2%26language%3Dzh_CN%26sn%3D060021001008%26layout%3D1","uri":"/new_retail/hotel_type/1","showInMainPage":1},{"effective":true,"id":"cvwefzdf23rgwed","isLastLevel":false,"name":"今日活动","order":1,"layout":2,"resUrl":"http%3A%2F%2Fdev.csjbot.com%3A8080%2Fcsjbotservice%2Fapi%2FcontentMessage%3Fid%3Dcvwefzdf23rgwed%26language%3Dzh_CN%26sn%3D060021001008%26layout%3D2","uri":"/new_retail/hotel_type/2","showInMainPage":1},{"effective":true,"id":"hawefzsdq1dfwef","isLastLevel":false,"name":"促销活动","order":1,"layout":1,"resUrl":"http%3A%2F%2Fdev.csjbot.com%3A8080%2Fcsjbotservice%2Fapi%2FcontentMessage%3Fid%3Dhawefzsdq1dfwef%26language%3Dzh_CN%26sn%3D060021001008%26layout%3D1","uri":"/new_retail/hotel_type/1","showInMainPage":1}]}
     */

    private String status;
    private String message;
    private ResultBean result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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

    public static class ResultBean {
        private List<ContentMessageBean> contentType;

        public List<ContentMessageBean> getContentType() {
            return contentType;
        }

        public void setContentType(List<ContentMessageBean> contentType) {
            this.contentType = contentType;
        }

        public static class ContentMessageBean {
            /**
             * effective : true
             * id : asdfwefw23xdf2
             * isLastLevel : false
             * name : 酒店介绍
             * order : 1
             * layout : 1
             * resUrl : http%3A%2F%2Fdev.csjbot.com%3A8080%2Fcsjbotservice%2Fapi%2FcontentMessage%3Fid%3Dasdfwefw23xdf2%26language%3Dzh_CN%26sn%3D060021001008%26layout%3D1
             * uri : /new_retail/hotel_type/1
             * showInMainPage : 1
             */

            private boolean effective;
            private String id;
            private boolean isLastLevel;
            private String name;
            private String words;
            private int order;
            private int layout;
            private String resUrl;
            private String uri;
            private int showInMainPage;

            public boolean isEffective() {
                return effective;
            }

            public void setEffective(boolean effective) {
                this.effective = effective;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public boolean isIsLastLevel() {
                return isLastLevel;
            }

            public void setIsLastLevel(boolean isLastLevel) {
                this.isLastLevel = isLastLevel;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }


            public String getWords() {
                return words;
            }

            public void setWords(String words) {
                this.words = words;
            }

            public int getOrder() {
                return order;
            }

            public void setOrder(int order) {
                this.order = order;
            }

            public int getLayout() {
                return layout;
            }

            public void setLayout(int layout) {
                this.layout = layout;
            }

            public String getResUrl() {
                return resUrl;
            }

            public void setResUrl(String resUrl) {
                this.resUrl = resUrl;
            }

            public String getUri() {
                return uri;
            }

            public void setUri(String uri) {
                this.uri = uri;
            }

            public int getShowInMainPage() {
                return showInMainPage;
            }

            public void setShowInMainPage(int showInMainPage) {
                this.showInMainPage = showInMainPage;
            }

            @Override
            public String toString() {
                return "ContentMessageBean{" +
                        "effective=" + effective +
                        ", id='" + id + '\'' +
                        ", isLastLevel=" + isLastLevel +
                        ", name='" + name + '\'' +
                        ", words='" + words + '\'' +
                        ", order=" + order +
                        ", layout=" + layout +
                        ", resUrl='" + resUrl + '\'' +
                        ", uri='" + uri + '\'' +
                        ", showInMainPage=" + showInMainPage +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "ResultBean{" +
                    "contentType=" + contentType +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ContentBean{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }

    //    /**
//     * message : ok
//     * result : {"contentType":[{"effective":false,"enableLanguages":["zh_CN","en_US","ja_JP"],"icon":"http://test.com/test.png","id":"f7cea6c34d4d404625","isLastLevel":false,"name":"","order":1,"resUrl":"http://dev.csjbot.com:8080/contentMessage?id=f7cea6c34d4d404625&sn=xxxxxxxxxx&language=zh_CN","showInMainPage":false,"uri":"xxxx/xxxxx/xxxx/xxxx"}]}
//     * status : 200
//     */
//
//    private String message;
//    private ResultBean result;
//    private String status;
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public ResultBean getResult() {
//        return result;
//    }
//
//    public void setResult(ResultBean result) {
//        this.result = result;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public static class ResultBean {
//        private List<ContentMessageBean> contentType;
//
//        public List<ContentMessageBean> getContentType() {
//            return contentType;
//        }
//
//        public void setContentType(List<ContentMessageBean> contentType) {
//            this.contentType = contentType;
//        }
//
//        public static class ContentMessageBean implements Serializable {
//            /**
//             * effective : false
//             * enableLanguages : ["zh_CN","en_US","ja_JP"]
//             * icon : http://test.com/test.png
//             * id : f7cea6c34d4d404625
//             * isLastLevel : false
//             * name :
//             * order : 1
//             * resUrl : http://dev.csjbot.com:8080/contentMessage?id=f7cea6c34d4d404625&sn=xxxxxxxxxx&language=zh_CN
//             * showInMainPage : false
//             * uri : xxxx/xxxxx/xxxx/xxxx
//             */
//
////            private boolean effective;
//            private String icon;
//            private String id;
//            private boolean isLastLevel;
//            private String name;
//            private int order;
//            private String resUrl;
////            private boolean showInMainPage;
//            private String uri;
//            private List<String> enableLanguages;
//
////            public boolean isEffective() {
////                return effective;
////            }
////
////            public void setEffective(boolean effective) {
////                this.effective = effective;
////            }
//
//            public String getIcon() {
//                return icon;
//            }
//
//            public void setIcon(String icon) {
//                this.icon = icon;
//            }
//
//            public String getId() {
//                return id;
//            }
//
//            public void setId(String id) {
//                this.id = id;
//            }
//
//            public boolean isIsLastLevel() {
//                return isLastLevel;
//            }
//
//            public void setIsLastLevel(boolean isLastLevel) {
//                this.isLastLevel = isLastLevel;
//            }
//
//            public String getName() {
//                return name;
//            }
//
//            public void setName(String name) {
//                this.name = name;
//            }
//
//            public int getOrder() {
//                return order;
//            }
//
//            public void setOrder(int order) {
//                this.order = order;
//            }
//
//            public String getResUrl() {
//                return resUrl;
//            }
//
//            public void setResUrl(String resUrl) {
//                this.resUrl = resUrl;
//            }
//
////            public boolean isShowInMainPage() {
////                return showInMainPage;
////            }
////
////            public void setShowInMainPage(boolean showInMainPage) {
////                this.showInMainPage = showInMainPage;
////            }
//
//            public String getUri() {
//                return uri;
//            }
//
//            public void setUri(String uri) {
//                this.uri = uri;
//            }
//
//            public List<String> getEnableLanguages() {
//                return enableLanguages;
//            }
//
//            public void setEnableLanguages(List<String> enableLanguages) {
//                this.enableLanguages = enableLanguages;
//            }
//        }
//    }
}
