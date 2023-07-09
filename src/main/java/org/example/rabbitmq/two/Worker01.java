package org.example.rabbitmq.two;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.example.rabbitmq.utils.RabbitMqUtils;

/**
 * 這是一個工作線程(相當於之前消費者)
 */
public class Worker01 {
    //隊列的名稱
    public static final String QUEUE_NAME="hello";
    //接收消息
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //消息的接收
        DeliverCallback deliverCallback=(consumerTag,message)->{
            System.out.println("接收到的消息: "+new String(message.getBody()));
        };
        //消息接收被取消時，執行下面的內容
        CancelCallback cancelCallback=(consumerTag)->{
            System.out.println("消息者取消消費接口回調邏輯");
        };
        /**
         * 消費者消費消息
         * 1.消費哪個隊列
         * 2.消費成功之後是否要自動應答true 代表的自動應答false代表手動應答
         * 3.消費者未成功消費的回調
         * 4.消費者取消消費的回調
         */
        System.out.println("C1等待接收消息....");
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
