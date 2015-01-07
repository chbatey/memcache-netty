package info.batey.netty.handlers;

import info.batey.netty.messages.MemcacheReplaceMessage;
import info.batey.netty.storage.MemcacheStorage;
import info.batey.netty.storage.Value;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class ReplaceHandler extends SimpleChannelInboundHandler<MemcacheReplaceMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReplaceHandler.class);

    private final MemcacheStorage memcacheStorage;

    public ReplaceHandler(MemcacheStorage memcacheStorage) {
        this.memcacheStorage = memcacheStorage;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, MemcacheReplaceMessage msg) throws Exception {
        LOGGER.debug("Received replace message {}", msg);
        ByteBuf buffer = ctx.alloc().buffer();
        if (memcacheStorage.replace(new Value(msg.getData(), msg.getKey(), msg.getTtl()))) {
            buffer.writeBytes("STORED\r\n".getBytes(Charset.forName("UTF-8")));
        } else {
            buffer.writeBytes("NOT_STORED\r\n".getBytes(Charset.forName("UTF-8")));
        }

        ctx.writeAndFlush(buffer);
    }
}
