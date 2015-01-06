package info.batey.netty

import spock.lang.Specification

class MemcacheStorageImplTest extends Specification {
    def "store and retrieve a value"() {
        given:
        def underTest = new MemcacheStorageImpl();
        byte[] data = [1,2,3,4,5]
        String key = "key"
        MemcacheStorage.Value value = new MemcacheStorage.Value(data, key, 0);

        when:
        underTest.store(value);
        def retrieve = underTest.retrieve(new MemcacheStorage.Key(key))

        then:
        retrieve == value
    }
}
