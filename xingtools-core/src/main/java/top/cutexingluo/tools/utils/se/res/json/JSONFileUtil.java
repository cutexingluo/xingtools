package top.cutexingluo.tools.utils.se.res.json;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * json文件工具类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/9/15 15:22
 */
public class JSONFileUtil {
    /**
     * 读取json文件到对象（反射）
     */
    public static <T> T readJsonFile(File file, Class<T> clazz) {
        ObjectMapper om = new ObjectMapper();
        T value = null;
        try {
            value = om.readValue(file, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;

    }


    /**
     * 存取json
     */
    public static <T> boolean saveJsonFile(File file, T data) {
        ObjectMapper om = new ObjectMapper();
        try {
            String json = om.writeValueAsString(data);
            om.writeValue(file, data);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
