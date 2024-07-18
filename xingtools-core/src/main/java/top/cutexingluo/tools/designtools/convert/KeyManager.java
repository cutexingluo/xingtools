package top.cutexingluo.tools.designtools.convert;


/**
 * key - value 转化器
 * <p>常规 版本</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/8 17:29
 * @since 1.0.4
 */
public class KeyManager {
    //-------------------转义区-----------------------------
    /**
     * space  空格
     */
    public static final KeyConvertor S = new KeyConvertor("s", () -> " ");

    /**
     * 4 space  4 空格
     */
    public static final KeyConvertor S4 = new KeyConvertor("s4", () -> "    ");

    /**
     * tab
     */
    public static final KeyConvertor TAB = new KeyConvertor("tab", () -> "\t");


    /**
     * start
     */
    public static final KeyConvertor START = new KeyConvertor("start", () -> "========== Start ==========");


    /**
     * end
     */
    public static final KeyConvertor END = new KeyConvertor("end", () -> "========== End ==========");


    //-------------------特殊区-----------------------------


    /**
     * 字符串组合
     */
    public static final String ADD_KEY = "addSplit";
    /**
     * 字符串组合
     * <p>可进行替换</p>
     * <p>regex 正则</p>
     */
    public static final String ADD = "\\+";


    /**
     * 换行
     */
    public static final String WRAP_KEY = "wrapSplit";

    /**
     * 换行
     * <p>可进行替换</p>
     * <p>regex 正则</p>
     */
    public static final String WRAP = ":";


    /**
     * 换行字符串
     */
    public static final String WRAP_STR = "wrap";

    /**
     * 换行符
     */
    public static final String WRAP_SYM = "\n";
}
