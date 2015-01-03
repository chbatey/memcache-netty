package info.batey.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ReplayingDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.List;

public class MemcacheDecoder extends ReplayingDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemcacheDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
//        int readableBytes = in.readableBytes();
//        byte[] bytes = new byte[readableBytes];
//        in.readBytes(bytes);

        int bytesToTake = in.bytesBefore((byte) ' ');
        String command = in.readBytes(bytesToTake).toString(Charset.defaultCharset());
        in.readByte();

        switch (command) {
            case "get": {
                handleGetMessage(in, out, ctx);
                break;
            }
            case "set": {
            handleSetMessage(in, out, ctx);
                break;
            }
        }

    }

    private void handleGetMessage(ByteBuf in, List<Object> out, ChannelHandlerContext ctx) {
        LOGGER.debug("Handling get message");
        int bytesToTake;
        bytesToTake = in.bytesBefore((byte) '\r');
        String key = in.readBytes(bytesToTake).toString(Charset.defaultCharset());
        in.readByte();


        MemcacheMessage get = new MemcacheMessage(Command.GET, key, 0, 0, 0);
        LOGGER.debug("Handling get message {}", get);
        out.add(get);
        ChannelPipeline pipeline = ctx.pipeline();
        pipeline.addLast(new GetHandler());
    }

    private void handleSetMessage(ByteBuf in, List<Object> out, ChannelHandlerContext ctx) {
        int bytesToTake;
        bytesToTake = in.bytesBefore((byte) ' ');
        String key = in.readBytes(bytesToTake).toString(Charset.defaultCharset());
        in.readByte();

        bytesToTake = in.bytesBefore((byte) ' ');
        Integer flags = Integer.parseInt(in.readBytes(bytesToTake).toString(Charset.defaultCharset()));
        in.readByte();

        bytesToTake = in.bytesBefore((byte) ' ');
        Integer ttl = Integer.parseInt(in.readBytes(bytesToTake).toString(Charset.defaultCharset()));
        in.readByte();

        bytesToTake = in.bytesBefore((byte) '\r');
        Integer number = Integer.parseInt(in.readBytes(bytesToTake).toString(Charset.defaultCharset()));
        in.readByte();

        out.add(new MemcacheMessage(Command.SET, key, flags, ttl, number));

        ChannelPipeline pipeline = ctx.pipeline();
        pipeline.addLast(new SetHandler());
    }
}
