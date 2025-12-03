package top.cutexingluo.tools.aop.thread.policy;

import cn.hutool.core.util.StrUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.PlatformTransactionManager;
import top.cutexingluo.tools.aop.thread.MainThread;
import top.cutexingluo.tools.aop.thread.run.ThreadAopHandler;
import top.cutexingluo.tools.aop.thread.run.ThreadPolicy;
import top.cutexingluo.tools.aop.thread.run.ThreadTimePolicy;
import top.cutexingluo.tools.utils.spring.SpringUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread aop  factory  线程 aop 工厂
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/1 15:55
 * @since 1.0.2
 */
public class ThreadAopFactory {

    /**
     * 获取Aop处理器
     */
    public static ThreadAopHandler getHandler(MainThread mainThread, @NotNull ConcurrentHashMap<String, Object> map, @NotNull PlatformTransactionManager transactionManager) {
        switch (mainThread.policy()) {
            case Future:
                return new FutureHandler(map, transactionManager);
            case AsyncSerial:
                return new AsyncSerialHandler(map, transactionManager);
            case Step:
                return new StepHandler(map, transactionManager);
            case Custom:
                ThreadAopHandler bean = null;
                if (StrUtil.isNotBlank(mainThread.customAopBean())) {
                    bean = SpringUtils.getBean(mainThread.customAopBean(),ThreadAopHandler.class);
                }
                if (bean == null) {
                    bean = SpringUtils.getBean(ThreadAopHandler.class);
                }
                if (bean != null) {
                    if (bean.needAutowired()) {
                        bean.setMap(map);
                        bean.setTransactionManager(transactionManager);
                    }
                    return bean;
                }
                throw new IllegalArgumentException("未找到 ThreadAopHandler Bean ! ");
            case AsyncParallel:
            default:
                return new AsyncParallelHandler(map, transactionManager);
        }
    }

    public static String getMainThreadNameInMain() {
        Thread thread = Thread.currentThread();
        return thread.getName().trim();
    }

    public static String getMainThreadNameInSon(ProceedingJoinPoint joinPoint, boolean lastArgThread) {
        Thread thread = Thread.currentThread();
        if (lastArgThread) {
            Object[] args = joinPoint.getArgs();
            if (args.length > 0 && args[args.length - 1] instanceof Thread) {
                thread = (Thread) args[args.length - 1];
            } else {
                throw new IllegalArgumentException("The last argument must be Thread !  子线程最后一个参数必须为主线程 Thread !  \n" +
                        "请为你的子线程方法的最后一个参数添加 Thread 类型的参数, 调用时填 Thread.currentThread() ! 并且为你的子线程方法添加@Async! \n" +
                        "原因： 此策略基于AOP, 使用@Async丢失主线程信息,  所以需要绑定主线程。 ");
            }
        }
        return thread.getName().trim();
    }

    /**
     * 获取执行时机
     */
    public static ThreadTimePolicy getRightTimePolicy(MainThread mainThread) {
        if (mainThread.policy() == ThreadPolicy.AsyncParallel || mainThread.policy() == ThreadPolicy.AsyncSerial) {
            switch (mainThread.startTime()) {
                case AsyncAfterLastSon:
                case GetResultAfterLastSon:
                case GetQuickResult:
                    return ThreadTimePolicy.AsyncAfterLastSon;
                case AsyncAfterMain:
                case GetFuture:
                default:
                    return ThreadTimePolicy.AsyncAfterMain;
            }
        } else if (mainThread.policy() == ThreadPolicy.Future) {
            switch (mainThread.startTime()) {
                case AsyncAfterLastSon:
                case GetResultAfterLastSon:
                    return ThreadTimePolicy.GetResultAfterLastSon;
                case GetQuickResult:
                    return ThreadTimePolicy.GetQuickResult;
                case AsyncAfterMain:
                case GetFuture:
                default:
                    return ThreadTimePolicy.GetFuture;
            }
        }
        return mainThread.startTime();
    }
}
