package top.cutexingluo.tools.utils.ee.web.socket;

import cn.hutool.core.io.IoUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * SocketServer
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/1 19:39
 * @since 1.0.4
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class SocketServer implements Closeable {

    protected ServerSocket serverSocket;

    protected String endTag;

    public SocketServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public Socket accept() throws IOException {
        return serverSocket.accept();
    }


    @Override
    public void close() throws IOException {
        serverSocket.close();
    }


    /**
     * 读取任务
     *
     * @param socket            socket
     * @param msgConsumer       消息
     * @param exceptionConsumer 异常
     * @return 任务
     */
    public Runnable readTask(Socket socket, Consumer<String> msgConsumer, Consumer<Exception> exceptionConsumer) {
        return () -> {
            try {
                InputStream inputStream = socket.getInputStream();
                BufferedReader reader = IoUtil.getUtf8Reader(inputStream);
                String msg;
                if (endTag != null) {
                    while ((msg = reader.readLine()) != null && !endTag.equals(msg)) {
                        if (msgConsumer != null) msgConsumer.accept(msg);
                    }
                } else {
                    while ((msg = reader.readLine()) != null) {
                        if (msgConsumer != null) msgConsumer.accept(msg);
                    }
                }
                inputStream.close();
                socket.close();
            } catch (IOException e) {
                if (exceptionConsumer != null) {
                    exceptionConsumer.accept(e);
                }
            }
        };
    }

    /**
     * 读取任务
     *
     * @param msgConsumer       消息
     * @param exceptionConsumer 异常
     * @return 任务
     */
    public Runnable newReadTask(Consumer<String> msgConsumer, Consumer<Exception> exceptionConsumer) throws IOException {
        Socket socket = serverSocket.accept();
        return readTask(socket, msgConsumer, exceptionConsumer);
    }
}
