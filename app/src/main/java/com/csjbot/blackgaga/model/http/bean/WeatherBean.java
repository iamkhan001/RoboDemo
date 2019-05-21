package com.csjbot.blackgaga.model.http.bean;

import java.util.List;

/**
 * Created by jingwc on 2018/3/1.
 */

public class WeatherBean {

    /**
     * message : ok
     * result : {"source":"stormorai","data":{"code":0,"data":{"answer":"咸阳 2018-02-28 星期三 温度:4-18° 晴 西风4级","info":[{"city":"咸阳","currentTemp":17,"date":"2018-02-28","dayTemp":"18","nightTemp":"4","pm25":"120","quality":"中度污染","weather":"多云转阴","week":"三","windDirect":"西风","windPower":"4级","ziwaixianDesc":"属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。","ziwaixianIndex":"最弱"}]},"detail":"success","is_dialog_end":true,"operation":"GENERAL","semantic":{"slots":{"DATERANGE":{"isLast":"false","originalValue":"今天","value":["2018-02-28"]},"LOCATION":{"isLast":"false","originalValue":"咸阳","value":"咸阳"}}},"service":"WEATHER","state":1,"text":"咸阳今天天气怎么样"}}
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
         * source : stormorai
         * data : {"code":0,"data":{"answer":"咸阳 2018-02-28 星期三 温度:4-18° 晴 西风4级","info":[{"city":"咸阳","currentTemp":17,"date":"2018-02-28","dayTemp":"18","nightTemp":"4","pm25":"120","quality":"中度污染","weather":"多云转阴","week":"三","windDirect":"西风","windPower":"4级","ziwaixianDesc":"属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。","ziwaixianIndex":"最弱"}]},"detail":"success","is_dialog_end":true,"operation":"GENERAL","semantic":{"slots":{"DATERANGE":{"isLast":"false","originalValue":"今天","value":["2018-02-28"]},"LOCATION":{"isLast":"false","originalValue":"咸阳","value":"咸阳"}}},"service":"WEATHER","state":1,"text":"咸阳今天天气怎么样"}
         */

        private String source;
        private DataBeanX data;

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public DataBeanX getData() {
            return data;
        }

        public void setData(DataBeanX data) {
            this.data = data;
        }

        public static class DataBeanX {
            /**
             * code : 0
             * data : {"answer":"咸阳 2018-02-28 星期三 温度:4-18° 晴 西风4级","info":[{"city":"咸阳","currentTemp":17,"date":"2018-02-28","dayTemp":"18","nightTemp":"4","pm25":"120","quality":"中度污染","weather":"多云转阴","week":"三","windDirect":"西风","windPower":"4级","ziwaixianDesc":"属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。","ziwaixianIndex":"最弱"}]}
             * detail : success
             * is_dialog_end : true
             * operation : GENERAL
             * semantic : {"slots":{"DATERANGE":{"isLast":"false","originalValue":"今天","value":["2018-02-28"]},"LOCATION":{"isLast":"false","originalValue":"咸阳","value":"咸阳"}}}
             * service : WEATHER
             * state : 1
             * text : 咸阳今天天气怎么样
             */

            private int code;
            private DataBean data;
            private String detail;
            private boolean is_dialog_end;
            private String operation;
            private SemanticBean semantic;
            private String service;
            private int state;
            private String text;

            public int getCode() {
                return code;
            }

            public void setCode(int code) {
                this.code = code;
            }

            public DataBean getData() {
                return data;
            }

            public void setData(DataBean data) {
                this.data = data;
            }

            public String getDetail() {
                return detail;
            }

            public void setDetail(String detail) {
                this.detail = detail;
            }

            public boolean isIs_dialog_end() {
                return is_dialog_end;
            }

            public void setIs_dialog_end(boolean is_dialog_end) {
                this.is_dialog_end = is_dialog_end;
            }

            public String getOperation() {
                return operation;
            }

            public void setOperation(String operation) {
                this.operation = operation;
            }

            public SemanticBean getSemantic() {
                return semantic;
            }

            public void setSemantic(SemanticBean semantic) {
                this.semantic = semantic;
            }

            public String getService() {
                return service;
            }

