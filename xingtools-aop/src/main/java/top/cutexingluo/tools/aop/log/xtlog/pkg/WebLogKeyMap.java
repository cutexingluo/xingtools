package top.cutexingluo.tools.aop.log.xtlog.pkg;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.aop.log.xtlog.LogKey;
import top.cutexingluo.tools.aop.log.xtlog.LogVarKey;
import top.cutexingluo.tools.aop.log.xtlog.base.WebLogConfig;
import top.cutexingluo.tools.basepackage.bundle.AspectBundle;
import top.cutexingluo.tools.designtools.convert.WebHandler;
import top.cutexingluo.tools.utils.se.map.XTHashMap;

import java.util.HashMap;
import java.util.function.Supplier;

/**
 * WebLog 键值对 map
 *
 * <p>SpEL 里面的 key为 =“msgMap”</p>
 *
 * <p>内部均为全局属性，无需并发处理</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/11 18:07
 * @since 1.0.4
 */
@Data
@AllArgsConstructor
public class WebLogKeyMap {
    /**
     * index = 1
     */
    protected HashMap<String, String> keyMap;
    /**
     * index = 2
     */
    protected HashMap<String, Supplier<String>> keyConverterMap;

    /**
     * index = 3
     */
    protected HashMap<String, WebHandler> keyHttpConverterMap;
    /**
     * index =4
     */
    protected HashMap<String, WebLogFunction> keyWebHttpConverterMap;

    public WebLogKeyMap() {
        keyMap = new HashMap<>();
        keyConverterMap = new HashMap<>();
        keyHttpConverterMap = new HashMap<>();
        keyWebHttpConverterMap = new HashMap<>();
    }

    public WebLogKeyMap(@NotNull WebLogKeyMap other) {
        keyMap = new HashMap<>(other.getKeyMap());
        keyConverterMap = new HashMap<>(other.getKeyConverterMap());
        keyHttpConverterMap = new HashMap<>(other.getKeyHttpConverterMap());
        keyWebHttpConverterMap = new HashMap<>(other.getKeyWebHttpConverterMap());
    }

    public void putAll(@NotNull WebLogKeyMap other) {
        keyMap.putAll(other.getKeyMap());
        keyConverterMap.putAll(other.getKeyConverterMap());
        keyHttpConverterMap.putAll(other.getKeyHttpConverterMap());
        keyWebHttpConverterMap.putAll(other.getKeyWebHttpConverterMap());
    }

    /**
     * 获取 map 值
     *
     * @return value
     */
    public <T> T get(@NotNull String key, int mapIndex) {
        switch (mapIndex) {
            case 1:
                return (T) keyMap.get(key);
            case 2:
                return (T) keyConverterMap.get(key);
            case 3:
                return (T) keyHttpConverterMap.get(key);
            case 4:
                return (T) keyWebHttpConverterMap.get(key);
            default:
                String s = keyMap.get(key);
                if (s == null) {
                    Supplier<String> supplier = keyConverterMap.get(key);
                    if (supplier == null) {
                        WebHandler handler = keyHttpConverterMap.get(key);
                        if (handler == null) {
                            WebLogFunction webLogFunction = keyWebHttpConverterMap.get(key);
                            return (T) webLogFunction;
                        }
                        return (T) handler;
                    }
                    return (T) supplier;
                }
                return (T) s;
        }
    }

//    /**
//     * 获取 string 值
//     *
//     * @return string value
//     */
//    public String getValue(@NotNull String key, int mapIndex, @NotNull WebLogConfig config, @NotNull Method method, @NotNull HttpServletRequest request, @Nullable ProceedingJoinPoint joinPoint) {
//        AspectBundle bundle = new AspectBundle(method, request, joinPoint);
//        return getValue(key, mapIndex, config, bundle);
//    }

    /**
     * 获取 string 值
     *
     * @return string value
     */
    public String getValue(@NotNull String key, int mapIndex, @NotNull WebLogConfig config, @NotNull AspectBundle bundle) {
        switch (mapIndex) {
            case 1:
                return keyMap.get(key);
            case 2:
                Supplier<String> stringSupplier = keyConverterMap.get(key);
                return stringSupplier == null ? null : stringSupplier.get();
            case 3:
                WebHandler handler = keyHttpConverterMap.get(key);
                return handler == null ? null : handler.apply(bundle);
            case 4:
                WebLogFunction f1 = keyWebHttpConverterMap.get(key);
                return f1 == null ? null : f1.apply(config, bundle);
            default:
                String s = keyMap.get(key);
                if (s == null) {
                    Supplier<String> supplier = keyConverterMap.get(key);
                    if (supplier == null) {
                        WebHandler handler2 = keyHttpConverterMap.get(key);
                        if (handler2 == null) {
                            WebLogFunction f2 = keyWebHttpConverterMap.get(key);
                            return f2 == null ? null : f2.apply(config, bundle);
                        }
                        return handler2.apply(bundle);
                    }
                    return supplier.get();
                }
                return s;
        }
    }


