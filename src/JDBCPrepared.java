package src;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class JDBCPrepared {
    

    public static void main(String[] args) throws Exception {
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



        //3.获取执行sql语句的对象——修改使用PreparedStatement来获取对象，避免SQL注入，但是一开始初始化的时候要输入SQL语句,如果要有输入的占位符，在创建对象时，要使用?来代替
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT id_Staff,name_Staff,salary_Staff,age_Staff FROM staff WHERE name_Staff =  ?");
        System.out.println("请输入你要查询的人名字：");

        

        //4.把?字符替换了然后执行sql语句,这里的setString的1索引就是第一个？的索引       
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        preparedStatement.setString(1,name);
        ResultSet resultSet = preparedStatement.executeQuery();



        //5.处理结果集 
        try {
            while (resultSet.next()) {
                int id_Staff = resultSet.getInt("id_Staff");
                String name_Staff = resultSet.getString("name_Staff");
                double salary_Staff = resultSet.getDouble("salary_Staff");
                int age_Staff = resultSet.getInt("age_Staff");
                System.out.println(id_Staff + " | " + name_Staff + " | " + salary_Staff + " | " + age_Staff);
            }
        } catch (SQLException e) {
            // Auto-generated catch block
            e.printStackTrace();
        }

        //6.释放资源（先开后关原则）
        resultSet.close();
        preparedStatement.close();
        connection.close();
        scanner.close();

    }

}
