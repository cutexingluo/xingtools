package top.cutexingluo.tools.utils.se.file;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.web.multipart.MultipartFile;
import top.cutexingluo.tools.utils.se.file.media.MimeTypeUtils;

import java.util.Objects;

/**
 * MultipartFile 工具类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/17 17:41
 */
public class XTMultipartFileUtil {

    /**
     * 获取文件名的后缀
     *
     * @param file 表单文件
     * @return 后缀名
     */
    public static String getExtension(MultipartFile file) {
        String extension = FileNameUtil.extName(file.getOriginalFilename());
        if (StrUtil.isEmpty(extension)) {
            extension = MimeTypeUtils.getExtension(Objects.requireNonNull(file.getContentType()));
        }
        return extension;
    }

}
