package top.cutexingluo.tools.utils.spring;

import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/16 12:25
 */
public abstract class ImportSelectorUtil {

    /**
     * 获取 AnnotationAttributes 对象
     */
    public static <T> AnnotationAttributes getAnnotationAttributes(AnnotationMetadata importingClassMetadata, Class<T> annoType) {
        Map<String, Object> annotationAttributes = importingClassMetadata
                .getAnnotationAttributes(annoType.getName(), false);
        return annotationAttributes != null ?
                AnnotationAttributes.fromMap(annotationAttributes) : null;
    }
}
