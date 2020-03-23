package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Consumer01 {

    public static final String Queue = "queue";

    public static void main(String[] args)  throws Exception{
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setPassword("guest");
        connectionFactory.setUsername("guest");
        connectionFactory.setVirtualHost("/");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(Queue,true,false,false,null);
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
            /**
             * @param consumerTag 消费标签
             * @param envelope 信封 可以拿到
             * @param properties 配置信息
             * @param body 传递的信息
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                long deliveryTag = envelope.getDeliveryTag();
                System.out.println("deliveryTag:"+deliveryTag);
                String exchange = envelope.getExchange();
                System.out.println("exchange:"+exchange);
                String routingKey = envelope.getRoutingKey();
                System.out.println("routingKey:"+routingKey);
                System.out.println("获得信息 "+new String(body));
            }
        };
        String s = channel.basicConsume(Queue, true, defaultConsumer);

    }

}
