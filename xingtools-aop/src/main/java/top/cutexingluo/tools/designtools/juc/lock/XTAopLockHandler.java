package top.cutexingluo.tools.designtools.juc.lock;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.redisson.api.RedissonClient;
import top.cutexingluo.tools.basepackage.basehandler.CallableHandler;
import top.cutexingluo.tools.designtools.juc.lock.extra.XTLockMeta;
import top.cutexingluo.tools.designtools.juc.lock.handler.XTExtLockHandler;
import top.cutexingluo.tools.designtools.juc.lock.handler.XTLockHandler;

import java.util.concurrent.Callable;

/**
 *  XTAopLock 注解 handler
 * <p>建议直接使用 {@link  top.cutexingluo.tools.designtools.juc.lock.handler.XTLockHandler}</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/4/6 21:13
 * @update 1.0.2, v1.1.1
 * @since  1.1.1
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class XTAopLockHandler  implements CallableHandler {
    protected XTAopLock lockAnno;
    protected XTLockMeta lockMeta;
    protected RedissonClient redissonClient;
    /**
     * 是否使用 RedissonClient 参数, true 且 redissonClient 不为空，则使用自动获取 RedissonClient  bean
     */
    protected boolean openRedissonClient = false;

    public XTAopLockHandler(XTAopLock lockAnno, RedissonClient redissonClient, boolean openRedissonClient) {
        this.lockAnno = lockAnno;
        this.redissonClient = redissonClient;
        this.openRedissonClient = openRedissonClient;
    }

    public XTAopLockHandler(XTAopLock lockAnno, RedissonClient redissonClient) {
        this(lockAnno,redissonClient,true);
    }

    public XTAopLockHandler(XTAopLock lockAnno, boolean openRedissonClient) {
        this(lockAnno,null,openRedissonClient);
    }

    /**
     * 获取 XTLockMeta 对象
     */
    @Contract("_ -> new")
    public static @NotNull XTLockMeta parseMeta(@NotNull XTAopLock lockAnno){
        return new XTLockMeta(lockAnno.name(), lockAnno.isFair(), lockAnno.lockType(), lockAnno.tryTimeout());
    }


    /**
     * 转成 XTLockHandler
     */
    @NotNull
    public XTLockHandler toXTLockHandler(){
        return new XTLockHandler(parseMeta(lockAnno), redissonClient, openRedissonClient);
    }


    /**
     * 转成 XTExtLockHandler
     */
    @NotNull
    public XTExtLockHandler toXTExtLockHandler(){
        return new XTExtLockHandler(parseMeta(lockAnno), redissonClient, openRedissonClient);
    }


    /**
     * 直接使用 XTLockHandler 执行器调用lock方法
     */
    public <T> T lock(Callable<T> task) throws Exception {
        XTLockHandler handler = toXTLockHandler();
        return handler.decorate(task).call();
    }
    /**
     * 直接使用 XTExtLockHandler 执行器并初始化调用lock方法
     */
    public <T> T extLock(Callable<T> task) throws Exception {
        XTExtLockHandler handler = toXTExtLockHandler().initSelf();
        return handler.decorate(task).call();
    }

    /**
     * 自动包装 XTLockHandler 执行器
     * <p>1.根据配置可自动获取 RedissonClient </p>
     * <p>2.自动调用 XTLockHandler 的 decorate 方法</p>
     */
    @Override
    public <T> Callable<T> decorate(Callable<T> callable) {
        XTExtLockHandler lockHandler = toXTExtLockHandler().initSelf();
        return lockHandler.decorate(callable);
    }
}
