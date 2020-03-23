package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer02_subscribe_emali {

    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    private static final String EXCHAGE_FANOUT_INFORM="exchange_fanout_inform";


    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setPassword("guest");
        connectionFactory.setUsername("guest");
        connectionFactory.setVirtualHost("/");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_INFORM_SMS,true,false,false,null);
        channel.exchangeDeclare(EXCHAGE_FANOUT_INFORM,BuiltinExchangeType.FANOUT);
        channel.queueBind(QUEUE_INFORM_SMS, EXCHAGE_FANOUT_INFORM,"");
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
        String s = channel.basicConsume(QUEUE_INFORM_SMS, true, defaultConsumer);

    }
}
