package info.batey.netty.handlers;

import info.batey.netty.MemcacheSetMessage;
import info.batey.netty.MemcacheStorage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetHandler extends ChannelHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetHandler.class);

    private final MemcacheStorage memcacheStorage;

    public SetHandler(MemcacheStorage memcacheStorage) {
        this.memcacheStorage = memcacheStorage;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MemcacheSetMessage memcacheSetMessage = (MemcacheSetMessage) msg;
        LOGGER.debug("Received memcache message {}", memcacheSetMessage);
        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeBytes("STORED\r\n".getBytes());
        ctx.writeAndFlush(buffer);
    }
}
