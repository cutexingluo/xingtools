package top.cutexingluo.tools.utils.web.ip;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import top.cutexingluo.tools.bridge.servlet.HttpServletRequestData;
import top.cutexingluo.tools.bridge.servlet.adapter.HttpServletRequestDataAdapter;
import top.cutexingluo.tools.utils.ee.web.holder.HttpContextUtil;
import top.cutexingluo.tools.utils.ee.web.ip.util.AddressUtil;
import top.cutexingluo.tools.utils.ee.web.ip.util.IPUtil;

import java.text.MessageFormat;

@Deprecated
@Aspect
@Slf4j
public class IpAspect {


    @Pointcut("@annotation(top.cutexingluo.tools.utils.ee.web.ip.ipregion.Ip)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequestData request = HttpContextUtil.getHttpServletRequestData();
        String ip = IPUtil.getIpAddr(HttpServletRequestDataAdapter.of(request));
        log.info(MessageFormat.format("当前IP为:[{0}]；当前IP地址解析出来的地址为:[{1}]", ip, AddressUtil.getCityInfo(ip)));
        return point.proceed();
    }

}
