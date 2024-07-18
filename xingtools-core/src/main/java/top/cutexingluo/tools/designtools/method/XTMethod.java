package top.cutexingluo.tools.designtools.method;


import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.designtools.builder.XTBuilder;
import top.cutexingluo.tools.designtools.method.core.MethodInfo;
import top.cutexingluo.tools.designtools.method.core.MethodObjBundle;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * XTMethod 方法类
 *
 * <p>对象方法调用器</p>
 *
 * <p>
 * 版本v1.2.0
 * <p>
 * <b>使用方法</b>
 * <ul>
 *     <li>
 *      法1.使用对象<br>
 *      <code> new XTMethod(obj,methodName,params);  </code>
 *      </li>
 * <li>
 * 法2.纯使用Builder<br>
 * <code> new XTMethod.Builder().setMethodName().setMethodParams().build();</code>
 * </li>
 * <li>
 * 法3.联合使用Builder<br>
 * <code>new XTMethod().getBuilder().build().invoke(item)</code>
 * </li>
 * </ul>
 *
 * @author XingTian
 * @version 1.2.1
 * @date 2023/2/2 20:28
 */

public class XTMethod implements BaseMethod {
    public Method method;
    public String methodName;
    public Class<?>[] params;
    public Object[] values;

    public XTMethod() {
        method = null;
    }

    /**
     * @since 1.0.5
     */
    public <T> XTMethod(T item, MethodInfo methodInfo) throws NoSuchMethodException {
        this(item, methodInfo.getMethodName(), methodInfo.getParameterTypes());
    }


    /**
     * @since 1.0.5
     */
    public <T> XTMethod(Method method) throws NoSuchMethodException {
        init(method);
    }

    /**
     * @since 1.0.5
     */
    public <T> XTMethod(@NotNull MethodObjBundle methodObjBundle) throws NoSuchMethodException {
        Objects.requireNonNull(methodObjBundle);
        this.method = methodObjBundle.getMethod();
        Objects.requireNonNull(method);
        init(method);
    }

    public <T> XTMethod(T item, String methodName, Class<?>... param) throws NoSuchMethodException {//对象，方法名，参数
        init(item, methodName, param);
    }

    protected <T> void init(Method method) throws NoSuchMethodException {
        this.methodName = method.getName();
        this.params = method.getParameterTypes();
        this.method = method;
        method.setAccessible(true);
    }

    protected <T> void init(T item, String methodName, Class<?>... param) throws NoSuchMethodException {
        this.methodName = methodName;
        this.params = param;
        try {
            method = item.getClass().getMethod(methodName, param);
            method.setAccessible(true);
        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
            throw new NoSuchMethodException("没有该方法，或者没有该权限");
        }
    }

    @Override
    public <T> Object invoke(T item, Object... values) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {// 对象，参数值
        this.values = values;
        Object ret;
        if (params.length != values.length)
            throw new IllegalArgumentException("参数数量和实参数量不匹配");
        if (method == null) {
            try {
                method = item.getClass().getMethod(this.methodName, this.params);
                method.setAccessible(true);
            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
                throw new NoSuchMethodException("没有该方法，或者没有该权限");
            }
        }
        try {
            ret = method.invoke(item, values);
        } catch (IllegalAccessException | InvocationTargetException e) {
//            throw new ServiceException(Constants.CODE_400.getCode(), "值错误，请检查类型");
            throw e;
        }
        return ret;
    }

    @Override
    public <T> Object invoke(T item) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return invoke(item, this.values);
    }

    public Builder getBuilder() {
        return (Builder) new Builder().setTarget(this);
    }

    public static class Builder extends XTBuilder<XTMethod> {
        {
            if (this.target == null) this.target = new XTMethod();
        }

        public Builder setMethodName(String methodName) {
            this.target.methodName = methodName;
            return this;
        }

        public Builder setMethodParams(Class<?>... param) {
            this.target.params = param;
            return this;
        }

        public Builder setMethodValues(Object... values) {
            this.target.values = values;
            return this;
        }


    }
}