package com.robin.dao;

import com.robin.mybatis.Student;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.List;

public class SelectStudentDao {
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
        List<Student> studentList = session.selectList("com.robin.mybatis.StudentMapper.selectStudent");
        for(Student student : studentList) {
            System.out.println(student.getName());
            System.out.println(student.getClazz().getId());
            System.out.println(student.getClazz().getCode());
        }
        session.commit();
        session.close();
    }
}
