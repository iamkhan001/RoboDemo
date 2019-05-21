package com.csjbot.blackgaga.model.http.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author : chenqi.
 * @e_mail : 1650699704@163.com.
 * @create_time : 2018/1/26.
 * @Package_name: BlackGaGa
 */

public class ContentTypeBean implements Serializable {
    /**
     * status : 200
     * message : ok
     * result : {"contentMessage":{"messageType":1,"parentId":"asdfwefw23xdf2","messageList":[{"
     * name":"酒店介绍","ccontentUrl":"http%3A%2F%2Fdev.csjbot.com%3A8080%2Fcsjbotservice%2Fapi%2Fcms%2Fcontent%2Fdetailed%3Fid%3Dasdfwefw23xdf2%26language%3Dzh_CN","order":1,"startDate":"","endDate":"","startTime":0,"endTime":0,"effective":true,"isDeleted":true,"parentId":""}]}}
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
        /**
         * contentMessage : {"messageType":1,"parentId":"asdfwefw23xdf2","messageList":[{"name":"酒店介绍","ccontentUrl":"http%3A%2F%2Fdev.csjbot.com%3A8080%2Fcsjbotservice%2Fapi%2Fcms%2Fcontent%2Fdetailed%3Fid%3Dasdfwefw23xdf2%26language%3Dzh_CN","order":1,"startDate":"","endDate":"","startTime":0,"endTime":0,"effective":true,"isDeleted":true,"parentId":""}]}
         */

        private ContentTypeMessageBean contentMessage;

        public ContentTypeMessageBean getContentMessage() {
            return contentMessage;
        }

        public void setContentMessage(ContentTypeMessageBean contentMessage) {
            this.contentMessage = contentMessage;
        }

        public static class ContentTypeMessageBean {
            /**
             * messageType : 1
             * parentId : asdfwefw23xdf2
             * messageList : [{"name":"酒店介绍","ccontentUrl":"http%3A%2F%2Fdev.csjbot.com%3A8080%2Fcsjbotservice%2Fapi%2Fcms%2Fcontent%2Fdetailed%3Fid%3Dasdfwefw23xdf2%26language%3Dzh_CN","order":1,"startDate":"","endDate":"","startTime":0,"endTime":0,"effective":true,"isDeleted":true,"parentId":""}]
             */

            private int messageType;
            private String parentId;
            private List<MessageListBean> messageList;

            public int getMessageType() {
                return messageType;
            }

            public void setMessageType(int messageType) {
                this.messageType = messageType;
            }

            public String getParentId() {
                return parentId;
            }

            public void setParentId(String parentId) {
                this.parentId = parentId;
            }

            public List<MessageListBean> getMessageList() {
                return messageList;
            }

            public void setMessageList(List<MessageListBean> messageList) {
                this.messageList = messageList;
            }

            public static class MessageListBean {
                /**
                 * name : 酒店介绍
                 * resUrl : http%3A%2F%2Fdev.csjbot.com%3A8080%2Fcsjbotservice%2Fapi%2Fcms%2Fcontent%2Fdetailed%3Fid%3Dasdfwefw23xdf2%26language%3Dzh_CN
                 * order : 1
                 * startDate :
                 * endDate :
                 * startTime : 0
                 * endTime : 0
                 * effective : true
                 * isDeleted : true
                 * parentId :
                 */

                private String name;
                private String ccontentUrl;
                private int order;
                private String speechScript;
                private String startDate;
                private String endDate;
                private int startTime;
                private int endTime;
                private boolean effective;
                private boolean isDeleted;
                private String parentId;
                private List<MessagesBean> messages;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }


                public String getCcontentUrl() {
                    return ccontentUrl;
                }

                public void setCcontentUrl(String ccontentUrl) {
                    this.ccontentUrl = ccontentUrl;
                }

                public int getOrder() {
                    return order;
                }

                public void setOrder(int order) {
                    this.order = order;
                }

                public String getSpeechScript() {
                    return speechScript;
                }

                public void setSpeechScript(String speechScript) {
                    this.speechScript = speechScript;
                }

                public String getStartDate() {
                    return startDate;
                }

                public void setStartDate(String startDate) {
                    this.startDate = startDate;
                }

                public String getEndDate() {
                    return endDate;
                }

                public void setEndDate(String endDate) {
                    this.endDate = endDate;
                }

                public int getStartTime() {
                    return startTime;
                }

                public void setStartTime(int startTime) {
                    this.startTime = startTime;
                }

                public int getEndTime() {
                    return endTime;
                }

