package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer02 {


    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    private static final String EXCHAGE_FANOUT_INFORM="exchange_fanout_inform";

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
        channel.exchangeDeclare(EXCHAGE_FANOUT_INFORM,BuiltinExchangeType.FANOUT);
        channel.queueBind(QUEUE_INFORM_EMAIL,EXCHAGE_FANOUT_INFORM,"");
        channel.queueBind(QUEUE_INFORM_SMS,EXCHAGE_FANOUT_INFORM,"");
        // String exchange, String routingKey, BasicProperties props, byte[] body
        for(int i = 1;i<=5;i++) {
            channel.basicPublish(EXCHAGE_FANOUT_INFORM, "", null,new String("hello world").getBytes());
            System.out.println("send successful");
        }
    }
}
