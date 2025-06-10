package com.cachesystem.client;

import com.cachesystem.protocol.RequestData;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
;

public class ClientProcessingHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx)
            throws Exception {
        RequestData msg = new RequestData();
        msg.setKeyLength(3);
        msg.setValueLength(42);
        msg.setKey("abc");
        msg.setValue("all work and no play makes jack a dull boy");
        RequestData msg1 = new RequestData();
        msg1.setKeyLength(3);
        msg1.setValueLength(0);
        msg1.setKey("def");
        msg1.setValue("");
        ctx.write(msg);
        ctx.writeAndFlush(msg1);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        System.out.println("message: "+msg.toString());
//        ctx.close();
    }
}