                public void setEndTime(int endTime) {
                    this.endTime = endTime;
                }

                public boolean isEffective() {
                    return effective;
                }

                public void setEffective(boolean effective) {
                    this.effective = effective;
                }

                public boolean isIsDeleted() {
                    return isDeleted;
                }

                public void setIsDeleted(boolean isDeleted) {
                    this.isDeleted = isDeleted;
                }

                public String getParentId() {
                    return parentId;
                }

                public void setParentId(String parentId) {
                    this.parentId = parentId;
                }

                public List<MessagesBean> getMessages() {
                    return messages;
                }

                public void setMessages(List<MessagesBean> messages) {
                    this.messages = messages;
                }

                public static class MessagesBean implements Serializable {
                    private static final long serialVersionUID = 1l;
                    /**
                     * name : 牡丹卡
                     * ccontentUrl : http:xxxxxxxx/xxxxxxxxx
                     * speechScript : 这个是点击牡丹卡选项卡时候说的话术
                     * startDate : 2018-01-25
                     * endDate : 2018-01-25
                     * startTime : 1800
                     * endTime : 3600
                     * effective : true
                     * isDeleted : false
                     * order : 0
                     */

                    private String name;
                    private String ccontentUrl;
                    private String speechScript;
                    private String startDate;
                    private String endDate;
                    private int startTime;
                    private int endTime;
                    private boolean effective;
                    private boolean isDeleted;
                    private int order;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getCcontentUrl() {
                        return ccontentUrl;
                    }

                    public void setCcontentUrl(String ccontentUrl) {
                        this.ccontentUrl = ccontentUrl;
                    }

                    public String getSpeechScript() {
                        return speechScript;
                    }

                    public void setSpeechScript(String speechScript) {
                        this.speechScript = speechScript;
                    }

                    public String getStartDate() {
                        return startDate;
                    }

                    public void setStartDate(String startDate) {
                        this.startDate = startDate;
                    }

                    public String getEndDate() {
                        return endDate;
                    }

                    public void setEndDate(String endDate) {
                        this.endDate = endDate;
                    }

                    public int getStartTime() {
                        return startTime;
                    }

                    public void setStartTime(int startTime) {
                        this.startTime = startTime;
                    }

                    public int getEndTime() {
                        return endTime;
                    }

                    public void setEndTime(int endTime) {
                        this.endTime = endTime;
                    }

                    public boolean isEffective() {
                        return effective;
                    }

                    public void setEffective(boolean effective) {
                        this.effective = effective;
                    }

                    public boolean isIsDeleted() {
                        return isDeleted;
                    }

                    public void setIsDeleted(boolean isDeleted) {
                        this.isDeleted = isDeleted;
                    }

                    public int getOrder() {
                        return order;
                    }

