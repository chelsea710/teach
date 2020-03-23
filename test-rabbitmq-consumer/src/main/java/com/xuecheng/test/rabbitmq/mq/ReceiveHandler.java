package com.xuecheng.test.rabbitmq.mq;

import com.rabbitmq.client.Channel;
import com.xuecheng.test.rabbitmq.config.RabbitMqConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ReceiveHandler {

    @RabbitListener(queues = {RabbitMqConfig.QUEUE_INFORM_EMAIL})
    public void receiveMessage(String msg, Message message,Channel channel){
        System.out.println(msg);

        System.out.println(new String(message.getBody()));
    }
}
