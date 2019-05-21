package com.csjbot.blackgaga.model.tcp.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jingwc on 2017/9/22.
 */

public class Position {

    /**
     * error_code : 0
     * msg_id : NAVI_GET_CURPOS_RSP
     * rotation : 5.142
     * x : 0.170
     * y : -0.018
     * z : 0.000
     */
    private String rotation;
    private String x;
    private String y;
    private String z;

    public String getRotation() {
        return rotation;
    }

    public void setRotation(String rotation) {
        this.rotation = rotation;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getZ() {
        return z;
    }

    public void setZ(String z) {
        this.z = z;
    }

    public String toJson() {
        JSONObject job = new JSONObject();
        try {
//            jo.put("msg_id", REQConstants.NAVI_ROBOT_MOVE_TO_REQ);
//            JSONObject job = new JSONObject();
//            jo.put("pos", job);
//            job.put("x", Float.parseFloat(x));
//            job.put("y", Float.parseFloat(y));
//            job.put("z", Float.parseFloat(z));
//
//            job.put("rotation", Float.parseFloat("80"));
            job.put("x", Float.parseFloat(x));
            job.put("y", Float.parseFloat(y));
            job.put("z", 0f);
            job.put("rotation",Float.parseFloat(rotation));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return job.toString();
    }
}
