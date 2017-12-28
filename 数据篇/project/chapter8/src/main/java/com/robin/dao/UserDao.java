package com.robin.dao;

import com.robin.mybatis.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.List;

public class UserDao {
    private static SqlSessionFactory sqlSessionFactory;
    private static InputStream inputStream;
    public static void main(String [] args) {
        try {
            inputStream = Resources.getResourceAsStream("mybatisConfig.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SqlSession session = sqlSessionFactory.openSession();
        List<User> userList = session.selectList("com.robin.mybatis.User.selectUser");
        for(User user : userList) {
            System.out.println(user.getName());
        }
        session.commit();
        session.close();
    }
}
