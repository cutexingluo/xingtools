package top.cutexingluo.tools.utils.ee.web.data;


import lombok.Data;
import top.cutexingluo.tools.bridge.servlet.HttpServletResponseData;
import top.cutexingluo.tools.bridge.servlet.adapter.HttpServletResponseDataAdapter;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * response 建造者
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2025/12/3 11:58
 * @since 1.2.1
 */
@Data
public class WebResponse {

    protected HttpServletResponseData response;

    public WebResponse(HttpServletResponse response) {
        this.response = HttpServletResponseData.of(response);
    }

    public WebResponse(HttpServletResponseData response) {
        this.response = response;
    }

    public static WebResponse of(HttpServletResponseData response) {
        return new WebResponse(response);
    }

    public static WebResponse of(HttpServletResponse response) {
        return new WebResponse(response);
    }

    // common

    public WebResponse handleAdapter(BiConsumer<WebResponse,HttpServletResponseDataAdapter> consumer ){
        Objects.requireNonNull(consumer);
        consumer.accept(this,HttpServletResponseDataAdapter.of(response));
        return this;
    }

    public WebResponse handleData(BiConsumer<WebResponse,HttpServletResponseData> consumer ){
        Objects.requireNonNull(consumer);
        consumer.accept(this,response);
        return this;
    }


    // other

    public WebResponse utf8(){
        response.data().setContentType(StandardCharsets.UTF_8.name());
        return this;
    }

    public WebResponse status(int  rspStatusCode){
        response.data().setStatus(rspStatusCode);
        return this;
    }


    /**
     * 设置文件名
     */
    public WebResponse filenameEncoded(String encodedFilename){
        response.data().setHeader("content-disposition",encodedFilename);
        return this;
    }

    /**
     *  设置文件名
     */
    public WebResponse filename(String filename) throws UnsupportedEncodingException {
        String encodeFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8.name());
        response.data().setHeader("Content-disposition", "attachment; filename=" + encodeFilename);
        return this;
    }

    /**
     * 设置文件流
     */
    public WebResponse contentTypeFile(){
        response.data().setContentType("application/octet-stream");
        return this;
    }

    /**
     * 设置json
     */
    public WebResponse contentTypeJson(){
        response.data().setContentType("application/json");
        return this;
    }


    /**
     * 设置 text/plain
     */
    public WebResponse contentTypeTextPlain(){
        response.data().setContentType("text/plain");
        return this;
    }


}
