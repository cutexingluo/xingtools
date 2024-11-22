package top.cutexingluo.tools.utils.ee.mysql;

import lombok.Data;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.util.Objects;

/**
 * TransactionMeta 事务元数据，简易配置
 * <p>目的是简化函数式接口的操作，减少参数</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/7 11:03
 * @since 1.1.6
 */
@Data
public class TransactionMeta {

    /**
     * 事务管理器
     */
    @NotNull
    private PlatformTransactionManager transactionManager;

    /**
     * 事务定义
     */
    @NotNull
    private TransactionDefinition transactionDefinition;


    public TransactionMeta(@NotNull PlatformTransactionManager transactionManager, @NotNull TransactionDefinition transactionDefinition) {
        Objects.requireNonNull(transactionManager, "transactionManager should not be null");
        Objects.requireNonNull(transactionDefinition, "transactionDefinition should not be null");
        this.transactionManager = transactionManager;
        this.transactionDefinition = transactionDefinition;
    }

    /**
     * 获取事务/开启事务
     */
    public TransactionStatus getTransaction() {
        return transactionManager.getTransaction(transactionDefinition);// 开启事务
    }

    /**
     * 开启事务
     */
    public Status begin() {
        return new Status(getTransaction());
    }

    /**
     * 提交事务
     */
    public void commit(TransactionStatus transaction) {
        transactionManager.commit(transaction);
    }

    /**
     * 回滚事务
     */
    public void rollback(TransactionStatus transaction) {
        transactionManager.rollback(transaction);
    }

    /**
     * 获取事务状态封装
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public Status toStatus(TransactionStatus transaction) {
        return new Status(transaction);
    }

    /**
     * 事务状态
     */
    @Getter
    public class Status {
        private final TransactionStatus status;

        public Status(TransactionStatus status) {
            this.status = status;
        }

        /**
         * 提交事务
         */
        public void commit() {
            TransactionMeta.this.commit(status);
        }

        /**
         * 回滚事务
         */
        public void rollback() {
            TransactionMeta.this.rollback(status);
        }
    }


}
