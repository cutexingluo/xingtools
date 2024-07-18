package top.cutexingluo.tools.utils.se.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.function.Function;

/**
 * 文件 IO 类
 *
 * <p>其他方法详见 {@link XTIOUtil}</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/3/6 17:57
 * @since 1.0.4
 */
public class XTFileIOUtil {


    /**
     * 合并文件
     *
     * @param targetFile 目标文件 pathName
     * @param srcFiles   源系列文件 pathName
     * @param cacheSize  缓存大小
     */
    public static void combineFilesWithRandomAccess(File targetFile, File[] srcFiles, int cacheSize) throws IOException {
        RandomAccessFile writeFile = null;
        try {
            writeFile = new RandomAccessFile(targetFile, "rw");
            byte[] b = new byte[cacheSize];
            for (File chunkFile : srcFiles) {
                int len = -1;
                RandomAccessFile readFile = null;
                try {
                    readFile = new RandomAccessFile(chunkFile, "r");
                    while ((len = readFile.read(b)) != -1) {
                        writeFile.write(b, 0, len);
                    }
                } catch (Exception e) {
                    throw e;
                } finally {
                    IoUtil.close(readFile);
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            IoUtil.close(writeFile);
        }
    }


    /**
     * 合并文件
     *
     * @param targetFile 目标文件 pathName
     * @param srcDir     源系列文件夹 pathDir
     * @param cacheSize  缓存大小
     * @param delSrcDir  是否删除源文件夹
     * @param filter     文件夹过滤器
     */
    public static void combineDirFilesWithRandomAccess(File targetFile, File srcDir, int cacheSize, boolean delSrcDir, Function<File, File[]> filter) throws IOException {
        try {
            File[] files;
            if (filter != null) {
                files = filter.apply(srcDir);
            } else {
                files = srcDir.listFiles();
            }
            if (files == null) {
                throw new IOException("srcDir files don't be empty");
            }
            combineFilesWithRandomAccess(targetFile, files, cacheSize);
        } catch (IOException e) {
            throw e;
        } finally {
            if (delSrcDir) {
                FileUtil.del(srcDir);
            }
        }
    }

    public static void combineDirFilesWithRandomAccess(File targetFile, File srcDir, int cacheSize, boolean delSrcDir) throws IOException {
        combineDirFilesWithRandomAccess(targetFile, srcDir, cacheSize, delSrcDir, null);
    }
}
