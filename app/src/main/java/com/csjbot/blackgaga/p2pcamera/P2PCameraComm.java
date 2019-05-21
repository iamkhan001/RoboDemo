package com.csjbot.blackgaga.p2pcamera;

/**
 * @author Ben
 * @date 2018/1/10
 */

public class P2PCameraComm {

    public static class ConnectionInfo {
        public static String USER = null;
        public static String PASSWD = null;
        public static String RELAYADDR = null;
        public static String SERVER_ADDRESS = null;
        public static String DEFAULT_SERVER_ADDRESS = "p2p.csjbot.com:7781";
        public static String DEFAULT_PASSWD = "123456";

        public static boolean ENABLED = false;
    }

    public static final String ACTION_VIDEO_STATUS = "com.csjbot.floatwindow.VideoStatus";
    public static final String ACTION_NOTIFY = "com.csjbot.floatwindow.Notify";
    public static final String ACTION_RENDER_JOIN = "com.csjbot.floatwindow.RenderJoin";
    public static final String ACTION_RENDER_LEAVE = "com.csjbot.floatwindow.RenderLeave";
    public static final String ACTION_MESSAGE = "com.csjbot.floatwindow.Message";
    public static final String ACTION_LOGIN = "com.csjbot.floatwindow.Login";
    public static final String ACTION_LOGIN0 = "com.csjbot.floatwindow.Login_success";
    public static final String ACTION_LOGIN1 = "com.csjbot.floatwindow.Login_failed";
    public static final String ACTION_LOGOUT = "com.csjbot.floatwindow.Logout";
    public static final String ACTION_CONNECT = "com.csjbot.floatwindow.Connect";
    public static final String ACTION_DISCONNECT = "com.csjbot.floatwindow.Disconnect";
    public static final String ACTION_OFFLINE = "com.csjbot.floatwindow.Offline";
    public static final String ACTION_LAN_SCAN_RESULT = "com.csjbot.floatwindow.LanScanResult";
    public static final String ACTION_FORWARD_ALLOC_REPLY = "com.csjbot.floatwindow.ForwardAllocReply";
    public static final String ACTION_FORWARD_FREE_REPLY = "com.csjbot.floatwindow.ForwardFreeReply";
    public static final String ACTION_VIDEO_CAMERA = "com.csjbot.floatwindow.VideoCamera";
    public static final String ACTION_SVR_NOTIFY = "com.csjbot.floatwindow.SvrNotify";

    // 向服务端发送消息
    public static final String SEND_MSG_TO_SERVER = "SEND_MSG_TO_SERVER";
    // 来自服务端的文本(推送机器人说话的文本)
    public static final String SEND_TEXT_MSG = "SPEECH_TTS_NTF";

     // 服务端请求介入
    public static final String HUMAN_INTERVENTI = "HUMAN_INTERVENTI";
    // 服务端请求释放
    public static final String REQ_HANGUP_HUMAN_INTERVENTI = "REQ_HANGUP_HUMAN_INTERVENTI";
    // 有客户端主动接入客服而返回的通知
    public static final String RESP_HUMAN_SERVICE = "RESP_HUMAN_SERVICE";

    public static final String REQ_HUMAN_INTERVENTI = "REQ_HUMAN_INTERVENTI";



}
