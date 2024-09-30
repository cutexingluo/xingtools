package top.cutexingluo.tools.utils.se.res.img.captcha;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

import java.util.Properties;

/**
 * google kaptcha 类
 *
 * <p>需要导入 pro.fessional:kaptcha 包</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/24 16:18
 * @since 1.0.4
 */
public class CaptchaFactory {

    /**
     * 默认 125 x 50
     * <p>注册到bean中</p>
     */
    public DefaultKaptcha kaptchaProducer() {
        return kaptchaProducer(125, 50);
    }

    /**
     * 使用方式
     * <pre>{@code
     * String text = producer.createText();
     * BufferedImage image = producer.createImage(text);
     *  }
     * </pre>
     * <p>建议将此类注入bean</p>
     */
    public DefaultKaptcha kaptchaProducer(int width, int height) {
        // 实例一个DefaultKaptcha
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        // 将以上属性设置为实例一个DefaultKaptcha的属性
        Config config = new Config(defaultProperties(width, height));
        defaultKaptcha.setConfig(config);
        // 将defaultKaptcha返回
        return defaultKaptcha;
    }

    /**
     * 默认配置
     */
    public Properties defaultProperties(int width, int height) {
        return defaultProperties(width, height, 4);
    }

    /**
     * 默认配置
     */
    public Properties defaultProperties(int width, int height, int charLength) {
        String widthStr = String.valueOf(width);
        String heightStr = String.valueOf(height);
        String charLengthStr = String.valueOf(charLength);
        // 创建配置对象
        Properties properties = new Properties();
        // 设置边框
        properties.setProperty("kaptcha.border", "yes");
        // 设置颜色
        properties.setProperty("kaptcha.border.color", "105,179,90");
        // 设置字体颜色
        properties.setProperty("kaptcha.textproducer.font.color", "blue");
        // 设置宽度
        properties.setProperty("kaptcha.image.width", widthStr);
        // 高度
        properties.setProperty("kaptcha.image.height", heightStr);
        // 设置session.key
        properties.setProperty("kaptcha.session.key", "code");
        // 设置文本长度
        properties.setProperty("kaptcha.textproducer.char.length", charLengthStr);
        // 设置字体
        properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
        return properties;
    }
}
