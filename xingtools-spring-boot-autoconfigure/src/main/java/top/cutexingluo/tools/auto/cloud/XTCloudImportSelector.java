package top.cutexingluo.tools.auto.cloud;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;


/**
 * @author XingTian
 * @version 1.0.0
 * @date 2023/5/3 22:54
 */

@EnableConfigurationProperties(AutoInjectCloudProperty.class)
@Slf4j
public class XTCloudImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(@NotNull AnnotationMetadata importingClassMetadata) {
        log.info("XingToolsCloudServer 启动成功 !  请享受你的日常的乐趣 !");
        return new String[]{
        };
    }
}
