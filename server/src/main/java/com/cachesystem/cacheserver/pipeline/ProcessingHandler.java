package com.cachesystem.cacheserver.pipeline;

import com.cachesystem.cacheserver.persistence.SegmentedCache;
import com.cachesystem.protocol.RequestData;
import com.cachesystem.protocol.ResponseData;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

public class ProcessingHandler extends ChannelInboundHandlerAdapter {
    private final SegmentedCache<String,Object> cache;
    public ProcessingHandler(){
        this.cache=new SegmentedCache<String, Object>(64,16);
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg){

        RequestData request=(RequestData) msg;

        String key= request.getKey();
        String value=request.getValue();
        System.out.println("key"+ key);
        ResponseData responseData= ResponseData.builder().build();
        Object data;
        if(value==null){
            data=cache.get(key);
        }else{
            cache.put(key,value);
            data=value;
        }
        responseData.setData(data);
        System.out.println(responseData.getData());
        ChannelFuture future=ctx.writeAndFlush(responseData);
//        future.addListener(ChannelFutureListener.CLOSE);
    }

}

