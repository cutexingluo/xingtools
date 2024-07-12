package top.cutexingluo.tools.utils.se.file.pkg;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

/**
 * 一个 File Data 封装类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/3 15:17
 * @update 2024/3/2 16:35,  2024/7/12 16:33
 * @updateFrom 1.0.4
 */
// DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class XTFile implements XTFileSource {
    /**
     * 原始名
     */
    protected String originalFilename; // 原始名
    /**
     * 唯一 id
     */
    protected String uuid; // 唯一 id
    /**
     * 扩展名
     */
    protected String type; // 扩展名
    /**
     * 大小, 默认Byte
     */
    protected Long size; // 大小
    /**
     * md5码, 需调用方法生成
     */
    protected String md5; //md5码

    /**
     * 文件名全称, uuid 名称, 需调用方法生成
     */
    protected String fileUUID;//新文件全称


    /**
     * getFilename 优先使用 originalFilename
     */
    protected boolean useOriginalFilenameFirst = true;

    @Override
    public boolean useOriginalFilenameFirst() {
        return useOriginalFilenameFirst;
    }

    /**
     * 不会生成 md5 和 fileUUID , 需手动调用
     */
    public XTFile(String originalFilename, String uuid, String type, Long size) {
        this(originalFilename, uuid, type, size, null, null, true);
    }

    /**
     * 不会生成 fileUUID , 需手动调用
     */
    public XTFile(String originalFilename, String uuid, String type, Long size, String md5) {
        this(originalFilename, uuid, type, size, md5, null, true);
    }


    /**
     * 得到file文件
     * <p>默认优先使用 originalFilename </p>
     * <p>不会生成 md5 和 fileUUID , 需手动调用</p>
     *
     * @param file 文件
     */
    public XTFile(File file) {
        this(file, true, false, false);
    }


    // exist

    /**
     * 得到file文件
     * <p>不会生成 md5 和 fileUUID , 需手动调用</p>
     *
     * @param file                     文件
     * @param useOriginalFilenameFirst 调用 getFilename 是否优先使用原始名
     */
    public XTFile(File file, boolean useOriginalFilenameFirst) {
        this(file, useOriginalFilenameFirst, false, false);
    }


    // exist

    /**
     * 得到file文件
     *
     * @param file                     文件
     * @param useOriginalFilenameFirst 调用 getFilename 是否优先使用原始名
     * @param genMd5                   是否生成md5
     * @param genUUID                  是否生成uuid
     */
    public XTFile(File file, boolean useOriginalFilenameFirst, boolean genMd5, boolean genUUID) {
        this.useOriginalFilenameFirst = useOriginalFilenameFirst;
        //获取原文件数据
        this.originalFilename = file.getName();
        //file.getContentType()
        this.type = FileUtil.extName(originalFilename);
        this.size = file.length();
        //获取md5码
        if (genMd5) genMd5(file);
        if (genUUID) {
            genUUID();
            genFileUUID();
        }
    }

    // exist

    /**
     * 生成md5 码
     */
    public String genMd5(File file) {
        if (StrUtil.isBlank(md5)) md5 = SecureUtil.md5(file);
        return md5;
    }

    /**
     * 生成文件唯一标识uuid
     * <p>可重写此方法</p>
     */
    public String genUUID() {
        if (StrUtil.isBlank(uuid)) uuid = IdUtil.fastSimpleUUID(); // 定义文件唯一标识
        return uuid;
    }

    /**
     * 生成文件唯一标识名称
     * <p>可重写此方法</p>
     */
    public String genFileUUID() {
        if (StrUtil.isBlank(fileUUID)) fileUUID = uuid + StrUtil.DOT + type;
        return fileUUID;
    }


    /**
     * 添加前缀
     */
    public String getURL(String urlPrefix) {
        return urlPrefix + getFileUUID();
    }


    /**
     * @param dirPath 文件夹目录
     */
    public File toFile(String dirPath) {
        return new File(dirPath, this.getFilename());
    }

    public File toFile() {
        return new File(this.getFilename());
    }
}
