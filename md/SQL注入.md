SQL 注入是一种常见的安全漏洞，攻击者可以通过在输入中插入恶意 SQL 代码来操控数据库查询。下面是一个简单的 SQL 注入示例，以及如何防范它。

---

### 1. **存在 SQL 注入的代码**

假设你有一个用户登录功能，代码如下：

```java
String query = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'";
Statement statement = connection.createStatement();
ResultSet resultSet = statement.executeQuery(query);
```

这里的 `username` 和 `password` 是用户从页面输入的值。假设攻击者输入以下内容：

- `username`: `' OR '1'='1`
- `password`: `' OR '1'='1`

最终生成的 SQL 语句会变成：

```sql
SELECT * FROM users WHERE username = '' OR '1'='1' AND password = '' OR '1'='1'
```

这条 SQL 语句的含义是：

- `'1'='1'` 是一个永远为真的条件。
- 攻击者通过输入构造了始终为真的条件，绕过了登录验证，从而可以以任意用户身份登录系统。

---

### 2. **更危险的 SQL 注入**

攻击者甚至可以删除整个表。例如，输入以下内容：

- `username`: `'; DROP TABLE users; --`

生成的 SQL 语句会变成：

```sql
SELECT * FROM users WHERE username = ''; DROP TABLE users; -- AND password = '...'
```

这会导致 `users` 表被删除，造成数据丢失。

---

### 3. **如何防止 SQL 注入**

为了防止 SQL 注入，应该使用 **预编译语句（PreparedStatement）**，而不是拼接字符串。修改后的代码如下：

```java
String query = "SELECT * FROM users WHERE username = ? AND password = ?";
PreparedStatement preparedStatement = connection.prepareStatement(query);
preparedStatement.setString(1, username);
preparedStatement.setString(2, password);
ResultSet resultSet = preparedStatement.executeQuery();
```

使用 `PreparedStatement` 的好处是：

- 用户输入会被自动转义，不会被当作 SQL 代码执行。
- 即使用户输入了恶意内容，也会被当作字符串处理，而不是直接拼接到 SQL 语句中。

---

### 4. **总结**

SQL 注入是一种通过恶意输入操控数据库查询的攻击方式。为了防止 SQL 注入：

- **永远不要直接拼接 SQL 语句。**
- 使用 `PreparedStatement` 来处理用户输入。
- 对用户输入进行验证和过滤（如限制输入长度、过滤特殊字符等）。

这样可以有效保护数据库安全，避免数据被篡改或删除。