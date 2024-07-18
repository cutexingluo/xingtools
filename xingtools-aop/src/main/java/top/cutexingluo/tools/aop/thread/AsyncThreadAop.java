package top.cutexingluo.tools.aop.thread;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.PlatformTransactionManager;
import top.cutexingluo.tools.aop.thread.policy.ThreadAopFactory;
import top.cutexingluo.tools.aop.thread.run.ThreadAopHandler;
import top.cutexingluo.tools.aop.thread.run.ThreadPolicy;

import java.util.concurrent.ConcurrentHashMap;


/**
 * 多线程 AOP 类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/1 16:16
 * @since 1.0.2
 */
@EnableAsync
@Aspect
@Data
@NoArgsConstructor
public class AsyncThreadAop {

    //主线程处理器
    public final ConcurrentHashMap<String, ThreadAopHandler> mainHandler = new ConcurrentHashMap<>();
    //用来存储各线程计数器数据(每次执行后会从map中删除)
    public final ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();

    @Autowired(required = false)
    private PlatformTransactionManager transactionManager;

    @Autowired(required = false)
    public AsyncThreadAop(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

//    /**
//     * 获取结果
//     */
//    public static List<Future<Object>> getFutureList() {
//        String name = ThreadAopFactory.getMainThreadNameInMain();
//        return (List<Future<Object>>) map.get(name + ":futures");
//    }

//    /**
//     * 获取结果
//     */
//    public List<Future<Object>> getFutures() {
//        String name = ThreadAopFactory.getMainThreadNameInMain();
//        return (List<Future<Object>>) map.get(name + ":futures");
//    }

//    /**
//     * 获取子线程执行结果
//     */
//    public static List<Object> getResultList() {
//        String name = ThreadAopFactory.getMainThreadNameInMain();
//        return (List<Object>) map.get(name + ":results");
//    }

//    /**
//     * 获取子线程执行结果
//     */
//    public List<Object> getResults() {
//        String name = ThreadAopFactory.getMainThreadNameInMain();
//        return (List<Object>) map.get(name + ":results");
//    }

    @Around("@annotation(mainThread)")
    public Object mainIntercept(ProceedingJoinPoint joinPoint, MainThread mainThread) throws Throwable {
        mainThread = AnnotationUtils.getAnnotation(mainThread, MainThread.class);

        ThreadAopHandler handler = ThreadAopFactory.getHandler(mainThread, map, transactionManager);
        String name = ThreadAopFactory.getMainThreadNameInMain();
        mainHandler.put(name, handler);
        Object result = handler.runInMainAop(joinPoint, mainThread);
        mainHandler.remove(name);
        return result;
    }

    @Around("@annotation(sonThread)")
    public Object sonIntercept(ProceedingJoinPoint joinPoint, SonThread sonThread) throws Throwable {
        sonThread = AnnotationUtils.getAnnotation(sonThread, SonThread.class);
        boolean isArgThread = sonThread.policy() == ThreadPolicy.Step; // 最后一个是否是线程
        String name = ThreadAopFactory.getMainThreadNameInSon(joinPoint, isArgThread);
        ThreadAopHandler handler = mainHandler.get(name);
        return handler.runInSonAop(joinPoint, sonThread);
    }

}
