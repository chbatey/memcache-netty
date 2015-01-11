package info.batey.netty.handlers;

import info.batey.netty.messages.MemcacheStorageMessage;
import info.batey.netty.storage.MemcacheStorage;
import info.batey.netty.storage.Value;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class OptionalStorageHandler<T extends MemcacheStorageMessage> extends SimpleChannelInboundHandler<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OptionalStorageHandler.class);

    private final MemcacheStorage memcacheStorage;

    public OptionalStorageHandler(Class<? extends T> clazz, MemcacheStorage memcacheStorage) {
        super(clazz, true);
        this.memcacheStorage = memcacheStorage;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, T msg) throws Exception {
        LOGGER.debug("Received add message {}", msg);
        ByteBuf buffer = ctx.alloc().buffer();
        if (memcacheStorage.add(new Value(msg.getData(), msg.getKey(), msg.getTtl()))) {
            buffer.writeBytes("STORED\r\n".getBytes(Charset.forName("UTF-8")));
        } else {
            buffer.writeBytes("NOT_STORED\r\n".getBytes(Charset.forName("UTF-8")));
        }

        ctx.writeAndFlush(buffer);
    }
}
