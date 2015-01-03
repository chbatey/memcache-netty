package info.batey.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetHandler extends ChannelHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MemcacheMessage memcacheMessage = (MemcacheMessage) msg;
        LOGGER.debug("Received memcache message {}", memcacheMessage);
        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeBytes("END\r\n".getBytes());
        ctx.writeAndFlush(buffer);
    }
}