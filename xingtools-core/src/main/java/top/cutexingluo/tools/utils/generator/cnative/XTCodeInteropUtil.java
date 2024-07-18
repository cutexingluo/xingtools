package top.cutexingluo.tools.utils.generator.cnative;


import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.python.core.PyFunction;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import javax.script.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 调用其他第三方的工具类
 * <p>例如:</p>
 * <ul>
 *     <li>执行控制台命令, 其他详见 {@link top.cutexingluo.tools.utils.se.thread.ProcessUtil}</li>
 *     <li>生成调用本地cpp的dll文件</li>
 *     <li>执行js代码</li>
 *     <li>编程式python代码</li>
 * </ul>
 *
 * <p>其中如果需要使用 python 需导入</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/9/15 16:49
 */
public class XTCodeInteropUtil {

    /**
     * 执行多条指令
     *
     * @param command 每一条指令
     * @param file    相对路径
     * @return {@link List}<{@link String}>
     */
    public static List<String> exec(File file, String... command) {
        Process process = RuntimeUtil.exec(null, file, command);
        return RuntimeUtil.getResultLines(process);
    }

    /**
     * 执行多条指令
     *
     * @param command 每一条指令
     * @return {@link String}
     */
    public static List<String> exec(String... command) {
        return RuntimeUtil.execForLines(command);
    }

    /**
     * 执行指令
     *
     * @param cmd 指令字符串
     * @return {@link String}
     */
    public static List<String> execute(String cmd) {
        return RuntimeUtil.execForLines(cmd);
    }

    //*****************************************

    /**
     * Java 部分
     */
    public static class JAVA {
        public static final String[] CMD = {
                "javac", "java"
        };

        /**
         * 编译
         *
         * @param javaSrcPath java源文件路径
         */
        public static List<String> compile(String javaSrcPath) {
            String[] cmd = new String[]{CMD[0], javaSrcPath};
            return exec(cmd);
        }

        /**
         * 运行
         *
         * @param javacSrcPath javac文件路径
         */
        public static List<String> run(String javacSrcPath) {
            String[] cmd = new String[]{CMD[1], javacSrcPath};
            return exec(cmd);
        }
    }
    //*****************************************

    /**
     * c++部分
     */
    public static class CPP {
        /**
         * 编译类型
         */
        public enum CompileType {
            /**
             * gcc
             */
            GCC("gcc"),
            /**
             * g++
             */
            GPP("g++");
            private String value;

            CompileType(String value) {
                this.value = value;
            }

            String getValue() {
                return value;
            }
        }

        public static final String[] CMD = {
                "javac", "-h",
                "-I", "-shared", "-o",
        };

        /**
         * 生成c头文件
         *
         * @param javaSrcPath java源文件路径 (要带 .java)
         * @param tarPath     目标生成路径 （默认 . 当前路径）
         */
        public static List<String> generateCHeader(@NotNull String javaSrcPath, @Nullable String tarPath) {
            if (StrUtil.isBlank(javaSrcPath)) {
                throw new RuntimeException("cannot generate C Header file, javaSrcPath is wrong (null or empty)");
            }
            if (!javaSrcPath.endsWith(".java")) {
                javaSrcPath += ".java";
            }
            if (StrUtil.isBlank(tarPath)) {
                tarPath = ".";
            }
            String[] cmd = new String[]{CMD[0], javaSrcPath, CMD[1], tarPath};
//            XTArrayUtil.printlnArray(cmd); //test output
            return exec(cmd);
        }


        /**
         * 编译c文件为dll 动态链接库文件
         * <p>需要提前安装 gcc 或者 g++ </p>
         *
         * @param compileType  编译方式
         * @param CPath        自己实现的c源文件 (需要带扩展名)
         * @param tarName      保存为dll文件的名称 (需要带 .dll)
         * @param includePaths c源文件中  引入的头文件的 存放路径
         */

        public static List<String> compileCSrcFile(CompileType compileType, @NotNull String CPath, @NotNull String tarName, List<String> includePaths) {
            if (StrUtil.isBlank(CPath)) {
                throw new RuntimeException("cannot generate DLL file, CPath is wrong (null or empty)");
            }
            if (StrUtil.isBlank(tarName)) {
                throw new RuntimeException("cannot generate DLL file, tarPath is wrong (null or empty)");
            }
            if (!tarName.endsWith(".dll")) {
                tarName += ".dll";
            }
            List<String> cmdList = new ArrayList<>(Arrays.asList(
                    compileType.getValue(), CMD[3], CPath, CMD[4], tarName
            ));
            if (includePaths == null) includePaths = new ArrayList<>();
            // 获取jdk
            String property = System.getProperty("java.home");
            if (property != null) {
                if (property.endsWith("jre")) {
                    // 去除尾部jre
                    property = property.substring(0, property.length() - 4);
                }
                // 存入环境
                String jniPath1 = property + File.separator + "include";
                String jniPath2 = jniPath1 + File.separator + "win32";

                includePaths.add(jniPath1);
                includePaths.add(jniPath2);
            }
//            System.out.println(property + " ==>  " + cmdList + " ==>" + includePaths);
            for (String path : includePaths) {
                cmdList.add(CMD[2]);
                cmdList.add(path);
            }
//            XTArrayUtil.printlnList(cmdList); // test output
            return exec(cmdList.toArray(new String[0]));
        }

