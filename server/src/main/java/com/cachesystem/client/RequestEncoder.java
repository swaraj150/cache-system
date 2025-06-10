package com.cachesystem.client;

import com.cachesystem.protocol.RequestData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


import java.nio.charset.StandardCharsets;

public class RequestEncoder extends MessageToByteEncoder<RequestData> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RequestData msg, ByteBuf out) throws Exception {
        out.writeInt(msg.getKeyLength());
        out.writeInt(msg.getValueLength());
        out.writeCharSequence(msg.getKey(),StandardCharsets.UTF_8);
        out.writeCharSequence(msg.getValue()==null?"":msg.getValue(),StandardCharsets.UTF_8);
//        for (int i = out.readerIndex(); i < out.writerIndex(); i++) {
//            byte b = out.getByte(i);
//            System.out.printf("Byte at %d = 0x%02X (%d)%n", i, b, b);
//        }

    }
}
