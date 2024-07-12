package top.cutexingluo.tools.utils.ee.amqp.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 不要使用 ， 不要调用
 * <p>仅参考，防止忘记用法</p>
 * <p>需导入 spring-rabbit</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/17 18:07
 */
public final class RabbitMqTest {

    private RabbitMqTest() {
    }

    public void common() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        // 1.直接
        // 队列名称，队列是否需要持久化，是否排他性，是否自动删除，额外参数
        // 队列名称 ，  一般true保存到Mnesia数据库中, 生产者、消费者（一般false）,没有消费者连接就会删除，
        channel.queueDeclare("01-hello", true, false, false, null);
        channel.basicQos(1); //独立一次接收个数
        // 2.发布订阅模式 ,direct
        // 交换机名称，订阅模式
        channel.exchangeDeclare("02-exchange", "fanout", true, false, null);

        // consumer
        String queue = channel.queueDeclare().getQueue();
        channel.queueBind(queue, "02-exchange", "");

        // 3.topic
        channel.exchangeDeclare("02-exchange", "topic");

        //----------------生产----------
        // 交换机名称,路由key,消息属性, 消息内容
        //MessageProperties.PERSISTENT_TEXT_PLAIN 消息持久化
        channel.basicPublish("02-exchange", "01-hello", MessageProperties.PERSISTENT_TEXT_PLAIN, "hello rabbitmq".getBytes());

        //----------------消费----------
        // 队列名称，自动签收
        channel.basicConsume("01-hello", true, (consumerTag, message) -> {
            // 取回来回调
            byte[] msg = message.getBody();
            System.out.println(new String(msg));
//            channel.basicAck(message.getEnvelope().getDeliveryTag(), false); //手动接收
            //getDeliveryTag, 拒绝所有,是否重试
            channel.basicNack(message.getEnvelope().getDeliveryTag(), false, true);
        }, consumerTag -> { //取消

        });

        channel.close();
        connection.close();
    }

    RabbitTemplate rabbitTemplate;

    //    private static final  String manual = AcknowledgeMode.MANUAL.name();
    // 配置文件 spring.rabbitmq.listener.simple.acknowledge-mode=manual
    // spring.rabbitmq.listener.simple.prefetch=1
    @RabbitListener(queuesToDeclare = @org.springframework.amqp.rabbit.annotation.Queue("01-hello"), ackMode = "MANUAL", concurrency = "1")
    @RabbitHandler  // 开启消息处理
    public void queue(Message msg, Channel channel) {// @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag
        rabbitTemplate.convertAndSend("02-exchange", "01-hello", "hello rabbitmq");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @org.springframework.amqp.rabbit.annotation.Queue("01-hello"),
            exchange = @Exchange(value = "02-exchange", type = "fanout")
    ))
    @RabbitHandler  // 开启消息处理
    public void pubsub(Message msg, Channel channel) throws IOException {// @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag
        rabbitTemplate.convertAndSend("02-exchange", "01-hello", "hello rabbitmq");
        channel.basicAck(msg.getMessageProperties().getDeliveryTag(), true);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @org.springframework.amqp.rabbit.annotation.Queue("01-hello"),
            exchange = @Exchange(value = "02-exchange", type = "direct"),
            key = {"user.*"}
    ))
    public void routing(Message msg, Channel channel) throws IOException {// @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag
        rabbitTemplate.convertAndSend("02-exchange", "01-hello", "hello rabbitmq");
        channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
        channel.basicNack(msg.getMessageProperties().getDeliveryTag(), false, true);
    }


    @Bean
    public void QueueA() {// @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag
        QueueBuilder.durable("01-hello").withArguments(new HashMap<>(16)).build();
    }

    @Bean("queueC")
    public Queue queueC() {
        Map<String, Object> arguments = new HashMap<>();
        //设置死信交换机
        arguments.put("x-dead-letter-exchange", "YQ");
        //设置死信routingKey
        arguments.put("x-dead-letter-routing-key", "YD");
        //这里就不设置TTL了
        return QueueBuilder.durable("QC").withArguments(arguments).build();
    }

    @Bean
    //@Qualifier按照名称注入，也就是上面bean的别名
    public Binding queueDBindingX(@Qualifier("queueD") Queue queueD,
                                  @Qualifier("yExchange") DirectExchange yExchange) {
        //后面的队列连前面的交换机
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }

    //备份交换机
    public static final String BACKUP_EXCHANGE_NAME = "backup_exchange";
    //备份队列
    public static final String BACKUP_QUEUE_NAME = "backup_queue";
    //报警队列
    public static final String WARNING_QUEUE_NAME = "warning_queue";

    @Bean
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    @Bean
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }

    @Bean
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }

    @Bean
    public Binding backupQueueBindingbackupExchange(@Qualifier("backupQueue") Queue backupQueue,
                                                    @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(backupQueue).to(backupExchange);//删除类型是没有routingKey的
    }

    @Bean
    public Binding warningQueueBindingbackupExchange(@Qualifier("warningQueue") Queue warningQueue,
                                                     @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(warningQueue).to(backupExchange);
    }

    @Bean
    public DirectExchange confirmExchange() {
        return ExchangeBuilder.directExchange("confirm_exchange").durable(true)
                .withArgument(RabbitMQArgsKey.ALTERNATE_EXCHANGE.toString(), BACKUP_EXCHANGE_NAME).build();
    }

    //接受报警消息
    @RabbitListener(queues = WARNING_QUEUE_NAME)
    public void receiveWarningMsg(Message message) throws UnsupportedEncodingException {
        String msg = new String(message.getBody(), "UTF-8");
//        log.error("报警发现不可路由的消息：{}", msg);
//        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(5).build();
    }
}
