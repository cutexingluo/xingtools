package top.cutexingluo.tools.aop.transactional;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * TransactionMeta 事务元数据，简易配置
 * <p>目的是简化函数式接口的操作，减少参数</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/1 20:56
 */
@Data
@AllArgsConstructor
public class TransactionMeta {

    private PlatformTransactionManager transactionManager;
    /**
     * 事务级别
     */
    private int propagationBehavior = TransactionDefinition.PROPAGATION_REQUIRED;

    private TransactionStatus transactionStatus;

    public TransactionMeta(PlatformTransactionManager transactionManager, int propagationBehavior) {
        this.transactionManager = transactionManager;
        this.propagationBehavior = propagationBehavior;
    }

    protected TransactionStatus getTransaction() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();// 开启事务
        def.setPropagationBehavior(propagationBehavior);// 设置事务隔离级别
        return transactionManager.getTransaction(def);
    }

    protected void commit(TransactionStatus transaction) {
        transactionManager.commit(transaction);
    }

    protected void rollback(TransactionStatus transaction) {
        transactionManager.rollback(transaction);
    }

    public void begin() {
        this.transactionStatus = getTransaction();
    }

    public void commit() {
        commit(this.transactionStatus);
    }

    public void rollback() {
        rollback(this.transactionStatus);
    }
}
