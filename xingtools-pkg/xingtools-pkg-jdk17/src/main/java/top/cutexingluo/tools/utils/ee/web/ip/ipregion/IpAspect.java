package top.cutexingluo.tools.utils.ee.web.ip.ipregion;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import top.cutexingluo.tools.utils.ee.web.ip.util.AddressUtil;
import top.cutexingluo.tools.utils.ee.web.ip.util.HttpContextUtil;
import top.cutexingluo.tools.utils.ee.web.ip.util.IPUtil;

import jakarta.servlet.http.HttpServletRequest;
import java.text.MessageFormat;


@ConditionalOnClass(HttpServletRequest.class)
@Aspect
@Component
@Slf4j
public class IpAspect {


    @Pointcut("@annotation(top.cutexingluo.tools.utils.ee.web.ip.ipregion.Ip)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        String ip = IPUtil.getIpAddr(request);
        log.info(MessageFormat.format("当前IP为:[{0}]；当前IP地址解析出来的地址为:[{1}]", ip, AddressUtil.getCityInfo(ip)));
        return point.proceed();
    }

}
