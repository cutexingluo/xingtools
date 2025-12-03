package top.cutexingluo.tools.utils.spring;


import cn.hutool.core.util.ArrayUtil;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * <p>最迟注入的，要晚于@Autowired, InitializingBean, @PostConstruct, @Bean</p>
 * 供参考，建议直接使用SpringUtil <br>
 * 该工具类也可以使用，开启Server服务后默认注入
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/2 22:08
 */

public class SpringUtils implements BeanFactoryPostProcessor, ApplicationContextAware {

    @Getter
    private static ApplicationContext applicationContext = null;
    /**
     * Spring应用上下文环境
     */
    @Getter
    private static ConfigurableListableBeanFactory beanFactory;

    @Override
    public void postProcessBeanFactory(@NotNull ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        beanFactory = configurableListableBeanFactory;
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        if (SpringUtils.applicationContext == null) {
            SpringUtils.applicationContext = applicationContext;
        }
    }


    /**
     * 获取所有XingToolsAutoConfiguration 的 bean 名称
     */
    public static List<String> getXingToolsBeans() {
        return getXingToolsBeans(applicationContext, true);
    }

    /**
     * 获取所有XingToolsAutoConfiguration 的 bean 名称
     */
    public static List<String> getXingToolsBeans(ApplicationContext application, boolean allPackage) {
        String[] names = getAllDefinitionBeans();
        List<String> result = new ArrayList<>(names.length);
        if (allPackage) {
            for (String name : names) {
                if (name.startsWith("top.cutexingluo.tools") || name.startsWith("top.cutexingluo")) {
                    result.add(name);
                }
            }
        } else {
            for (String name : names) {
                if (name.startsWith("top.cutexingluo.tools")) {
                    result.add(name);
                }
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


    //通过name获取 Bean.

    /**
     * 通过name获取 Bean.
     */
    @NotNull
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    /**
     * 通过class获取Bean.
     * <p>无异常，返回空</p>
     */
    @Nullable
    public static Object getBeanNoExc(String name) {
        try {
            return applicationContext.getBean(name);
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
    public static Object getBeanNoExc(@NotNull ApplicationContext applicationContext, String name) {
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
        return applicationContext.getBean(clazz);
    }

    /**
     * 通过class获取Bean.
     * <p>无异常，返回空</p>
     *
     * @since 1.0.4
     */
    @Nullable
    public static <T> T getBeanNoExc(@NotNull ApplicationContext applicationContext, Class<T> clazz) {
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
            return applicationContext.getBean(clazz);
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
        return applicationContext.getBean(name, clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     * <p>无异常，返回空</p>
     *
     * @since 1.0.4
     */
    @Nullable
    public static <T> T getBeanNoExc(@NotNull ApplicationContext applicationContext, String name, Class<T> clazz) {
        try {
            return applicationContext.getBean(name, clazz);
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
            return applicationContext.getBean(name, clazz);
        } catch (BeansException e) {
            return null;
        }
    }

    public static Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.getType(name);
    }


    /**
     * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
     */
    public static boolean containsBean(String name) {
        return beanFactory.containsBean(name);
    }

    /**
     * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
     */
    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return beanFactory.isSingleton(name);
    }

    /**
     * 如果给定的bean名字在bean定义中有别名，则返回这些别名
     */
    public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
        return beanFactory.getAliases(name);
    }

    /**
     * 获取aop代理对象
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public static <T> Supplier<T> getAopProxy(T invoker) {
        return () -> (T) AopContext.currentProxy();
    }


    /**
     * 获取当前的环境配置，无配置返回null
     *
     * @return 当前的环境配置
     */
    public static String[] getActiveProfiles() {
        return applicationContext.getEnvironment().getActiveProfiles();
    }

    /**
     * 获取当前的环境配置，当有多个环境配置时，只获取第一个
     *
     * @return 当前的环境配置
     */
    @Nullable
    public static String getActiveProfile() {
        final String[] activeProfiles = getActiveProfiles();
        return ArrayUtil.isNotEmpty(activeProfiles) ? activeProfiles[0] : null;
    }

    /**
     * 获取配置文件中的值
     *
     * @param key 配置文件的key
     * @return 当前的配置文件的值
     */
    @NotNull
    public static String getRequiredProperty(String key) {
        return applicationContext.getEnvironment().getRequiredProperty(key);
    }

}
