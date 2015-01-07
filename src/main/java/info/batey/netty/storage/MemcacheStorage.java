package info.batey.netty.storage;

public interface MemcacheStorage {
    void store(Value value);
    Value retrieve(Key key);
    boolean replace(Value value);
}
