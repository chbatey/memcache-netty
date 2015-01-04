package info.batey.netty;

public interface MemcacheStorage {
    void store(Value value);
    Value retrieve(Key key);


    public static class Value {
        private final byte[] data;
        private final String key;
        private final int ttl;

        public Value(byte[] data, String key, int ttl) {
            this.data = data;
            this.key = key;
            this.ttl = ttl;
        }
    }

    public static class Key {
        private final String key;

        public Key(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }
}
