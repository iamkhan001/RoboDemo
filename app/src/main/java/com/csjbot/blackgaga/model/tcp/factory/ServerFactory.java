package com.csjbot.blackgaga.model.tcp.factory;

import com.csjbot.blackgaga.model.tcp.body_action.BodyActionImpl;
import com.csjbot.blackgaga.model.tcp.body_action.IAction;
import com.csjbot.blackgaga.model.tcp.chassis.ChassisImpl;
import com.csjbot.blackgaga.model.tcp.chassis.IChassis;
import com.csjbot.blackgaga.model.tcp.config.ConfigImpl;
import com.csjbot.blackgaga.model.tcp.config.IConfig;
import com.csjbot.blackgaga.model.tcp.dance.DanceImpl;
import com.csjbot.blackgaga.model.tcp.dance.IDance;
import com.csjbot.blackgaga.model.tcp.expression.ExpressionImpl;
import com.csjbot.blackgaga.model.tcp.expression.IExpression;
import com.csjbot.blackgaga.model.tcp.face.FaceImpl;
import com.csjbot.blackgaga.model.tcp.face.IFace;
import com.csjbot.blackgaga.model.tcp.print.IPrint;
import com.csjbot.blackgaga.model.tcp.print.PrintImpl;
import com.csjbot.blackgaga.model.tcp.robot_state.IRobotState;
import com.csjbot.blackgaga.model.tcp.robot_state.RobotStateImpl;
import com.csjbot.blackgaga.model.tcp.speech.ISpeech;
import com.csjbot.blackgaga.model.tcp.speech.SpeechImpl;
import com.csjbot.blackgaga.model.tcp.tts.ISpeak;
import com.csjbot.blackgaga.model.tcp.tts.SpeakImpl;
import com.csjbot.blackgaga.model.tcp.version.IVersion;
import com.csjbot.blackgaga.model.tcp.version.VersionImpl;

/**
 * Created by jingwc on 2017/9/21.
 */

public class ServerFactory {

    /* 语音合成 */
    public static ISpeak getSpeakInstance() {
        return new SpeakImpl();
    }

    /* 肢体动作 */
    public static IAction getActionInstance() {
        return new BodyActionImpl();
    }

    /* 底盘功能 */
    public static IChassis getChassisInstance() {
        return new ChassisImpl();
    }

    /* 表情 */
    public static IExpression getExpressionInstantce() {
        return new ExpressionImpl();
    }

    /* 人脸识别 */
    public static IFace getFaceInstance() {
        return new FaceImpl();
    }

    /* 语音识别 */
    public static ISpeech getSpeechInstance() {
        return new SpeechImpl();
    }

    /* 打印 */
    public static IPrint getPrintInstance() {
        return new PrintImpl();
    }

    /* 跳舞 */
    public static IDance getDanceInstatnce() {
        return new DanceImpl();
    }

    /* 机器人状态 */
    public static IRobotState getRobotState() {
        return new RobotStateImpl();
    }

    /* 机器人配置 */
    public static IConfig getConfigInstance() {
        return new ConfigImpl();
    }

    public static IVersion getVersionInstance() {
        return new VersionImpl();
    }
}
