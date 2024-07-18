package top.cutexingluo.tools.utils.se.file.space;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;

/**
 * @author XingTian
 * @version 1.0.0
 * @date 2024/3/6 13:34
 */
public class DiskSpaceUtil {


    /**
     * 获取 FileSystem
     */
    public static FileSystem getFileSystem(@NotNull File file) {
        return FileSystems.getFileSystem(file.toURI());
    }


    /**
     * 是否有剩余空间
     *
     * @param file     文件路径,该文件路径必须存在，否则空间会得到0
     * @param resSpace 允许剩余的空间，毕竟一般不会把空间占完
     */
    public static boolean isEnoughSpace(@NotNull File file, long resSpace, long... usedSpace) {
        long res = file.getUsableSpace() - resSpace;
        if (res < 0) {
            return false;
        }
        for (long used : usedSpace) {
            res -= used;
            if (res < 0) {
                return false;
            }
        }
        return true;
    }


}
