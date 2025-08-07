package src.advance;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.junit.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


//使用ORM的实现来封装对象和集合

public class JDBCAdvance {

    // 定义一个内部类来封装员工记录
    public class personRecord {
        private int id;
        private String name;
        private double salary;
        private int age;

        public personRecord(int id, String name, double salary, int age) {
            this.id = id;
            this.name = name;
            this.salary = salary;
            this.age = age;
        }
        // 添加setter方法
        public void setId(int id) {
            this.id = id;
        }
                // 添加getter方法
        public int getId() {
            return id;
        }
        

        public String getName() {
            return name;
        }

        public double getSalary() {
            return salary;
        }

        public int getAge() {
            return age;
        }
        @Override
        public String toString() {
            return "ID: " + id + ", Name: " + name + ", Salary: " + salary + ", Age: " + age;
        }
    }   


    @Test
    //使用ORM的实现来封装对象和集合
    public void getAllORMdata () throws Exception{

            //1.注册驱动
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                System.out.println("驱动注册成功！");
            }catch(ClassNotFoundException e){
                System.out.println("驱动注册失败！");
                e.printStackTrace();
            }

            //2.获取对象
            Connection conection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "123456");

            //3. 使用预处理来获取执行sql语句的对象——修改使用PreparedStatement来获取对象，避免SQL注入
            String sql = "SELECT id_Staff, name_Staff, salary_Staff, age_Staff FROM staff WHERE name_Staff = ?";
            PreparedStatement statement = conection.prepareStatement(sql);

            //4. 设置参数,替换字符
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入要查询的员工姓名：");
            String name = scanner.nextLine().trim();
            ResultSet resultSet = statement.executeQuery();// 执行查询并返回结果集


 
            //5. 处理结果集,创建集合和对象存储数据
            List<personRecord> personList = new ArrayList<>();
        
            while(resultSet.next()){ 
                personRecord person = new personRecord(resultSet.getInt("id_Staff"), resultSet.getString("name_Staff"), resultSet.getDouble("salary_Staff"), resultSet.getInt("age_Staff"));
                personList.add(person);
            }
            //6. 输出结果
            while(!personList.isEmpty()){
                System.out.println(personList.remove(0));
            }

            statement.close();
            conection.close();
            scanner.close();
    }

    @Test 
    //测试返回主键回显
    //主要就是实现了添加了一个员工之后，可以知道这个添加的员工的ID在数据库中是多少，方便做一些操作
    public void testReturnPK() throws Exception{
        //1.获取链接
        Connection conn = null;
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","123456");
        }catch(SQLException e){
            System.out.println("获取链接失败！");
            e.printStackTrace();
        }

        //2.预编译语句,并指定返回主键
        String sql = "INSERT INTO staff (name_Staff, salary_Staff, age_Staff) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        //3.建立一个对象来存储数据，然后用这个对象来设置参数
        personRecord newPerson = new personRecord(0, "张强", 5000.00, 30);
        preparedStatement.setString(1, newPerson.getName());
        preparedStatement.setDouble(2, newPerson.getSalary());
        preparedStatement.setInt(3, newPerson.getAge());

        //4.执行SQL语句，并返回主键
        int rowsAffected = preparedStatement.executeUpdate();

        //5.处理结果
        ResultSet generatedKeys = null;
        if(rowsAffected > 0){
            System.out.println("收到数据");

            //这个返回的是一个结果集，放的是单行单列
            generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()){
                int newId = generatedKeys.getInt(1);//获取新插入的记录的ID
                newPerson.setId(newId);//更新对象的ID属性
                System.out.println("员工添加成功，员工ID为：" + newId);
            }else{
                System.out.println("员工添加成功，但无法获取员工ID");
            }
        }

        //6.关闭资源
        if(generatedKeys != null){// 避免结果集是null时空指针异常
            generatedKeys.close();
        }

        preparedStatement.close();
        conn.close();
        




    }

    @Test
    //测试批量插入
    //使用循环来实现插入的操作，好耗时很长
    public void testMoreInsert() throws Exception{ 
        //1.注册驱动，直接跳过

        //2.获取连接
        Connection connection = null;
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "123456");
            System.out.println("获取连接成功！");
        }catch(SQLException e){
            System.out.println("获取连接失败！");
            e.printStackTrace();
        }

        //3.获取执行sql语句的对象
        String sql = "insert into staff values(null,'?',?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        //4.设置参数并执行批量插入
        for(int i = 0; i < 5; i++){
            // 使用预处理语句设置参数
            preparedStatement.setString(1, "测试名字" + i);
            preparedStatement.setDouble(2, 100.0+i);
            preparedStatement.setInt(3, 20+i);
           
            preparedStatement.executeUpdate(); // 执行插入
        }

        //6.关闭连接
        preparedStatement.close();
        connection.close();
    }



    @Test
    //测试批量插入
    //使用的批处理来实现插入的操作，效率更高
    //注意：批处理的操作需要在连接数据库的URL后面追加?rewrite
    public void testBatch() throws Exception{ 
        //1.注册驱动，直接跳过

        //2.获取连接
        Connection connection = null;
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/?testrewriteBatchedStatements=true", "root", "123456");
            System.out.println("获取连接成功！");
        }catch(SQLException e){
            System.out.println("获取连接失败！");
            e.printStackTrace();
        }
            /*
                注意：1、必须在连接数据库的URL后面追加?rewriteBatchedStatements=true，允许批量操作(?这个要加在URL的最后面)
                    2、新增SQL必须用values。且语句最后不要追加;结束
                    3、调用addBatch()方法，将SQL语句进行批量添加操作
                    4、统一执行批量操作，调用executeBatch()
             */        

        //3.获取执行sql语句的对象
        String sql = "insert into staff values(null,'?',?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        //4.设置参数并执行批量插入
        for(int i = 0; i < 5; i++){
            // 使用预处理语句设置参数
            preparedStatement.setString(1, "测试名字" + i);
            preparedStatement.setDouble(2, 100.0+i);
            preparedStatement.setInt(3, 20+i);
           
            preparedStatement.addBatch();// 添加到批处理
        }

        //5.执行批量操作
        preparedStatement.executeBatch();
        System.out.println("批量插入成功！");

        //6.释放资源
        preparedStatement.close();
        connection.close();
        System.out.println("连接已关闭！");


    }



}



