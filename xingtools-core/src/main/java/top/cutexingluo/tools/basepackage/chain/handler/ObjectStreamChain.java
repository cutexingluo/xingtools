package top.cutexingluo.tools.basepackage.chain.handler;

import top.cutexingluo.tools.basepackage.chain.core.StreamChain;

/**
 * 流式链式处理器实体类
 *
 * <p>可以直接面向对象流式编程</p>
 * <p>可以直接传入 Object 任意对象，后续再通过cast 转型成自己需要的类型</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/6 17:32
 */
public class ObjectStreamChain extends StreamChain<Object> {
    public ObjectStreamChain(Object source) {
        super(source);
    }
}
