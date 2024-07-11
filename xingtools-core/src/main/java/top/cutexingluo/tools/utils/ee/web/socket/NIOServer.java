package top.cutexingluo.tools.utils.ee.web.socket;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * NIOServer
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/1 20:35
 * @since 1.0.4
 */
@Data
@Accessors(chain = true)
public class NIOServer {

    /**
     * 是否运行开关
     */
    protected volatile boolean run = true;

    /**
     * 写消息
     * <p>返回本次写入的消息</p>
     */
    protected Function<SocketChannel, String> writeMsg;


    /**
     * 读消息
     * <p>服务端收到消息</p>
     * <p>返回本次给客户端的消息</p>
     */
    protected BiFunction<SocketChannel, String, String> readMsg;

    //通道管理器
    protected Selector selector;

    /**
     * 获得一个ServerSocket通道，并对该通道做一些初始化的工作
     *
     * @param port 绑定的端口号
     */
    public void initServer(int port) throws IOException {
        // 获得一个ServerSocket通道
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        // 设置通道为非阻塞
        serverChannel.configureBlocking(false);
        // 将该通道对应的ServerSocket绑定到port端口
        serverChannel.socket().bind(new InetSocketAddress(port));
        // 获得一个通道管理器
        this.selector = Selector.open();
        //将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_ACCEPT事件,注册该事件后，
        //当该事件到达时，selector.select()会返回，如果该事件没到达selector.select()会一直阻塞。
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    /**
     * 采用轮询的方式监听selector上是否有需要处理的事件，如果有，则进行处理
     */
    public void listen() throws IOException {
//        System.out.println("服务端启动成功！");
        // 轮询访问selector
        while (isRun()) {
            //当注册的事件到达时，方法返回；否则,该方法会一直阻塞
            selector.select();
            // 获得selector中选中的项的迭代器，选中的项为注册的事件
            Iterator<SelectionKey> ite = this.selector.selectedKeys().iterator();
            while (ite.hasNext()) {
                SelectionKey key = ite.next();
                // 删除已选的key,以防重复处理
                ite.remove();
                // 客户端请求连接事件
                if (key.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    // 获得和客户端连接的通道
                    SocketChannel channel = server.accept();
                    // 设置成非阻塞
                    channel.configureBlocking(false);

                    String s = "";
                    if (writeMsg != null) s = writeMsg.apply(channel);

                    //在这里可以给客户端发送信息哦
                    channel.write(ByteBuffer.wrap(s.getBytes()));
                    //在和客户端连接成功之后，为了可以接收到客户端的信息，需要给通道设置读的权限。
                    channel.register(this.selector, SelectionKey.OP_READ);

                    // 获得了可读的事件
                } else if (key.isReadable()) {
                    read(key);
                }

            }

        }
    }

    /**
     * 处理读取客户端发来的信息 的事件
     */
    public void read(@NotNull SelectionKey key) throws IOException {
        NIOUtil.read(key, this.readMsg);
    }
}
