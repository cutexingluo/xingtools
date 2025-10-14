package top.cutexingluo.tools.utils.se.file.pkg;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import top.cutexingluo.core.utils.se.file.pkg.XTHuFile;

import java.io.IOException;

/**
 * XTFile 捆绑包
 * <p>含原文件</p>
 * <p>于 1.1.1 版本变为 XTFIle 的扩展包</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/3/2 18:02
 * @since 1.0.4
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class XTFileBundle {

    protected MultipartFile multipartFile;

    protected XTHuFile xtFile;


    /**
     * 得到file文件
     *
     * @param file                     文件
     * @param useOriginalFilenameFirst 调用 getFilename 是否优先使用原始名
     * @param genMd5                   是否生成md5
     * @param genUUID                  是否生成uuid
     * @throws IOException IOException
     */
    public XTFileBundle(MultipartFile file, boolean useOriginalFilenameFirst, boolean genMd5, boolean genUUID) throws IOException {
        this.multipartFile = file;
        this.xtFile = new XTHuFile();
        xtFile.setUseOriginalFilenameFirst(useOriginalFilenameFirst);
        //获取原文件数据
        xtFile.setOriginalFilename(file.getOriginalFilename());
        //file.getContentType()
        xtFile.setType(FileUtil.extName(xtFile.getOriginalFilename()));
        xtFile.setSize(file.getSize());//获取大小
        //获取md5码
        if (genMd5) genMd5(file);
        if (genUUID) {
            xtFile.genUUID();
            xtFile.genFileUUID();
        }
    }

    public String genMd5(MultipartFile file) throws IOException {
        if (StrUtil.isBlank(xtFile.getMd5())) xtFile.setMd5(SecureUtil.md5(file.getInputStream()));
        return xtFile.getMd5();
    }

    /**
     * 得到file文件
     * <p>默认优先使用 originalFilename </p>
     * <p>不会生成 md5 和 fileUUID , 需手动调用</p>
     *
     * @param multipartFile 文件
     */
    public XTFileBundle(MultipartFile multipartFile) throws IOException {
        this(multipartFile, true, false, false);
    }

    /**
     * 得到file文件
     * <p>不会生成 md5 和 fileUUID , 需手动调用</p>
     *
     * @param file                     文件
     * @param useOriginalFilenameFirst 调用 getFilename 是否优先使用原始名
     */
    public XTFileBundle(MultipartFile file, boolean useOriginalFilenameFirst) throws IOException {
        this(file, useOriginalFilenameFirst, false, false);
    }

}
