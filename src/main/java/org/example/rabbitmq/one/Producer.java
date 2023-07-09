package org.example.rabbitmq.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {
    //對列名稱
    private static final String QUEUE_NAME ="hello";

    //發消息
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory=new ConnectionFactory();
        //工廠ip連接rabbitMQ的隊
        factory.setHost("127.0.0.1");
        //用戶名稱
        factory.setUsername("guest");
        //密碼
        factory.setPassword("guest");
        //創建連接
        Connection connection=factory.newConnection();
        //獲取信道
        Channel channel = connection.createChannel();
        /**
         * 生成一個隊列
         * 1.隊列名稱
         * 2.隊列裡面的消息是否持久化(磁盤) 默認情況消息存儲在內存中
         * 3.該隊列是否只供一個消費者進行消費 是否進行消息共享，true可以多個消費者消費 false:只能一個消費者消費
         * 4.是否自動刪除 最後一個消費者端開連接以後 該隊一句是否自動刪除 true 自動刪除 false不自動刪除
         * 5.其他參數
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //發消息
        String message="hello world";//初次使用
        /**
         * 發送一個消費
         * 1.發送到哪個交換機
         * 2.路由的key值 是哪個 本次是隊列的名稱
         * 3.其他參數信息
         * 4.發送消息的消息體
         */
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        System.out.println("消息發送完畢");
    }
}
