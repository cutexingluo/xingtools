package top.cutexingluo.tools.autoconfigure.server.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import top.cutexingluo.tools.anno.EnableXTCloudSecurity;
import top.cutexingluo.tools.auto.server.XingToolsAutoConfiguration;
import top.cutexingluo.tools.autoconfigure.server.security.controller.SecurityControllerConfig;
import top.cutexingluo.tools.autoconfigure.server.security.property.XTSecurityProperties;
import top.cutexingluo.tools.security.XTSignGlobal;
import top.cutexingluo.tools.start.log.LogInfoAuto;
import top.cutexingluo.tools.utils.spring.ImportSelectorUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 自动配置
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/5/12 19:47
 */
@Slf4j
@AutoConfigureAfter(XingToolsAutoConfiguration.class)
@EnableConfigurationProperties(XTSecurityProperties.class)
public class XTSecurityImportSelector implements ImportSelector{
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {

        initSignKey(importingClassMetadata);

        // 获取属性成功后
        List<String> classNames = new ArrayList<>(2);

        classNames.add(XTSpringSecurityConfig.class.getName()); // 开启导入和配置
        classNames.add(SecurityControllerConfig.class.getName()); // 是否覆盖接口

        if (LogInfoAuto.enabled) {
            log.info("XTSecurityConfig 已开启！");
        }
        return classNames.toArray(new String[0]);
    }

    protected void initSignKey(AnnotationMetadata importingClassMetadata){
        AnnotationAttributes attributes = ImportSelectorUtil.getAnnotationAttributes(importingClassMetadata, EnableXTCloudSecurity.class);
        if (attributes == null) log.error("attributes is null");
        String signKey = attributes.getString("sign_key");
        if (signKey == null) log.error("sign_key is null");
        else{
            XTSignGlobal.setSign(signKey);
        }
    }

}
