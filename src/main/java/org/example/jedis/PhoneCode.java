package org.example.jedis;

import redis.clients.jedis.Jedis;

import java.util.Random;
public class PhoneCode {
    public static void main(String[] args) {
        //模擬驗證碼發送
        verifyCode("1234567890");

//        getRedisCode("1234567890","7327720");
    }
    //3驗證碼校驗
    public static void getRedisCode(String phone,String code){
        Jedis jedis =new Jedis("192.168.1.110",6379);
        String codeKey="VerifyCode"+phone+":code";
        String redisKey=jedis.get(codeKey);
        if(redisKey.equals(code)){
            System.out.println("成功");
        }else{
            System.out.println("失敗");
        }
    }
    //2每個手機每天只能發送三次，驗證碼放到redis中，設置過期時間120秒
    public static void verifyCode(String phone){
        //連接redis
        Jedis jedis =new Jedis("192.168.1.110",6379);
        //拼接key
        //手機發送次數key
        String countKey="VerifyCode"+phone+":count";
        //驗證碼key
        String codeKey="VerifyCode"+phone+":code";
        //每個手機每天只能發送三次
        String count=jedis.get(countKey);
        if(count==null){
            //沒有發送次數，第一次發送
            //設置發送次數為一 (24*60*60 一天時間)
            jedis.setex(countKey,24*60*60,"1");
        }else if(Integer.parseInt(count)<=2){
            //發送次數+1
            jedis.incr(countKey);
        }else if(Integer.parseInt(count)>2){
            //發送三次，不能再發送了
            System.out.println("今天已經超過三次不能再發送了");
            jedis.close();
        }
        //發送驗證碼放到redis裡面
        String vcode=getCode();
        jedis.setex(codeKey,120,vcode);
        jedis.close();
    }
    //1生成6位數字驗證碼
    public static String getCode(){
        Random random=new Random();
        String code="";
        for(int i=0;i<6;i++){
            int rand=random.nextInt(10);
            code +=rand;
        }
        return code;
    }
}
