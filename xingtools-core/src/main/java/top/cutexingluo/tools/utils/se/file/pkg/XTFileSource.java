package top.cutexingluo.tools.utils.se.file.pkg;

import cn.hutool.core.util.StrUtil;

/**
 * 获取文件全名称
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/6/25 12:01
 */
public interface XTFileSource extends XTFileBaseSource {


    /**
     * 优先使用原始文件名称
     */
    default boolean useOriginalFilenameFirst() {
        return true;
    }

    /**
     * 得到新文件全称
     *
     * @return {@link String}
     */
    String getFileUUID();


    /**
     * 获取原始文件名称
     */
    String getOriginalFilename();


    /**
     * 获取文件全称
     * <p>原始文件名称为空，则使用uuid file 名</p>
     * <p>可以重写此方法得到想要的文件名</p>
     */
    @Override
    default String getFilename() {
        if (useOriginalFilenameFirst()) {
            String originalFilename = getOriginalFilename();
            if (StrUtil.isBlank(originalFilename)) return getFileUUID();
            return originalFilename;
        } else {
            String fileUUID = getFileUUID();
            if (StrUtil.isBlank(fileUUID)) return getOriginalFilename();
            return fileUUID;
        }

    }
}
