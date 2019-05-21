package com.csjbot.blackgaga.manual_control.handler;

import com.csjbot.blackgaga.manual_control.constants.ConnectConstants;
import com.csjbot.blackgaga.manual_control.event.ServerEvent;
import com.csjbot.blackgaga.manual_control.server.MobileServer;
import com.csjbot.coshandler.log.CsjlogProxy;

import org.greenrobot.eventbus.EventBus;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author ShenBen
 * @date 2018/11/17 11:04
 * @email 714081644@qq.com
 */

public class MessageServerHandler extends SimpleChannelInboundHandler<String> {

    private static MsgRecListener sListener;
    public static boolean sIsConnected = false;

    public static void setMsgRecListener(MsgRecListener listener) {
        sListener = listener;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        sIsConnected = true;
        CsjlogProxy.getInstance().debug(ConnectConstants.TAG + "连接成功 " + ctx.channel().remoteAddress().toString());
        EventBus.getDefault().post(new ServerEvent(ConnectConstants.CLIENT_CONNECTED, ctx.channel().remoteAddress().toString()));
        MobileServer.getInstace().addChannelHandlerContext(ctx);
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String body = new String(bytes, "UTF-8");
        if (sListener != null) {
            sListener.messageRec(body);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        sIsConnected = false;
        CsjlogProxy.getInstance().debug(ConnectConstants.TAG + "断开连接 " + ctx.channel().remoteAddress().toString());
        EventBus.getDefault().post(new ServerEvent(ConnectConstants.CLIENT_DISCONNECTED, ctx.channel().remoteAddress().toString()));
        MobileServer.getInstace().removeChannelHandlerContext(ctx);
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
