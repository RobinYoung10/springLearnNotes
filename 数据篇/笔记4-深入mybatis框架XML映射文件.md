# 深入MyBatis XML映射文件

---

MyBatis的真正强大之处在于它的映射语句,SQL映射文件常用元素如下:
* select:映射查询语句.
* insert:映射插入语句.
* update:映射更新语句.
* delete:映射删除语句.
* sql: 可被其他语句引用的可重用语句块.
* cache:给定命名空间的缓存配置.
* cache-ref:其他命名空间缓存配置的引用.
* resultMap:最复杂也是最强大的元素,用来描述如何从数据库结果集中加载对象.

---

### select语句

一条简单的select语句:

```xml
<select id="getForum" resultType="Forum" parameterType="int" >
		SELECT 
		 forum_id  forumId,
		 forum_name  forumName,
		 forum_desc  forumDesc
		FROM t_forum 
		WHERE forum_id = #{forumId}
  </select>
```

`#{forumId}`是传递的参数,是一个预处理语句参数,相当于JDBC语句里面的"?"标识.

`resultType`属性表示返回的类型,如果是查询集合的情形,则表示每一项的类型.

`parameterType`表示参数类型.

主要的属性如下图:

![select属性](https://gitee.com/robin10/TuPianCangKu/raw/master/select%E5%B1%9E%E6%80%A7.png)

---

### insert和update语句

简单用法:

```xml
<insert id="addForum" parameterType="Forum">
		INSERT INTO t_forum(forum_id,forum_name,forum_desc)
		VALUES(#{forumId},#{forumName}, #{forumDesc})
</insert>
```

```xml
  <update id="updateForum" parameterType="Forum">
		UPDATE t_forum f
		SET forum_name=#{forumName},forum_desc=#{forumDesc}
		WHERE f.forum_id = #{forumId}
  </update>
```

`select`有的属性都有,还具有下图所示的特有属性:

![insert-update属性](https://gitee.com/robin10/TuPianCangKu/raw/master/insert-update%E5%B1%9E%E6%80%A7.png)

---

### delete语句

简单用法:

```xml
<delete id="deleteUser" parameterType="int">
	delete from tb_user where id = #{id}
</delete>
```

---

### ResultMap语句

`resultMap`元素是MyBatis中最强大的元素.他的作用是告诉MyBatis从结果集中取出的数据转换成开发者所需要的对象.

简单用法:

```xml
<mapper namespace="com.robin.mybatis.User">
    <!--type属性表示实际返回的类型-->
    <resultMap id="userResultMap" type="com.robin.mybatis.User" >
        <id property="id" column="id" />
        <result property="name" column="name" />
        <result property="sex" column="sex" />
        <result property="age" column="age" />
    </resultMap>
    <!--resultMap="userResultMap"表示引用上面的resultMap进行数据表和返回类型对象的映射-->
    <!--实际返回的类型就是resultMap的type-->
    <select id="selectUser" resultMap="userResultMap">
        SELECT * FROM tb_user
    </select>
</mapper>
```

`resultMap`元素常用的属性如下:

* id:`resultMap`的唯一标识符.
* type:`resultMap`实际返回的类型.

它有两个子元素`id`和`result`,其中`id`表示数据库主键,`result`表示普通列.这两个元素都有两个属性,其中`column`
属性表示数据库的列名,`property`表示数据库映射到返回类型的域.

在配置文件配置好映射后,就可以使用select查询了:

```java
        List<User> userList = session.selectList("com.robin.mybatis.User.selectUser");
        for(User user : userList) {
            System.out.println(user.getName());
        }
        session.commit();
        session.close();
```

---

#### 使用resultMap关联映射实现多表查询

两表结构如下:

clazz表

![clazz表](https://gitee.com/robin10/TuPianCangKu/raw/master/clazz%E8%A1%A8.png)

student表

![student表](https://gitee.com/robin10/TuPianCangKu/raw/master/student%E8%A1%A8.png)

在创建student表时通过语句`foreign key (clazz_id) references tb_clazz(id)`关联clazz表.

#### (一)通过student查询clazz

Student.java:

```java
public class Student {
    private Integer id;
    private String name;
    private String sex;
    private int age;
    private Clazz clazz;//关联clazz对象
    ...
}
```

映射文件(Student.xml):

```xml
<mapper namespace="com.robin.mybatis.StudentMapper">
    <resultMap id="studentResultMap" type="com.robin.mybatis.Student">
        <id property="id" column="id" />
        <result property="name" column="name" />
        <result property="sex" column="sex" />
        <result property="age" column="age" />
        <!--关联映射-->
        <association property="clazz" column="clazz_id" javaType="com.robin.mybatis.Clazz" select="selectClazzWithId" />
    </resultMap>
    <select id="selectClazzWithId" resultType="com.robin.mybatis.Clazz">
        SELECT * FROM tb_clazz where id = #{id}
    </select>
    <select id="selectStudent" resultMap="studentResultMap">
        SELECT * FROM tb_student
    </select>
</mapper>
```

`<association.../>`元素属性如下:
* property:表示返回的属性名,即Student的一个属性名(clazz).
* column:表示数据库表的列名.
* javaType:表示该属性对应的类型.
* select:执行一条查询语句,将查询到的数据封装到property所代表的类的属性中.

**执行过程:**

1. 调用id为"selectStudent"的`select`元素,查询到所有学生数据,因为用到了`resultMap`属性,在每一条学生数据中,都会
执行`<association.../>`关联映射.

2. 在关联映射中,`column`为"clazz\_id"的列的数据传递到`select`元素id为"selectClazzWithId"的查询语句中

3. 执行通过id查询,参数为"clazz\_id",返回一个clazz数据表的数据,并绑定在`Student`类的`clazz`属性中.

4. 查询完毕,返回一个类型为`Student`的集合.

#### (二)通过clazz查询student

Clazz.java:

```java
public class Clazz {
    private Integer id;
    private String code;
    private List<Student> students;
    ...
}
```

映射文件(Clazz.xml):

```xml
<mapper namespace="com.robin.mybatis.ClazzMapper">
    <resultMap id="clazzResultMap" type="com.robin.mybatis.Clazz">
        <id property="id" column="id" />
        <result property="code" column="code" />
	<!--collection属性是一个集合-->
        <collection property="students" javaType="ArrayList" column="id"
                    ofType="com.robin.mybatis.Student" select="selectStudentWithId" />
    </resultMap>
    <select id="selectStudentWithId" resultType="com.robin.mybatis.Student">
        SELECT * FROM tb_student where clazz_id = #{id}
    </select>
    <select id="selectClazz" resultMap="clazzResultMap">
        SELECT * FROM tb_clazz
    </select>
</mapper>
```

`<collection.../>`元素属性如下:
* property:表示返回的属性名(Student).
* javaType:表示该属性对应的类型.
* column:表示以哪个列作为参数传递到后面进行的select查询.
* ofType:表示集合当中的类型.
* select:查询语句.

执行过程跟上列相似.

---

项目地址:[project](https://gitee.com/robin10/Springlearnnotes/tree/master/%E6%95%B0%E6%8D%AE%E7%AF%87/project/chapter8)
