package src;
import java.sql.*;
import java.util.Scanner;

public class JDBCPreparedv2 {
    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");
        // 1. 注册驱动（可省略，JDBC 4.0+会自动加载）
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL驱动未找到！请检查库依赖");
            return;
        }

        // 2. 数据库连接信息
        String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC";
        String user = "root";
        String password = "123456";
        
        // 使用try-with-resources自动关闭资源
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Scanner scanner = new Scanner(System.in, "UTF-8")) {
            
            System.out.println("数据库连接成功！");
            
            // 3. 准备SQL语句
            String sql = "SELECT id_Staff, name_Staff, salary_Staff, age_Staff FROM test.staff WHERE name_Staff = ?";
            
            System.out.print("请输入要查询的员工姓名：");
            String name = scanner.nextLine().trim();  // 去除前后空格
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // 4. 设置参数
                preparedStatement.setString(1, name);
                
                // 5. 执行查询并处理结果
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    boolean found = false;
                    while (resultSet.next()) {
                        found = true;
                        int id = resultSet.getInt("id_Staff");
                        String nameStaff = resultSet.getString("name_Staff");
                        double salary = resultSet.getDouble("salary_Staff");
                        int age = resultSet.getInt("age_Staff");
                        
                        System.out.printf("ID: %d | 姓名: %s | 薪资: %.2f | 年龄: %d%n", 
                                id, nameStaff, salary, age);
                    }
                    
                    if (!found) {
                        System.out.println("未找到员工: " + name);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("数据库操作异常:");
            e.printStackTrace();
        }
    }
}