package info.batey.netty.storage;

import java.util.Arrays;

public final class Value {
    private final byte[] data;
    private final String key;
    private final int ttl;

    public Value(byte[] data, String key, int ttl) {
        this.data = data;
        this.key = key;
        this.ttl = ttl;
    }

    public byte[] getData() {
        return data;
    }

    public String getKey() {
        return key;
    }

    public int getTtl() {
        return ttl;
    }

    @Override
    public String toString() {
        return "Value{" +
                "data=" + Arrays.toString(data) +
                ", key='" + key + '\'' +
                ", ttl=" + ttl +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Value value = (Value) o;

        return ttl == value.ttl && Arrays.equals(data, value.data) && !(key != null ? !key.equals(value.key) : value.key != null);
    }

    @Override
    public int hashCode() {
        int result = data != null ? Arrays.hashCode(data) : 0;
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + ttl;
        return result;
    }
}
