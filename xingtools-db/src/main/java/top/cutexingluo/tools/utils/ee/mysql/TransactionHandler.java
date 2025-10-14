package top.cutexingluo.tools.utils.ee.mysql;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionTemplate;
import top.cutexingluo.core.basepackage.basehandler.SupplierHandler;
import top.cutexingluo.core.basepackage.struct.Refreshable;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * 事务处理器
 * <p>需要导入 spring-tx 包</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/7 14:06
 * @since 1.1.6
 */
public class TransactionHandler extends TransactionTemplate implements SupplierHandler, InitializingBean, Refreshable<TransactionHandler>, TransactionOperations {

    @NotNull
    protected TransactionMeta meta;

    public TransactionHandler(@NotNull TransactionMeta meta) {
        this.meta = meta;
    }

    public TransactionHandler(PlatformTransactionManager transactionManager) {
        super(transactionManager);
        this.meta = new TransactionMeta(transactionManager, this);
    }

    public TransactionHandler(PlatformTransactionManager transactionManager, TransactionDefinition transactionDefinition) {
        super(transactionManager, transactionDefinition);
        this.meta = new TransactionMeta(transactionManager, transactionDefinition);
    }

    public TransactionMeta getMeta() {
        return meta;
    }

    public void setMeta(TransactionMeta meta) {
        this.meta = meta;
    }

    @Override
    public void afterPropertiesSet() {
        if (getTransactionManager() == null) {
            throw new IllegalArgumentException("Property 'transactionManager' is required");
        }
        Objects.requireNonNull(meta, "meta should not be null");
    }

    /**
     * 刷新配置到父类
     *
     * <p>meta 更改时, 如果需要调用原生 TransactionTemplate.execute 方法，需要刷新给父类</p>
     */
    @Override
    public TransactionHandler refresh() {
        super.setTransactionManager(meta.getTransactionManager());
        TransactionDefinition def = meta.getTransactionDefinition();
        setPropagationBehavior(def.getPropagationBehavior());
        setIsolationLevel(def.getIsolationLevel());
        setReadOnly(def.isReadOnly());
        setTimeout(def.getTimeout());
        setName(def.getName());
        return this;
    }

    /**
     * 设置事务管理器 (两处)
     */
    @Override
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        super.setTransactionManager(transactionManager);
        meta.setTransactionManager(transactionManager);
    }

    @Override
    public PlatformTransactionManager getTransactionManager() {
        return meta.getTransactionManager();
    }

    /**
     * 执行事务 (包装)
     */
    @Override
    public <T> Supplier<T> decorate(Supplier<T> supplier) {
        return () -> runTask(status -> supplier.get());
    }

    public <V> V runTask(TransactionCallback<V> task) {
        return runTask(task, null, null, true);
    }

    public <V> V runTask(Supplier<V> task) {
        return runTask((status) -> task.get());
    }

    public <V> V runTask(Runnable task) {
        return runTask((status) -> {
            task.run();
            return null;
        });
    }


    public <V> V runTask(TransactionCallback<V> task, BiConsumer<TransactionMeta.Status, V> successConsumer, BiFunction<TransactionMeta.Status, Exception, V> failHandler, boolean failHandlerAutoRollback) {
        return runTaskHandler(task, (status, ret) -> {
            if (successConsumer != null) successConsumer.accept(status, ret);
            return ret;
        }, failHandler, failHandlerAutoRollback);
    }

    /**
     * 事务处理器
     *
     * @param task                       任务
     * @param successHandler             提交后处理（同时也在try 里面）
     * @param failHandler                失败处理器
     * @param hasFailHandlerAutoRollback 失败处理器存在时, 是否自动回滚
     */
    public <V> V runTaskHandler(TransactionCallback<V> task, BiFunction<TransactionMeta.Status, V, V> successHandler, BiFunction<TransactionMeta.Status, Exception, V> failHandler, boolean hasFailHandlerAutoRollback) {
        TransactionMeta.Status status = meta.begin();
        V ret = null;
        try {
            ret = task.doInTransaction(status.getStatus());
            status.commit();
            if (successHandler != null) ret = successHandler.apply(status, ret);
        } catch (Exception e) {
            if (failHandler == null) {
                status.rollback();
            } else {
                if (hasFailHandlerAutoRollback) status.rollback();
                ret = failHandler.apply(status, e);
            }
        }
        return ret;
    }


}
