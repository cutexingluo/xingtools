package top.cutexingluo.tools.basepackage.basehandler.aop;

import org.aspectj.lang.ProceedingJoinPoint;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/5 18:47
 */
public interface BaseJoinPointTaskHandler {
    /**
     * 获得任务，手动操作inCatch，否则直接输出异常
     */
    default <V> Callable<V> getTask(ProceedingJoinPoint joinPoint, Consumer<Exception> inCatch) {
        return () -> {
            V result = null;
            try {
                result = (V) getTask(joinPoint).call();
            } catch (Exception e) {
                if (inCatch != null) inCatch.accept(e);
                else throw e;
            }
            return result;
        };
    }

    /**
     * 获得任务, 装饰重新抛出
     */
    default <V> Callable<V> getTask(ProceedingJoinPoint joinPoint) {
        return () -> {
            V result = null;
            try {
                if (joinPoint != null) result = (V) joinPoint.proceed();
            } catch (Exception e) {
                throw e;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
            return result;
        };
    }

    /**
     * 获得任务, 如果不允许执行就跳过
     */
    default <V> Callable<V> getTask(ProceedingJoinPoint joinPoint, Supplier<Boolean> canRunTask) {
        return () -> {
            if (canRunTask != null && !canRunTask.get()) {
                return null;
            }
            V result = null;
            try {
                if (joinPoint != null) result = (V) joinPoint.proceed();
            } catch (Exception e) {
                throw e;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
            return result;
        };
    }

    /**
     * 获得任务, 如果不允许执行就跳过
     */
    default <V> Callable<V> getTask(ProceedingJoinPoint joinPoint, Supplier<Boolean> canRunTask, Consumer<Exception> inCatch) {
        return () -> {
            if (canRunTask != null && !canRunTask.get()) {
                return null;
            }
            V result = null;
            try {
                result = (V) getTask(joinPoint).call();
            } catch (Exception e) {
                if (inCatch != null) inCatch.accept(e);
                else throw e;
            }
            return result;
        };
    }
}
