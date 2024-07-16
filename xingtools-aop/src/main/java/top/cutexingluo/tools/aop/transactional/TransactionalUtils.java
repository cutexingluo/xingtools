package top.cutexingluo.tools.aop.transactional;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

/**
 * 事务工具类
 * <p>历史遗留类</p>
 *
 * <p>需要导入 spring-jdbc</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2022/11/22 19:27
 */
//@Component
@Data
public class TransactionalUtils {

    @Autowired
    protected DataSourceTransactionManager dataSourceTransactionManager;

    // 开启事务
    // 只开不提交或回滚 会有行锁

    /**
     * 开启事务<br>
     * 只开不提交或回滚 会有行锁
     */
    public TransactionStatus begin() {
        // 事务隔离级别属于mysql，事务传播行为属于spring
        return dataSourceTransactionManager.getTransaction(new DefaultTransactionAttribute());
    }

    /**
     * 提交事务
     */
    public void commit(TransactionStatus transaction) {
        dataSourceTransactionManager.commit(transaction);
    }

    /**
     * 回滚事务
     */
    public void rollback(TransactionStatus transaction) {
        dataSourceTransactionManager.rollback(transaction);
    }


}
