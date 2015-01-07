package info.batey.netty.handlers;

import info.batey.netty.messages.MemcacheGetMessage;
import info.batey.netty.storage.Key;
import info.batey.netty.storage.MemcacheStorage;
import info.batey.netty.storage.Value;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class GetHandlerTest {

    private GetHandler underTest;

    @Mock
    private MemcacheStorage memcacheStorage;

    @Mock
    private ChannelHandlerContext context;

    @Mock
    private ByteBufAllocator alloc;

    @Mock
    private ByteBuf buffer;

    @Before
    public void setUp() throws Exception {
        given(context.alloc()).willReturn(alloc);
        given(alloc.buffer()).willReturn(buffer);
        underTest = new GetHandler(memcacheStorage);
    }

    @Test
    public void returnsNothingIfNothingInStore() throws Exception {
        //given
        String key = "batey";
        MemcacheGetMessage message = new MemcacheGetMessage(key);

        //when
        underTest.channelRead(context, message);

        //then
        verify(buffer).writeBytes("END\r\n".getBytes());
    }

    @Test
    public void writesAndFlushesBytes() throws Exception {
        //given
        String key = "batey";
        MemcacheGetMessage message = new MemcacheGetMessage(key);

        //when
        underTest.messageReceived(context, message);

        //then
        verify(context).writeAndFlush(buffer);
    }

    @Test
    public void sendsValueIfInStore() throws Exception {
        //given
        String key = "batey";
        MemcacheGetMessage message = new MemcacheGetMessage(key);
        byte[] data = new byte[] {1,2,3,4};
        Value value = new Value(data, key, 0);
        given(memcacheStorage.retrieve(any(Key.class))).willReturn(value);

        //when
        underTest.messageReceived(context, message);

        //then
        verify(buffer).writeBytes("VALUE batey 0 4\r\n".getBytes());
        verify(buffer).writeBytes(data);
        verify(buffer).writeBytes("\r\n".getBytes());
        verify(buffer).writeBytes("END\r\n".getBytes());
    }
}
