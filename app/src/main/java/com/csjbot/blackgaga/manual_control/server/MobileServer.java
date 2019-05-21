package com.csjbot.blackgaga.manual_control.server;

import android.text.TextUtils;

import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.manual_control.constants.ConnectConstants;
import com.csjbot.blackgaga.manual_control.handler.MessageServerInitializer;
import com.csjbot.coshandler.log.CsjlogProxy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author ShenBen
 * @date 2018/11/17 10:00
 * @email 714081644@qq.com
 */

public class MobileServer {

    private int prot;
    public static final String DELIMITER = "#@#";   //分隔符

    private List<ChannelHandlerContext> mChannels;
    private List<String> mCmds;

    private static final class Holder {
        private static final MobileServer INSTANCE = new MobileServer();
    }

    private MobileServer() {
        //端口  小屏 端口：6666，大屏端口：9067
//        if ((TextUtils.equals(BuildConfig.FLAVOR, BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
//                || TextUtils.equals(BuildConfig.FLAVOR, BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))) {
//            prot = 9067;
//        } else {
//            prot = 6666;
//        }
        prot = 6666;
        mChannels = new ArrayList<>();
        mCmds = new LinkedList<>();
    }

    public void init() {
        thread.start();
    }

    public static MobileServer getInstace() {
        return Holder.INSTANCE;
    }

    public void addChannelHandlerContext(ChannelHandlerContext channelHandlerContext) {
        if (channelHandlerContext != null && !mChannels.contains(channelHandlerContext)) {
            mChannels.add(channelHandlerContext);
        }
    }

    public void removeChannelHandlerContext(ChannelHandlerContext channelHandlerContext) {
        if (channelHandlerContext != null && mChannels.contains(channelHandlerContext)) {
            mChannels.remove(channelHandlerContext);
        }
    }

    public void setCmd(String cmd) {
        nettySendMsg(cmd + DELIMITER);
    }

    private Thread thread = new Thread(() -> {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .childHandler(new MessageServerInitializer());

            ChannelFuture f = b.bind(prot).sync();
            f.addListener((ChannelFutureListener) channelFuture -> {
                if (f.isSuccess()) {
                    CsjlogProxy.getInstance().debug(ConnectConstants.TAG + "NettyServer operationComplete");
                } else {
                    CsjlogProxy.getInstance().debug(ConnectConstants.TAG + "NettyServer not operationComplete");
                }
            });

            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();

        } finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    });

    private void nettySendMsg(String cmd) {
        ByteBuf buf;
        for (ChannelHandlerContext ctx : mChannels) {
            buf = Unpooled.buffer(cmd.getBytes().length);
            buf.writeBytes((cmd).getBytes());
            ChannelFuture channelFuture = ctx.writeAndFlush(buf);
            channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {
                if (channelFuture1.isSuccess()) {
                    CsjlogProxy.getInstance().debug(ConnectConstants.TAG + "send to " + ctx.channel().remoteAddress());
                } else {
                    CsjlogProxy.getInstance().debug(ConnectConstants.TAG + "send failed");
                }
            });
        }
    }

}
