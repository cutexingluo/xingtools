package top.cutexingluo.tools.utils.ee.web.socket;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.function.BiFunction;

/**
 * NIO  Utils
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/1 20:59
 * @since 1.0.4
 */
public class NIOUtil {

    /**
     * 处理读取SelectionKey发来的信息 的事件
     * <p>readMsg 返回 null 则不返回消息</p>
     */
    public static void read(@NotNull SelectionKey key, BiFunction<SocketChannel, String, String> readMsg) throws IOException {
        // 服务器可读取消息:得到事件发生的Socket通道
        SocketChannel channel = (SocketChannel) key.channel();
        // 创建读取的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(10);
        channel.read(buffer);
        byte[] data = buffer.array();
        String msg = new String(data).trim();
        String ret = null;
        if (readMsg != null) ret = readMsg.apply(channel, msg);
        if (ret != null) {
            ByteBuffer outBuffer = ByteBuffer.wrap(msg.getBytes());
            channel.write(outBuffer);// 将消息回送给通道
        }
    }
}
