package com.xuecheng.test.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置类
 */
@Configuration
public class RabbitMqConfig {
    public static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    public static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    public static final String EXCHAGE_TOPIC_INFORM="exchange_topic_inform";
    public static final String EMAIL_ROUTING="inform.#.email.#";
    public static final String SMS_ROUTING="inform.#.sms.#";


    //声明交换及
    @Bean(EXCHAGE_TOPIC_INFORM)
    public Exchange EXCHAGE_TOPIC_INFORM(){
        return ExchangeBuilder.topicExchange(EXCHAGE_TOPIC_INFORM).durable(true).build();
    }

    //声明队列
    @Bean(QUEUE_INFORM_EMAIL)
    public Queue QUEUE_INFORM_EMAIL(){
        return new Queue(QUEUE_INFORM_EMAIL);
    }

    @Bean(QUEUE_INFORM_SMS)
    public Queue QUEUE_INFORM_SMS(){
        return new Queue(QUEUE_INFORM_SMS);
    }

    //队列绑定交换机
    @Bean
    public Binding Bind_QUEUE_INFORM_EMAIL(@Qualifier(EXCHAGE_TOPIC_INFORM) Exchange exchange,
                                           @Qualifier(QUEUE_INFORM_EMAIL) Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with(EMAIL_ROUTING).noargs();
    }

    @Bean
    public Binding Bind_QUEUE_INFORM_SMS(@Qualifier(EXCHAGE_TOPIC_INFORM) Exchange exchange,
                                           @Qualifier(QUEUE_INFORM_SMS) Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with(SMS_ROUTING).noargs();
    }
}