        /**
         * 编译c文件为dll 动态链接库文件
         * <p>需要提前安装 gcc 或者 g++ </p>
         *
         * 编译方式 默认 g++
         *
         * @param CPath        自己实现的c源文件 (需要带扩展名)
         * @param tarName      保存为dll文件的路径 (需要带 .dll)
         * @param includePaths c源文件中  引入的头文件的 存放路径
         */

        public static List<String> compileCSrcFile(@NotNull String CPath, @NotNull String tarName, List<String> includePaths) {
            return compileCSrcFile(CompileType.GPP, CPath, tarName, includePaths);
        }

        /**
         * 加载dll文件
         *
         * @param dllPath dll路径
         */
        public static void load(String dllPath) {
            System.load(dllPath);
        }
    }

    //*****************************************

    /**
     * JavaScript部分
     */
    public static class JS {
        /**
         * 获取引擎
         */
        public static ScriptEngine getScriptEngine() {
            ScriptEngineManager engineManager = new ScriptEngineManager();
            ScriptEngine nashorn = engineManager.getEngineByName("nashorn");
            return nashorn;
        }

        /**
         * 执行js代码
         *
         * @param script   脚本
         * @param bindings 加载进去的数据
         */
        public static Object runScript(ScriptEngine engine, String script, SimpleBindings bindings) throws ScriptException {
            return engine.eval(script, bindings);
        }

        /**
         * 加载指定路径的js文件
         *
         * @param scriptPath 脚本路径
         * @param bindings   加载进去的数据
         */
        public static Object loadScript(ScriptEngine engine, @NotNull String scriptPath, SimpleBindings bindings) throws ScriptException {
            return engine.eval("load('" + scriptPath + "')", bindings);
        }

        /**
         * 调用脚本的方法
         *
         * @param scriptPath 脚本路径
         * @param funcName   方法名
         * @param args       参数
         */
        public static Object invokeFunc(ScriptEngine engine, @NotNull String scriptPath, String funcName, Object... args) throws ScriptException, FileNotFoundException, NoSuchMethodException {
            FileReader reader = new FileReader(scriptPath);
            engine.eval(reader); // 解析文件
            Invocable invocable = (Invocable) engine;
            return invocable.invokeFunction(funcName, args);
        }
    }
    //*****************************************

    /**
     * 需要导入Jython依赖
     * <p>1. 通过 Py.newInteger得到 PyObject对象, Py为转化类</p>
     * <p>2. PythonInterpreter 中 get 获取, set 存入, exec 执行 , execfile 执行文件</p>
     * <p>
     * 导入依赖 <br>
     * <code>
     * <dependency>
     * &lt;groupId>org.python&lt;/groupId> <br>
     * &lt;artifactId>jython-standalone&lt;/artifactId>
     * </dependency>
     * </code>
     * </p>
     */
    public static class Python {
        @FunctionalInterface
        public interface PythonHandler {
            void handle(PythonInterpreter interpreter);
        }

        /**
         * 获取解释器
         */
        public static PythonInterpreter getInterpreter() {
            return new PythonInterpreter();
        }

        /**
         * 清除关闭
         */
        public static void cleanClose(PythonInterpreter interpreter) {
            interpreter.cleanup();
            interpreter.close();
        }

        /**
         * 一般执行器，
         * 执行并关闭
         */
        public static void run(PythonHandler pythonHandler) {
            PythonInterpreter pi = getInterpreter();
            pythonHandler.handle(pi);
            cleanClose(pi);
        }

        /**
         * 执行py文件 (含.py) 并关闭
         */
        public static void runFile(String filePath) {
            PythonHandler handler = (interpreter) -> {
                interpreter.execfile(filePath);
            };
            run(handler);
        }

        /**
         * <p>调用函数(反射)</p>
         * <p>如果没有该函数，请执行pi.execfile 导入函数</p>
         *
         * @param funcName 函数名
         * @param args     参数
         */
        public static PyObject callFunc(PythonInterpreter pi, String funcName, PyObject... args) {
            PyFunction func = pi.get(funcName, PyFunction.class);
            return func.__call__(args);
        }

        /**
         * <p>调用对象里面的函数(反射)</p>
         * <p>如果没有该函数，请执行pi.execfile 导入函数</p>
         *
         * @param tarObj   目标对象
         * @param funcName 函数名
         * @param args     参数
         */
        public static PyObject callFunc(PythonInterpreter pi, PyObject tarObj, String funcName, PyObject... args) {
            return tarObj.invoke(funcName, args);
        }


    }


}
