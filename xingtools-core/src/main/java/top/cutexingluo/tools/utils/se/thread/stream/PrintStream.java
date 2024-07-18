package top.cutexingluo.tools.utils.se.thread.stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

/**
 * 对输入流结果进行读取防止线程阻塞
 * <p>监视输出</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/3/8 17:34
 * @since 1.0.4
 */
public class PrintStream extends Thread {

    protected InputStream inputStream = null;
    protected BufferedReader bufferedReader = null;
    public StringBuffer stringBuffer = new StringBuffer();

    /**
     * 读取失败消费者
     */
    protected Consumer<Exception> inputReadExceptionConsumer = null;
    /**
     * 关闭失败消费者
     */
    protected Consumer<Exception> inputCloseExceptionConsumer = null;


    public PrintStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public PrintStream setInputReadExceptionConsumer(Consumer<Exception> inputReadExceptionConsumer) {
        this.inputReadExceptionConsumer = inputReadExceptionConsumer;
        return this;
    }

    public PrintStream setInputCloseExceptionConsumer(Consumer<Exception> inputCloseExceptionConsumer) {
        this.inputCloseExceptionConsumer = inputCloseExceptionConsumer;
        return this;
    }

    @Override
    public void run() {
        try {
            if (null == inputStream) {
                return;
            }
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
        } catch (Exception e) {
//            logger.error("读取输入流出错了！错误信息：" + e.getMessage());
            if (inputReadExceptionConsumer != null) {
                inputReadExceptionConsumer.accept(e);
            }
        } finally {
            try {
                if (null != bufferedReader) {
                    bufferedReader.close();
                }
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (IOException e) {
//                logger.error("调用PrintStream读取输出流后，关闭流时出错！");
                if (inputCloseExceptionConsumer != null) {
                    inputCloseExceptionConsumer.accept(e);
                }
            }
        }
    }
}
