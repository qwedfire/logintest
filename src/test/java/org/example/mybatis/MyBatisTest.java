package org.example.mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.example.mybatis.mapper.UserMapper;
import org.example.mybatis.pojo.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;
public class MyBatisTest {
    /**
     * SqlSession默認不自動提交事務，若需要自動提交事務
     * 可以使用SqlSessionFactory.openSession(true);
     * @throws IOException
     */
    @Test
    public void testInsert() throws IOException {
        //加載核心配置文件
        InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
        //獲取SqlSessionFactoryBuilder
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder=new SqlSessionFactoryBuilder();
        //獲取sqlSessionFactory
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is);
        //獲取SqlSession
        SqlSession sqlSession=sqlSessionFactory.openSession(true);
        //獲取mapper接口對象
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //測試功能
        int result=mapper.insertUser();
        //提交事務
//        sqlSession.commit();

        System.out.println("result:"+result);
    }
    @Test
    public void testUpdate() throws IOException {
        InputStream is =Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession sqlSession=sqlSessionFactory.openSession(true);
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.updateUser();
    }
    @Test
    public void testdelete() throws IOException {
        InputStream is =Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession sqlSession=sqlSessionFactory.openSession(true);
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.deleteUser();
    }
    @Test
    public void testgetUserById() throws IOException {
        InputStream is =Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession sqlSession=sqlSessionFactory.openSession(true);
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user=mapper.getUserById();
        System.out.println(user);
    }
    @Test
    public void testgetAllUser() throws IOException {
        InputStream is =Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession sqlSession=sqlSessionFactory.openSession(true);
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> list = mapper.getAllUser();
        list.forEach(user -> System.out.println(user));
    }
    @Test
    public void testgetUserByName() throws IOException {
        InputStream is =Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession sqlSession=sqlSessionFactory.openSession(true);
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        String name="user1";
        User user=mapper.getUserByName(name);
        System.out.println(user);
    }
}
