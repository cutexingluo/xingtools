package top.cutexingluo.tools.exception;


import cn.hutool.core.util.StrUtil;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *异常工具类
 *
 * @author XingTian, Ruoyi
 * @version 1.0.0
 * @date 2023/10/2 16:24
 */
public class ExceptionUtil extends cn.hutool.core.exceptions.ExceptionUtil {

    /**
     * 转化为Exception
     */
    public static <T extends Throwable> Exception toException(T throwable) {
        if (throwable instanceof Exception) return (Exception) throwable;
        else return new Exception(throwable);
    }


    /**
     * 获取exception的详细错误信息。
     */
    public static String getExceptionMessage(Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        return sw.toString();
    }

    public static String getRootErrorMessage(Exception e) {
        Throwable root = getRootCause(e);
        root = (root == null ? e : root);
        if (root == null) {
            return "";
        }
        String msg = root.getMessage();
        if (msg == null) {
            return "null";
        }
        return StrUtil.nullToDefault(msg, "");
    }
}
