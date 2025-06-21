package com.cachesystem.cacheserver.pipeline;

import com.cachesystem.protocol.ResponseData;
import com.cachesystem.utils.AES;
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
        int l1=0,l2=0;
        byte[] encryptedBytes1=new byte[0],encryptedBytes2=new byte[0];
        if(msg.getData()!=null){
            ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
            ObjectOutputStream oos1 = new ObjectOutputStream(bos1);
            oos1.writeObject(msg.getData());
            oos1.flush();
            byte[] plain=bos1.toByteArray();

            encryptedBytes1 = AES.encrypt(plain);
            l1=encryptedBytes1.length;


        }
        if(msg.getError()!=null){
            ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
            ObjectOutputStream oos2 = new ObjectOutputStream(bos2);
            oos2.writeObject(msg.getError());
            oos2.flush();
            byte[] plain=bos2.toByteArray();

            encryptedBytes2 =  AES.encrypt(plain);
            l2= encryptedBytes2.length;
        }
        out.writeInt(l1);
        out.writeInt(l2);
        out.writeBytes(encryptedBytes1);
        out.writeBytes(encryptedBytes2);
    }
}
