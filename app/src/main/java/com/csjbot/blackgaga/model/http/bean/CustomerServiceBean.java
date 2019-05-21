package com.csjbot.blackgaga.model.http.bean;

/**
 * Created by jingwc on 2018/3/8.
 */

public class CustomerServiceBean {

    /**
     * message : ok
     * result : {"enabled":true,"account":"csj010501","serverAddr":""}
     * status : 200
     */

    private String message;
    private ResultBean result;
    private String status;

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

    public static class ResultBean {
        /**
         * enabled : true
         * account : csj010501
         * serverAddr :
         */

        private boolean enabled;
        private String account;
        private String serverAddr;
        private String passwd;
        private String relayAddr;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getServerAddr() {
            return serverAddr;
        }

        public void setServerAddr(String serverAddr) {
            this.serverAddr = serverAddr;
        }

        public String getPasswd() {
            return passwd;
        }

        public void setPasswd(String passwd) {
            this.passwd = passwd;
        }


        public String getRelayAddr() {
            return relayAddr;
        }

        public void setRelayAddr(String relayAddr) {
            this.relayAddr = relayAddr;
        }
    }
}
