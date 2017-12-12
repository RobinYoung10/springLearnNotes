# 使用Spring JDBC

### 一、几个重要的类

1. JdbcTemplate
JdbcTemplate用于完成数据访问操作

2. KeyHolder
org.springframework.support.KeyHolder是一个回调接口，Spring使用它来保存新增记录对应的主键。

3. RowCallbackHandler
也是一个回调接口，通过该接口可以定义如何从结果集中获取数据。仅有一个方法：

```java
void processRow(ResultSet rs) throws SQLException
```

4. RowMapper<T>
该接口定义结果集映射逻辑。在结果集为多行记录时，该接口跟容易使用。仅有一个接口方法：

```java
T mapRow(ResultSet rs, int rowNum)
```

### 二、Spring配置文件的编写

1. 定义DataSource
2. 定义JdbcTemplate

如下所示：
```xml
<context:property-placeholder location="classpath:jdbc.properties" />
<bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource"
  destroy-method="close"
  p:driverClassName="${jdbc.driverClassName}"
  p:url="${jdbc.url}"
  p:username="${jdbc.username}"
  p:password="${jdbc.password}" />

<bean id="jdbcTemplate"
      class="org.springframework.jdbc.core.JdbcTemplate"
      p:dataSource-ref="dataSource"/>
```

### 三、更改数据

JdbcTemplate提供了若干个`update()`方法，允许对数据表进行更改和删除操作。

```java
//插入数据后返回 自增主键值
public int returnKeyHolederInsert(final String username, final String password) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(new PreparedStatementCreator() {
        public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
            PreparedStatement ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, username);
            ps.setString(2, password);
            return ps;
        }
    }, keyHolder);
    return keyHolder.getKey().intValue();
}
```

### 四、批量操作

```java
/**
 * 批量更新数据
 *
 * @param forums
 */
public void addForums(final List<Forum> forums) {
  final String sql = "INSERT INTO t_forum(forum_name,forum_desc) VALUES(?,?)";
  jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
    public int getBatchSize() {
      return forums.size();
    }

    public void setValues(PreparedStatement ps, int index)
        throws SQLException {
      Forum forum = forums.get(index);
      ps.setString(1, forum.getForumName());
      ps.setString(2, forum.getForumDesc());
    }
  });
}
```

### 五、查询数据
使用RowCallbackHandler或者RowMapper
```java
/**
 * 根据ID获取Forum对象
 *
 * @param forumId
 * @return
 */
public Forum getForum(final int forumId) {
  String sql = "SELECT forum_name,forum_desc FROM t_forum WHERE forum_id=?";
  final Forum forum = new Forum();

  jdbcTemplate.query(sql, new Object[] { forumId },
      new RowCallbackHandler() {
        public void processRow(ResultSet rs) throws SQLException {
          forum.setForumId(forumId);
          forum.setForumName(rs.getString("forum_name"));
          forum.setForumDesc(rs.getString("forum_desc"));
        }
      });
  return forum;
}
```

```java
public List<Forum> getForums(final int fromId, final int toId) {
  String sql = "SELECT forum_id,forum_name,forum_desc FROM t_forum WHERE forum_id between ? and ?";
  // 方法1：使用RowCallbackHandler接口
  /*
   * final List<Forum> forums = new ArrayList<Forum>();
   * jdbcTemplate.query(sql,new Object[]{fromId,toId},new
   * RowCallbackHandler(){ public void processRow(ResultSet rs) throws
   * SQLException { Forum forum = new Forum();
   * forum.setForumId(rs.getInt("forum_id"));
   * forum.setForumName(rs.getString("forum_name"));
   * forum.setForumDesc(rs.getString("forum_desc")); forums.add(forum);
   * }}); return forums;
   */

  return jdbcTemplate.query(sql, new Object[] { fromId, toId },
      new RowMapper<Forum>() {
        public Forum mapRow(ResultSet rs, int index)
            throws SQLException {
          Forum forum = new Forum();
          forum.setForumId(rs.getInt("forum_id"));
          forum.setForumName(rs.getString("forum_name"));
          forum.setForumDesc(rs.getString("forum_desc"));
          return forum;
        }
      });

}
```
