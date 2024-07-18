package top.cutexingluo.tools.utils.ee.feign.retry;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import top.cutexingluo.tools.basepackage.basehandler.aop.BaseAspectAroundHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 *重试 AOP 类
 *
 *
 *
 * @author XingTian
 * @version 1.1.0
 * @date  2023/5/6 15:49
 * @update 2024/7/15 15:41  , v1.1.1
 */
@Slf4j
@ConditionalOnClass({RetryTemplate.class})
public class FeignRetryAop implements BaseAspectAroundHandler<FeignRetry> {


    @Override
    public Object around(@NotNull ProceedingJoinPoint joinPoint, FeignRetry feignRetry) throws Throwable {
        Annotation annotation = getAnnotation(feignRetry, FeignRetry.class);
        if (annotation == null) return joinPoint.proceed();
        Method method = getMethod(joinPoint);

        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setBackOffPolicy(prepareBackOffPolicy(feignRetry));
        retryTemplate.setRetryPolicy(prepareSimpleRetryPolicy(feignRetry));

        return retryTemplate.execute(retryContext -> {
                    int retryCount = retryContext.getRetryCount();
                    log.info("Sending request method: {}, max attempt: {}, delay: {}, retryCount: {}",
                            method.getName(),
                            feignRetry.maxAttempt(),
                            feignRetry.backoff().delay(),
                            retryCount
                    );
                    return joinPoint.proceed(joinPoint.getArgs());
                },
                context -> {
                    //重试失败后执行的代码
                    log.info("重试了{}次后，最终还是失败了======", context.getRetryCount());
                    return "failed callback";
                }
        );
    }


    protected @NotNull BackOffPolicy prepareBackOffPolicy(@NotNull FeignRetry feignRetry) {
        if (feignRetry.backoff().multiplier() != 0) {
            ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
            backOffPolicy.setInitialInterval(feignRetry.backoff().delay());
            backOffPolicy.setMaxInterval(feignRetry.backoff().maxDelay());
            backOffPolicy.setMultiplier(feignRetry.backoff().multiplier());
            return backOffPolicy;
        } else {
            FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
            fixedBackOffPolicy.setBackOffPeriod(feignRetry.backoff().delay());
            return fixedBackOffPolicy;
        }
    }


    @Contract("_ -> new")
    protected @NotNull SimpleRetryPolicy prepareSimpleRetryPolicy(@NotNull FeignRetry feignRetry) {
        Map<Class<? extends Throwable>, Boolean> policyMap = new HashMap<>();
        policyMap.put(RetryException.class, true);  // Connection refused or time out
        if (feignRetry.include().length != 0) {
            for (Class<? extends Throwable> t : feignRetry.include()) {
                policyMap.put(t, true);
            }
        }
        return new SimpleRetryPolicy(feignRetry.maxAttempt(), policyMap, true);
    }
}
