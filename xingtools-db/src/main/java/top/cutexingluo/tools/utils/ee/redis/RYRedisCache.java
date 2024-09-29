package top.cutexingluo.tools.utils.ee.redis;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * spring redis 工具类
 *
 * 若依版本的工具类
 * <p>推荐使用</p>
 *
 * <p>需要导入 spring-data-redis 相关的包 </p>
 *
 * <p>
 * 1.使用前需要注入 redisTemplate , 或者 开启 redisconfig 配置 <br>
 * 2.有 redisTemplate 后，可自行注入bean, 或者 开启 redisconfig-util 配置
 * </p>
 *
 * @author ruoyi, XingTian
 * @updateFrom 1.0.4
 **/
@SuppressWarnings(value = {"unchecked", "rawtypes"})
@ConditionalOnBean(RedisTemplate.class)
//@AutoConfigureAfter(RedisTemplate.class)
//@Component
@Slf4j
@Data
public class RYRedisCache {
    //    @Resource
//    @Qualifier("redisTemplate")
//    @Qualifier("xtRedisTemplate")
    public RedisTemplate<String, Object> redisTemplate;

    public RYRedisCache() {
    }

    public RYRedisCache(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public static boolean printTrace = true;
    public static Consumer<Exception> exceptionHandler = null;

    /**
     * 保证泛型是 String, Object
     *
     * @since 1.1.4
     */
    public RedisTemplate<String, Object> get() {
        Objects.requireNonNull(redisTemplate, "redisTemplate is null !");
        return (RedisTemplate<String, Object>) redisTemplate;
    }

    //--------------------------common--------------------------------------

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void setCacheObject(final String key, final T value, final long timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 增加缓存值
     *
     * @param key   缓存的键值
     * @param value 增长的值
     */
    public <T> T incrementCacheValue(final String key, final T value) {
        if (value instanceof Long) {
            return (T) redisTemplate.opsForValue().increment(key, (Long) value);
        } else if (value instanceof Double) {
            return (T) redisTemplate.opsForValue().increment(key, (Double) value);
        }
        return null;
    }

    /**
     * 如果没有设置缓存对象 setnx <br>
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void setCacheObjectIfAbsent(final String key, final T value, final long timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeUnit);
    }

    /**
     * 如果没有设置缓存对象 setnx <br>
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObjectIfAbsent(final String key, final T value) {
        redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    /**
     * 如果存在设置缓存对象 <br>
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void setCacheObjectIfPresent(final String key, final T value, final long timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().setIfPresent(key, value, timeout, timeUnit);
    }

    /**
     * 如果存在设置缓存对象 <br>
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObjectIfPresent(final String key, final T value) {
        redisTemplate.opsForValue().setIfPresent(key, value);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, timeout, unit));
    }

    /**
     * 获取有效时间
     *
     * @param key Redis键
     * @return 有效时间
     */
    public Long getExpire(final String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 获取有效时间
     *
     * @param key Redis键
     * @return 有效时间
     */
    public long getExpireCheck(final String key) {
        Long expire = redisTemplate.getExpire(key);
        return expire != null ? expire : -1;
    }


    /**
     * 判断是否过期
     *
     * @param key Redis键
     * @return 是否过期
     * @since 1.0.4
     */
    public boolean isExpire(final String key) {
        Long expire = redisTemplate.getExpire(key);
        return expire == null || expire <= 0;
    }

    /**
     * 判断 key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key) {
        ValueOperations<String, T> operation = (ValueOperations<String, T>) redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 获得缓存的long 数值。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     * @since 1.0.4
     */
    public Long getCacheLong(final String key) {
        Object valueObj = redisTemplate.opsForValue().get(key);
        if (valueObj instanceof Number) {
            Number obj = (Number) valueObj;
            return obj.longValue();
        }
        return null;
    }


    /**
     * 获得缓存的number 数值。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     * @since 1.1.4
     */
    public Number getCacheNumber(final String key) {
        Object valueObj = redisTemplate.opsForValue().get(key);
        if (valueObj instanceof Number) {
            return (Number) valueObj;
        }
        return null;
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key   缓存键值
     * @param clazz 限定类型 更加安全
     * @return 缓存键值对应的数据
     * @since 1.0.4
     */
    @Nullable
    public <T> T getCacheObject(final String key, final Class<T> clazz) {
        if (hasKey(key)) {
            Object valueObj = redisTemplate.opsForValue().get(key);
            if (clazz.isInstance(valueObj)) {
                return (T) valueObj;
            } else if (clazz == Long.class && valueObj instanceof Integer) {
                Integer obj = (Integer) valueObj;
                return (T) Long.valueOf(obj.longValue());
            }
        }
        return null;
    }

    /**
     * 获得缓存的基本对象。
     * <p>并且更新缓存有效期</p>
     *
     * @param key        缓存键值
     * @param clazz      限定类型 更加安全
     * @param updateLive 是否更新缓存有效期
     * @param timeout    有效期时长
     * @param unit       时间单位
     * @return 缓存键值对应的数据
     * @since 1.0.4
     */
    @Nullable
    public <T> T getCacheObject(final String key, final Class<T> clazz, boolean updateLive, long timeout, TimeUnit unit) {
        if (hasKey(key)) {
            Object valueObj = redisTemplate.opsForValue().get(key);
            if (updateLive) {
                redisTemplate.expire(key, timeout, unit);
            }
            if (clazz.isInstance(valueObj)) {
                return (T) valueObj;
            } else if (clazz == Long.class && valueObj instanceof Integer) {
                Integer obj = (Integer) valueObj;
                return (T) Long.valueOf(obj.longValue());
            }
        }
        return null;
    }


    /**
     * 删除单个对象
     */
    public boolean deleteObject(final String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     */
    public boolean deleteObject(final Collection collection) {
        Long delete = redisTemplate.delete(collection);
        return delete != null && delete > 0;
    }

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> long setCacheList(final String key, final List<T> dataList) {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null ? 0 : count;
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(final String key) {
        return (List<T>) redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public <T> BoundSetOperations<String, T> setCacheSet(final String key, final Set<T> dataSet) {
        BoundSetOperations<String, T> setOperation = (BoundSetOperations<String, T>) redisTemplate.boundSetOps(key);
        Iterator<T> it = dataSet.iterator();
        while (it.hasNext()) {
            setOperation.add(it.next());
        }
        return setOperation;
    }

    /**
     * 获得缓存的set
     */
    public <T> Set<T> getCacheSet(final String key) {
        return (Set<T>) redisTemplate.opsForSet().members(key);
    }

    /**
     * 缓存Map
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap) {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    /**
     * 获得缓存的Map
     */
    public <T> Map<String, T> getCacheMap(final String key) {
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.entries(key);
    }

    /**
     * 往Hash中存入数据
     *
     * @param key   Redis键
     * @param hKey  Hash键
     * @param value 值
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value) {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public <T> T getCacheMapValue(final String key, final String hKey) {
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hKey);
    }

    /**
     * 增加缓存映射值 <br>
     * 往Hash中增加一定值
     *
     * @param key   Redis键
     * @param hKey  Hash键
     * @param value instanceof Long or Double 值
     */
    public <T> void incrementCacheMapValue(final String key, final String hKey, final T value) {
        if (value == null) return;
        HashOperations hashOperations = redisTemplate.opsForHash();
        if (value instanceof Integer)
            hashOperations.increment(key, hKey, ((Integer) value).longValue());
        else if (value instanceof Long) hashOperations.increment(key, hKey, (Long) value);
        else if (value instanceof Double) hashOperations.increment(key, hKey, (Double) value);
        else if (value instanceof String) {
            Object tar = hashOperations.get(key, hKey);
            try {
                if (tar instanceof String) {
                    try {
                        String s = com((String) tar, (String) value, 0);
                        hashOperations.put(key, hKey, s);
                    } catch (Exception e) {
                        try {
                            String s = com((String) tar, (String) value, 1);
                            hashOperations.put(key, hKey, s);
                        } catch (Exception e2) {
                            try {
                                String s = com((String) tar, (String) value, 2);
                                hashOperations.put(key, hKey, s);
                            } catch (Exception e3) {
                                String s = com((String) tar, (String) value, 3);
                                hashOperations.put(key, hKey, s);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                if (exceptionHandler != null) exceptionHandler.accept(e);
                else if (printTrace) e.printStackTrace();
                log.error("update failed ...");
            }
        }
    }

    protected String com(String v1, String v2, int switchMode) throws Exception {
        if (switchMode == 0) {
            int i = Integer.parseInt(v1);
            int j = Integer.parseInt(v2);
            return String.valueOf(i + j);
        } else if (switchMode == 1) {
            long i = Long.parseLong(v1);
            long j = Long.parseLong(v2);
            return String.valueOf(i + j);
        } else if (switchMode == 2) {
            double i = Double.parseDouble(v1);
            double j = Double.parseDouble(v2);
            return String.valueOf(i + j);
        } else {
            return v1 + v2;
        }
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key   Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys) {
        return (List<T>) redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    /**
     * 删除Hash中的某条数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return 是否成功
     */
    public boolean deleteCacheMapValue(final String key, final String hKey) {
        return redisTemplate.opsForHash().delete(key, hKey) > 0;
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Collection<String> keys(final String pattern) {
        return redisTemplate.keys(pattern);
    }


}
