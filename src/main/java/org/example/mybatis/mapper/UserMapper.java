package org.example.mybatis.mapper;

import org.example.mybatis.pojo.User;

import java.util.List;

public interface UserMapper {
    /**
     * MyBatis面相接口編成的兩個一致:
     * 1. 映射文件的namespace
     * 2. 映射文件中SQL語句的id
     * @return
     */
    int insertUser();
    /**
     * 修改用戶信息
     */
    void updateUser();
    /**
     * 刪除用戶信息
     */
    void deleteUser();
    /**
     * 查詢用戶by ID
     */
    User getUserById();
    /**
     * 查詢所有的用戶信息
     */
    List<User> getAllUser();

    /**
     * 查詢用戶by帳號
     */
    User getUserByName(String name);
}
