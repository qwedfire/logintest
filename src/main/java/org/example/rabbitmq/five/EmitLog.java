package org.example.rabbitmq.five;


import com.rabbitmq.client.Channel;
import org.example.rabbitmq.utils.RabbitMqUtils;

import java.util.Scanner;

/**
 * 發消息、交換機  輸入文字
 */
public class EmitLog {
    public static final String EXCHANGE_NAME="logs";
    public static void main(String[] args) throws Exception {
        Channel channel=RabbitMqUtils.getChannel();
//        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        Scanner sc=new Scanner(System.in);
        while(sc.hasNext()){
            String message=sc.next();
            channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes("UTF-8"));
            System.out.println("生產者發出消息:"+message);
        }
    }
}
