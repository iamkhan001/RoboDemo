package com.csjbot.blackgaga.localbean;

/**
 * Created by xiasuhuei321 on 2017/10/24.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class DeviceInfo {

    /**
     * downPlate : 000000010000000104050607
     * error_code : 0
     * msg_id : GET_LOCAL_DEVICE_RSP
     * nav : empty
     * upComputer : alice20171024101749109
     * upPlate : 0100010001000100F3642133
     */

    // 下身板
    private String downPlate;
    // 错误码
    private int error_code;
    // message id
    private String msg_id;
    // 导航板
    private String nav;
    // 上位机
    private String upComputer;
    // 上身板
    private String upPlate;

    public static final String EMPTY = "empty";

    public String getDownPlate() {
        return downPlate;
    }

    public void setDownPlate(String downPlate) {
        this.downPlate = downPlate;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getNav() {
        return nav;
    }

    public void setNav(String nav) {
        this.nav = nav;
    }

    public String getUpComputer() {
        return upComputer;
    }

    public void setUpComputer(String upComputer) {
        this.upComputer = upComputer;
    }

    public String getUpPlate() {
        return upPlate;
    }

    public void setUpPlate(String upPlate) {
        this.upPlate = upPlate;
    }

    /**
     * 判断四个关键信息中是否有 empty
     * @return true 如果有empty，反之 false
     */
    public boolean containsEmpty(){
        if(downPlate.contains(EMPTY)) return true;
        if(upPlate.contains(EMPTY)) return true;
        if(nav.contains(EMPTY)) return true;
        if(upComputer.contains(EMPTY)) return true;

        return false;

    }
}
