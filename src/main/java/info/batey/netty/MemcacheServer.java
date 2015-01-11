package info.batey.netty;

import info.batey.netty.handlers.*;
import info.batey.netty.messages.MemcacheAddMessage;
import info.batey.netty.messages.MemcacheReplaceMessage;
import info.batey.netty.storage.MemcacheStorageImpl;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class MemcacheServer implements Runnable {

    private int port;

    public MemcacheServer(int port) {
        this.port = port;
    }

    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            MemcacheStorageImpl memcacheStorage = new MemcacheStorageImpl();
                            ch.pipeline().addLast(new MemcacheDecoder());
                            ch.pipeline().addLast(new SetHandler(memcacheStorage));
                            ch.pipeline().addLast(new GetHandler(memcacheStorage));
                            ch.pipeline().addLast(new OptionalStorageHandler<>(MemcacheAddMessage.class, memcacheStorage::add));
                            ch.pipeline().addLast(new OptionalStorageHandler<>(MemcacheReplaceMessage.class, memcacheStorage::replace));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 9090;
        }
        new MemcacheServer(port).run();
    }
}
