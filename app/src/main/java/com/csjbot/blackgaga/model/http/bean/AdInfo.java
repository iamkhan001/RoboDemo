package com.csjbot.blackgaga.model.http.bean;

import java.util.List;

public class AdInfo {

    /**
     * message : ok
     * result : {"ADVERTISMENT_AUDIO":[],"ADVERTISMENT_PIC":[{"imgName":"1493714541691.jpg","url":"localhost:8001/ams/1493714541691.jpg"}],"ADVERTISMENT_VIDEO":[{"movieName":"1493368055167.mp4","url":"localhost:8001/ams/1493368055167.mp4"}],"zipName":"nMHr.zip","zipUrl":"localhost:8001/zip/nMHr.zip"}
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
         * ADVERTISMENT_AUDIO : []
         * ADVERTISMENT_PIC : [{"imgName":"1493714541691.jpg","url":"localhost:8001/ams/1493714541691.jpg"}]
         * ADVERTISMENT_VIDEO : [{"movieName":"1493368055167.mp4","url":"localhost:8001/ams/1493368055167.mp4"}]
         * zipName : nMHr.zip
         * zipUrl : localhost:8001/zip/nMHr.zip
         */

        private String zipName;
        private String zipUrl;
        private List<?> ADVERTISMENT_AUDIO;
        private List<ADVERTISMENTPICBean> ADVERTISMENT_PIC;
        private List<ADVERTISMENTVIDEOBean> ADVERTISMENT_VIDEO;

        public String getZipName() {
            return zipName;
        }

        public void setZipName(String zipName) {
            this.zipName = zipName;
        }

        public String getZipUrl() {
            return zipUrl;
        }

        public void setZipUrl(String zipUrl) {
            this.zipUrl = zipUrl;
        }

        public List<?> getADVERTISMENT_AUDIO() {
            return ADVERTISMENT_AUDIO;
        }

        public void setADVERTISMENT_AUDIO(List<?> ADVERTISMENT_AUDIO) {
            this.ADVERTISMENT_AUDIO = ADVERTISMENT_AUDIO;
        }

        public List<ADVERTISMENTPICBean> getADVERTISMENT_PIC() {
            return ADVERTISMENT_PIC;
        }

        public void setADVERTISMENT_PIC(List<ADVERTISMENTPICBean> ADVERTISMENT_PIC) {
            this.ADVERTISMENT_PIC = ADVERTISMENT_PIC;
        }

        public List<ADVERTISMENTVIDEOBean> getADVERTISMENT_VIDEO() {
            return ADVERTISMENT_VIDEO;
        }

        public void setADVERTISMENT_VIDEO(List<ADVERTISMENTVIDEOBean> ADVERTISMENT_VIDEO) {
            this.ADVERTISMENT_VIDEO = ADVERTISMENT_VIDEO;
        }

        public static class ADVERTISMENTPICBean {
            /**
             * imgName : 1493714541691.jpg
             * url : localhost:8001/ams/1493714541691.jpg
             */

            private String imgName;
            private String url;

            public String getImgName() {
                return imgName;
            }

            public void setImgName(String imgName) {
                this.imgName = imgName;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class ADVERTISMENTVIDEOBean {
            /**
             * movieName : 1493368055167.mp4
             * url : localhost:8001/ams/1493368055167.mp4
             */

            private String movieName;
            private String url;

            public String getMovieName() {
                return movieName;
            }

            public void setMovieName(String movieName) {
                this.movieName = movieName;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
