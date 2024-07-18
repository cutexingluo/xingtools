package top.cutexingluo.tools.utils.ee.web.socket;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * SocketClient
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/1 20:16
 * @since 1.0.4
 */
@Data
@AllArgsConstructor
public class SocketClient implements Closeable {

    protected Socket socket;

    public SocketClient(String host, int port) throws IOException {
        socket = new Socket(host, port);
    }


    @Override
    public void close() throws IOException {
        socket.close();
    }

    /**
     * 发送消息
     */
    public void send(@NotNull String msg) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(msg.getBytes());
        outputStream.flush();
    }
}
