package top.cutexingluo.tools.utils.se.thread;

import cn.hutool.core.util.StrUtil;
import top.cutexingluo.tools.designtools.juc.async.XTAsync;
import top.cutexingluo.tools.utils.se.thread.stream.PrintStream;

import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 * 命令行类
 * <p>使用线程监控命令行结果, 保证命令执行完</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/3/8 17:54
 */
public class ProcessUtil {

    /**
     * 全局读取失败消费者
     */
    protected static Consumer<Exception> inputReadExceptionConsumer = null;
    /**
     * 全局关闭失败消费者
     */
    protected static Consumer<Exception> inputCloseExceptionConsumer = null;


    /**
     * 静默执行且不使用线程池
     *
     * @param cmd 执行命令
     * @return 输出结果, 异常返回 null
     */
    public static String execWaitFor(String cmd) {
        return execWaitFor(cmd, null, null);
    }

    /**
     * 静默执行
     *
     * @param cmd      执行命令
     * @param executor 执行器
     * @return 输出结果, 异常返回 null
     */
    public static String execWaitFor(String cmd, Executor executor) {
        return execWaitFor(cmd, executor, null);
    }

    /**
     * @param cmd              执行命令
     * @param executor         执行器
     * @param exceptionHandler 异常消费者
     * @return 输出结果, 异常返回 null
     */
    public static String execWaitFor(String cmd, Executor executor, Consumer<Exception> exceptionHandler) {
        if (StrUtil.isBlank(cmd)) {
            return null;
        }

        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
            // 取出输出流和错误流的信息
            // 注意：必须要取出在执行命令过程中产生的输出信息，如果不取的话当输出流信息填满jvm存储输出留信息的缓冲区时，线程就回阻塞住
            PrintStream errorStream = new PrintStream(process.getErrorStream());
            PrintStream inputStream = new PrintStream(process.getInputStream());
            if (null != exceptionHandler) {
                errorStream.setInputReadExceptionConsumer(exceptionHandler);
                inputStream.setInputReadExceptionConsumer(exceptionHandler);
            } else if (null != inputReadExceptionConsumer) {
                errorStream.setInputReadExceptionConsumer(inputReadExceptionConsumer);
                inputStream.setInputReadExceptionConsumer(inputReadExceptionConsumer);
            }
            if (null != exceptionHandler) {
                errorStream.setInputCloseExceptionConsumer(exceptionHandler);
                inputStream.setInputCloseExceptionConsumer(exceptionHandler);
            } else if (null != inputCloseExceptionConsumer) {
                errorStream.setInputCloseExceptionConsumer(inputCloseExceptionConsumer);
                inputStream.setInputCloseExceptionConsumer(inputCloseExceptionConsumer);
            }
            if (executor != null) { // 使用线程池执行
                XTAsync.runAsyncCheck(errorStream, executor); // v1.0.5 fix bug
                XTAsync.runAsyncCheck(inputStream, executor);
            } else { // 创建俩线程
                errorStream.start();
                inputStream.start();
            }
            // 等待命令执行完
            process.waitFor();
            // 获取执行结果字符串
            String result = errorStream.stringBuffer.append(inputStream.stringBuffer).append("\n").toString();
            // 输出执行的命令信息

            return result;
        } catch (Exception e) {
            if (exceptionHandler != null) {
                exceptionHandler.accept(e);
            }
            return null;
        } finally {
            if (null != process) {
                ProcessKiller processKiller = new ProcessKiller(process);
                runtime.addShutdownHook(processKiller);
            }
        }
    }
}
