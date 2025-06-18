package com.cachesystem.cacheserver.pipeline;

import com.cachesystem.protocol.RequestData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class RequestDecoder extends ReplayingDecoder<RequestData> {
    /**
     * Decode from one {@link ByteBuf} to another. This method will be called till either the input
     * {@link ByteBuf} has nothing to read when return from this method or till nothing was read from the input
     * {@link ByteBuf}.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link ByteToMessageDecoder} belongs to
     * @param in  the {@link ByteBuf} from which to read data
     * @param out the {@link List} to which decoded messages should be added
     * @throws Exception is thrown if an error occurs
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int keyLength=in.readInt();
        int valueLength=in.readInt();
        String key=(String) in.readCharSequence(keyLength, StandardCharsets.UTF_8);
        String value=valueLength==0?null:(String) in.readCharSequence(valueLength, StandardCharsets.UTF_8);
        RequestData requestData=RequestData
                .builder()
                .key(key)
                .value(value)
                .build();
//        System.out.println("hello there!: "+key);

        out.add(requestData);
    }
}