            public void setService(String service) {
                this.service = service;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public static class DataBean {
                /**
                 * answer : 咸阳 2018-02-28 星期三 温度:4-18° 晴 西风4级
                 * info : [{"city":"咸阳","currentTemp":17,"date":"2018-02-28","dayTemp":"18","nightTemp":"4","pm25":"120","quality":"中度污染","weather":"多云转阴","week":"三","windDirect":"西风","windPower":"4级","ziwaixianDesc":"属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。","ziwaixianIndex":"最弱"}]
                 */

                private String answer;
                private List<InfoBean> info;

                public String getAnswer() {
                    return answer;
                }

                public void setAnswer(String answer) {
                    this.answer = answer;
                }

                public List<InfoBean> getInfo() {
                    return info;
                }

                public void setInfo(List<InfoBean> info) {
                    this.info = info;
                }

                public static class InfoBean {
                    /**
                     * city : 咸阳
                     * currentTemp : 17
                     * date : 2018-02-28
                     * dayTemp : 18
                     * nightTemp : 4
                     * pm25 : 120
                     * quality : 中度污染
                     * weather : 多云转阴
                     * week : 三
                     * windDirect : 西风
                     * windPower : 4级
                     * ziwaixianDesc : 属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。
                     * ziwaixianIndex : 最弱
                     */

                    private String city;
                    private int currentTemp;
                    private String date;
                    private String dayTemp;
                    private String nightTemp;
                    private String pm25;
                    private String quality;
                    private String weather;
                    private String week;
                    private String windDirect;
                    private String windPower;
                    private String ziwaixianDesc;
                    private String ziwaixianIndex;

                    public String getCity() {
                        return city;
                    }

                    public void setCity(String city) {
                        this.city = city;
                    }

                    public int getCurrentTemp() {
                        return currentTemp;
                    }

                    public void setCurrentTemp(int currentTemp) {
                        this.currentTemp = currentTemp;
                    }

                    public String getDate() {
                        return date;
                    }

                    public void setDate(String date) {
                        this.date = date;
                    }

                    public String getDayTemp() {
                        return dayTemp;
                    }

                    public void setDayTemp(String dayTemp) {
                        this.dayTemp = dayTemp;
                    }

                    public String getNightTemp() {
                        return nightTemp;
                    }

                    public void setNightTemp(String nightTemp) {
                        this.nightTemp = nightTemp;
                    }

                    public String getPm25() {
                        return pm25;
                    }

                    public void setPm25(String pm25) {
                        this.pm25 = pm25;
                    }

                    public String getQuality() {
                        return quality;
                    }

                    public void setQuality(String quality) {
                        this.quality = quality;
                    }

                    public String getWeather() {
                        return weather;
                    }

                    public void setWeather(String weather) {
                        this.weather = weather;
                    }

                    public String getWeek() {
                        return week;
                    }

                    public void setWeek(String week) {
                        this.week = week;
                    }

                    public String getWindDirect() {
                        return windDirect;
                    }

                    public void setWindDirect(String windDirect) {
                        this.windDirect = windDirect;
                    }

                    public String getWindPower() {
                        return windPower;
                    }

                    public void setWindPower(String windPower) {
                        this.windPower = windPower;
                    }

                    public String getZiwaixianDesc() {
                        return ziwaixianDesc;
                    }

                    public void setZiwaixianDesc(String ziwaixianDesc) {
                        this.ziwaixianDesc = ziwaixianDesc;
                    }

                    public String getZiwaixianIndex() {
                        return ziwaixianIndex;
                    }

                    public void setZiwaixianIndex(String ziwaixianIndex) {
                        this.ziwaixianIndex = ziwaixianIndex;
                    }
                }
            }

            public static class SemanticBean {
                /**
                 * slots : {"DATERANGE":{"isLast":"false","originalValue":"今天","value":["2018-02-28"]},"LOCATION":{"isLast":"false","originalValue":"咸阳","value":"咸阳"}}
                 */

                private SlotsBean slots;

                public SlotsBean getSlots() {
                    return slots;
                }

                public void setSlots(SlotsBean slots) {
                    this.slots = slots;
                }

                public static class SlotsBean {
                    /**
                     * DATERANGE : {"isLast":"false","originalValue":"今天","value":["2018-02-28"]}
                     * LOCATION : {"isLast":"false","originalValue":"咸阳","value":"咸阳"}
                     */

                    private DATERANGEBean DATERANGE;
                    private LOCATIONBean LOCATION;

                    public DATERANGEBean getDATERANGE() {
                        return DATERANGE;
                    }

                    public void setDATERANGE(DATERANGEBean DATERANGE) {
                        this.DATERANGE = DATERANGE;
                    }

                    public LOCATIONBean getLOCATION() {
                        return LOCATION;
                    }

                    public void setLOCATION(LOCATIONBean LOCATION) {
                        this.LOCATION = LOCATION;
                    }

                    public static class DATERANGEBean {
                        /**
                         * isLast : false
                         * originalValue : 今天
                         * value : ["2018-02-28"]
                         */

                        private String isLast;
                        private String originalValue;
                        private List<String> value;

                        public String getIsLast() {
                            return isLast;
                        }

                        public void setIsLast(String isLast) {
                            this.isLast = isLast;
                        }

                        public String getOriginalValue() {
                            return originalValue;
                        }

                        public void setOriginalValue(String originalValue) {
                            this.originalValue = originalValue;
                        }

                        public List<String> getValue() {
                            return value;
                        }

                        public void setValue(List<String> value) {
                            this.value = value;
                        }
                    }

                    public static class LOCATIONBean {
                        /**
                         * isLast : false
                         * originalValue : 咸阳
                         * value : 咸阳
                         */

                        private String isLast;
                        private String originalValue;
                        private String value;

                        public String getIsLast() {
                            return isLast;
                        }

                        public void setIsLast(String isLast) {
                            this.isLast = isLast;
                        }

                        public String getOriginalValue() {
                            return originalValue;
                        }

                        public void setOriginalValue(String originalValue) {
                            this.originalValue = originalValue;
                        }

                        public String getValue() {
                            return value;
                        }

                        public void setValue(String value) {
                            this.value = value;
                        }
                    }
                }
            }
        }
    }
}
