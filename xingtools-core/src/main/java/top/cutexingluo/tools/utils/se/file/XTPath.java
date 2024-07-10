package top.cutexingluo.tools.utils.se.file;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;
import java.util.regex.Matcher;

/**
 * Path 封装工具类
 * <p>可以通过此工具类获取你的项目文件位置</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/3 15:10
 */
public class XTPath {

    /**
     * 环境类型
     */
    public enum EnvType {
        /**
         * 开发环境，即直接运行Application项目
         */
        DEV("dev"),
        /**
         * 测试环境，即ApplicationTests中测试
         */
        TEST("test"),
        /**
         * 发布环境，即打包成jar包环境,
         */
        PROD("prod");

        final String code;

        EnvType(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    /**
     * 自动选择环境
     *
     * @param code 编码
     * @return {@link EnvType}
     */
    public static EnvType selectEnv(String code) {
        if (EnvType.DEV.getCode().equalsIgnoreCase(code)) {
            return EnvType.DEV;
        }
        if (EnvType.TEST.getCode().equalsIgnoreCase(code)) {
            return EnvType.TEST;
        }
        return EnvType.PROD;
    }

    public static final String SEPARATOR = File.separator;

    public static final char UNIX_CHAR_SEPARATOR = '/';
    public static final String UNIX_SEPARATOR = "/";
    public static final int UNIX_MOD = 0b01;

    public static final char WIN_CHAR_SEPARATOR = '\\';
    public static final String WIN_SEPARATOR = "\\";
    public static final int WIN_MOD = 0b10;


    public static final String CLASSPATH = "classpath:/";
    public static final String MAIN_PATH = SEPARATOR + "src" + SEPARATOR + "main";


    /**
     * 获取地址分隔符
     */
    public static char getCharSeparator(boolean useWinSeparator) {
        return useWinSeparator ? WIN_CHAR_SEPARATOR : UNIX_CHAR_SEPARATOR;
    }

    /**
     * 获取地址分隔符
     */
    public static String getSeparator(boolean useWinSeparator) {
        return useWinSeparator ? WIN_SEPARATOR : UNIX_SEPARATOR;
    }

    /**
     * 获取项目地址（ idea 打开的地址）
     *
     * @return 项目地址
     */
    @Deprecated //由于只能获取 当前用户idea打开项目的根目录，已弃用
    public static String getProjectPath() { // 获取项目绝对路径
        return System.getProperty("user.dir");
    }

    //极力推荐用，后面发布会有用
    //由于class文件根目录是classes

//    /**
//     * 利用Spring获取项目绝对路径
//     *
//     * @param clazz 目标类
//     * @return 项目地址
//     */
//    public static String getProjectPath(Class<?> clazz) {
//        return getProjectPath(clazz, EnvType.PROD);
//    }

//    /**
//     * 利用Spring获取项目绝对路径
//     * <p>PathType 代表环境</p>
//     *
//     * @param clazz 目标类
//     * @return 项目地址
//     */
//    public static String getProjectPath(Class<?> clazz, EnvType envType) {
//        ApplicationHome applicationHome = new ApplicationHome(clazz);
//        //classes -> target  ->  project path
//        //getParentFile().getParentFile().
//        File homeDir = applicationHome.getDir();
//        if (envType == EnvType.DEV) {
//            homeDir = homeDir.getParentFile().getParentFile();
//        }
//        return homeDir.getAbsolutePath();
//    }

//    /**
//     * 从字节码位置推测 ,
//     * 利用Spring获取源项目地址中java文件夹路径
//     * <ul>
//     *     <li>在 test , dev 环境下运行</li>
//     *     <li>不要 prod 下使用该方法, 除非jar包在根目录下 </li>
//     * </ul>
//     *
//     * @param clazz   目标类
//     * @param envType 环境类型
//     * @return 项目地址
//     */
//    public static String getProjectJavaPath(Class<?> clazz, EnvType envType) {
//        return getProjectPath(clazz, envType) + MAIN_PATH + SEPARATOR + "java";
//    }
//
//    /**
//     * 从字节码位置推测 ,
//     * 利用Spring获取源项目地址中resource文件夹路径
//     * <ul>
//     *     <li>在 test , dev 环境下运行</li>
//     *     <li>不要 prod 下使用该方法 </li>
//     * </ul>
//     *
//     * @param clazz   目标类
//     * @param envType 环境类型
//     * @return 项目地址
//     */
//    public static String getProjectResPath(Class<?> clazz, EnvType envType) {
//        return getProjectPath(clazz, envType) + MAIN_PATH + SEPARATOR + "resources";
//    }

//    /**
//     * 从字节码位置推测 java源文件的路径 （只是推测，慎用）
//     * <ul>
//     *     <li>在 test , dev 环境下运行</li>
//     *     <li>不要 prod 下使用该方法 </li>
//     * </ul>
//     *
//     * @param clazz 目标类
//     * @return 目标类的 java 绝对路径
//     */
//    public static String getTryJavaAbsolutePath(Class<?> clazz, EnvType envType) {
//        return getProjectJavaPath(clazz, envType) + SEPARATOR + packageToPath(clazz);
//    }

//    /**
//     * 从字节码位置推测 java源文件的路径 （包含文件名） （只是推测，慎用）
//     * <ul>
//     *     <li>在 test , dev 环境下运行</li>
//     *     <li>不要 prod 下使用该方法 </li>
//     * </ul>
//     *
//     * @param clazz 目标类
//     * @return 目标类的 java 绝对路径
//     */
//    public static String getTryJavaAbsolutePathWithName(Class<?> clazz, EnvType envType) {
//        return getTryJavaAbsolutePath(clazz, envType) + SEPARATOR + clazz.getSimpleName() + ".java";
//    }


    //********************************* 以下适用于Dev状态，class字节码用上面的

    /**
     * @param clazz        目标类
     * @param relativePath 相对位置
     * @return 目标类的 class 路径的相对位置
     */
    public static String getPath(@NotNull Class<?> clazz, String relativePath) {
        return new File(Objects.requireNonNull(clazz.getResource(relativePath)).getPath()).getAbsolutePath();
    }

    /**
     * @param thisObj      目标对象
     * @param relativePath 相对位置
     * @return 目标对象的 class 路径的相对位置
     */
    public static <T> String getRelativeToPath(T thisObj, String relativePath) {
        return new File(Objects.requireNonNull(thisObj.getClass().getResource(relativePath)).getPath()).getAbsolutePath();
    }

    /**
     * @param clazz 目标类
     * @return 目标类的 class 绝对路径
     */
    public static String getAbsolutePath(Class<?> clazz) {
        return getPath(clazz, "./");
    }


    /**
     * @param thisObj 目标对象
     * @return 目标对象 class 的绝对路径
     */
    public static <T> String getCurrentAbsolutePath(T thisObj) {
        return getAbsolutePath(thisObj.getClass());
    }

    // 包名

    /**
     * @param clazz 类名
     * @return 类所在的包名（不包含类名）
     */
    public static String getPackageName(Class<?> clazz) {
        return clazz.getPackage().getName();
    }

    // 包名包含本身

    /**
     * @param clazz 类名
     * @return 类所在的包名（包含类名）
     */
    public static String getPackageNameWIthClass(Class<?> clazz) {
        return clazz.getCanonicalName();
    }

    // 包名转路径

    /**
     * 包名转相对路径(com.example.common  =>   com/example/common)
     *
     * @param packageName 包名
     * @return 路径
     */
    public static String packageNameToSrcPath(String packageName) {
        return packageName.replaceAll("\\.", Matcher.quoteReplacement(File.separator));
    }

    // 类路径

    /**
     * 将类所在的包名转为相对路径(com.example.common  =>   com/example/common)
     *
     * @param clazz 类名
     * @return 路径
     */
    public static String packageToPath(Class<?> clazz) {
        return packageNameToSrcPath(getPackageName(clazz));
    }


    /**
     * 规则化路径
     *
     * @param path            路径
     * @param useWinSeparator 使用Win分隔符
     */
    @NotNull
    public static String normalizePath(String path, boolean useWinSeparator) {
        if (!useWinSeparator) {
            return path.replace(WIN_CHAR_SEPARATOR, UNIX_CHAR_SEPARATOR);
        } else {
            return path.replace(UNIX_CHAR_SEPARATOR, WIN_CHAR_SEPARATOR);
        }
    }

    /**
     * 规则化路径
     *
     * @param path      路径
     * @param separator 新分隔符
     */
    @NotNull
    public static String normalizePathAll(String path, String separator) {
        return path.replace(WIN_SEPARATOR, separator).replace(UNIX_SEPARATOR, separator);
    }


    /**
     * 合并路径
     * <p>仅进行组合. 不进行分隔符的转化修复</p>
     * <p>合成后路径首尾没有判定</p>
     * <p>需自行trim()</p>
     *
     * @param paths 路径集合
     * @return 合成的路径
     * @since 1.0.4
     */
    @NotNull
    public static String combinePaths(String separator, @NotNull String... paths) {
        StringBuilder sb = new StringBuilder();
        if (paths.length > 0) {
            String append = paths[0];
            sb.append(append);
            for (int i = 1; i < paths.length; i++) {
                append = paths[i];
                if (sb.indexOf(separator, sb.length() - separator.length()) != -1 && append.startsWith(separator)) {
                    append = paths[i].substring(1);
                } else if (sb.indexOf(separator, sb.length() - separator.length()) == -1 && !append.startsWith(separator)) {
                    append = separator + paths[i];
                }
                sb.append(append);
            }
        }
        return sb.toString();
    }

    /**
     * 合并并修复路径
     * <p><b>推荐使用</b></p>
     * <p>合成后路径首尾没有判定</p>
     * <p>需自行trim()</p>
     *
     * @param paths 路径集合
     * @return 合成的路径
     * @since 1.0.4
     */
    @NotNull
    public static String combinePathsFix(boolean useWinSeparator, @NotNull String... paths) {
        String separator = getSeparator(useWinSeparator);
        StringBuilder sb = new StringBuilder();
        if (paths.length > 0) {
            String fixPath = normalizePath(paths[0], useWinSeparator);
            sb.append(fixPath);
            for (int i = 1; i < paths.length; i++) {
                fixPath = normalizePath(paths[i], useWinSeparator);
                if (sb.indexOf(separator, sb.length() - separator.length()) != -1 && fixPath.startsWith(separator)) {
                    fixPath = fixPath.substring(1);
                } else if (sb.indexOf(separator, sb.length() - separator.length()) == -1 && !fixPath.startsWith(separator)) {
                    fixPath = separator + fixPath;
                }
                sb.append(fixPath);
            }
        }
        return sb.toString();
    }

    /**
     * 合并路径
     * <p>仅进行组合. 不进行分隔符的转化修复</p>
     * <p>合成后路径首尾没有判定</p>
     * <p>自动trim()</p>
     *
     * @param parentPath 父路径 (D://hello ,D://hello/)
     * @param sonPath    子路径 (world ,/world)
     * @param separator  分隔符
     * @return 合成的路径 (D://hello/world)
     * @since 1.0.4
     */
    @NotNull
    public static String combinePath(@NotNull String parentPath, @NotNull String sonPath, String separator) {
        parentPath = parentPath.trim();
        sonPath = sonPath.trim();
        if (!parentPath.endsWith(separator) && !sonPath.startsWith(separator)) parentPath += separator;
        else if (parentPath.endsWith(separator) && sonPath.startsWith(separator)) sonPath = sonPath.substring(1);
        return parentPath + sonPath;
    }

    /**
     * 合并并修复路径
     * <p><b>推荐使用</b></p>
     * <p>combinePathsFix 别名</p>
     *
     * @return 合成的路径 ( hello/world/xx )
     * @since 1.0.4
     */
    @NotNull
    public static String combinePath(boolean useWinSeparator, @NotNull String... paths) {
        return combinePathsFix(useWinSeparator, paths);
    }


    /**
     * 合并路径
     * <p>仅进行组合. 不进行分隔符的转化修复</p>
     * <p>合成后路径首尾没有判定</p>
     * <p>自动trim()</p>
     *
     * @param parentPath 父路径 (D://hello ,D://hello/)
     * @param sonPath    子路径 (world ,/world)
     * @return 合成的路径 (D://hello/world)
     */
    @NotNull
    public static String combinePath(@NotNull String parentPath, @NotNull String sonPath) {
        return combinePath(parentPath, sonPath, SEPARATOR);
    }


    /**
     * 如果不存在则加前缀
     *
     * @since 1.0.4
     */
    @NotNull
    @Contract(pure = true)
    public static String addPrefix(@NotNull String s, String prefix) { //前面加斜线
        if (!s.startsWith(prefix)) s = prefix + s;
        return s;
    }

    /**
     * 如果不存在则加后缀
     *
     * @since 1.0.4
     */
    @NotNull
    @Contract(pure = true)
    public static String addSuffix(@NotNull String s, String suffix) {//后面加斜线
        if (!s.endsWith(suffix)) s += suffix;
        return s;
    }

    /**
     * 是否以分隔符开头
     *
     * @param path 路径
     * @param mode 模式, 1 以 / 分隔符判定, 2: 以 \ 分隔符判定, 3 以 /  或 \ 分隔符判定
     * @return boolean
     * @since 1.0.4
     */
    public static boolean hasPrefixSeparator(@NotNull String path, int mode) {
        switch (mode) {
            case 3:
                return path.startsWith(UNIX_SEPARATOR) || path.startsWith(WIN_SEPARATOR);
            case 1:
                return path.startsWith(UNIX_SEPARATOR);
            case 2:
                return path.startsWith(WIN_SEPARATOR);
            default:
                return false;
        }
    }

    /**
     * 是否以分隔符结尾
     *
     * @param path 路径
     * @param mode 模式, 1 以 / 分隔符判定, 2: 以 \ 分隔符判定, 3 以 /  或 \ 分隔符判定
     * @return boolean
     * @since 1.0.4
     */
    public static boolean hasSuffixSeparator(@NotNull String path, int mode) {
        switch (mode) {
            case 3:
                return path.endsWith(UNIX_SEPARATOR) || path.endsWith(WIN_SEPARATOR);
            case 1:
                return path.endsWith(UNIX_SEPARATOR);
            case 2:
                return path.endsWith(WIN_SEPARATOR);
            default:
                return false;
        }
    }


    /**
     * 存在则转化分隔符开头字符
     *
     * @param path        路径
     * @param mode        模式, 1 以 / 分隔符判定, 2: 以 \ 分隔符判定, 3 以 /  或 \ 分隔符判定
     * @param toSeparator 转化后的分隔符
     * @since 1.0.4
     */
    public static String covertPrefixSeparator(@NotNull String path, int mode, String toSeparator) {
        switch (mode) {
            case 3:
                if (path.startsWith(UNIX_SEPARATOR) || path.startsWith(WIN_SEPARATOR)) {
                    return toSeparator + path.substring(1);
                }
                return path;
            case 1:
                if (path.startsWith(UNIX_SEPARATOR)) {
                    return toSeparator + path.substring(1);
                }
                return path;
            case 2:
                if (path.startsWith(WIN_SEPARATOR)) {
                    return toSeparator + path.substring(1);
                }
                return path;
            default:
                return path;
        }
    }


    /**
     * 存在则转化分隔符结尾字符
     *
     * @param path        路径
     * @param mode        模式, 1 以 / 分隔符判定, 2: 以 \ 分隔符判定, 3 以 /  或 \ 分隔符判定
     * @param toSeparator 转化后的分隔符
     * @since 1.0.4
     */
    public static String covertSuffixSeparator(@NotNull String path, int mode, String toSeparator) {
        switch (mode) {
            case 3:
                if (path.endsWith(UNIX_SEPARATOR) || path.endsWith(WIN_SEPARATOR)) {
                    return path.substring(0, path.length() - 2) + toSeparator;
                }
                return path;
            case 1:
                if (path.endsWith(UNIX_SEPARATOR)) {
                    return path.substring(0, path.length() - 2) + toSeparator;
                }
                return path;
            case 2:
                if (path.endsWith(WIN_SEPARATOR)) {
                    return path.substring(0, path.length() - 2) + toSeparator;
                }
                return path;
            default:
                return path;
        }
    }
}
