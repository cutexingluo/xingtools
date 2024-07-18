package top.cutexingluo.tools.cloud.feign.dynamic;

import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * 动态服务接口
 *
 * @author XingTian
 * @date 2023/5/6 15:23
 */
public interface DynamicService {
    /**
     * 调用 post 方法
     *
     * @param url    接口
     * @param params RequestBody对象
     * @return 返回值
     */
    @PostMapping("{url}")
    Object executePostApi(@PathVariable("url") String url, @RequestBody Object params);

    /**
     * 调用 get 方法
     *
     * @param url    接口
     * @param params SpringQueryMap对象
     * @return 返回值
     */
    @GetMapping("{url}")
    Object executeGetApi(@PathVariable("url") String url, @SpringQueryMap Object params);

}
