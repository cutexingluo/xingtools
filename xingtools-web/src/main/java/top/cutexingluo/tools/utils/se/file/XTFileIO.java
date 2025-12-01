package top.cutexingluo.tools.utils.se.file;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.multipart.MultipartFile;
import top.cutexingluo.core.designtools.method.XTObjUtil;
import top.cutexingluo.core.utils.se.file.pkg.XTFileSource;
import top.cutexingluo.core.utils.se.file.pkg.XTHuFile;
import top.cutexingluo.tools.utils.se.file.pkg.XTFileBundle;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 文件IO工具类
 *
 * <p>该类部分方法过时，仅做参考</p>
 * <p>1.0.4 更新 , 未来可能重构, 现在详见 {@link XTFileHandler}</p>
 * <p>计划 1.2.1 过时，未来将重构或移除</p>
 *
 * @author XingTian
 * @version 1.1.0
 * @date 2023/2/3 15:21
 * @update 2024/3/2 16:35
 * @updateFrom 1.0.4
 */
@Deprecated
public class XTFileIO {
    public static final Charset UTF_8 = StandardCharsets.UTF_8;
    public static final String separator = File.separator;


    /**
     * 前面加斜线
     */
    public static String addNamePreSlash(String s) { //前面加斜线
        if (!s.startsWith(separator)) s = separator + s;
        return s;
    }

    /**
     * 后面加斜线
     */
    public static String addUrlSlash(String s) {//后面加斜线
        if (s.endsWith(separator)) s += separator;
        return s;
    }

    //*******************


    /**
     * 存到磁盘
     * <p>需要提前判定md5码是否冲突</p>
     *
     * @param file               上传的文件
     * @param xtFile             获取的xtFile整合包
     * @param fileUploadDiskPath 要上传的磁盘路径
     * @throws IOException 读写异常
     */
    @Deprecated
    public static void uploadToDisk(MultipartFile file, XTFileSource xtFile, String fileUploadDiskPath) throws IOException {
        String urlSlash = addUrlSlash(fileUploadDiskPath);
        File uploadFile = new File(urlSlash + xtFile.getFileUUID());//含路径文件
        File parentFile = uploadFile.getParentFile();
        if (!parentFile.exists()) {//创建文件夹
            parentFile.mkdirs();
        }
        file.transferTo(uploadFile); // 存储到磁盘中
    }


    /**
     * 获得md5码反射备用版
     * <p>从数据库获取md5</p>
     * <p>当前方法已过时，因为涉及数据库操作，未来将被移除</p>
     *
     * @param fileType   查询出来的实体类类型
     * @param fileMapper mp mapper 对象，必须拥有selectList 方法
     * @param md5ColName md5字段
     * @param md5        md5 值
     * @return 文件实体类对象
     */
    @Deprecated
    public static <T, TM> T getFileByMd5(Class<T> fileType, TM fileMapper, String md5ColName, String md5) throws InvocationTargetException, IllegalAccessException {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(md5ColName, md5);
        Method method = ReflectUtil.getMethod(fileMapper.getClass(), "selectList", QueryWrapper.class);
        List<T> filesList = (List<T>) method.invoke(fileMapper, queryWrapper);
//        List filesList = filesMapper.selectList(queryWrapper);
        return filesList.size() == 0 ? null : filesList.get(0);
    }

    /**
     * 默认使用 md5 字段名获取文件实体类对象
     */
    @Deprecated
    public static <T, TM> T getFileByMd5(Class<T> fileType, TM fileMapper, String md5) throws InvocationTargetException, IllegalAccessException {
        return getFileByMd5(fileType, fileMapper, "md5", md5);
    }

    /**
     * 存到数据库反射备用版
     * <p>当前方法已过时，因为涉及数据库操作，未来将被移除</p>
     *
     * @param DBFile     数据库的实体类
     * @param fileMapper 数据库的DAO层
     * @param fieldName  字段名数组,默认原始名，扩展名，大小, md5码
     * @param xtFile     XTFile信息包
     * @param url        需要存入的url
     * @param <T>        DBFile的类型
     * @param <TM>       数据库的DAO层Mapper类型
     */
    @Deprecated
    public static <T, TM> void saveToDB(T DBFile, TM fileMapper, String[] fieldName, XTHuFile xtFile, String url) {
        ReflectUtil.setFieldValue(DBFile, "url", url);
        ReflectUtil.setFieldValue(DBFile, fieldName[0], xtFile.getOriginalFilename());
        ReflectUtil.setFieldValue(DBFile, fieldName[1], xtFile.getType());
        ReflectUtil.setFieldValue(DBFile, fieldName[2], xtFile.getSize()); //KB
        ReflectUtil.setFieldValue(DBFile, fieldName[3], xtFile.getMd5());
        ReflectUtil.invoke(fileMapper, "insert", DBFile);
//        fileMapper.insert(saveFIle);
    }

    /**
     * 字段名数组,默认原始名，扩展名，大小, md5码
     * 默认 "name","type","size","md5"
     * <p>当前方法已过时，因为涉及数据库操作，未来将被移除</p>
     */
    @Deprecated
    public static <T, TM> void saveToDB(T DBFile, TM fileMapper, XTHuFile xtFile, String url) {
        String[] fieldName = {"name", "type", "size", "md5"};
        saveToDB(DBFile, fileMapper, fieldName, xtFile, url);
    }

    /**
     * <p>当前方法已过时，因为涉及数据库操作，未来将被移除</p>
     * <p>
     * 上传反射备用版
     * <p>
     * 参数为 上传文件，DB文件类型，DBMapper对象,url前缀，磁盘父路径
     * 返回值为 url
     * <p>
     * 全部默认值 上传
     * 实体类关键字如下
     * "name","type","size","md5","url",
     * DB字段
     * "insert" ,"selectList"
     *
     * @param file           上传文件
     * @param fileType       DB文件类型
     * @param fileMapper     DBMapper对象
     * @param urlPrefix      url前缀
     * @param fileUploadPath 磁盘父路径
     * @return url
     */
    @Deprecated
    public static <T, TM> String upload(MultipartFile file, Class<T> fileType, TM fileMapper, String urlPrefix, String fileUploadPath)
            throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        XTFileBundle bundle = new XTFileBundle(file);
        XTHuFile xtFile = bundle.getXtFile();
        bundle.genMd5(file); // 生成md5
        xtFile.genUUID();
        xtFile.genFileUUID();
//        return xtFile.getFileUUID();
        T files = getFileByMd5(fileType, fileMapper, "md5", xtFile.getMd5());
//        System.out.println(xtFile.getFileUUID());
        String url;
        if (files == null) {//如果没有就存到磁盘
            url = urlPrefix + xtFile.getFileUUID();
            uploadToDisk(file, xtFile, fileUploadPath);
        } else {
//            url = files.getUrl();
//            url= (String) ReflectUtil.getFieldValue(files,"url"); //获取字段
            url = (String) XTObjUtil.getProperty(files, "getUrl");//获取方法
        }
        //存储到数据库
        T newFiles = fileType.getConstructor().newInstance();
        saveToDB(newFiles, fileMapper, xtFile, url);
        return url;
    }
}
