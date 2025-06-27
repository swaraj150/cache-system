package com.cachesystem.cacheserver.pipeline;

import com.cachesystem.protocol.RequestData;
import com.cachesystem.utils.AES;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
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

        RequestData requestData=new RequestData();
        if (in.readableBytes() < 4) return;
        in.markReaderIndex();
        int encryptedKeyLength = in.readInt();
        if (encryptedKeyLength==0 || in.readableBytes() < encryptedKeyLength) {
            in.resetReaderIndex();
            return;
        }
        byte[] encryptedKeyBytes = new byte[encryptedKeyLength];
        in.readBytes(encryptedKeyBytes);
//        System.out.println(Arrays.toString(encryptedKeyBytes));

        byte[] decryptedKeyBytes = AES.decrypt(encryptedKeyBytes);
        String key=new String(decryptedKeyBytes,StandardCharsets.UTF_8);
        requestData.setKey(key);
        requestData.setKeyLength(key.length());
        if (in.readableBytes() < 4) return;
        in.markReaderIndex();
        int encryptedValueLength = in.readInt();
        if(encryptedValueLength==0){
            requestData.setValueLength(0);
            requestData.setValue(null);
        }else{
            if (in.readableBytes() < encryptedValueLength) {
                in.resetReaderIndex();
                return;
            }
            byte[] encryptedValueBytes = new byte[encryptedValueLength];
            in.readBytes(encryptedValueBytes);
            byte[] decryptedValueBytes = AES.decrypt(encryptedValueBytes);
            String value=new String(decryptedValueBytes,StandardCharsets.UTF_8);
            requestData.setValue(value);
            requestData.setValueLength(value.length());
        }
        out.add(requestData);
    }
}
