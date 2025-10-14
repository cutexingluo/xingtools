package top.cutexingluo.tools.utils.ee.amqp.rabbitmq;

import com.rabbitmq.client.Channel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.amqp.core.Message;
import top.cutexingluo.core.basepackage.basehandler.CallableTryHandler;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * RabbitMQ 处理程序
 *
 * <p>需导入 spring-rabbit</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/17 22:33
 */
@Data
@AllArgsConstructor
public class RabbitMQConsumerHandler implements CallableTryHandler {

    private final Channel channel;
    private Message msg;
    private long deliveryTag;


    /**
     * 是否帮助自动 ACK
     */
    private boolean autoCheckAck = true;

    /**
     * @param channel      通道
     * @param msg          Message
     * @param autoCheckAck 是否自动检查确认, 如果已经在配置层面开启, 这里设为 false
     */
    public RabbitMQConsumerHandler(Channel channel, Message msg, boolean autoCheckAck) {
        this(channel, msg.getMessageProperties().getDeliveryTag(), autoCheckAck);
        this.msg = msg;
    }

    /**
     * @param channel      通道
     * @param deliveryTag  tag
     * @param autoCheckAck 是否自动检查确认, 如果已经在配置层面开启, 这里设为 false
     */
    public RabbitMQConsumerHandler(Channel channel, long deliveryTag, boolean autoCheckAck) {
        this.channel = channel;
        this.deliveryTag = deliveryTag;
        this.autoCheckAck = autoCheckAck;
    }

    /**
     * 对 task 进行操作
     *
     * @param task    目标任务
     * @param inCatch 异常捕获
     * @return {@link Callable}<{@link T}>
     */
    public <T> Callable<T> decorate(Callable<T> task, Consumer<Exception> inCatch) {
        return decorate(task, null, inCatch);
    }


    @Override
    public <T> Callable<T> decorate(Callable<T> task, Supplier<Boolean> canRunTask, Consumer<Exception> inCatch) {
        return () -> {
            if (canRunTask != null && !canRunTask.get()) {
                return null;
            }
            T result = null;
            try {
                result = task.call();
                if (autoCheckAck) {
                    channel.basicAck(deliveryTag, false);
                }
            } catch (Exception e) {
                if (inCatch != null) {
                    inCatch.accept(e);
                }
                if (autoCheckAck) {
                    channel.basicNack(deliveryTag, false, true);
                }
            }
            return result;
        };
    }
}
