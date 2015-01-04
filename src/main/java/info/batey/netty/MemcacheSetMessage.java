package info.batey.netty;

public class MemcacheSetMessage {
    //todo change to enum
    private final String key;
    private final int flags;
    private final int ttl;
    private final int numberOfBytes;

    public MemcacheSetMessage(String key, int flags, int ttl, int numberOfBytes) {
        this.key = key;
        this.flags = flags;
        this.ttl = ttl;
        this.numberOfBytes = numberOfBytes;
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

    @Override
    public String toString() {
        return "MemcacheSetMessage{" +
                "key='" + key + '\'' +
                ", flags=" + flags +
                ", ttl=" + ttl +
                ", numberOfBytes=" + numberOfBytes +
                '}';
    }
}
