package info.batey.netty.handlers

import info.batey.netty.messages.MemcacheSetMessage
import info.batey.netty.messages.MemcacheStorageMessage
import info.batey.netty.storage.MemcacheStorage
import info.batey.netty.storage.Value
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import io.netty.channel.ChannelHandlerContext
import spock.lang.Specification

class SetHandlerTest extends Specification {
    def "Should store in the memcache storage"() {
        given:
        def memcacheStorage = Mock(MemcacheStorage)
        def contextMock = Mock(ChannelHandlerContext)
        ByteBufAllocator alloc = Mock(ByteBufAllocator)
        contextMock.alloc() >> alloc
        ByteBuf mockByteBuffer = Mock(ByteBuf)
        alloc.buffer() >> mockByteBuffer
        def underTest = new SetHandler(memcacheStorage)
        byte[] data = [1,2]
        def key = "batey"
        def memcacheMessage = new MemcacheSetMessage(new MemcacheStorageMessage(key, 0, 0, data));

        when:
        underTest.messageReceived(contextMock, memcacheMessage)

        then:
        1 * mockByteBuffer.writeBytes("STORED\r\n".getBytes())
        1 * contextMock.writeAndFlush(mockByteBuffer)
        1 * memcacheStorage.store(new Value(data, key, 0))
    }
}
