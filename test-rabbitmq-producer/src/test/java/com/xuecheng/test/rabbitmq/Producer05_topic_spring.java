package com.xuecheng.test.rabbitmq;

import com.xuecheng.test.rabbitmq.config.RabbitMqConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Producer05_topic_spring {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testsend(){
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHAGE_TOPIC_INFORM,"inform.email","send message to email");
    }

}
