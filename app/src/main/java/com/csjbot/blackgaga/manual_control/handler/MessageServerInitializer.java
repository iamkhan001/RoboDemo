package com.csjbot.blackgaga.manual_control.handler;

import com.csjbot.blackgaga.manual_control.constants.ConnectConstants;
import com.csjbot.blackgaga.manual_control.server.MobileServer;
import com.csjbot.coshandler.log.CsjlogProxy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author ShenBen
 * @date 2018/11/17 10:55
 * @email 714081644@qq.com
 */

public class MessageServerInitializer extends ChannelInitializer<SocketChannel> {

    private final ByteBuf delimiter = Unpooled.copiedBuffer(MobileServer.DELIMITER.getBytes());

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        CsjlogProxy.getInstance().debug(ConnectConstants.TAG + "initChannel");
        pipeline.addLast(new StringEncoder());
        pipeline.addLast(new DelimiterBasedFrameDecoder(102400, delimiter));
        pipeline.addLast(new MessageServerHandler());
    }
}
