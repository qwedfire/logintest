package org.example.rabbitmq.one;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    //隊列的名稱
    public static final String QUEUE_NAME="hello";
    //接收消息
    public static void main(String[]ares) throws IOException, TimeoutException {
        ConnectionFactory factory=new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection=factory.newConnection();
        Channel channel = connection.createChannel();
        //聲明 接收消息
        DeliverCallback deliverCallback=(consumerTag,message)->{
            System.out.println(new String(message.getBody()));
        };
        //取消消息時的回調
        CancelCallback cancelCallback=consumerTag -> {
            System.out.println("消息消費被中斷");
        };
        /**
         * 消費者消費消息
         * 1.消費哪個隊列
         * 2.消費成功之後是否要自動應答true 代表的自動應答false代表手動應答
         * 3.消費者未成功消費的回調
         * 4.消費者取消消費的回調
         */
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
