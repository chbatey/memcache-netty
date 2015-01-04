package info.batey.netty.handlers;

import info.batey.netty.MemcacheGetMessage;
import info.batey.netty.MemcacheStorage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetHandler extends ChannelHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetHandler.class);

    private final MemcacheStorage memcacheStorage;

    public GetHandler(MemcacheStorage memcacheStorage) {
        this.memcacheStorage = memcacheStorage;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MemcacheGetMessage memcacheGetMessage = (MemcacheGetMessage) msg;
        LOGGER.debug("Received memcache message {}", memcacheGetMessage);
        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeBytes("END\r\n".getBytes());
        ctx.writeAndFlush(buffer);
    }
}
