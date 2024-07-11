package top.cutexingluo.tools.utils.se.res.img.captcha;

import cn.hutool.captcha.CaptchaUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * 验证码工具类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/24 15:07
 * @since 1.0.4
 */
public class XTCaptchaUtil extends CaptchaUtil {


    /**
     * jpg 图片转为 base64 编码字符串
     */
    public static String getImageStrJPG(BufferedImage image, String formatName) throws IOException {
        return getImageStr(image, "jpg");
    }

    /**
     * 图片转为 base64 编码字符串
     *
     * @param formatName 格式 ，("jpg", "png", "gif")
     */
    public static String getImageStr(BufferedImage image, String formatName) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, formatName, outputStream);
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(outputStream.toByteArray());
    }

    /**
     * 变成可解析的 base64 字符串
     *
     * @param formatName 格式 ，("jpg", "png", "gif")
     */
    @NotNull
    @Contract(pure = true)
    public static String getDataImageStr(String imageStr, String formatName) throws IOException {
        return "data:image/" + formatName + ";base64," + imageStr;
    }
}
