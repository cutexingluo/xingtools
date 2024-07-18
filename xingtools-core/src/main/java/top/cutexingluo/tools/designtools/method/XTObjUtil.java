package top.cutexingluo.tools.designtools.method;


import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.common.Constants;
import top.cutexingluo.tools.exception.ServiceException;
import top.cutexingluo.tools.utils.se.character.XTCharUtil;
import top.cutexingluo.tools.utils.se.map.XTSetUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * <p>反射工具类</p>
 * <p>反射设置属性值</p>
 * <p>于 v1.1.1 合并ruoyi BeanUtils</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/2 21:42
 */
public class XTObjUtil {

    /**
     * Bean方法名中属性名开始的下标
     */
    protected static final int BEAN_METHOD_PROP_INDEX = 3;

    /**
     * get
     */
    public static final  String STR_GET = "get";

    /**
     * set
     */
    public static final  String STR_SET= "set";

    /**
     * 匹配getter方法的正则表达式
     */
    protected static final Pattern GET_PATTERN = Pattern.compile("get(\\p{javaUpperCase}\\w*)");

    /**
     * 匹配setter方法的正则表达式
     */
    protected static final Pattern SET_PATTERN = Pattern.compile("set(\\p{javaUpperCase}\\w*)");




    public static class Unsafe {
        public static Unsafe newInstance() {
            return new Unsafe();
        }


