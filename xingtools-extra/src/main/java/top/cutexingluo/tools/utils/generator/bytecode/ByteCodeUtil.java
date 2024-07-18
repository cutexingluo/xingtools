package top.cutexingluo.tools.utils.generator.bytecode;


import org.springframework.asm.ClassReader;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.ClassWriter;
import top.cutexingluo.tools.utils.se.file.XTIOUtil;

import java.io.IOException;

/**
 * @author XingTian
 * @version 1.0.0
 * @date 2023/9/16 15:54
 */
public class ByteCodeUtil {

    @FunctionalInterface
    public interface VisitorHandler {
        /**
         * <p>操作ClassWriter </p>
         * 需要返回一个ClassVisitor对象
         *
         * @param classWriter 类编写
         * @return {@link ClassVisitor}
         */
        ClassVisitor handle(ClassWriter classWriter);
    }

    /**
     * 获取类读取器
     *
     * @param classPath 类路径
     * @return {@link ClassReader}
     * @throws IOException IOEXCEPTION
     */
    public static ClassReader getClassReader(String classPath) throws IOException {
        return new ClassReader(classPath);
    }

    /**
     * 获取类编写器
     *
     * @return {@link ClassWriter}
     * @throws IOException IOEXCEPTION
     */
    public static ClassWriter getClassWriter() throws IOException {
        return new ClassWriter(ClassWriter.COMPUTE_MAXS);
    }

    /**
     * 修改字节码文件
     *
     * @param classPath      字节码路径
     * @param tarPath        目标路径（含文件名）
     * @param visitorHandler 访客处理程序
     */
    public static void change(String classPath, String tarPath, VisitorHandler visitorHandler) throws IOException {
        ClassReader classReader = getClassReader(classPath);
        ClassWriter classWriter = getClassWriter();
        // 操作
        ClassVisitor classVisitor = visitorHandler.handle(classWriter);
        classReader.accept(classVisitor, ClassReader.SKIP_CODE);
        byte[] bytes = classWriter.toByteArray();
        // 输出
        XTIOUtil.writeFile(tarPath, bytes);

    }
}
