package com.cachesystem.cacheserver.pipeline;

import com.cachesystem.protocol.ResponseData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public class ResponseEncoder extends MessageToByteEncoder<ResponseData> {

    /**
     * Encode a message into a {@link ByteBuf}. This method will be called for each written message that can be handled
     * by this encoder.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link MessageToByteEncoder} belongs to
     * @param msg the message to encode
     * @param out the {@link ByteBuf} into which the encoded message will be written
     * @throws Exception is thrown if an error occurs
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, ResponseData msg, ByteBuf out) throws Exception {
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        ObjectOutputStream oos=new ObjectOutputStream(bos);
        oos.writeObject(msg.getData());
        oos.flush();
        byte[] bytes=bos.toByteArray();
        int size=bytes.length;
        out.writeInt(size);
        out.writeBytes(bytes);
    }
}
