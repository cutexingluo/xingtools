package top.cutexingluo.tools.utils.ee.web.front;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.utils.ee.web.data.WebResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;

/**
 * web file 工具
 *
 * <p>其他流式处理使用 hutool 包解决，需要用户态的均流式，本地无需用户态的选零拷贝</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/8/12 14:52
 * @since 1.1.3
 */
public class WebFileUtils {

    /**
     * 把文件解析成字节数组到 response
     *
     * @param targetFile 目标文件
     * @param response   response 对象
     */
    public static void sendToResponse(@NotNull File targetFile, @NotNull HttpServletResponse response) throws IOException {
        // 1. 设置响应头
        WebResponse webResponse = new WebResponse(response);
        webResponse
                .utf8()
                .contentTypeFile();

        //读取文件字节流
        try (ServletOutputStream os = response.getOutputStream()) {
            Files.copy(targetFile.toPath(), os);
        }
    }

    /**
     * 把文件以零拷贝方式写出到响应流
     *
     * <p>不走用户态</p>
     */
    public static void sendToResponseQuick(@NotNull File targetFile,
                                           @NotNull HttpServletResponse response) throws IOException {

        // 1. 设置响应头
        WebResponse webResponse = new WebResponse(response);
        webResponse
                .utf8()
                .contentTypeFile();

        // 2. 零拷贝：FileChannel -> ServletOutputStream
        try (FileInputStream fis = new FileInputStream(targetFile);
             FileChannel inChan = fis.getChannel();
             ServletOutputStream sos = response.getOutputStream();
             WritableByteChannel outChan = Channels.newChannel(sos)) {

            long size = inChan.size();
            long written = 0;
            while (written < size) {
                written += inChan.transferTo(written, size - written, outChan);
            }
        }
    }


}
