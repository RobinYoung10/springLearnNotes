# Spring MVC 常用注解

---

### 1.`@Controller`注解

`@Controller`注解用于标记一个类，使用它的类就是一个SpringMVC Controller对象，即一个控制器。

保证Spring能查找到控制器，需要完成Spring配置文件的编写：

* 使用`<context:component-scan/>`元素，扫描对应包。

### 2.`@RequestMapping`注解

用于指示Spring用哪一个类或方法来处理请求动作，该注解可用于类或方法。

#### 几个重要属性：

（1）value属性

即请求的路径。

（2）method属性

指示该方法处理哪些HTTP请求方式。

（3）consumes属性

指示处理请求的提交内容类型（Content-Type）。

如：`@RequestMapping(value = "/hello", method = RequestMethod.POST, consumes = "application/json")`

（4）produces属性

指定返回的内容类型。

如：`@RequestMapping(value = "/hello", method = RequestMethod.POST, produces = "application/json")`

（5）params属性

指定request中必须包含某些参数值时，才让该方法处理。

（6）headers属性

指定request中必须包含某些指定的header值。

#### 请求处理方法可出现的参数类型

![](https://gitee.com/robin10/Springlearnnotes/raw/master/spring-mvc%E7%AF%87/image/1.png)

![](https://gitee.com/robin10/Springlearnnotes/raw/master/spring-mvc%E7%AF%87/image/2.png)

#### 请求处理方法可返回的类型

![](https://gitee.com/robin10/Springlearnnotes/raw/master/spring-mvc%E7%AF%87/image/3.png)
