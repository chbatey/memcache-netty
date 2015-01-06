package info.batey.netty.handlers;

import info.batey.netty.MemcacheGetMessage;
import info.batey.netty.MemcacheStorage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetHandler extends SimpleChannelInboundHandler<MemcacheGetMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetHandler.class);

    private final MemcacheStorage memcacheStorage;

    public GetHandler(MemcacheStorage memcacheStorage) {
        this.memcacheStorage = memcacheStorage;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, MemcacheGetMessage memcacheGetMessage) throws Exception {
        LOGGER.debug("Received memcache message {}", memcacheGetMessage);

        MemcacheStorage.Value retrieve = memcacheStorage.retrieve(new MemcacheStorage.Key(memcacheGetMessage.getKey()));

        ByteBuf buffer = ctx.alloc().buffer();
        if (retrieve != null) {
            buffer.writeBytes(String.format("VALUE %s 0 %d\r\n", retrieve.getKey(), retrieve.getData().length).getBytes());
            buffer.writeBytes(retrieve.getData());
            buffer.writeBytes("\r\n".getBytes());
        }
        buffer.writeBytes("END\r\n".getBytes());
        ctx.writeAndFlush(buffer);
    }
}