    /**
     * 存入 key-value
     *
     * @return value
     */
    public <V> V put(@NotNull String key, int mapIndex, @NotNull V value) {
        switch (mapIndex) {
            case 1:
                return (V) keyMap.put(key, String.valueOf(value));
            case 2:
                return (V) keyConverterMap.put(key, (Supplier<String>) value);
            case 3:
                return (V) keyHttpConverterMap.put(key, (WebHandler) value);
            case 4:
                return (V) keyWebHttpConverterMap.put(key, (WebLogFunction) value);
            default:
                if (value instanceof Supplier) {
                    return (V) keyConverterMap.put(key, (Supplier<String>) value);
                } else if (value instanceof WebHandler) {
                    return (V) keyHttpConverterMap.put(key, (WebHandler) value);
                } else if (value instanceof WebLogFunction) {
                    return (V) keyWebHttpConverterMap.put(key, (WebLogFunction) value);
                } else {
                    return (V) keyMap.put(key, String.valueOf(value));
                }
        }
    }

    /**
     * 默认 key - value  赋值
     */
    public static void defaultKeyMap(HashMap<String, String> map) {
        XTHashMap.putMapEntriesFromDValues(map,
                LogKey.ADD_KEY, LogKey.ADD,
                LogKey.WRAP_KEY, LogKey.WRAP,
                LogKey.WRAP_STR, LogKey.WRAP_SYM
        );
    }

    /**
     * 默认 key - Supplier  赋值
     */
    public static void defaultKeyConverterMap(HashMap<String, Supplier<String>> map) {
        XTHashMap.putMapEntriesFromDValues(map,
                LogKey.S.getKey(), LogKey.S.getValue(),
                LogKey.S4.getKey(), LogKey.S4.getValue(),
                LogKey.TAB.getKey(), LogKey.TAB.getValue(),
                LogKey.START.getKey(), LogKey.START.getValue(),
                LogKey.END.getKey(), LogKey.END.getValue()
        );
    }

    /**
     * 默认 key - WebHandler  赋值
     */
    public static void defaultKeyHttpConverterMap(HashMap<String, WebHandler> map) {
        XTHashMap.putMapEntriesFromDValues(map,
                LogVarKey.IP.getKey(), LogVarKey.IP.getValue(),
                LogVarKey.REAL_IP.getKey(), LogVarKey.REAL_IP.getValue(),
                LogVarKey.HOST_IP.getKey(), LogVarKey.HOST_IP.getValue(),
                LogVarKey.HEADER.getKey(), LogVarKey.HEADER.getValue(),
                LogVarKey.METHOD.getKey(), LogVarKey.METHOD.getValue(),
                LogVarKey.METHOD_NAME.getKey(), LogVarKey.METHOD_NAME.getValue(),
                LogVarKey.HTTP_URI.getKey(), LogVarKey.HTTP_URI.getValue(),
                LogVarKey.HTTP_METHOD.getKey(), LogVarKey.HTTP_METHOD.getValue(),
                LogVarKey.G_METHOD.getKey(), LogVarKey.G_METHOD.getValue(),
                LogVarKey.TYPE.getKey(), LogVarKey.TYPE.getValue()
        );

    }

    /**
     * 默认 key - WebLogFunction  赋值
     */
    public static void defaultKeyWebHttpConverterMap(HashMap<String, WebLogFunction> map) {
        XTHashMap.putMapEntriesFromDValues(map,
                LogVarKey.B_NAME.getKey(), LogVarKey.B_NAME.getValue(),
                LogVarKey.CLASS_METHOD.getKey(), LogVarKey.CLASS_METHOD.getValue(),
                LogVarKey.S_NAME.getKey(), LogVarKey.S_NAME.getValue(),
                LogVarKey.S_TYPE_NAME.getKey(), LogVarKey.S_TYPE_NAME.getValue(),
                LogVarKey.ARGS.getKey(), LogVarKey.ARGS.getValue()
        );
    }
}
