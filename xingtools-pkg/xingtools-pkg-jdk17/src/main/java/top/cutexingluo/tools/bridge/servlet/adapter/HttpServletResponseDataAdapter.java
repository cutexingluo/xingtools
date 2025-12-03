package top.cutexingluo.tools.bridge.servlet.adapter;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.core.bridge.servlet.adapter.HttpServletResponseAdapter;
import top.cutexingluo.core.common.base.IData;
import top.cutexingluo.core.common.base.IResult;
import top.cutexingluo.tools.bridge.servlet.HttpServletResponseData;
import top.cutexingluo.tools.utils.ee.web.front.XTResponseUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

/**
 * HttpServletResponseData 适配器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/8/12 11:40
 */
@Data
@AllArgsConstructor
public class HttpServletResponseDataAdapter implements HttpServletResponseAdapter, IData<HttpServletResponseData> {

    protected HttpServletResponseData data;

    @Contract("_ -> new")
    public static @NotNull
    HttpServletResponseDataAdapter of(HttpServletResponseData data) {
        return new HttpServletResponseDataAdapter(data);
    }

    @Override
    public HttpServletResponseData data() {
        return data;
    }


    // 公有，未来将移入 xingcore

    public String getCharacterEncoding(){
        return data.getResponse().getCharacterEncoding();
    }

    public String getContentType(){
        return data.getResponse().getContentType();
    }

    public PrintWriter getWriter() throws IOException{
        return data.getResponse().getWriter();
    }

    public HttpServletResponseDataAdapter setCharacterEncoding(String characterEncoding){
        data.getResponse().setCharacterEncoding(characterEncoding);
        return this;
    }

    public HttpServletResponseDataAdapter  setContentLength(int length){
        data.getResponse().setContentLength(length);
        return this;
    }

    public HttpServletResponseDataAdapter setContentLengthLong(long length){
        data.getResponse().setContentLengthLong(length);
        return this;
    }

    public HttpServletResponseDataAdapter setContentType(String contentType){
        data.getResponse().setContentType(contentType);
        return this;
    }

    public HttpServletResponseDataAdapter setBufferSize(int bufferSize){
        data.getResponse().setBufferSize(bufferSize);
        return this;
    }

    public int getBufferSize(int bufferSize){
        return data.getResponse().getBufferSize();
    }

    public HttpServletResponseDataAdapter flushBuffer() throws IOException{
        data.getResponse().flushBuffer();
        return this;
    }

    public HttpServletResponseDataAdapter resetBuffer(){
        data.getResponse().resetBuffer();
        return this;
    }

    public boolean isCommitted(){
        return data.getResponse().isCommitted();
    }

    public HttpServletResponseDataAdapter  reset(){
        data.getResponse().reset();
        return this;
    }

    public HttpServletResponseDataAdapter  setLocale(Locale locale){
        data.getResponse().setLocale(locale);
        return this;
    }

    public Locale getLocale(){
        return data.getResponse().getLocale();
    }

    // common HttpServletResponse
    public boolean containsHeader(String name){
        return data.getResponse().containsHeader(name);
    }

    public String encodeURL(String url){
        return data.getResponse().encodeURL(url);
    }

    public String encodeRedirectURL(String url){
        return data.getResponse().encodeRedirectURL(url);
    }

    public HttpServletResponseDataAdapter sendError(int sc, String msg) throws IOException{
        data.getResponse().sendError(sc,msg);
        return this;
    }

    public HttpServletResponseDataAdapter sendError(int sc) throws IOException{
        data.getResponse().sendError(sc);
        return this;
    }


    public HttpServletResponseDataAdapter  sendRedirect(String location) throws IOException{
        data.getResponse().sendRedirect(location);
        return this;
    }

    public HttpServletResponseDataAdapter  setDateHeader(String name, long value){
        data.getResponse().setDateHeader(name, value);
        return this;
    }

    public HttpServletResponseDataAdapter  addDateHeader(String name, long value){
        data.getResponse().addDateHeader(name, value);
        return this;
    }

    public HttpServletResponseDataAdapter  setHeader(String name, String value){
        data.getResponse().setHeader(name,value);
        return this;
    }

    public HttpServletResponseDataAdapter  addHeader(String name, String value){
        data.getResponse().addHeader(name,value);
        return this;
    }

    public HttpServletResponseDataAdapter  setIntHeader(String name, int value){
        data.getResponse().setIntHeader(name,value);
        return this;
    }

    public HttpServletResponseDataAdapter  addIntHeader(String name, int value){
        data.getResponse().addIntHeader(name,value);
        return this;
    }

    public HttpServletResponseDataAdapter  setStatus(int status){
        data.getResponse().setStatus(status);
        return this;
    }


    public int getStatus(){
        return data.getResponse().getStatus();
    }

    public String getHeader(String name){
        return data.getResponse().getHeader(name);
    }

    public Collection<String> getHeaders(String name){
        return data.getResponse().getHeaders(name);
    }

    public Collection<String> getHeaderNames(){
        return data.getResponse().getHeaderNames();
    }


    // pkg 包单有，禁止其他包使用，仅供项目使用

    public ServletOutputStream getOutputStream() throws IOException{
        return data.getResponse().getOutputStream();
    }

    public HttpServletResponseDataAdapter addCookie(Cookie cookie){
        data.getResponse().addCookie(cookie);
        return this;
    }

    @Override
    public void response(String content, int rspStatusCode) throws IOException {
        XTResponseUtil.response(data.getResponse(), content, rspStatusCode);
    }

    @Override
    public <C, T> void response(IResult<C, T> result, int rspStatusCode) throws IOException {
        XTResponseUtil.response(data.getResponse(), result, rspStatusCode);
    }
}
