package info.batey.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.List;

public class MemcacheDecoder extends ReplayingDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemcacheDecoder.class);


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        int bytesToTake = in.bytesBefore((byte) ' ');
        String command = in.readBytes(bytesToTake).toString(Charset.defaultCharset()).trim();
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
            default: {
                throw new RuntimeException("Unexpected command: |" + command + "|");
            }
        }

    }

    private void handleGetMessage(ByteBuf in, List<Object> out, ChannelHandlerContext ctx) {
        LOGGER.debug("Handling get message");
        int bytesToTake;
        bytesToTake = in.bytesBefore((byte) '\r');
        String key = in.readBytes(bytesToTake).toString(Charset.defaultCharset());
        in.readByte();

        MemcacheGetMessage get = new MemcacheGetMessage(key);
        LOGGER.debug("Handling get message {}", get);
        out.add(get);
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
        Integer number = Integer.parseInt(in.readBytes(bytesToTake).toString(Charset.defaultCharset()).trim());
        in.readByte(); // the \r
        in.readByte(); // the \n

        byte[] data = new byte[number];
        in.readBytes(data);
        in.readByte(); // the \r
        in.readByte(); // the \n

        out.add(new MemcacheSetMessage(key, flags, ttl, number, data));
    }
}
