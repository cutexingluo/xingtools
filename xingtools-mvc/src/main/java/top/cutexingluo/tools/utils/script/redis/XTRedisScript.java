package top.cutexingluo.tools.utils.script.redis;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * xingtools 自带的 redis 脚本
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/9 17:21
 */
public class XTRedisScript {

    /**
     * 通用 本地限流 lua 脚本路径
     * <p>支持节流和防抖</p>
     * <p>节流 和 localRedisLastLimitPath 类似</p>
     * <p>防抖 时间会在每一次提交后刷新, 不管是接受还是拒绝</p>
     */
    public static final String localRedisLimitPath = "lua/redis-limit.lua";

    /**
     * 最后提交 本地限流 lua 脚本路径
     * <p>支持节流和防抖</p>
     * <p>节流 和 localRedisLimitPath 类似</p>
     * <p>防抖 仅在最大次数拒绝 刷新时间后, 不再刷新</p>
     */
    public static final String localRedisLastLimitPath = "lua/redis-last-limit.lua";


    /**
     * 初始化本地限流脚本
     */
    @NotNull
    public static DefaultRedisScript<Long> initLocalRedisLimitScript() {
        return initScript(localRedisLimitPath, Long.class);
    }

    /**
     * 初始化本地节流限流脚本
     */
    @NotNull
    public static DefaultRedisScript<Long> initLocalRedisLastLimitScript() {
        return initScript(localRedisLastLimitPath, Long.class);
    }

    /**
     * 初始化脚本
     *
     * @param classPath class 路径下的文件
     */
    @NotNull
    public static <T> DefaultRedisScript<T> initScript(String classPath, Class<T> resultClazz) {
        DefaultRedisScript<T> redisScript = new DefaultRedisScript<>();
        ClassPathResource resource = new ClassPathResource(classPath);
        ResourceScriptSource scriptSource = new ResourceScriptSource(resource);
        redisScript.setResultType(resultClazz);
        redisScript.setScriptSource(scriptSource);
        return redisScript;
    }

}
