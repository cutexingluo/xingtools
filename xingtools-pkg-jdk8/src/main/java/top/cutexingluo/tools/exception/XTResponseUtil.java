package top.cutexingluo.tools.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import top.cutexingluo.tools.common.Constants;
import top.cutexingluo.tools.common.Result;
import top.cutexingluo.tools.common.base.IResult;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 数据返回处理工具类 <br>
 * 类似 WebUtils
 * <p>可以在拦截器里面使用</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/5/17 21:48
 */
public class XTResponseUtil {

    /**
     * 返回请求封装
     *
     * @param rsp           返回请求
     * @param result        返回数据
     * @param rspStatusCode 返回码
     */
    public static <C, T> void response(HttpServletResponse rsp, IResult<C, T> result, int rspStatusCode) throws IOException {
        byte[] responseBytes = new ObjectMapper().writeValueAsBytes(result);
        rsp.addHeader("Content-Type", "application/json;charset=UTF-8");
        rsp.setStatus(rspStatusCode);
        rsp.getWriter().write(new String(responseBytes));
    }

    /**
     * 返回请求封装<br>
     * 返回码 200 SC_OK
     *
     * @param rsp    返回请求
     * @param result 返回数据
     */
    public static <C, T> void success(HttpServletResponse rsp, IResult<C, T> result) throws IOException {
        response(rsp, result, HttpServletResponse.SC_OK);
    }

    /**
     * 返回请求封装<br>
     * 返回码 400 SC_BAD_REQUEST
     *
     * @param rsp    返回请求
     * @param result 返回数据
     */
    public static <C, T> void badRequest(HttpServletResponse rsp, IResult<C, T> result) throws IOException {
        response(rsp, result, HttpServletResponse.SC_BAD_REQUEST);
    }

    /**
     * 返回请求封装<br>
     * 返回码 500 SC_INTERNAL_SERVER_ERROR
     *
     * @param rsp    返回请求
     * @param result 返回数据
     */
    public static <C, T> void serverError(HttpServletResponse rsp, IResult<C, T> result) throws IOException {
        response(rsp, result, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    /**
     * 未验证请求 401
     *
     * @param rsp    返回请求
     * @param result 返回数据
     */
    public static <C, T> void unauthorized(HttpServletResponse rsp, IResult<C, T> result) throws IOException {
        response(rsp, result, HttpServletResponse.SC_UNAUTHORIZED);
    }

    /**
     * 权限不足，访问拒绝 403
     *
     * @param rsp    返回请求
     * @param result 返回数据
     * @since 1.0.4
     */
    public static <C, T> void forbidden(HttpServletResponse rsp, IResult<C, T> result) throws IOException {
        response(rsp, result, HttpServletResponse.SC_FORBIDDEN);
    }


    /**
     * 未验证请求 401
     *
     * @param rsp 返回请求
     */
    public static void unauthorized(HttpServletResponse rsp) throws IOException {
        response(rsp, Result.error(Constants.CODE_401, "Authentication failed, Insufficient permissions"), HttpServletResponse.SC_UNAUTHORIZED);
    }

    /**
     * 权限不足，访问拒绝 403
     *
     * @param rsp 返回请求
     * @since 1.0.4
     */
    public static void forbidden(HttpServletResponse rsp) throws IOException {
        response(rsp, Result.error(Constants.CODE_403, "Insufficient permissions, Access denied"), HttpServletResponse.SC_UNAUTHORIZED);
    }


    /**
     * 未经授权
     *
     * @param httpServletResponse http servlet响应
     * @param msg                 消息
     * @throws IOException ioexception
     */
    public static void unauthorized(HttpServletResponse httpServletResponse, String msg) throws IOException {
        Result error = Result.error(Constants.CODE_401, msg);
        unauthorized(httpServletResponse, error);
    }

    /**
     * 权限不足, 访问拒绝
     *
     * @param httpServletResponse http servlet响应
     * @param msg                 消息
     * @throws IOException ioexception
     */
    public static void forbidden(HttpServletResponse httpServletResponse, String msg) throws IOException {
        Result error = Result.error(Constants.CODE_403, msg);
        forbidden(httpServletResponse, error);
    }

    /**
     * 未验证请求 401
     *
     * @param rsp 返回请求
     */
    public static void unauthorizedCN(HttpServletResponse rsp) throws IOException {
        response(rsp, Result.error(Constants.CODE_401, "认证失败, 权限不足"), HttpServletResponse.SC_UNAUTHORIZED);
    }


    /**
     * 权限不足，访问拒绝 403
     *
     * @param rsp 返回请求
     */
    public static void forbiddenCN(HttpServletResponse rsp) throws IOException {
        response(rsp, Result.error(Constants.CODE_403, "权限不足, 访问拒绝"), HttpServletResponse.SC_FORBIDDEN);
    }
}
