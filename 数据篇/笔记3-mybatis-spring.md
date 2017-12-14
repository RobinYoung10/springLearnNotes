# 在Spring中使用MyBatis

---

MyBatis通过一个配置文件对多个SQL映射文件进行装配，同时在该文件中定义一些控制属性的信息。如下：

```xml
<!--mybatisConfig.xml-->
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <settings><!--控制MyBatis框架运行行为的属性信息-->
        <setting name="lazyLoadingEnabled" value="false"/>
    </settings>
    <typeAliases><!--定义全限定类名的别名，在映射文件中可以通过别名代替具体的类名-->
        <typeAlias alias="Tuser" type="com.robin.domain.Tuser" />
    </typeAliases>
    <mappers><!--引用SQL映射文件，在spring配置中提供了更为简便的方法，可以删掉这个标签-->
        <mapper resource="com/robin/domain/mybatis/Tuser.xml" />
    </mappers>
</configuration>
```

映射文件：

```xml
<!--Tuser.xml-->
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.robin.dao.mybatis.TuserMybatisDao">
    <select id="getTuser" resultType="Tuser" parameterType="int">
        SELECT
         user_id userId,<!--userId为Tuser类里面的域-->
         user_name userName,
         password password
        FROM t_user
        WHERE user_id = #{userId}
    </select>
</mapper>
```

通过以下语句就可以调用上面的getTuser映射语句：

```java
SqlSession session = sqlMapper.openSession();
try {
  Tuser tuser = (Tuser) session.selectOne("com.robin.dao.mybatis.TuserMybatisDao.gegetTuser", 1);
} finally {
  session.close();
}
```

---

### 在Spring中配置MyBatis

MyBatis提供的mybatis-spring整合包实现Spring和MyBatis的整合。

Spring配置文件如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context" xmlns:p="http://www.springframework.org/schema/p"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">

    <context:component-scan base-package="com.robin" />
    <context:property-placeholder location="classpath:jdbc.properties" />
    <bean id="dataSource"
          class="org.apache.commons.dbcp.BasicDataSource"
          destroy-method="close"
          p:driverClassName="${jdbc.driverClassName}"
          p:url="${jdbc.url}"
          p:username="${jdbc.username}"
          p:password="${jdbc.password}" />

    <bean id="sqlSessionFactory"
          class="org.mybatis.spring.SqlSessionFactoryBean"
          p:dataSource-ref="dataSource"
          p:configLocation="classpath:mybatisConfig.xml"
          p:mapperLocations="classpath:com/robin/domain/*.xml" />
          <!--mapperLocations支持扫描是加载SQL映射文件-->

    <!--通过SqlSessionTemplate模板类轻松访问数据库-->
    <bean class="org.mybatis.spring.SqlSessionTemplate">
        <constructor-arg ref="sqlSessionFactory" />
    </bean>

    <!--MapperScannerConfigurer是个神奇的转换器，可以将映射接口直接转换为Sping容器中的Bean-->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer"
          p:sqlSessionFactoryBeanName="sqlSessionFactory"
          p:basePackage="com.robin.dao.mybatis" />

</beans>
```

#### 使用映射接口

MyBatis提供一种可以将SQL映射文件中的映射项通过名称匹配接口进行调用的方法：接口名称和映射命名空间相同，接口方法和映射元素的id相同。如下：

```java
//TuserMybatisDao.java
package com.robin.dao.mybatis;

import com.robin.domain.Tuser;

public interface TuserMybatisDao {
    Tuser getTuser(int userId);
}
```

在spring配置文件中声明bean`org.mybatis.spring.mapper.MapperScannerConfigurer`，它可以将映射接口直接转换为Sping容器中的Bean，如上配置文件

这样就可以在Service中直接注入映射接口的Bean，并且间接调用映射文件的sql语句进行数据库访问操作，如下：

```java
package com.robin.service;

import com.robin.dao.mybatis.TuserMybatisDao;
...

@Service
public class TuserService {
    @Autowired
    private TuserMybatisDao tuserDao;

    public Tuser getTuser(int userId) {
        return tuserDao.getTuser(userId);
    }

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-mybatis.xml");
        TuserService service = (TuserService) ctx.getBean("tuserService");
        Tuser tuser = service.getTuser(2);
        System.out.println(tuser.getUserName());
    }
}
```
