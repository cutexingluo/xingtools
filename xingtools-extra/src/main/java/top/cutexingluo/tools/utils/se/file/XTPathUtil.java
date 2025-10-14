package top.cutexingluo.tools.utils.se.file;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.system.ApplicationHome;
import top.cutexingluo.core.utils.se.file.XTPath;

import java.io.File;

/**
 * Path 封装工具类
 * <p>可以通过此工具类获取你的项目文件位置</p>
 * <p>于 1.1.1 版本 拆分 原XTPath为 XTPath 和 XTPathUtil </p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/12 16:03
 * @since 1.1.1
 */
public class XTPathUtil extends XTPath {

    //极力推荐用，后面发布会有用
    //由于class文件根目录是classes

    /**
     * 利用Spring获取项目绝对路径
     *
     * @param clazz 目标类
     * @return 项目地址
     */
    @NotNull
    public static String getProjectPath(Class<?> clazz) {
        return getProjectPath(clazz, EnvType.PROD);
    }

    /**
     * 利用Spring获取项目绝对路径
     * <p>PathType 代表环境</p>
     *
     * @param clazz 目标类
     * @return 项目地址
     */
    @NotNull
    public static String getProjectPath(Class<?> clazz, EnvType envType) {
        ApplicationHome applicationHome = new ApplicationHome(clazz);
        //classes -> target  ->  project path
        //getParentFile().getParentFile().
        File homeDir = applicationHome.getDir();
        if (envType == EnvType.DEV) {
            homeDir = homeDir.getParentFile().getParentFile();
        }
        return homeDir.getAbsolutePath();
    }

    /**
     * 从字节码位置推测 ,
     * 利用Spring获取源项目地址中java文件夹路径
     * <ul>
     *     <li>在 test , dev 环境下运行</li>
     *     <li>不要 prod 下使用该方法, 除非jar包在根目录下 </li>
     * </ul>
     *
     * @param clazz   目标类
     * @param envType 环境类型
     * @return 项目地址
     */
    @NotNull
    public static String getProjectJavaPath(Class<?> clazz, EnvType envType) {
        return getProjectPath(clazz, envType) + MAIN_PATH + SEPARATOR + "java";
    }

    /**
     * 从字节码位置推测 ,
     * 利用Spring获取源项目地址中resource文件夹路径
     * <ul>
     *     <li>在 test , dev 环境下运行</li>
     *     <li>不要 prod 下使用该方法 </li>
     * </ul>
     *
     * @param clazz   目标类
     * @param envType 环境类型
     * @return 项目地址
     */
    @NotNull
    public static String getProjectResPath(Class<?> clazz, EnvType envType) {
        return getProjectPath(clazz, envType) + MAIN_PATH + SEPARATOR + "resources";
    }

    /**
     * 从字节码位置推测 java源文件的路径 （只是推测，慎用）
     * <ul>
     *     <li>在 test , dev 环境下运行</li>
     *     <li>不要 prod 下使用该方法 </li>
     * </ul>
     *
     * @param clazz 目标类
     * @return 目标类的 java 绝对路径
     */
    @NotNull
    public static String getTryJavaAbsolutePath(Class<?> clazz, EnvType envType) {
        return getProjectJavaPath(clazz, envType) + SEPARATOR + packageToPath(clazz);
    }

    /**
     * 从字节码位置推测 java源文件的路径 （包含文件名） （只是推测，慎用）
     * <ul>
     *     <li>在 test , dev 环境下运行</li>
     *     <li>不要 prod 下使用该方法 </li>
     * </ul>
     *
     * @param clazz 目标类
     * @return 目标类的 java 绝对路径
     */
    @NotNull
    public static String getTryJavaAbsolutePathWithName(Class<?> clazz, EnvType envType) {
        return getTryJavaAbsolutePath(clazz, envType) + SEPARATOR + clazz.getSimpleName() + ".java";
    }
}
