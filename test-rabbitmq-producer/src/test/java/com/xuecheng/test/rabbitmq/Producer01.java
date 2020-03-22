package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * rabbitMq 测试程序
 */
public class Producer01 {

    private static final String QUEUE = "queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");

        Connection connection = null;
        Channel channel = null;
        try {
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();
            /**
             *
             * String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
             *  durable 是否持久化
             *  exclusive 是否排他
             *  autoDelete 是否自动删除
             *  arguments 配置参数
             */
            channel.queueDeclare(QUEUE,true,false,false,null);
            /**
             * String exchange, String routingKey, BasicProperties props, byte[] body
             * exchange 制定交换机
             * routingKey key
             * props 配置参数
             * body 消息体
             */
            channel.basicPublish("",QUEUE,null,new String("hello rabbitmq").getBytes());
            System.out.println("send successful");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            channel.close();

            connection.close();
        }
    }
}
