#Spring MVC初体验(基于xml和注解配置bean)

---

### 一、Spring MVC的DispatcherServlet

在许多的MVC架构中，都包含一个用于调度控制的Servlet。在Spring MVC中，负责这个任务的是DispatcherServlet，所有的请求都围绕它来分派请求。

在web.xml中配置DispatcherServlet:

```xml
<!--web.xml-->
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">
  <display-name>Archetype Created Web Application</display-name>

  <!--将DispatcherServlet用作前端控制器-->
  <servlet>
    <servlet-name>springmvc</servlet-name>
    <servlet-class>
      org.springframework.web.servlet.DispatcherServlet
    </servlet-class>
    <init-param>
      <!--声明当前servlet的参数信息-->
      <!--即springmvc配置文件-->
      <param-name>contextConfigLocation</param-name>
      <param-value>/WEB-INF/springmvc-config.xml</param-value>
    </init-param>
    <!--在web应用启动时立刻加载Servlet-->
    <load-on-startup>1</load-on-startup>
  </servlet>
  <!--Servlet映射声明，监听当前域的所有请-->
  <servlet-mapping>
    <servlet-name>springmvc</servlet-name>
    <url-pattern>/</url-pattern>
  </servlet-mapping>

</web-app>
```

---

### 二、编写基于注解的控制器(Controller)

这是一个基于注解的控制器，用来处理映射“/hello”请求：

```java
//HelloController.java
package com.robin.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


/**
 * HelloController是一个基于注解的控制器
 * 可以处理多个的请求动作
 */
@Controller
public class HelloController {
    private static final Log logger = LogFactory.getLog(HelloController.class);


    @RequestMapping("/hello")
    public ModelAndView hello() {
        logger.info("hello方法 被调用");
        //创建准备返回的ModelAndView对象，该对象包含视图名、模型名称和模型对象
        ModelAndView mv = new ModelAndView();
        //添加模型数据，可以是任意的POJO对象
        mv.addObject("message", "hello world!");
        //设置逻辑视图名,返回的视图路径为/WEB-INF/views/welcome.jsp
        mv.setViewName("/WEB-INF/views/welcome.jsp");
        return mv;
    }
}
```

---

### 三、Spring MVC配置文件

基于xml的配置文件：

```xml
<!--springmvc-config.xml-->
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">

    <context:component-scan base-package="com.robin" />

    <!--配置annotation类型的处理映射器-->
    <bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping" />

    <!--配置annotation类型的处理器适配器-->
    <bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter" />

    <!--视图解析器-->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver" />

</beans>
```

---

### 四、编写view页面

/WEB-INF/views/welcome.jsp：

```html
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Welcome</title>
</head>
<body>
    <h1>${requestScope.message}</h1>
</body>
</html>
```
