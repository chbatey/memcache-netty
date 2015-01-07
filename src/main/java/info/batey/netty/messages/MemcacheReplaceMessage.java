package info.batey.netty.messages;

import java.util.Arrays;

public class MemcacheReplaceMessage {
    private final String key;
    private final int flags;
    private final int ttl;
    private final int numberOfBytes;
    private final byte[] data;

    public MemcacheReplaceMessage(String key, int flags, int ttl,  byte[] data) {
        this.key = key;
        this.flags = flags;
        this.ttl = ttl;
        this.numberOfBytes = data.length;
        this.data = data;
    }

    public String getKey() {
        return key;
    }

    public int getFlags() {
        return flags;
    }

    public int getNumberOfBytes() {
        return numberOfBytes;
    }

    public int getTtl() {
        return ttl;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        return "MemcacheReplaceMessage{" +
                "key='" + key + '\'' +
                ", flags=" + flags +
                ", ttl=" + ttl +
                ", numberOfBytes=" + numberOfBytes +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