                    public void setOrder(int order) {
                        this.order = order;
                    }
                }
            }
        }
    }

    /*private static final long serialVersionUID = 1l;

    *//**
     * message : ok
     * result : {"contentMessage":{"messageType":3,"messageList":[{"scene":"所属场景","parentId":"f7cea6c34d4d404625","name":"银行卡","ccontentUrl":"http:xxxxxxxx/xxxxxxxxx","speechScript":"这个是点击银行卡选项卡时候说的话术","startDate":"2018-01-25","endDate":"2018-01-25","startTime":1800,"endTime":3600,"effective":true,"isDeleted":false,"order":0,"messages":[{"scene":"所属场景","parentId":"f7cea6c34d4d404625","name":"牡丹卡","ccontentUrl":"http:xxxxxxxx/xxxxxxxxx","speechScript":"这个是点击牡丹卡选项卡时候说的话术","startDate":"2018-01-25","endDate":"2018-01-25","startTime":1800,"endTime":3600,"effective":true,"isDeleted":false,"order":0}]},{"scene":"所属场景","parentId":"f7cea6c34d4d404625","name":"理财产品","ccontentUrl":"http:xxxxxxxx/xxxxxxxxx","speechScript":"你好，这里是爱丽丝的话术，请查收","startDate":"2018-01-25","endDate":"2018-01-25","startTime":1800,"endTime":3600,"effective":true,"isDeleted":false,"order":1,"messages":[{"scene":"所属场景","parentId":"f7cea6c34d4d404625","name":"牡丹卡","ccontentUrl":"http:xxxxxxxx/xxxxxxxxx","speechScript":"这个是点击牡丹卡选项卡时候说的话术","startDate":"2018-01-25","endDate":"2018-01-25","startTime":1800,"endTime":3600,"effective":true,"isDeleted":false,"order":1}]}]}}
     * status : 200
     *//*

    private String message;
    private ResultBean result;
    private String status;
    private String scene;
    private String parentId;

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class ResultBean implements Serializable{
        private static final long serialVersionUID = 1l;
        *//**
     * contentMessage : {"messageType":3,"messageList":[{"scene":"所属场景","parentId":"f7cea6c34d4d404625","name":"银行卡","ccontentUrl":"http:xxxxxxxx/xxxxxxxxx","speechScript":"这个是点击银行卡选项卡时候说的话术","startDate":"2018-01-25","endDate":"2018-01-25","startTime":1800,"endTime":3600,"effective":true,"isDeleted":false,"order":0,"messages":[{"scene":"所属场景","parentId":"f7cea6c34d4d404625","name":"牡丹卡","ccontentUrl":"http:xxxxxxxx/xxxxxxxxx","speechScript":"这个是点击牡丹卡选项卡时候说的话术","startDate":"2018-01-25","endDate":"2018-01-25","startTime":1800,"endTime":3600,"effective":true,"isDeleted":false,"order":0}]},{"scene":"所属场景","parentId":"f7cea6c34d4d404625","name":"理财产品","ccontentUrl":"http:xxxxxxxx/xxxxxxxxx","speechScript":"你好，这里是爱丽丝的话术，请查收","startDate":"2018-01-25","endDate":"2018-01-25","startTime":1800,"endTime":3600,"effective":true,"isDeleted":false,"order":1,"messages":[{"scene":"所属场景","parentId":"f7cea6c34d4d404625","name":"牡丹卡","ccontentUrl":"http:xxxxxxxx/xxxxxxxxx","speechScript":"这个是点击牡丹卡选项卡时候说的话术","startDate":"2018-01-25","endDate":"2018-01-25","startTime":1800,"endTime":3600,"effective":true,"isDeleted":false,"order":1}]}]}
     *//*

        private ContentTypeMessageBean contentMessage;

        public ContentTypeMessageBean getContentMessage() {
            return contentMessage;
        }

        public void setContentMessage(ContentTypeMessageBean contentMessage) {
            this.contentMessage = contentMessage;
        }

        public static class ContentTypeMessageBean implements Serializable{
            private static final long serialVersionUID = 1l;
            *//**
     * messageType : 3
     * scene : 所属场景
     * parentId : f7cea6c34d4d404625
     * messageList : [{"scene":"所属场景","parentId":"f7cea6c34d4d404625","name":"银行卡","ccontentUrl":"http:xxxxxxxx/xxxxxxxxx","speechScript":"这个是点击银行卡选项卡时候说的话术","startDate":"2018-01-25","endDate":"2018-01-25","startTime":1800,"endTime":3600,"effective":true,"isDeleted":false,"order":0,"messages":[{"scene":"所属场景","parentId":"f7cea6c34d4d404625","name":"牡丹卡","ccontentUrl":"http:xxxxxxxx/xxxxxxxxx","speechScript":"这个是点击牡丹卡选项卡时候说的话术","startDate":"2018-01-25","endDate":"2018-01-25","startTime":1800,"endTime":3600,"effective":true,"isDeleted":false,"order":0}]},{"scene":"所属场景","parentId":"f7cea6c34d4d404625","name":"理财产品","ccontentUrl":"http:xxxxxxxx/xxxxxxxxx","speechScript":"你好，这里是爱丽丝的话术，请查收","startDate":"2018-01-25","endDate":"2018-01-25","startTime":1800,"endTime":3600,"effective":true,"isDeleted":false,"order":1,"messages":[{"scene":"所属场景","parentId":"f7cea6c34d4d404625","name":"牡丹卡","ccontentUrl":"http:xxxxxxxx/xxxxxxxxx","speechScript":"这个是点击牡丹卡选项卡时候说的话术","startDate":"2018-01-25","endDate":"2018-01-25","startTime":1800,"endTime":3600,"effective":true,"isDeleted":false,"order":1}]}]
     *//*

            private int messageType;
            private List<MessageListBean> messageList;

            public int getMessageType() {
                return messageType;
            }

            public void setMessageType(int messageType) {
                this.messageType = messageType;
            }

            public List<MessageListBean> getMessageList() {
                return messageList;
            }

            public void setMessageList(List<MessageListBean> messageList) {
                this.messageList = messageList;
            }

            public static class MessageListBean implements Serializable{
                private static final long serialVersionUID = 1l;
                *//**
     * name : 银行卡
     * ccontentUrl : http:xxxxxxxx/xxxxxxxxx
     * speechScript : 这个是点击银行卡选项卡时候说的话术
     * startDate : 2018-01-25
     * endDate : 2018-01-25
     * startTime : 1800
     * endTime : 3600
     * effective : true
     * isDeleted : false
     * order : 0
     * messages : [{"scene":"所属场景","parentId":"f7cea6c34d4d404625","name":"牡丹卡","ccontentUrl":"http:xxxxxxxx/xxxxxxxxx","speechScript":"这个是点击牡丹卡选项卡时候说的话术","startDate":"2018-01-25","endDate":"2018-01-25","startTime":1800,"endTime":3600,"effective":true,"isDeleted":false,"order":0}]
     *//*
                private String name;
                private String ccontentUrl;
                private String speechScript;
                private String startDate;
                private String endDate;
                private int startTime;
                private int endTime;
                private boolean effective;
                private boolean isDeleted;
                private int order;
                private List<MessagesBean> messages;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getCcontentUrl() {
                    return ccontentUrl;
                }

                public void setCcontentUrl(String ccontentUrl) {
                    this.ccontentUrl = ccontentUrl;
                }

                public String getSpeechScript() {
                    return speechScript;
                }

                public void setSpeechScript(String speechScript) {
                    this.speechScript = speechScript;
                }

                public String getStartDate() {
                    return startDate;
                }

                public void setStartDate(String startDate) {
                    this.startDate = startDate;
                }

                public String getEndDate() {
                    return endDate;
                }

                public void setEndDate(String endDate) {
                    this.endDate = endDate;
                }

                public int getStartTime() {
                    return startTime;
                }

                public void setStartTime(int startTime) {
                    this.startTime = startTime;
                }

                public int getEndTime() {
                    return endTime;
                }

                public void setEndTime(int endTime) {
                    this.endTime = endTime;
                }

                public boolean isEffective() {
                    return effective;
                }

                public void setEffective(boolean effective) {
                    this.effective = effective;
                }

                public boolean isIsDeleted() {
                    return isDeleted;
                }

                public void setIsDeleted(boolean isDeleted) {
                    this.isDeleted = isDeleted;
                }

                public int getOrder() {
                    return order;
                }

                public void setOrder(int order) {
                    this.order = order;
                }

                public List<MessagesBean> getMessages() {
                    return messages;
                }

                public void setMessages(List<MessagesBean> messages) {
                    this.messages = messages;
                }

                public static class MessagesBean implements Serializable{
                    private static final long serialVersionUID = 1l;
                    *//**
     * name : 牡丹卡
     * ccontentUrl : http:xxxxxxxx/xxxxxxxxx
     * speechScript : 这个是点击牡丹卡选项卡时候说的话术
     * startDate : 2018-01-25
     * endDate : 2018-01-25
     * startTime : 1800
     * endTime : 3600
     * effective : true
     * isDeleted : false
     * order : 0
     *//*

                    private String name;
                    private String ccontentUrl;
                    private String speechScript;
                    private String startDate;
                    private String endDate;
                    private int startTime;
                    private int endTime;
                    private boolean effective;
                    private boolean isDeleted;
                    private int order;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getCcontentUrl() {
                        return ccontentUrl;
                    }

                    public void setCcontentUrl(String ccontentUrl) {
                        this.ccontentUrl = ccontentUrl;
                    }

                    public String getSpeechScript() {
                        return speechScript;
                    }

                    public void setSpeechScript(String speechScript) {
                        this.speechScript = speechScript;
                    }

                    public String getStartDate() {
                        return startDate;
                    }

                    public void setStartDate(String startDate) {
                        this.startDate = startDate;
                    }

                    public String getEndDate() {
                        return endDate;
                    }

                    public void setEndDate(String endDate) {
                        this.endDate = endDate;
                    }

                    public int getStartTime() {
                        return startTime;
                    }

                    public void setStartTime(int startTime) {
                        this.startTime = startTime;
                    }

                    public int getEndTime() {
                        return endTime;
                    }

                    public void setEndTime(int endTime) {
                        this.endTime = endTime;
                    }

                    public boolean isEffective() {
                        return effective;
                    }

                    public void setEffective(boolean effective) {
                        this.effective = effective;
                    }

                    public boolean isIsDeleted() {
                        return isDeleted;
                    }

                    public void setIsDeleted(boolean isDeleted) {
                        this.isDeleted = isDeleted;
                    }

                    public int getOrder() {
                        return order;
                    }

                    public void setOrder(int order) {
                        this.order = order;
                    }
                }
            }
        }
    }*/

}
