package info.batey.netty;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemcacheStorageImpl implements MemcacheStorage {

    private Map<Key, Value> cache = new ConcurrentHashMap<>();

    @Override
    public void store(Value value) {
        cache.put(new Key(value.getKey()), value);
    }

    @Override
    public Value retrieve(Key key) {
        return cache.get(key);
    }
}
