package top.cutexingluo.tools.utils.ee.web.front;

import cn.hutool.core.io.FileUtil;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.utils.se.file.XTPath;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * web file 工具
 *
 * <p>移植 XTFileIO 和 XTFileHandler 部分 servlet 到此类</p>
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
        response.addHeader("Content-Disposition", "attachment;filename=" +
                URLEncoder.encode(targetFile.getName(), StandardCharsets.UTF_8.name()));
        response.setContentType("application/octet-stream");
        ServletOutputStream os = response.getOutputStream();
        //读取文件字节流
        try {
            os.write(FileUtil.readBytes(targetFile));
        } finally {
            os.flush();
            os.close();
        }
    }

    /**
     * 根据文件路径返回文件
     * <p>下载，输入 下载路径和下载文件名 以及 请求对象</p>
     *
     * @param filePath 文件父路径 /xx/xx
     * @param fileUUID 文件名 abc.txt
     * @param response 请求
     * @throws IOException 读写异常
     */
    //
    public static void sendToResponse(String filePath, String fileUUID, HttpServletResponse response) throws IOException {
        filePath = XTPath.combinePath(filePath, fileUUID);
        File file = new File(filePath);
        sendToResponse(file, response);
    }

}
