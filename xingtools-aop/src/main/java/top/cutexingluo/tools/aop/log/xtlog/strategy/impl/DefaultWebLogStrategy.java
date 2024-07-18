package top.cutexingluo.tools.aop.log.xtlog.strategy.impl;

import cn.hutool.core.util.StrUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.aop.log.xtlog.LogKey;
import top.cutexingluo.tools.aop.log.xtlog.base.WebLogConfig;
import top.cutexingluo.tools.aop.log.xtlog.pkg.WebLogKeyMap;
import top.cutexingluo.tools.basepackage.bundle.AspectBundle;
import top.cutexingluo.tools.utils.se.obj.spel.SpELObject;

/**
 * 默认日志策略
 * <p>提供了默认的 msg 组装模板，可以自定义</p>
 * <p>实现 Strategy 策略可以继承该类</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/8 15:41
 * @since 1.0.4
 */
public class DefaultWebLogStrategy extends CommonWebLogStrategy {


    @Override
    public String getMsg(WebLogConfig config, @NotNull WebLogKeyMap msgMap, AspectBundle bundle) {
        if (config == null) return "";
        if (StrUtil.isNotBlank(config.getSpEL())) { // SpEL 覆盖 msg
            ProceedingJoinPoint joinPoint = bundle.getJoinPoint(); // 获取切点
            SpELObject spELObject = new SpELObject(joinPoint != null ? joinPoint.getTarget() : config.getLogStrategy()).builder()
                    .setVariable("msgMap", msgMap)
                    .setVariable("config", config)
                    .setVariable("bundle", bundle)
                    .springBeanFactory().build()
                    .parseExpression(config.getSpEL());
            return spELObject.getValue(String.class);
        } else if (StrUtil.isNotBlank(config.getMsg())) { // msg 覆盖 match
            return super.getMsg(config, msgMap, bundle);
        } else if (StrUtil.isNotBlank(config.getMatch())) {
            String match = config.getMatch();  // not trim
            String splitter = msgMap.get(LogKey.WRAP_KEY, 1);
            String addSplitter = msgMap.get(LogKey.ADD_KEY, 1);
            boolean addCheck = StrUtil.isNotBlank(addSplitter);
            String[] strings = null;
            if (StrUtil.isNotBlank(splitter)) {
                strings = match.split(splitter);
            } else {
                strings = new String[]{match};
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < strings.length; i++) {
                if (i != 0) sb.append('\n'); //wrap
                String line = strings[i];
                if (StrUtil.isNotBlank(line)) {
                    line = line.trim();
                    if (addCheck) { // add
                        String[] split = line.split(addSplitter);
                        for (String s : split) {
                            String value = msgMap.getValue(s, 0, config, bundle);
                            sb.append(value != null ? value : s);
                        }
                    } else {
                        String value = msgMap.getValue(line, 0, config, bundle);
                        sb.append(value != null ? value : line);
                    }
                }
            }
            return sb.toString();
        }
        return "";
    }
}
