package com.cachesystem.cacheclient.pipeline;

import com.cachesystem.protocol.RequestData;
import com.cachesystem.protocol.ResponseData;
import io.netty.channel.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Slf4j
public class ClientProcessingHandler extends ChannelInboundHandlerAdapter {
    private Channel channel;
    private final CompletableFuture<Channel> channelFuture;



    @Override
    public void channelActive(ChannelHandlerContext ctx)
            throws Exception {
        log.info("Channel is active now");
        this.channel=ctx.channel();
        channelFuture.complete(ctx.channel());

    }
    public CompletableFuture<Void> sendRequest(RequestData request) {

        return channelFuture.thenCompose(ch->{
            try{
                if(ch.isActive()){
                    ChannelFuture writeFuture = ch.writeAndFlush(request);
                    return toCompletableFuture(writeFuture);
                }
                else {
                    return CompletableFuture.failedFuture(
                            new IllegalStateException("Channel is not active"));
                }

            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        });


    }
    private CompletableFuture<Void> toCompletableFuture(ChannelFuture channelFuture) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        channelFuture.addListener(future -> {
            if (future.isSuccess()) {

                completableFuture.complete(null);
            } else {
                completableFuture.completeExceptionally(future.cause());
            }
        });
        return completableFuture;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        ResponseData data=(ResponseData)(msg);
        if(data.getData()!=null){
            log.info("result: {}",data.getData());
        }
        else if(data.getError()!=null){
            log.error("error: {}",data.getError());
        }
        else{
            log.error("Server Error");
        }


    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();

        ctx.close();
    }

}
