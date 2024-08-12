package top.cutexingluo.tools.utils.se.file;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;
import top.cutexingluo.tools.utils.se.file.pkg.XTFile;
import top.cutexingluo.tools.utils.se.file.pkg.XTFileBundle;

import java.io.File;
import java.io.IOException;

/**
 * 文件处理器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/3/2 16:51
 */
@Data
public class XTFileHandler {

    /**
     * 分隔线
     */
    public static final String separator = File.separator;


    protected XTFile xtFile;

    public XTFileHandler(XTFile xtFile) {
        this.xtFile = xtFile;
    }

    public static XTFileHandler of(File file) {
        return new XTFileHandler(new XTFile(file));
    }

    public static XTFileHandler of(MultipartFile file) throws IOException {
        return new XTFileHandler(new XTFileBundle(file).getXtFile());
    }

    /**
     * 加前缀
     */
    public static String addPrefix(String s, String prefix) { //前面加斜线
        return XTPath.addPrefix(s, prefix);
    }

    /**
     * 加后缀
     */
    public static String addSuffix(String s, String suffix) {//后面加斜线
        return XTPath.addSuffix(s, suffix);
    }

    //---------static---------

    /**
     * 存储到磁盘
     *
     * @param file              文件
     * @param uploadFile        上传的文件 (路径+文件全名)
     * @param notMkdirs         是否创建目录文件夹 , true => 无目录存储失败
     * @param transferIfPresent 是否覆盖, false =>  不做任何处理
     * @return path 存储地址
     */
    public static String transferToDisk(MultipartFile file, @NotNull File uploadFile, boolean notMkdirs, boolean transferIfPresent) throws IOException {
        File parentFile = uploadFile.getParentFile();
        if (!parentFile.exists()) {//创建文件夹
            if (!notMkdirs) {
                boolean b = parentFile.mkdirs();
                if (!b) {
                    throw new IOException("create dir failed");
                }
            } else {
                throw new IOException("notMkdirs is true, but no dir exists");
            }
        }
        if (!transferIfPresent && uploadFile.exists()) {
            return uploadFile.getPath();
        }
        file.transferTo(uploadFile); // 存储到磁盘中
        return uploadFile.getPath();
    }


    /**
     * 存储到磁盘
     *
     * @param file       文件
     * @param uploadFile 上传的文件 (路径+文件全名)
     * @return path 存储地址
     */
    public static String transferToDisk(MultipartFile file, @NotNull File uploadFile) throws IOException {
        return transferToDisk(file, uploadFile, false, true);
    }


    //---------------methods-----------

    /**
     * 存储到磁盘
     *
     * @param file                  文件
     * @param fileUploadDiskDirPath 上传目录地址
     * @param notMkdirs             是否创建目录文件夹 , true => 无目录存储失败
     * @param transferIfPresent     是否覆盖, false =>  不做任何处理
     * @return path 存储地址
     */
    public String transferToDisk(MultipartFile file, String fileUploadDiskDirPath, boolean notMkdirs, boolean transferIfPresent) throws IOException {
        String url = addSuffix(fileUploadDiskDirPath, separator);
        String pathName = url + xtFile.getFilename();
        File uploadFile = new File(pathName);
        transferToDisk(file, uploadFile, notMkdirs, transferIfPresent);
        return pathName;
    }

    /**
     * 存储到磁盘
     *
     * @param file                  文件
     * @param fileUploadDiskDirPath 上传目录地址
     * @return path 存储地址
     */
    public String transferToDisk(MultipartFile file, String fileUploadDiskDirPath) throws IOException {
        return transferToDisk(file, fileUploadDiskDirPath, false, true);
    }


//    /**
//     * 把文件解析成字节数组到 response
//     *
//     * @param xtFileDirPath xtFile 目标文件的目录地址
//     * @param response      response 对象
//     */
//    public XTFileHandler sendToResponse(@NotNull String xtFileDirPath, @NotNull HttpServletResponse response) throws IOException {
//        xtFileDirPath = addSuffix(xtFileDirPath, separator);
//        File file = new File(xtFileDirPath + xtFile.getFilename());
//        sendToResponse(file, response);
//        return this;
//    }


}
