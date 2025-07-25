package src;
import java.sql.*;
import org.junit.Test;


public class JDBCOperation {

    @Test
    public void test() throws Exception {
        // 1. 注册驱动
        Class.forName("com.mysql.jdbc.Driver");

        // 2. 获取连接
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "123456");

        // 3. 创建PreparedStatement对象
        String sql = "SELECT * FROM test.staff";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        // 4. 执行查询
        ResultSet rs = pstmt.executeQuery();

        // 5. 处理结果集
        while (rs.next()) { 
            int id = rs.getInt("id_Staff");
            String name = rs.getString("name_Staff");
            double salary = rs.getDouble("salary_Staff");
            int age = rs.getInt("age_Staff");

            System.out.printf("ID: %d, Name: %s, Salary: %.2f, Age: %d%n", id, name, salary, age);
        }
        
        // 6. 关闭资源
        rs.close();
        pstmt.close();
        conn.close();


    }
}


