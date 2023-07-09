package org.example.rabbitmq.two;

import com.rabbitmq.client.Channel;
import org.example.rabbitmq.utils.RabbitMqUtils;

import java.util.Scanner;

public class Task01 {
    //隊列名稱
    public static final String QUEUE_NAME="hello";
    public static void main(String[] args) throws Exception {
        //發送大量消息
        Channel channel = RabbitMqUtils.getChannel();
        /**
         * 生成一個隊列
         * 1.隊列名稱
         * 2.隊列裡面的消息是否持久化(磁盤) 默認情況消息存儲在內存中
         * 3.該隊列是否只供一個消費者進行消費 是否進行消息共享，true可以多個消費者消費 false:只能一個消費者消費
         * 4.是否自動刪除 最後一個消費者端開連接以後 該隊一句是否自動刪除 true 自動刪除 false不自動刪除
         * 5.其他參數
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //從控制台當中接收信息
        Scanner sc=new Scanner(System.in);
        while(sc.hasNext()){
            String message=sc.next();
            /**
             * 發送一個消費
             * 1.發送到哪個交換機
             * 2.路由的key值是哪個 本次是隊列的名稱
             * 3.其他參數設定
             *4.發送消息的消息體
             */
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("發送消息完成: "+message);
        }
    }
}
