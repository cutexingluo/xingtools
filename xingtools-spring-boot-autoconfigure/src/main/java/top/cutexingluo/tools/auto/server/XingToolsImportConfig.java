package top.cutexingluo.tools.auto.server;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.type.AnnotationMetadata;
import top.cutexingluo.tools.property.ServerProperty;
import top.cutexingluo.tools.start.log.LogInfoAuto;

/**
 * @author XingTian
 * @version 1.0.0
 * @date 2023/4/26 21:36
 */
@Primary
@Slf4j
@EnableConfigurationProperties({AutoInjectProperty.class, ServerProperty.class})
public class XingToolsImportConfig implements ImportSelector, Ordered {


    @Override
    public String[] selectImports(@NotNull AnnotationMetadata importingClassMetadata) {
        return new String[]{
                XingToolsAutoConfiguration.class.getName(), //配置信息
                LogInfoAuto.class.getName()
        };
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
