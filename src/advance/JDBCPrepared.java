package src.advance;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;

import java.beans.ParameterDescriptor;
import java.lang.Thread.State;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


//使用ORM的实现来封装对象和集合


public class JDBCPrepared {

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

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Salary: " + salary + ", Age: " + age;
    }
    
}




@Test
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




    }
}