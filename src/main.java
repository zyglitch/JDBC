package src;
import java.sql.Statement;
import java.lang.Thread.State;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


public class main {
    



    public static void main(String[] args) throws SQLException {
        //1.注册驱动
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("驱动注册成功！");
        }catch(ClassNotFoundException e){
            System.out.println("驱动注册失败！");
            e.printStackTrace();
        }

        //2.获取对象
        String url ="jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "123456";

        Connection connection = null;
        try{
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("获取对象成功！");
        }catch(SQLException e){
            System.out.println("获取对象失败！");
            e.printStackTrace();
        }            

        //3.获取执行sql语句的对象
        Statement statement = null;
        try{
            statement = connection.createStatement();
        }catch(SQLException e){ 

        }

        
        //4.编写SQL语句并且执行
        String sql = "select * from staff";
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(sql); // 执行查询
        } catch (SQLException e) {
            System.out.println("执行SQL查询失败！");
            e.printStackTrace();
        }



        //5.处理结果集
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id_Staff");
                String name = resultSet.getString("name_Staff");
                double salary = resultSet.getDouble("salary_Staff");
                int age = resultSet.getInt("age_Staff");
                System.out.println(id + " | " + name + " | " + salary + " | " + age);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //6.释放资源（先开后关原则）
        resultSet.close();
        statement.close();
        connection.close();

    }

}
