# Spring MVC 常用注解

---

### 1.`@Controller`注解

`@Controller`注解用于标记一个类，使用它的类就是一个SpringMVC Controller对象，即一个控制器。

保证Spring能查找到控制器，需要完成Spring配置文件的编写：

* 使用`<context:component-scan/>`元素，扫描对应包。

---

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

#### Model和ModelAndView

Model的使用：

```java
@RequestMapping(value = "/helloworld")
public String helloWorld(Model model) {
    model.addAttribute("message", "Hello World!");
    return "welcome";
}
```

ModelAndView的使用：

```java
@RequestMapping(value = "/hello")
public ModelAndView hello() {
    logger.info("hello方法 被调用");
    //创建准备返回的ModelAndView对象，该对象包含视图名、模型名称和模型对象
    ModelAndView mv = new ModelAndView();
    //添加模型数据，可以是任意的POJO对象
    mv.addObject("message", "hello world!");
    //设置逻辑视图名,返回的视图路径为/WEB-INF/views/welcome.jsp
    mv.setViewName("welcome");
    return mv;
}
```

---

### 3.`@RequestParam`注解

| 属性         | 类型    | 说明                           |
|:-------------|:--------|:-------------------------------|
| name         | String  | 指定请求头绑定的名称           |
| value        | String  | name属性的别名                 |
| required     | boolean | 指定参数是否必须绑定           |
| defaultValue | String  | 如果没有传递参数而使用的默认值 |

例子：

```java
@RequestMapping(value = "/register", method = RequestMethod.POST)
public String register(
        @RequestParam(value="loginname", defaultValue="World") String loginname,
        @RequestParam("password") String password,
        @RequestParam("username") String username,
        Model model) {
    ...
    return "success";
}
```

---

### 4.`@PathVariable`注解

使用这个注解可以方便的获取请求URL中的动态参数（请求的path）。

例子：

```java
@RequestMapping(value = "/{formName}")
public String loginForm(@PathVariable String formName) {
    return formName;
}
```

---

### 5.`@RequestHeader`注解

将请求的头信息映射到处理方法的参数上。

---

### 6.`@CookieValue`注解

将请求的Cookie映射到处理方法的参数上。

---

### 7.`@SessionAttributes`注解

指定Model中哪些属性需要转存到HttpSession对象中。

如：

```java
@Controller
@RequestMapping("/value")
//将user注入session中，另一种写法：(types={User.class}, value="user")
//设置多个对象到HttpSession:(types={User.class,Dept.class}, value={"user", "dept"})
@SessionAttributes("user")
public class UserController {
    ...
}
```

---

### 8.`@ModelAttribute`注解

将请求参数绑定到Model对象。该注解只支持一个属性`value`，类型为`String`，表示绑定的属性名称。

>被@ModelAttribute注释的方法会在Controller每个方法执行前被执行，因此在一个Controller映射多个URL时，要谨慎使用。

例子：

```java
@Controller
public class ModelAttribute1Controller {
    //已经隐式绑定参数loginname到Model对象中，在login1方法中返回Model对象
    @ModelAttribute("loginname")
    public String userModel1(@RequestParam("loginname") String loginname) {
        return loginname;
    }
    @RequestMapping(value = "/login1")
    public String login1() {
        return "result1";
    }
}
```
