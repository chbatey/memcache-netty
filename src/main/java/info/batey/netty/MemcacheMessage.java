package info.batey.netty;

public class MemcacheMessage {
    //todo change to enum
    private final Command command;
    private final String key;
    private final int flags;
    private final int ttl;
    private final int numberOfBytes;

    public MemcacheMessage(Command command, String key, int flags, int ttl, int numberOfBytes) {
        this.command = command;
        this.key = key;
        this.flags = flags;
        this.ttl = ttl;
        this.numberOfBytes = numberOfBytes;
    }

    public Command getCommand() {
        return command;
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
        return "MemcacheMessage{" +
                "command='" + command + '\'' +
                ", key='" + key + '\'' +
                ", flags=" + flags +
                ", ttl=" + ttl +
                ", numberOfBytes=" + numberOfBytes +
                '}';
    }
}
