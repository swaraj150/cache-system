package com.cachesystem.cacheclient;

import com.cachesystem.cacheclient.pipeline.*;
import com.cachesystem.protocol.RequestData;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;

import java.util.concurrent.CompletableFuture;

@Data
public class NettyCacheClientImpl implements Client{
    private final String host;
    private final int port;
    private EventLoopGroup group;
    private Bootstrap bootstrap;
    private ClientProcessingHandler handler;
    private final CompletableFuture<Channel> channelFuture=new CompletableFuture<>();

    public NettyCacheClientImpl(String host, int port) {
        this.host = host;
        this.port = port;
    }


    @Override
    public void connect() throws InterruptedException {
        try{
            this.handler = new ClientProcessingHandler(channelFuture);
            this.group=new NioEventLoopGroup();
            this.bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(
                                    new RequestEncoder(),
//                                    new EncryptionHandler(),// wont work, since netty doesnt trigger encode twice on data type
                                    new ResponseDecoder(),
//                                    new DecryptionHandler(),
                                    handler
                            );
                        }
                    });

            try{
                ChannelFuture f = bootstrap.connect(host, port);
                f.addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        System.out.println("Connected to server");
                    } else {
                        System.err.println("Failed to connect: " + future.cause().getMessage());
                        channelFuture.completeExceptionally(future.cause());
                    }
                });

            }
            catch(Exception e){
                channelFuture.completeExceptionally(e);
            }
        }catch(Exception e){
            channelFuture.completeExceptionally(e);
        }
    }

    @Override
    public CompletableFuture<Void> put(String key, String value) {
        RequestData request = new RequestData();
        request.setKey(key);
        request.setKeyLength(key.length());
        request.setValue(value);
        request.setValueLength(value.length());
        return handler.sendRequest(request);
    }



    @Override
    public CompletableFuture<Void> get(String key) {
        RequestData request = new RequestData();
        request.setKey(key);
        request.setKeyLength(key.length());
        return handler.sendRequest(request);
    }

    @Override
    public void shutdown() {
        group.shutdownGracefully();
    }
}
