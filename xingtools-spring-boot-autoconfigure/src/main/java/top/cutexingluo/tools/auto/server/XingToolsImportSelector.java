package top.cutexingluo.tools.auto.server;


import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.context.annotation.Primary;
import org.springframework.core.type.AnnotationMetadata;
import top.cutexingluo.tools.autoconfigure.satoken.SaTokenConfiguration;
import top.cutexingluo.tools.autoconfigure.server.aop.xtlock.XTLockAopAutoConfigure;
import top.cutexingluo.tools.autoconfigure.server.spring.SpringCacheAutoConfiguration;
import top.cutexingluo.tools.autoconfigure.server.spring.SpringUtilsAutoConfiguration;
import top.cutexingluo.tools.start.log.LogInfoAuto;

import java.util.function.Predicate;


/**
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/2 20:07
 */
@Slf4j
@Primary
@AutoConfigureAfter({LogInfoAuto.class, XingToolsAutoConfiguration.class})
public class XingToolsImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(@NotNull AnnotationMetadata annotationMetadata) {
        log.info("XingToolsServer 启动成功 !  请享受你的日常的乐趣 !");
        return new String[]{
        };
    }

    @Override
    public Predicate<String> getExclusionFilter() {
        return ImportSelector.super.getExclusionFilter();
    }
}
