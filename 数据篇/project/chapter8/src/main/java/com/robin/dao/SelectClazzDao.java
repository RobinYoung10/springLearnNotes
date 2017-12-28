package com.robin.dao;

import com.robin.mybatis.Clazz;
import com.robin.mybatis.Student;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.List;

public class SelectClazzDao {
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
        List<Clazz> clazzList = session.selectList("com.robin.mybatis.ClazzMapper.selectClazz");
        for(Clazz clazz : clazzList) {
            System.out.println(clazz.getId());
            List<Student> studentList = clazz.getStudents();
            for(Student student : studentList) {
                System.out.println(student.getName());
            }
        }
        session.commit();
        session.close();
    }
}
