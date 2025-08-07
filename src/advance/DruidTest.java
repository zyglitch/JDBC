package src.advance;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import javax.sql.DataSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.junit.Test;


public class DruidTest { 

    @Test
    /*
    这个是软Druid连接池的测试类
    依赖库还是要导入，但是配置文件的话写在resources目录下的db.properties文件中
    */
    public void testResourcesDruid() throws IOException, Exception {
                //1.创建Properties集合，用于存储外部配置文件的key和value值。
                Properties properties = new Properties();
        
                //2.读取外部配置文件，获取输入流，加载到Properties集合里。
                /*
                    * 第一行：创建一个指向"resources/db.properties"文件的File对象
                    * 第二行：创建文件输入流，用于从文件中读取数据
                    * 第三行：将输入流中的属性配置加载到Properties对象中，以便后续使用
                    */
                File file = new File("resources/db.properties");
                InputStream inputStream = new FileInputStream(file);
                properties.load(inputStream);
        

                //3.基于Properties集合构建DruidDataSource连接池
                DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
        
                //4.通过连接池获取连接对象
                Connection connection = dataSource.getConnection();
                System.out.println(connection);
        
                //5.开发CRUD 
                
             
                //6.回收连接
                connection.close();     
    }




}


