package com.cachesystem.cacheclient.pipeline;

import com.cachesystem.protocol.RequestData;
import com.cachesystem.utils.AES;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class RequestEncoder extends MessageToByteEncoder<RequestData> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RequestData msg, ByteBuf out) throws Exception {
        try{
            System.out.println("Encoding");
            byte[] encryptedKeyBytes=new byte[0];
            byte[] encryptedValueBytes=new byte[0];

            byte[] plainKeyBytes =  msg.getKey().getBytes(StandardCharsets.UTF_8);
            encryptedKeyBytes = AES.encrypt(plainKeyBytes);
            System.out.println(Arrays.toString(encryptedKeyBytes));
            if(msg.getValue()!=null){
                byte[] plainValueBytes = msg.getValue().getBytes(StandardCharsets.UTF_8);
                encryptedValueBytes = AES.encrypt(plainValueBytes);

            }
            out.writeInt(encryptedKeyBytes.length);
            out.writeBytes(encryptedKeyBytes);
            out.writeInt(encryptedValueBytes.length);
            out.writeBytes(encryptedValueBytes);

        }catch (Exception e){
            throw new RuntimeException(e);
        }



    }
}