        /**
         * 设置属性为空
         * <p>含有该属性名的字段设置为null </p>
         *
         * @param item       对象
         * @param properties 属性名
         * @return {@link T}
         */
        public <T> T setNull(@NotNull T item, @NotNull String... properties) {
            return setPropertyContains(item, field -> {
                try {
                    field.set(item, null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }, properties);
        }


        /**
         * 设置属性值
         * <p>直接访问属性</p>
         *
         * @param item     对象
         * @param property 属性名
         * @param newValue 新值
         * @return {@link T}
         */
        public <T> T setProperty(@NotNull T item, @NotNull String property, Object newValue) {
            try {
                Field field = item.getClass().getDeclaredField(property);
                field.setAccessible(true);
                field.set(item, newValue);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                throw new ServiceException(Constants.CODE_400.getCode(), "没有该属性，或者没有该权限");
            }
            return item;
        }

        /**
         * 遍历字段自行设置属性值
         * <p>直接访问属性</p>
         *
         * @param item          对象
         * @param fieldConsumer 属性遍历操作消费者
         * @return {@link T}
         */
        public <T> T setPropertyAllFields(@NotNull T item, Consumer<Field> fieldConsumer) {
            Field[] fields = item.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                fieldConsumer.accept(field);
            }
            return item;
        }

        /**
         * 设置属性
         * <p>包含 propertyNames 名称的会进入Consumer</p>
         * <p>直接访问属性</p>
         * 遍历字段自行设置属性值
         *
         * @param item          对象
         * @param fieldConsumer 属性遍历操作消费者
         * @param propertyNames 满足需要修改的属性名称，会过滤到消费者
         * @return {@link T}
         */
        public <T> T setPropertyContains(@NotNull T item, @NotNull Consumer<Field> fieldConsumer, @NotNull String... propertyNames) {
            Field[] fields = item.getClass().getDeclaredFields();
            HashSet<String> set = XTSetUtil.hashSet(propertyNames);
            for (Field field : fields) {
                field.setAccessible(true);
                if (set.contains(field.getName())) {
                    fieldConsumer.accept(field);
                }
            }
            return item;
        }

        /**
         * 设置属性
         * <p> propertyNames 名称的所有字段都会进入Consumer</p>
         * <p>没有 propertyNames  的字段就会报错，请使用setPropertyContains</p>
         * <p>直接访问属性</p>
         * 遍历字段自行设置属性值
         *
         * @param item          对象
         * @param fieldConsumer 属性遍历操作消费者
         * @param propertyNames 需要修改的属性名称，都会过滤到消费者
         * @return {@link T}
         */
        public <T> T setProperty(@NotNull T item, @NotNull Consumer<Field> fieldConsumer, @NotNull String... propertyNames) {
            for (String name : propertyNames) {
                try {
                    Field field = item.getClass().getDeclaredField(name);
                    field.setAccessible(true);
                    fieldConsumer.accept(field);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
            return item;
        }

        /**
         * 获取属性值
         * <p>直接访问属性</p>
         *
         * @param item     对象
         * @param property 属性名
         * @return {@link Object}
         */
        public <T> Object getProperty(@NotNull T item, @NotNull String property) {
            Object result;
            try {
                Field field = item.getClass().getDeclaredField(property);
                field.setAccessible(true);
                result = field.get(item);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                throw new ServiceException(Constants.CODE_400.getCode(), "没有该属性，或者没有该权限");
            }
            return result;
        }
    }


    /**
     * 利用反射获取对象属性值
     * 只获取get和set方法
     * 为了安全，不使用getDeclaredFields()
     *
     * @param item     对象
     * @param property 属性名，目标属性必须含有get方法。
     *                 如果参数未带get,将会自动加上get
     * @param isDone   是否不添加get前缀
     * @param <T>      对象的类型
     * @return 返回Object对象，注意转型
     */
    public static <T> Object getProperty(@NotNull T item, @NotNull String property, boolean isDone) {
        Object value = null;
        property = XTCharUtil.addCheckPrefix(property, STR_GET, isDone);
        try {
            Method method = item.getClass().getMethod(property);
            method.setAccessible(true);
            value = method.invoke(item);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new ServiceException(Constants.CODE_400.getCode(), "没有该方法，或者没有该权限");
        }
        return value;
    }

    public static <T> Object getProperty(@NotNull T item, @NotNull String property) {
        return getProperty(item, property, false);
    }

    /**
     * 利用反射设置对象属性值
     * 只获取get和set方法
     * 为了安全，不使用getDeclaredFields()
     *
     * @param item     对象
     * @param property 属性名，目标属性必须含有set方法。
     *                 如果参数未带set,将会自动加上set
     * @param newValue 设置的新值
     * @param isDone   是否不添加set前缀
     * @param param    新值的类型，即set参数类型
     * @param <T>      对象类型
     */
    public static <T> T setProperty(@NotNull T item, @NotNull String property,
                                    Object newValue, boolean isDone, @NotNull Class<?> param) {
        property = XTCharUtil.addCheckPrefix(property, STR_SET, isDone);
        try {
            Method method;
            method = item.getClass().getMethod(property, param);
            method.setAccessible(true);
            method.invoke(item, newValue);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new ServiceException(Constants.CODE_400.getCode(), "没有该方法，或者没有该权限");
        }
        return item;
    }

    public static <T> T setProperty(@NotNull T item, @NotNull String property,
                                    Object newValue, @NotNull Class<?> param) {
        return setProperty(item, property, newValue, false, param);
    }

    /**
     * 利用反射获取对象列表单一属性的属性值
     * 只获取get和set方法
     * 为了安全，不使用getDeclaredFields()
     *
     * @param items    对象列表
     * @param property 属性名，目标属性必须含有get方法。
     *                 如果参数未带get,将会自动加上get
     * @param <T>      对象类型
     */
    @NotNull
    public static <T> List<Object> getListProperty(@NotNull List<T> items, String property) {
        List<Object> res = new ArrayList<Object>();
        property = XTCharUtil.addCheckPrefix(property, STR_GET, false);
        for (T item : items) {
            res.add(getProperty(item, property, true));
        }
        return res;
    }

    /**
     * 利用反射设置对象列表的单一属性的属性值
     * 只获取get和set方法
     * 为了安全，不使用getDeclaredFields()
     *
     * @param items    对象列表
     * @param property 属性名，目标属性必须含有set方法。
     *                 如果参数未带set,将会自动加上set
     * @param newValue 设置的新值
     * @param param    新值的类型，如果未知可不写，或为null
     * @param <T>      对象类型
     */
    public static <T> List<T> setListProperty(@NotNull List<T> items, String property, Object newValue, Class<?> param) {
        property = XTCharUtil.addCheckPrefix(property, STR_SET, false);
        for (T item : items) {
            setProperty(item, property, newValue, true, param);
        }
        return items;
    }

    // ruoyi
    /**
     * 获取对象的setter方法。
     *
     * @param obj 对象
     * @return 对象的setter方法列表
     */
    public static @NotNull List<Method> getSetterMethods(@NotNull Object obj) {
        // setter方法列表
        List<Method> setterMethods = new ArrayList<Method>();

        // 获取所有方法
        Method[] methods = obj.getClass().getMethods();

        // 查找setter方法

        for (Method method : methods) {
            Matcher m = SET_PATTERN.matcher(method.getName());
            if (m.matches() && (method.getParameterTypes().length == 1)) {
                setterMethods.add(method);
            }
        }
        // 返回setter方法列表
        return setterMethods;
    }

    /**
     * 获取对象的getter方法。
     *
     * @param obj 对象
     * @return 对象的getter方法列表
     */
    public static @NotNull List<Method> getGetterMethods(@NotNull Object obj) {
        // getter方法列表
        List<Method> getterMethods = new ArrayList<Method>();
        // 获取所有方法
        Method[] methods = obj.getClass().getMethods();
        // 查找getter方法
        for (Method method : methods) {
            Matcher m = GET_PATTERN.matcher(method.getName());
            if (m.matches() && (method.getParameterTypes().length == 0)) {
                getterMethods.add(method);
            }
        }
        // 返回getter方法列表
        return getterMethods;
    }

    /**
     * 检查Bean方法名中的属性名是否相等。<br>
     * 如getName()和setName()属性名一样，getName()和setAge()属性名不一样。
     *
     * @param m1 方法名1
     * @param m2 方法名2
     * @return 属性名一样返回true，否则返回false
     */

    public static boolean isMethodPropEquals(@NotNull String m1, @NotNull String m2) {
        return m1.substring(BEAN_METHOD_PROP_INDEX).equals(m2.substring(BEAN_METHOD_PROP_INDEX));
    }










}

/**
 * <!-- https://mvnrepository.com/artifact/com.alibaba/fastjson -->
 * <dependency>
 * <groupId>com.alibaba</groupId>
 * <artifactId>fastjson</artifactId>
 * <version>1.2.41</version>
 * </dependency>
 * <p>
 * JSONArray JSON.parseArray( String('JSON') )
 * JSONObject JSONArrayImpl.getJSONObject(index)
 * String JSONObject.getString(String('index'))
 * <p>
 * String jsonString = JSON.toJSONString(obj);
 * VO vo = JSON.parseObject("...", VO.class);
 * import com.alibaba.fastjson.TypeReference;
 * List<VO> list = JSON.parseObject("...", new TypeReference<List<VO>>() {});
 */

