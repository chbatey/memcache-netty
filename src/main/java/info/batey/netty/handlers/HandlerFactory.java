package info.batey.netty.handlers;

import info.batey.netty.CommandType;
import info.batey.netty.MemcacheStorage;
import io.netty.channel.ChannelHandlerAdapter;

public class HandlerFactory {

    private final MemcacheStorage memcacheStorage;

    public HandlerFactory(MemcacheStorage memcacheStorage) {
        this.memcacheStorage = memcacheStorage;
    }

    public ChannelHandlerAdapter createHandler(CommandType commandTypeType) {
        switch(commandTypeType) {
            case GET : {
                return new GetHandler(memcacheStorage);
            }
            case SET : {
                return new SetHandler(memcacheStorage);
            }
            default: {
                throw new IllegalArgumentException("Unknown command type: " + commandTypeType);
            }
        }
    }
}
