package com.cachesystem.cacheclient.pipeline;

import com.cachesystem.cacheclient.RetryPolicy;
import com.cachesystem.protocol.RequestData;
import com.cachesystem.protocol.ResponseData;
import io.netty.channel.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

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
        CompletableFuture<Void> promise=new CompletableFuture<>();
        sendRequestWithRetry(request,0,promise);
        return promise;
    }

    private void sendRequestWithRetry(RequestData requestData,int attempt,CompletableFuture<Void> promise){
        channelFuture.thenAccept(channel1 -> {
            if(!channel1.isActive()){
                retryOrFail(requestData,attempt,promise,"Channel Not Active");
            }
            ChannelFuture writeFuture=channel1.writeAndFlush(requestData);
            writeFuture.addListener(f->{
                if(f.isSuccess()){
                    promise.complete(null);
                }else {
                    retryOrFail(requestData,attempt,promise,f.cause().getMessage());

                }
            });
        });

    }

    private void retryOrFail(RequestData requestData,int attempt,CompletableFuture<Void> promise,String cause){
        if(attempt+1>= RetryPolicy.attempts){
            promise.completeExceptionally(new IllegalStateException("Send Failed after retries: "+cause));
        }else{
            int next=attempt+1;
            long delayMs=RetryPolicy.delayForAttempt(next);
            channelFuture.thenAccept(ch->
                    ch.eventLoop().schedule(
                            ()->sendRequestWithRetry(requestData,next,promise),delayMs, TimeUnit.MILLISECONDS
                    )
            );
        }
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
