package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.example.mybatis.mapper.UserMapper;
import org.example.mybatis.pojo.User;
import org.example.rabbitmq.utils.RabbitMqUtils;
import redis.clients.jedis.Jedis;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static final String EXCHANGE_NAME = "logs";
    private static Jedis jedis = new Jedis("192.168.1.110", 6379);

    public static void main(String[] args) throws Exception {
        login();
    }

    //1. 實作一個登入API，當帳號密碼與DB的一致時，Access Token，不一致時，回傳錯誤訊息
    public static void login() throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.print("請輸入帳號:");
        String account = sc.nextLine();
        System.out.print("請輸入密碼:");
        String password = sc.nextLine();
        InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.getUserByName(account);
        if (user != null) {
            //存在帳戶
            if (user.getPassword().equals(password)) {
                //密碼正確 給一串token
                //2. 將Access Token存進Redis，多重登入時使用後踢前的策略
                if (!isLogin(account)) {
                    //第一次登入
                    //3. 使用Pub/Sub方式，將用戶登入的結果記錄在DB或Log File
                    receiveLogs(user.getUsername());
                    emitLog("登入成功");
                    System.out.println("登入成功");
                    addTokenToRedis(user.getUsername(), getJwtToken(user.getUsername()));
                } else {
                    //將舊的token 更新使其失效 實現後踢前
                    jedis.set(user.getUsername(), getJwtToken(user.getUsername()));
                    receiveLogs(user.getUsername());
                    emitLog("登入成功");
                    System.out.println("踢掉舊使用者");
                }
                //addTokenToRedis(user.getUsername(), token);
            } else {
                //密碼錯誤 返回錯誤訊息
                System.out.println("帳號或密碼有誤");
                receiveLogs(user.getUsername());
                emitLog("登入失敗");
            }
        } else {
            System.out.println("帳號或密碼有誤");
        }
    }

    /**
     * access token
     */
    public static String getJwtToken(String username) {
        Instant now = Instant.now();
        byte[] secret = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded();
        String jwt = Jwts.builder()
                .claim("name", username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(1, ChronoUnit.MINUTES)))
                .signWith(Keys.hmacShaKeyFor(secret))
                .compact();
        return jwt;
    }

    /**
     * 添加token到redis中
     */
    public static void addTokenToRedis(String userName, String token) {
        jedis.setnx(userName, token);
    }

    /**
     * 檢查是否已登入狀態 (如果已登入狀態會將新的token賦予使用者)
     */
    public static boolean isLogin(String username) {
        // 檢查 Redis 中是否存在該使用者的 access token
        String storedAccessToken = jedis.get(username);
        // 如果已經存在該使用者的
        if (storedAccessToken != null) {
            // 使用者 原本就已是登入狀態
            return true;
        }
        // 沒有多重登入
        return false;
    }
    //使用Pub/Sub方式，將用戶登入的結果記錄在DB或Log File
    /**
     * 取得success or fail
     * message 登入成功與否的參數
     */
    public static void emitLog(String message) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
        System.out.println("生產者發出消息:" + message);
    }

    /**
     * 接收消息 傳入db
     */
    public static void receiveLogs(String userName) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //聲明一個交換機
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        //聲明一個隊列 臨時隊列
        /**
         * 生成一個臨時隊列、隊列德名稱是隨機的
         * 當消費者段開與隊列的連接的時候隊列就自動刪除
         */
        String queueName = channel.queueDeclare().getQueue();
        /**
         * 綁定交換機與隊列
         */
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        //接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String receivedMessage = new String(message.getBody(), "UTF-8");
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/knight", "root", "root");
                 PreparedStatement statement = connection.prepareStatement("UPDATE  users SET access_token = ? WHERE username = ?")) {
                statement.setString(1, receivedMessage);
                statement.setString(2, userName);
                statement.executeUpdate();
                System.out.println("消息已存入資料庫");
            } catch (Exception e) {
                System.out.println("存入資料庫時發生錯誤：" + e.getMessage());
            }
        };
        //消費者取消消息時回調接口
        channel.basicConsume(queueName, true, deliverCallback, consumer -> {
        });
    }
}