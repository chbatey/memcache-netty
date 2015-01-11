package info.batey.netty

import info.batey.netty.storage.Key
import info.batey.netty.storage.MemcacheStorageImpl
import info.batey.netty.storage.Value
import spock.lang.Specification

class MemcacheStorageImplTest extends Specification {

    def underTest = new MemcacheStorageImpl();

    def "store and retrieve a value"() {
        given:
        byte[] data = [1,2,3,4,5]
        String key = "key"
        Value value = new Value(data, key, 0);

        when:
        underTest.store(value);
        def retrieve = underTest.retrieve(new Key(key))

        then:
        retrieve == value
    }

    def "should not replace non-existent value"() {
        given:
        byte[] data = [1,2,3,4,5]
        String key = "key"
        Value value = new Value(data, key, 0);

        when:
        def replaced = underTest.replace(value);

        then:
        !replaced
    }

    def "should replace existent value"() {
        given:
        byte[] data = [1,2,3,4,5]
        byte[] replacedData = [6,7,8,9,10]
        String key = "key"
        Value value = new Value(data, key, 0);
        Value replacedValue = new Value(replacedData, key, 0);
        underTest.store(value)

        when:
        def replaced = underTest.replace(replacedValue);
        def newValue = underTest.retrieve(Key.forName(key))

        then:
        replaced
        newValue == replacedValue
    }

    def "add should store if it doesn't exist"() {
        given:
        byte[] data = [1,2,3,4,5]
        String key = "key"
        Value value = new Value(data, key, 0);

        when:
        def added = underTest.add(value);
        def actualValue = underTest.retrieve(Key.forName(key))

        then:
        added
        actualValue == value
    }

    def "add should not store if it key already exists"() {
        given:
        byte[] oldData = [1,2,3,4,5]
        byte[] newData = [6,7,8,9,10]
        String key = "key"
        Value oldValue = new Value(oldData, key, 0);
        Value newValue = new Value(newData, key, 0);
        underTest.store(oldValue)

        when:
        def added = underTest.add(newValue);
        def actualValue = underTest.retrieve(Key.forName(key))

        then:
        !added
        actualValue == oldValue
    }
}
