package info.batey.netty.messages;

import java.util.Arrays;

public class StorageCommand {
    private final String key;
    private final int flags;
    private final int ttl;
    private final int numberOfBytes;
    private final byte[] data;

    public StorageCommand(String key, int flags, int ttl, byte[] data) {
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

    public int getTtl() {
        return ttl;
    }

    public int getNumberOfBytes() {
        return numberOfBytes;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        return "StorageCommand{" +
                "key='" + key + '\'' +
                ", flags=" + flags +
                ", ttl=" + ttl +
                ", numberOfBytes=" + numberOfBytes +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
