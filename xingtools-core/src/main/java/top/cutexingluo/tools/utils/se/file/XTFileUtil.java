package top.cutexingluo.tools.utils.se.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * File的封装工具类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/3 15:06
 */
public class XTFileUtil {

    /**
     * 点
     */
    public static final String DOT = ".";

    /**
     * 路径得到文件
     *
     * @param path 路径
     * @return {@link File}
     */
    public static File getFile(String path) {
        return FileUtil.file(path);
    }

    /**
     * 用class类名得到文件
     *
     * @param clazz clazz
     * @return {@link File}
     */// 先获取路径，再获取文件
    public static File getFile(Class<?> clazz) {
        return getFile(XTPath.getAbsolutePath(clazz));
    }

    public static String getFileName(File file) {
        return FileNameUtil.mainName(file);
    }

    public static String getClassName(Class<?> clazz) {
        return clazz.getSimpleName();
    }

    /**
     * 获取扩展名, 带点(.)
     */
    @NotNull
    public static String getDotExtName(File file) {
        return DOT + FileNameUtil.extName(file);
    }

    /**
     * 获取扩展名, 带点(.)
     */
    @NotNull
    public static String getDotExtName(String filename) {
        return DOT + FileNameUtil.extName(filename);
    }

    /**
     * 获取扩展名, 不带点(.)
     */
    public static String getExtName(File file) {
        return FileNameUtil.extName(file);
    }

    /**
     * 获取扩展名, 不带点(.)
     */
    public static String getExtName(String filename) {
        return FileNameUtil.extName(filename);
    }


    /**
     * 所有文件名是否是ext扩展名
     *
     * @param ext   ext扩展名
     * @param names 文件名称集合
     * @return boolean 是否全部是该扩展名结尾
     */
    public static boolean isExtName(String ext, String... names) {
        if (ext.startsWith(".")) {
            ext = ext.substring(1);
        }
        boolean allIsExtension = true;
        for (String name : names) {
            if (!FileNameUtil.extName(name).equals(ext)) {
                allIsExtension = false;
                break;
            }
        }
        return allIsExtension;
    }

    /**
     * 文件是否是以ext之一结尾
     *
     * @param name 文件名字
     * @param exts 扩展名列表
     * @return boolean 是否存在扩展名结尾
     */
    public static boolean hasExtName(String name, String... exts) {
        boolean hasExtension = false;
        for (String ext : exts) {
            if (ext.startsWith(".")) ext = ext.substring(1);
            if (FileNameUtil.extName(name).equals(ext)) {
                hasExtension = true;
                break;
            }
        }
        return hasExtension;
    }

    /**
     * 递归查找某目录下的所有文件名称
     *
     * @param path 路径
     * @return 文件名
     */
    public static List<String> getFilesByDfs(String path) {
        List<String> files = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();
        if (tempList != null)
            for (File value : tempList) {
                if (value.isFile()) { //如果是文件
                    files.add(value.toString());
                }
                if (value.isDirectory()) {    //如果是文件夹
                    files.addAll(getFilesByDfs(value.toString()));
                }
            }
        return files;
    }

    /**
     * 将多个文本文件合并为一个文本文件
     *
     * @param outFileName 输出文件名
     * @param inFileNames 输入文件名列表
     * @param filter      过滤器
     */
    public static void merge(String outFileName, List<String> inFileNames, Function<String, String> filter) throws Exception {
        FileWriter writer = new FileWriter(outFileName, false);
        for (String inFileName : inFileNames) {
//            System.out.println();
            String txt = FileUtil.readUtf8String(inFileName);
            if (filter != null) txt = filter.apply(txt);
            writer.write(txt);
//            System.out.println(txt);
        }
        writer.close();
    }

    /**
     * 将多个文本文件合并为一个文本文件
     *
     * @param outFileName 输出文件名
     * @param inFileNames 输入文件名列表
     */
    public static void merge(String outFileName, List<String> inFileNames) throws Exception {
        merge(outFileName, inFileNames, null);
    }
}
