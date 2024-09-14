package top.cutexingluo.tools.designtools.protocol.serializer.impl.json;

import com.google.gson.Gson;
import lombok.Data;
import top.cutexingluo.tools.designtools.protocol.serializer.StringSerializer;

import java.io.IOException;

/**
 * Gson 序列化器
 * <p>需要导入 com.google.code.gson:gson 包</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/12 11:53
 * @since 1.1.4
 */
@Data
public class GsonSerializer implements StringSerializer {

    protected Gson gson;

    public GsonSerializer() {
        this.gson = new Gson();
    }

    public GsonSerializer(Gson gson) {
        this.gson = gson;
    }


    @Override
    public <T> String stringify(T obj) {
        return gson.toJson(obj);
    }

    @Override
    public <T> T parse(String data, Class<T> clz) throws IOException {
        return gson.fromJson(data, clz);
    }

    //------------ other ------------
    

}
