package com.cachesystem.cacheclient.pipeline;

import com.cachesystem.protocol.ResponseData;
import com.cachesystem.utils.AES;
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
        byte[] bytes=null;
        if (in.readableBytes() < 8) {
            return;
        }
        ResponseData data = new ResponseData();
        in.markReaderIndex();
        int dataLength=in.readInt();
        in.markReaderIndex();
        int errorLength=in.readInt();

        if(dataLength!=0){
            if (in.readableBytes() < dataLength) {
                in.resetReaderIndex();
                return;
            }
            bytes=new byte[dataLength];
            in.readBytes(bytes);
            bytes= AES.decrypt(bytes);
            ByteArrayInputStream bis=new ByteArrayInputStream(bytes);
            ObjectInputStream ois=new ObjectInputStream(bis);
            data.setData(ois.readObject());
        }
        if(errorLength!=0){
            if (in.readableBytes() < errorLength) {
                in.resetReaderIndex();
                return;
            }
            bytes=new byte[errorLength];
            in.readBytes(bytes);
            bytes= AES.decrypt(bytes);
            ByteArrayInputStream bis1=new ByteArrayInputStream(bytes);
            ObjectInputStream ois1=new ObjectInputStream(bis1);
            data.setError(ois1.readObject());
        }
        out.add(data);



    }
}
