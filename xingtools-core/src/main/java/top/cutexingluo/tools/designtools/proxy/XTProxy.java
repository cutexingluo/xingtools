package top.cutexingluo.tools.designtools.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 代理接口<br>
 * 实现了的代理处理器<br>
 * 可以 被继承或使用 的代理类
 *
 * @author XingTian
 * @version v1.0.0
 * @date 2023/10/16
 */
public class XTProxy extends ProxyInvocationHandler implements InvocationHandler {


    @Override
    public void beforeTodo(Object proxy, Method method, Object[] args) {

    }

    @Override
    public void afterDone(Object result) {

    }
}
