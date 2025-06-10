package com.cachesystem.client;

import com.cachesystem.protocol.ResponseData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

public class ResponseDecoder
        extends ReplayingDecoder<ResponseData> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }
        in.markReaderIndex();
        int length=in.readInt();
        System.out.println("length: "+length);
        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }
        ResponseData data = new ResponseData();
        byte[] bytes=new byte[length];
        in.readBytes(bytes);
        ByteArrayInputStream bis=new ByteArrayInputStream(bytes);
        ObjectInputStream ois=new ObjectInputStream(bis);
        data.setData(ois.readObject());
        out.add(data);
    }
}
