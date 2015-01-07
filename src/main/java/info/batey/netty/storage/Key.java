package info.batey.netty.storage;

/**
* Created by chbatey on 07/01/2015.
*/
public final class Key {

    public static Key forName(String name) {
        return new Key(name);
    }

    private final String key;

    public Key(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Key key1 = (Key) o;

        if (key != null ? !key.equals(key1.key) : key1.key != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }
}
