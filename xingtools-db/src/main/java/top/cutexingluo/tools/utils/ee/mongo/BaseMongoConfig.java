package top.cutexingluo.tools.utils.ee.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * mongodb 基础配置
 * <p>使用方式如下</p>
 * <pre>
 *      1. 新建配置类继承该类或者直接注册到容器
 *      2. 使用 @EnableMongoRepositories 注解
 *      3. 注册 mongoTemplate
 *  </pre>
 * <p>需要导入 spring-boot-starter-data-mongodb 包</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/2/11 17:56
 */
@ConditionalOnClass(AbstractMongoClientConfiguration.class)
//@EnableMongoRepositories(basePackages = "top.cutexingluo.xing")
public class BaseMongoConfig extends AbstractMongoClientConfiguration {


    @Value("${spring.data.mongodb.host:127.0.0.1}")
    protected String host;

    @Value("${spring.data.mongodb.port:27017}")
    protected int port;

    @Value("${spring.data.mongodb.database:test}")
    protected String database;

    @Value("${spring.data.mongodb.username:root}")
    protected String username;

    @Value("${spring.data.mongodb.password:root}")
    protected String password;

    @NotNull
    @Override
    protected String getDatabaseName() {
        return database;
    }

    @NotNull
    @Override
    public MongoClient mongoClient() {
        String format = String.format("mongodb://%s:%s@%s:%d", username, password, host, port);
        return MongoClients.create(new ConnectionString(format));
    }

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }

    //    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), database);
    }

}
