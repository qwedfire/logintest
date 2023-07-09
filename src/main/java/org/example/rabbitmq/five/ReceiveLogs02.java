package org.example.rabbitmq.five;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.example.rabbitmq.utils.RabbitMqUtils;

/**
 * 接收消息
 */
public class ReceiveLogs02 {
    //交換機的名稱
    public static final String EXCHANGE_NAME ="logs";
    public static void main(String[] args) throws Exception {
        Channel channel= RabbitMqUtils.getChannel();
        //聲明一個交換機
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        //聲明一個隊列 臨時隊列
        /**
         * 生成一個臨時隊列、隊列德名稱是隨機的
         * 當消費者段開與隊列的連接的時候隊列就自動刪除
         */
        String queueName = channel.queueDeclare().getQueue();
        /**
         * 綁定交換機與隊列
         */
        channel.queueBind(queueName,EXCHANGE_NAME,"");
        System.out.println("等待接收消息，把接收到消息打印在屏幕上");
        //接收消息
        DeliverCallback deliverCallback=(consumerTag, message) ->{
            System.out.println("ReceiveLogs02控制台打印接收到的消息:"+new String(message.getBody(),"UTF-8"));
        };
        //消費者取消消息時回調接口
        channel.basicConsume(queueName,true,deliverCallback,consumer ->{});
    }
}
