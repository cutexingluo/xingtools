package top.cutexingluo.tools.utils.spring;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>最迟注入的，要晚于@Autowired, InitializingBean, @PostConstruct, @Bean</p>
 * 供参考，建议直接使用SpringUtil <br>
 * 该工具类也可以使用，开启Server服务后默认注入
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/2 22:08
 */

public class SpringUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;


    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        if (SpringUtils.applicationContext == null) {
            SpringUtils.applicationContext = applicationContext;
        }

    }

    /**
     * 获取注册 XingToolsAutoConfiguration 的 bean 名称
     * <br>
     * 不包含 aop 注解 bean
     */
    public static List<String> getXingToolsBeans() {
        return getXingToolsBeans(applicationContext);
    }

    /**
     * 获取注册 XingToolsAutoConfiguration 的 bean 名称
     * <br>
     * 不包含 aop 注解 bean
     */
    public static List<String> getXingToolsBeans(ApplicationContext application) {
        String[] names = application.getBeanDefinitionNames();
        List<String> result = new ArrayList<>();
        for (String name : names) {
            if (name.startsWith("top.cutexingluo.tools") || name.startsWith("top.cutexingluo")) {
                result.add(name);
            }
        }
        return result;
    }

    /**
     * 获取所有XingToolsAutoConfiguration 的 bean 名称
     * <br>
     * 包含 aop 注解 bean
     */
    public static List<String> getAllXingToolsBeans() {
        return getAllXingToolsBeans(applicationContext);
    }

    /**
     * 获取所有XingToolsAutoConfiguration 的 bean 名称
     * <br>
     * 包含 aop 注解 bean
     */
    public static List<String> getAllXingToolsBeans(ApplicationContext application) {
        String[] names = getAllDefinitionBeans();
        List<String> result = new ArrayList<>();
        List<String> simpleBeans = Arrays.asList(XTBeanConfig.getSimpleBeanNames());
        for (String name : names) {
            if (name.startsWith("top.cutexingluo.tools") || name.startsWith("top.cutexingluo")) {
                result.add(name);
            } else if (simpleBeans.contains(name)) {
                result.add(name);
            }
        }

        return result;
    }

    /**
     * 模糊查询Bean名称
     */
    public static List<String> getBeansLike(String likeName) {
        String[] names = getAllDefinitionBeans();
        List<String> result = new ArrayList<>();
        for (String name : names) {
            if (name.contains(likeName)) {
                result.add(name);
            }
        }
        return result;
    }

    // 获取所有定义的bean

    /**
     * 获取所有定义的bean
     */
    public static String[] getAllDefinitionBeans() {
        return applicationContext.getBeanDefinitionNames();
    }

    //获取applicationContext

    /**
     * 获取applicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    //通过name获取 Bean.

    /**
     * 通过name获取 Bean.
     */
    @NotNull
    public static Object getBean(String name) throws BeansException {
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean.
     * <p>无异常，返回空</p>
     */
    @Nullable
    public static Object getBeanNoExc(String name) {
        try {
            return getApplicationContext().getBean(name);
        } catch (BeansException e) {
            return null;
        }
    }

    /**
     * 通过class获取Bean.
     * <p>无异常，返回空</p>
     *
     * @since 1.0.4
     */
    @Nullable
    public static Object getBeanNoExc(@NonNull ApplicationContext applicationContext, String name) {
        try {
            return applicationContext.getBean(name);
        } catch (BeansException e) {
            return null;
        }
    }

    //通过class获取Bean.

    /**
     * 通过class获取Bean.
     */
    @NotNull
    public static <T> T getBean(Class<T> clazz) throws BeansException {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过class获取Bean.
     * <p>无异常，返回空</p>
     *
     * @since 1.0.4
     */
    @Nullable
    public static <T> T getBeanNoExc(@NonNull ApplicationContext applicationContext, Class<T> clazz) {
        try {
            return applicationContext.getBean(clazz);
        } catch (BeansException e) {
            return null;
        }
    }

    /**
     * 通过class获取Bean.
     * <p>无异常，返回空</p>
     */
    @Nullable
    public static <T> T getBeanNoExc(Class<T> clazz) {
        try {
            return getApplicationContext().getBean(clazz);
        } catch (BeansException e) {
            return null;
        }
    }


    //通过name,以及Clazz返回指定的Bean

    /**
     * 通过name,以及Clazz返回指定的Bean
     */
    @NotNull
    public static <T> T getBean(String name, Class<T> clazz) throws BeansException {
        return getApplicationContext().getBean(name, clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     * <p>无异常，返回空</p>
     *
     * @since 1.0.4
     */
    @Nullable
    public static <T> T getBeanNoExc(@NonNull ApplicationContext applicationContext, String name, Class<T> clazz) {
        try {
            return getApplicationContext().getBean(name, clazz);
        } catch (BeansException e) {
            return null;
        }
    }


    /**
     * 通过name,以及Clazz返回指定的Bean
     * <p>无异常，返回空</p>
     */
    @Nullable
    public static <T> T getBeanNoExc(String name, Class<T> clazz) {
        try {
            return getApplicationContext().getBean(name, clazz);
        } catch (BeansException e) {
            return null;
        }
    }

    public static Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return getApplicationContext().getType(name);
    }


}
