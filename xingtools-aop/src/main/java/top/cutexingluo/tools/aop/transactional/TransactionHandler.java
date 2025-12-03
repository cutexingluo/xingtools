package top.cutexingluo.tools.aop.transactional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import top.cutexingluo.core.basepackage.basehandler.CallableHandler;
import top.cutexingluo.core.basepackage.struct.Initializable;
import top.cutexingluo.tools.utils.spring.SpringUtils;

import java.util.concurrent.Callable;
import java.util.function.BiConsumer;

/**
 * 事务处理器
 * <p>需要导入 spring-tx 包</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/1 20:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHandler implements CallableHandler, Initializable {

    private boolean transaction = false;
    private PlatformTransactionManager transactionManager;
    /**
     * 事务级别
     */
    private int propagationBehavior = TransactionDefinition.PROPAGATION_REQUIRES_NEW;


    public TransactionHandler(PlatformTransactionManager transactionManager) {
        transaction = true;
        this.transactionManager = transactionManager;
    }

    @Override
    public void init() {
        if (transaction && transactionManager == null) {
            transactionManager = SpringUtils.getBean(PlatformTransactionManager.class);
            if (transactionManager == null) {
                throw new IllegalStateException("transactionManager is null !!!");
            }
        }
    }


    /**
     * 装饰任务，自动事务
     *
     * @param task 任务
     * @return {@link Callable}<{@link T}> 执行的任务
     */
    @Override
    public <T> Callable<T> decorate(Callable<T> task) {
        if (!transaction) return task;
        init();
        TransactionMeta meta = new TransactionMeta(transactionManager, propagationBehavior);
        meta.begin();
        return () -> {
            T result = null;
            try {
                if (task != null) result = task.call();
                // 提交
                meta.commit();
            } catch (Exception e) {
                // 回滚
                meta.rollback();
                throw e;
            }
            return result;
        };
    }

    /**
     * 装饰任务，手动事务
     *
     * @param task    任务
     * @param inTry   执行完成时，在该回调里需要手动提交
     * @param inCatch 在catch里面，在该回调里需要手动回滚
     * @return {@link Callable}<{@link T}> 执行的任务
     */
    public <T> Callable<T> decorate(Callable<T> task, @Nullable BiConsumer<T, TransactionMeta> inTry, @Nullable BiConsumer<Exception, TransactionMeta> inCatch) {
        if (!transaction) return task;
        init();
        TransactionMeta meta = new TransactionMeta(transactionManager, propagationBehavior);
        meta.begin();
        return () -> {
            T result = null;
            try {
                if (task != null) result = task.call();
                if (inTry != null) {
                    inTry.accept(result, meta);
                }
            } catch (Exception e) {
                // 回滚
                if (inCatch != null) {
                    inCatch.accept(e, meta);
                }
            }
            return result;
        };
    }


}
