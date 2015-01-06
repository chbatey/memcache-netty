package info.batey.netty.handlers;

import info.batey.netty.MemcacheSetMessage;
import info.batey.netty.MemcacheStorage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetHandler extends SimpleChannelInboundHandler<MemcacheSetMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetHandler.class);

    private final MemcacheStorage memcacheStorage;

    public SetHandler(MemcacheStorage memcacheStorage) {
        this.memcacheStorage = memcacheStorage;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, MemcacheSetMessage memcacheSetMessage) throws Exception {
        LOGGER.debug("Received memcache message {}", memcacheSetMessage);

        MemcacheStorage.Value value = new MemcacheStorage.Value(memcacheSetMessage.getData(), memcacheSetMessage.getKey(), memcacheSetMessage.getTtl());
        memcacheStorage.store(value);

        ByteBufAllocator alloc = ctx.alloc();
        ByteBuf buffer = alloc.buffer();
        buffer.writeBytes("STORED\r\n".getBytes());
        ctx.writeAndFlush(buffer);
    }
}
