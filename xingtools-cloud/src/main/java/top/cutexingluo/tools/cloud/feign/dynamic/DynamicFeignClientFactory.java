package top.cutexingluo.tools.cloud.feign.dynamic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.openfeign.FeignClientBuilder;
import org.springframework.context.ApplicationContext;

/**
 * dynamic-feign 开启
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/5/6 15:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DynamicFeignClientFactory<T> {

    private FeignClientBuilder feignClientBuilder;

    public DynamicFeignClientFactory(ApplicationContext appContext) {
        this.feignClientBuilder = new FeignClientBuilder(appContext);
    }

    public T getFeignClient(final Class<T> type, String serviceId) {
        return this.feignClientBuilder.forType(type, serviceId).build();
    }
}
