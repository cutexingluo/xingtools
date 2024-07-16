package top.cutexingluo.tools.aop.thread;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.Nullable;
import top.cutexingluo.tools.aop.thread.policy.ThreadAopFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * ThreadAOP 执行结果类
 * <p>- 通过注入该类，可以调用 getResults  和  getFutures  两个方法获取线程返回值</p>
 * <p>- 注意在主线程里面获取值，否则无效</p>
 * <p><b>个别比较晚阻塞的策略会无法获取返回值</b> </p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/6 19:50
 * @since 1.0.2
 */
@Data
@AllArgsConstructor
public class ThreadResults {
    public final ConcurrentHashMap<String, Object> map;

    /**
     * 获取Futures结果
     * <p>可自行 get () 阻塞线程</p>
     * <p>按策略排序</p>
     */
    public List<Future<Object>> getFutures() {
        String name = ThreadAopFactory.getMainThreadNameInMain();
        return (List<Future<Object>>) map.get(name + ":futures");
    }

    /**
     * 获取子线程的执行结果
     * <p>按策略排序</p>
     */
    @Nullable
    public List<Object> getResults() {
        String name = ThreadAopFactory.getMainThreadNameInMain();
        return (List<Object>) map.get(name + ":results");
    }

}
