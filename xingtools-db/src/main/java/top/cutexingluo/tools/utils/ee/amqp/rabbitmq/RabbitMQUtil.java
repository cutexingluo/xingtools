package top.cutexingluo.tools.utils.ee.amqp.rabbitmq;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import top.cutexingluo.core.designtools.builder.XTBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

/**
 * RabbitMQ 执行工具
 *
 * <p>需导入 spring-boot-starter-amqp</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/17 22:32
 */
public class RabbitMQUtil {

    public static boolean printTrace = true;
    public static Consumer<Exception> exceptionHandler = null;

    /**
     * 生成延时队列交换机
     * <p>需要为 RabbitMQ 安装延迟队列插件</p>
     */
    public static CustomExchange newDelayedExchange(String name) {
        Map<String, Object> args = new HashMap<>(16);
        args.put(RabbitMQArgsKey.X_DELAY_TYPE.toString(), "direct");
        return new CustomExchange(name, RabbitMQArgsKey.X_DELAY_MESSAGE.toString(),
                true, false, args);
    }

    public static Channel getChannel(RabbitProperties properties) {
        return builder(properties).build();
    }

    public static ChannelBuilder builder(RabbitProperties properties) {
        return new ChannelBuilder(properties);
    }

    /**
     * 使用编程式生成Channel
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class ChannelBuilder extends XTBuilder<Channel> {
        private RabbitProperties properties;
        private Connection connection;

        public ChannelBuilder(RabbitProperties properties) {
            this.properties = properties;
        }

        protected ConnectionFactory getFactory() {
            ConnectionFactory factory = new ConnectionFactory();
            if (properties.getHost() != null) factory.setHost(properties.getHost());
            if (properties.getPort() != null) factory.setPort(properties.getPort());
            if (properties.getUsername() != null) factory.setUsername(properties.getUsername());
            if (properties.getPassword() != null) factory.setPassword(properties.getPassword());
            return factory;
        }

        public Channel getChannel() throws IOException, TimeoutException {
            this.connection = getFactory().newConnection();
            Channel channel = connection.createChannel();
            this.target = channel;
            return channel;
        }


        public void closeAll() throws IOException, TimeoutException {
            this.target.close();
            this.connection.close();
        }

        @Override
        public Channel build() {
            if (this.target == null) {
                try {
                    return getChannel();
                } catch (IOException | TimeoutException e) {
                    if (exceptionHandler != null) exceptionHandler.accept(e);
                    else if (printTrace) e.printStackTrace();
                }
            }
            return this.target;
        }


    }
}
