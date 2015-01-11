package info.batey.netty.handlers;

import info.batey.netty.messages.MemcacheReplaceMessage;
import info.batey.netty.messages.StorageCommand;
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
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class ReplaceHandlerTest {

    private ReplaceHandler underTest;

    @Mock
    private MemcacheStorage memcacheStorage;

    @Mock
    private ChannelHandlerContext ctx;

    @Mock
    private ByteBufAllocator alloc;

    @Mock
    private ByteBuf buffer;

    @Before
    public void setUp() throws Exception {
        underTest = new ReplaceHandler(memcacheStorage);
        given(ctx.alloc()).willReturn(alloc);
        given(alloc.buffer()).willReturn(buffer);
    }

    @Test
    public void shouldNotStoreNewKey() throws Exception {
        //given
        MemcacheReplaceMessage message = new MemcacheReplaceMessage(new StorageCommand("notexist", 0, 0, new byte[] {123}));

        //when
        underTest.messageReceived(ctx, message);

        //then
        verify(ctx).alloc();
        verify(buffer).writeBytes("NOT_STORED\r\n".getBytes());
        verify(ctx).writeAndFlush(buffer);
    }

    @Test
    public void shouldStorePreviouslySetKey() throws Exception {
        //given
        String key = "exists";
        byte[] data = {123};
        MemcacheReplaceMessage message = new MemcacheReplaceMessage(new StorageCommand(key, 0, 0, data));
        given(memcacheStorage.replace(any(Value.class))).willReturn(true);

        //when
        underTest.messageReceived(ctx, message);

        //then
        verify(ctx).alloc();
        verify(buffer).writeBytes("STORED\r\n".getBytes());
        verify(ctx).writeAndFlush(buffer);
    }


}