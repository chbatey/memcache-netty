package info.batey.netty;

public class MemcacheGetMessage {
    private final String key;

    public MemcacheGetMessage(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "MemcacheGetMessage{" +
                "key='" + key + '\'' +
                '}';
    }
}
