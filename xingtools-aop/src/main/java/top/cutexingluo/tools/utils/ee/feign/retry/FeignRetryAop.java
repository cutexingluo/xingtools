package top.cutexingluo.tools.utils.ee.feign.retry;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import top.cutexingluo.tools.basepackage.basehandler.aop.BaseAspectAroundHandler;
import top.cutexingluo.tools.utils.log.handler.LogHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 *重试 AOP 类
 *
 *
 * @author XingTian
 * @version 1.1.0
 * @date  2023/5/6 15:49
 * @update 2024/7/15 15:41  , v1.1.1
 */
@Aspect
public class FeignRetryAop implements BaseAspectAroundHandler<FeignRetry> {


    @Around("@annotation(feignRetry)")
    @Override
    public Object around(@NotNull ProceedingJoinPoint joinPoint, FeignRetry feignRetry) throws Throwable {
        Annotation annotation = getAnnotation(feignRetry, FeignRetry.class);
        if (annotation == null) return joinPoint.proceed();
        Method method = getMethod(joinPoint);

        LogHandler log = new LogHandler(feignRetry.logType().intCode());

        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setBackOffPolicy(prepareBackOffPolicy(feignRetry));
        retryTemplate.setRetryPolicy(prepareSimpleRetryPolicy(feignRetry));

        return retryTemplate.execute(retryContext -> {
                    int retryCount = retryContext.getRetryCount();
                    String format = MessageFormat.format("Sending request method: {0}, max attempt: {1}, delay: {2}, retryCount: {3}",
                            method.getName(),
                            feignRetry.maxAttempt(),
                            feignRetry.backoff().delay(),
                            retryCount
                    );
                    log.send(format);
                    return joinPoint.proceed(joinPoint.getArgs());
                },
                context -> {
                    //重试失败后执行的代码
//                    log.send("重试了{}次后，最终还是失败了======" + context.getRetryCount());
                    log.send("Sending request method: " + method.getName() + " failed, retryCount: " + context.getRetryCount());
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
