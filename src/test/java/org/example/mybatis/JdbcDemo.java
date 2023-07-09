package org.example.mybatis;

import org.junit.Test;
import java.sql.*;

public class JdbcDemo {
    private Connection connection;
    @Test
    public void doJdbc(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection= DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/knight?serverTimezone=UTC","root","root");
            PreparedStatement ps=connection.prepareStatement("select * from users");
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString("username")+rs.getString("password"));
            }
            rs.close();
            ps.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(connection!=null){
                try{
                    connection.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
