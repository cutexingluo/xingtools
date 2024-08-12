package top.cutexingluo.tools.bridge.servlet.adapter;

import top.cutexingluo.tools.common.base.IResult;
import top.cutexingluo.tools.designtools.protocol.serializer.impl.json.JacksonSerializer;

import java.io.IOException;

/**
 * HttpServletResponse 适配器
 *
 * <p>由于 jdk 只识别签名，故1.1.2 版本 在支持jdk17 版本的众多方法会报错 , NoSuchMethodError 找不到方法</p>
 * <p>故在 xingtools 里面 不对外暴露方法, 面向接口</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/8/12 11:34
 * @since 1.1.3
 */
public interface HttpServletResponseAdapter {

    /**
     * 返回通用方法
     *
     * @param content       返回的字符串数据
     * @param rspStatusCode http 状态码
     */
    void response(String content, int rspStatusCode) throws IOException;


    /**
     * 返回 result 方法
     *
     * @param result        返回的 result 字符串数据
     * @param rspStatusCode http 状态码
     */
    default <C, T> void response(IResult<C, T> result, int rspStatusCode) throws IOException {
        JacksonSerializer jacksonSerializer = new JacksonSerializer();
        String stringify = jacksonSerializer.stringify(result);
        response(stringify, rspStatusCode);
    }


}
