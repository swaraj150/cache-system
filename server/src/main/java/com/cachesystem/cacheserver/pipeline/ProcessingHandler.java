package com.cachesystem.cacheserver.pipeline;

import com.cachesystem.cacheserver.CacheStore;
import com.cachesystem.cacheserver.persistence.SegmentedCache;
import com.cachesystem.protocol.RequestData;
import com.cachesystem.protocol.ResponseData;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class ProcessingHandler extends SimpleChannelInboundHandler<RequestData> {
    private final SegmentedCache<String,Object> cache= CacheStore.GLOBAL_CACHE;
//    public ProcessingHandler(){
//        this.cache=new SegmentedCache<String, Object>(64,16);
//    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.info("Client connected : {}",ctx.channel().id().asLongText());
    }



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestData request) throws Exception {
        try{

            String key= request.getKey();
            String value=request.getValue();
//            System.out.println("key"+ key);
//            System.out.println("value"+ value);
            ResponseData responseData= ResponseData.builder().build();
            Object data;
            if(value==null){
                log.info("GET REQUEST INCOMING FOR KEY {}",key);
//                cache.print();
                data=cache.get(key);
            }else{
                log.info("PUT REQUEST INCOMING FOR KEY {}",key);
                cache.put(key,value);
                data=value;
//                cache.print();
            }
            responseData.setData(data);
//            System.out.println(responseData.getData());
            ChannelFuture future=ctx.writeAndFlush(responseData);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
        String errorMsg = "Internal Server Error: " + cause.getMessage();
        log.error(errorMsg);
        cause.printStackTrace();
        ResponseData responseData=ResponseData.builder().data(null).error(errorMsg).build();
//        System.out.println(responseData.toString());
        ctx.writeAndFlush(responseData);
    }

}

