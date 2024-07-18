package top.cutexingluo.tools.utils.se.file;

import cn.hutool.core.io.LineHandler;
import cn.hutool.core.io.file.FileAppender;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.Tailer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * hutools IO 封装操作类
 * <p>不建议使用，建议直接使用 new FileReader 等</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/3 14:41
 */
public class XTIOhuUtil {
    // 获取HuTool FileReader  一个参数默认UTF-8
    public static FileReader getFileReader(String fileName) {
        return new FileReader(fileName);
    }

    // 获取HuTool FileWriter
    public static FileWriter getFileWriter(String fileName) throws IOException {
        return new FileWriter(fileName);
    }

    // 获取HuTool FileAppender
    public static FileAppender getFileAppender(File file, int capacity, boolean isNewLineMode) throws IOException {
        return new FileAppender(file, capacity, isNewLineMode);
    }

    // 有时候我们要启动一个线程实时“监控”文件的变化，
    // 比如有新内容写出到文件时，我们可以及时打印出来，这个功能非常类似于Linux下的tail -f命令。
    // start 开启
    //注意 此方法会阻塞当前线程  注意开线程
    public static Tailer getTailer(File file, LineHandler lineHandler, int initReadLine) {
        return new Tailer(file, lineHandler, initReadLine);
    }

    public static Tailer getTailer(File file, int initReadLine) {
        return new Tailer(file, Tailer.CONSOLE_HANDLER, initReadLine);
    }

    public static Tailer getTailer(File file) {
        return new Tailer(file, Tailer.CONSOLE_HANDLER, 2);
    }

    //******************************聚合方法区

    public static String getFileReaderString(String fileName) {
        return new FileReader(fileName).readString();
    }


    // 写入模式
    public static void writeFileWriterString(String fileName, String content) throws IOException {
        new FileWriter(fileName).write(content);
    }

    // 追加模式
    public static void appendFileWriterString(String fileName, String content) throws IOException {
        new FileWriter(fileName).append(content);
    }


    // 多行追加
    public static String fileAppenderRun(FileAppender fileAppender, String... contents) {
        Arrays.stream(contents).forEach(fileAppender::append);
        return fileAppender.flush().toString();
    }

    // 多行追加
    public static String fileAppenderRun(FileAppender fileAppender, List<String> contents) {
        contents.forEach(fileAppender::append);
        return fileAppender.flush().toString();
    }

}
