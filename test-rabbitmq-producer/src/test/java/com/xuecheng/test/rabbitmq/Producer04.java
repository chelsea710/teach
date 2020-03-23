package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer04 {


    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    private static final String EXCHAGE_ROUTING_INFORM="exchange_routing_inform";
    private static final String EMAIL_ROUTING="email_routing";
    private static final String SMS_ROUTING="sms_routing";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_INFORM_EMAIL,true,false,false,null);
        channel.queueDeclare(QUEUE_INFORM_SMS,true,false,false,null);
        channel.exchangeDeclare(EXCHAGE_ROUTING_INFORM,BuiltinExchangeType.TOPIC);
        channel.queueBind(QUEUE_INFORM_EMAIL,EXCHAGE_ROUTING_INFORM,EMAIL_ROUTING);
        /**
         *
         * topic 模式
         * #:表示一个或者多个词 用逗号分隔
         * *:表示一个词
         */
        channel.queueBind(QUEUE_INFORM_EMAIL,EXCHAGE_ROUTING_INFORM,"inform.#.email.#");
        channel.queueBind(QUEUE_INFORM_SMS,EXCHAGE_ROUTING_INFORM,SMS_ROUTING);
        channel.queueBind(QUEUE_INFORM_SMS,EXCHAGE_ROUTING_INFORM,"inform.#.sms.#");
        // String exchange, String routingKey, BasicProperties props, byte[] body
        for(int i = 1;i<=5;i++) {
//            channel.basicPublish(EXCHAGE_ROUTING_INFORM, EMAIL_ROUTING, null,new String("hello world").getBytes());
//            System.out.println("send email successful");
//
//            channel.basicPublish(EXCHAGE_ROUTING_INFORM, SMS_ROUTING, null,new String("hello world").getBytes());
//            System.out.println("send email successful");

            channel.basicPublish(EXCHAGE_ROUTING_INFORM, "inform", null,new String("hello world").getBytes());
            System.out.println("send successful");
        }
    }
}
