package top.cutexingluo.tools.designtools.juc.utils;


import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * <p>
 * JUC 工具类 ，主要用于  获取同步Collections
 *
 * @author XingTian
 * @version 1.0
 * @date 2022-11-21
 */

public class XTJUCUtils {
    //*********************************************
    //DCL懒汉式
    public static synchronized <T> T getNew(T object, Class<T> clazz, Callable<T> callable) throws Exception {
        if (object == null) {
            synchronized (clazz) {
                if (object == null) {
                    object = callable.call();
                }
            }
        }
        return object;
    }

    //*********************************************

    //    @XTException(name = "XTJUCUtils getCollectionsSync error", desc = "getCollectionsSync 内部方法错误")
    public static <T> Collection<T> toCollectionsSync(Collection<T> collection) {//同步集合类
        if (collection == null) return null;
        return Collections.synchronizedCollection(collection);
    }

    //    @XTException(name = "XTJUCUtils getCollectionsSyncList error", desc = "可能其内部同步方法错误")
    public static <T> List<T> toCollectionsSyncList(List<T> list) {//同步集合列表
        if (list == null) return null;
        return Collections.synchronizedList(list);
    }

    //    @XTException(name = "XTJUCUtils getCollectionsSyncSet error", desc = "可能其内部同步方法错误")
    public static <T> List<T> getCollectionsSyncSet(List<T> list) {//同步集合列表
        if (list == null) return null;
        return Collections.synchronizedList(list);
    }

    //    @XTException(name = "XTJUCUtils getCollectionsSyncSet error", desc = "可能其内部同步方法错误")
    public static <K, V> Map<K, V> toCollectionsSyncHashMap(Map<K, V> map) {//同步集合列表
        if (map == null) return null;
        return Collections.synchronizedMap(map);
    }

    //*********************************************

    public static <T> List<T> getSyncList() { //写入时复制
        return new CopyOnWriteArrayList<T>();
    }

    public static <T> Set<T> getSyncSet() { //写入时复制
        return new CopyOnWriteArraySet<T>();
    }

    public static <K, V> ConcurrentHashMap<K, V> getSyncHashMap() { //并发HashMap
        return new ConcurrentHashMap<K, V>();
    }
    //*********************************************
}
