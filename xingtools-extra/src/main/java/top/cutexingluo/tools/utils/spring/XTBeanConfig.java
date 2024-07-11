package top.cutexingluo.tools.utils.spring;


/**
 * @author XingTian
 * @version 1.0.0
 * @date 2023/6/20 15:15
 */
public class XTBeanConfig {

    /**
     * 得到一些XT简单的bean 名称
     *
     * @return {@link String[]}
     */
    public static String[] getSimpleBeanNames() {
        return new String[]{
                "springUtils",
                "rySpringUtils",
                "xtExceptionAop",
                "printLogAop",
                "methodLogAop",
                "xtSystemLogAop",
                "extTransactionAop",
                "xtLockAop",
                "corsFilter",
        };
    }
}
