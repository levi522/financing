package com.r3.financing;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import redis.clients.jedis.JedisPool;

@EnableScheduling
@ComponentScan({"com.r3.financing"})
@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.class, MongoAutoConfiguration.class, MongoDataAutoConfiguration.class},scanBasePackages = "com.r3.financing.*")
@MapperScan({"com.r3.financing.dao"})
@EnableTransactionManagement
@EnableAsync
@ServletComponentScan
@Import({
        JedisPool.class
})
public class FinancingApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinancingApplication.class, args);
    }

}
