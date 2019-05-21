package com.csjbot.blackgaga.bean;

/**
 * @项目名 BlackGaGa
 * @路径 name：com.csjbot.blackgaga.bean
 * @创建者 Wql
 * @创建时间 2017/12/27 17:52
 */
public class UpdateAPKBean {

    /**
     * message : ok
     * result : {"resule":{"category":"NewRetail_alies","channel":"standard","checksum":"234CDCFE4906F2BEDA57A5E5699D60CB","upgrade":true,"url":"120.27.233.57:8001/tms/1513041997088.apk","version_code":21,"version_name":"TE_NewRetail_Alice_V0037"}}
     * status : 200
     */

    private String message;
    private ResultEntity result;
    private String status;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setResult(ResultEntity result) {
        this.result = result;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public ResultEntity getResult() {
        return result;
    }

    public String getStatus() {
        return status;
    }

    public static class ResultEntity {
        /**
         * resule : {"category":"NewRetail_alies","channel":"standard","checksum":"234CDCFE4906F2BEDA57A5E5699D60CB","upgrade":true,"url":"120.27.233.57:8001/tms/1513041997088.apk","version_code":21,"version_name":"TE_NewRetail_Alice_V0037"}
         */

        private ResuleEntity resule;

        public void setResule(ResuleEntity resule) {
            this.resule = resule;
        }

        public ResuleEntity getResule() {
            return resule;
        }

        public static class ResuleEntity {
            /**
             * category : NewRetail_alies
             * channel : standard
             * checksum : 234CDCFE4906F2BEDA57A5E5699D60CB
             * upgrade : true
             * url : 120.27.233.57:8001/tms/1513041997088.apk
             * version_code : 21
             * version_name : TE_NewRetail_Alice_V0037
             */

            private String category;
            private String channel;
            private String checksum;
            private boolean upgrade;
            private String url;
            private int version_code;
            private String version_name;

            public void setCategory(String category) {
                this.category = category;
            }

            public void setChannel(String channel) {
                this.channel = channel;
            }

            public void setChecksum(String checksum) {
                this.checksum = checksum;
            }

            public void setUpgrade(boolean upgrade) {
                this.upgrade = upgrade;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public void setVersion_code(int version_code) {
                this.version_code = version_code;
            }

            public void setVersion_name(String version_name) {
                this.version_name = version_name;
            }

            public String getCategory() {
                return category;
            }

            public String getChannel() {
                return channel;
            }

            public String getChecksum() {
                return checksum;
            }

            public boolean getUpgrade() {
                return upgrade;
            }

            public String getUrl() {
                return url;
            }

            public int getVersion_code() {
                return version_code;
            }

            public String getVersion_name() {
                return version_name;
            }
        }
    }
}
