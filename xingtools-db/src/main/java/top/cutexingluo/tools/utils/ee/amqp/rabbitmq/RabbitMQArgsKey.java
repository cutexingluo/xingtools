package top.cutexingluo.tools.utils.ee.amqp.rabbitmq;

import top.cutexingluo.tools.common.base.XTStrCode;

/**
 * RabbitMQ 键值
 *
 *@author XingTian
 * @version 1.0.0
 * @date 2023/10/18 17:25
 * @since 1.0.2
 */
public enum RabbitMQArgsKey  implements XTStrCode {


    /**
     * 延迟类型  x-delay-type
     */
    X_DELAY_TYPE("x-delay-type"),
    /**
     * 延迟消息  x-delay-message
     */
    X_DELAY_MESSAGE("x-delay-message"),

    /**
     * 死信交换机  x-dead-letter-exchange
     */
    X_DEAD_LETTER_EXCHANGE("x-dead-letter-exchange"),

    /**
     * 死信routingKey  x-dead-letter-routing-key
     */
    X_DEAD_LETTER_ROUTING_KEY("x-dead-letter-routing-key"),

    /**
     * TTL（过期时间）  x-message-ttl
     */
    X_MESSAGE_TTL("x-message-ttl"),


    /**
     * 交替/备份 交换机 alternate-exchange
     */
    ALTERNATE_EXCHANGE("alternate-exchange"),

    /**
     * 惰性队列  x-queue-mode
     */
    X_QUEUE_MODE("x-queue-mode"),
    ;

    private final String key;

    RabbitMQArgsKey(final String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return getKey();
    }

    @Override
    public String strCode() {
        return key;
    }
}
