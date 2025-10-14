package top.cutexingluo.tools.utils.se.obj.spel;

import lombok.Data;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import top.cutexingluo.core.designtools.builder.XTBuilder;
import top.cutexingluo.tools.utils.spring.SpringUtils;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * SpEL 实体类
 * <p>可以解析 spel  字符串</p>
 *
 * <p>示例如下</p>
 * <pre>{@code
 * new SpELObject().builder() // builder 构建上下文
 * .setRootObject(new TestService())  // 方法所在的实体类对象
 * .springBeanFactory()  // 加载springBeanFactory的bean
 * .setVariable("variable", "value") // 设置变量
 * .build()
 * .parseExpression("check(@testBean, #variable, 'hello world')") // 调用方法 , @testBean 代表容器里面的bean , #variable 代表设置的变量
 * // 其中 check方法必须是 TestService 里面的 public 方法
 * .getValue(Boolean.class) // 获取返回值
 * }</pre>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/20 17:57
 * @see SpelExpressionParser
 * @since 1.0.4
 */
@Data
public class SpELObject {

    /**
     * 解析器
     */
    @NonNull
    protected ExpressionParser parser;

    /**
     * 上下文
     */
    protected StandardEvaluationContext context;

    /**
     * 解析后的表达式
     */
    protected Expression expression;


    protected static final ParameterNameDiscoverer DISCOVERER = new DefaultParameterNameDiscoverer();

    public SpELObject() {
        this(new SpelExpressionParser());
    }


    public SpELObject(@NonNull ExpressionParser parser) {
        this.parser = parser;
        this.context = new StandardEvaluationContext();
    }

    public SpELObject(StandardEvaluationContext context) {
        this(new SpelExpressionParser(), context);
    }

    /**
     * @param rootObject 根对象，可以直接在表达式调用方法
     */
    public SpELObject(Object rootObject) {
        this(new SpelExpressionParser(), new StandardEvaluationContext(rootObject));
    }


    /**
     * @param parser  解析器
     * @param context 上下文
     */
    public SpELObject(@NonNull ExpressionParser parser, StandardEvaluationContext context) {
        this.parser = parser;
        this.context = context;
    }

    /**
     * @param parser     解析器
     * @param rootObject 根对象，可以直接在表达式调用方法
     */
    public SpELObject(@NonNull ExpressionParser parser, Object rootObject) {
        this(parser, new StandardEvaluationContext(rootObject));
    }

    /**
     * 解析表达式
     *
     * @param expression 表达式
     */
    public SpELObject parseExpression(String expression) {
        this.expression = parser.parseExpression(expression);
        return this;
    }


    /**
     * 执行表达式
     *
     * @param returnType 返回类型
     */
    public <T> T getValue(@Nullable Class<T> returnType) {
        Objects.requireNonNull(expression, ("expression is null, you should call parseExpression() first"));
        return expression.getValue(context, returnType);
    }

    public Builder builder() {
        Objects.requireNonNull(context); // require context
        return new Builder(this);
    }


    public class Builder extends XTBuilder<SpELObject> {

        public Builder(SpELObject target) {
            this.target = target;
        }

        /**
         * 设置根对象
         *
         * @param root 根对象，可以直接在表达式调用方法
         */
        public Builder setRootObject(@NonNull Object root) {
            context.setRootObject(root);
            return this;
        }

        /**
         * 设置上下文
         */
        public Builder contextHolder(Consumer<StandardEvaluationContext> contextHolder) {
            if (contextHolder != null) {
                contextHolder.accept(context);
            }
            return this;
        }

        /**
         * 设置变量
         * <p>使用 #name 调用</p>
         */
        public Builder setVariable(@Nullable String name, @Nullable Object value) {
            context.setVariable(name, value);
            return this;
        }

        /**
         * 通过目标方法的入参设置变量
         * <p>使用 #name 调用实参</p>
         *
         * @param method 方法
         * @param args   参数
         */
        public Builder setVariables(@NonNull Method method, @NonNull Object[] args) {
            for (int i = 0; i < args.length; i++) {
                // 读取方法参数
                MethodParameter methodParam = new SynthesizingMethodParameter(method, i);
                methodParam.initParameterNameDiscovery(DISCOVERER);
                // 设置方法 参数名和值 为spel变量
                context.setVariable(methodParam.getParameterName(), args[i]);
            }
            return this;
        }

        /**
         * 设置 beanFactory 解决器
         * <p>使用@bean 调用bean </p>
         */
        public Builder setBeanFactory(BeanFactory beanFactory) {
            context.setBeanResolver(new BeanFactoryResolver(beanFactory));
            return this;
        }

        /**
         * 直接设置 springBeanFactory
         * <p>使用@bean 调用bean </p>
         */
        public Builder springBeanFactory() {
            return setBeanFactory(SpringUtils.getApplicationContext());
        }
    }
}
