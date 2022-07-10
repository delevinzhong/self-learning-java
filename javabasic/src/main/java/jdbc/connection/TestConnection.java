package jdbc.connection;

import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class TestConnection {

    @Test
    public void testConnection1() throws SQLException {

        Driver driver = new com.mysql.jdbc.Driver();
        String url = "jdbc:mysql://localhost:3306/cost_center";
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "root@1234");

        Connection conn = driver.connect(url, properties);
        System.out.println(conn);
    }

    @Test
    public void testConnection2() throws Exception {
        Class driverClass = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) driverClass.newInstance();

        String url = "jdbc:mysql://localhost:3306/cost_center";
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "root@1234");

        Connection conn = driver.connect(url, properties);
        System.out.println(conn);
    }

    @Test
    public void testConnection3() throws Exception {
        Class driverClass = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) driverClass.newInstance();

        String url = "jdbc:mysql://localhost:3306/cost_center";
        String user = "root";
        String password = "root@1234";

        DriverManager.registerDriver(driver);

        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println(conn);
    }

    @Test
    public void testConnection4() throws Exception{
        Class.forName("com.mysql.jdbc.Driver");

        String url = "jdbc:mysql://localhost:3306/cost_center";
        String user = "root";
        String password = "root@1234";

        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println(conn);
    }

    @Test
    public void testConnection5() throws Exception{
        // 读取配置文件信息
        InputStream inputStream = TestConnection.class.getClassLoader().getResourceAsStream("jdbc.properties");

        Properties props = new Properties();
        props.load(inputStream);

        String user = props.getProperty("user");
        String password = props.getProperty("password");
        String url = props.getProperty("url");
        String driverClass = props.getProperty("driverClass");

        Class.forName(driverClass);

        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println(conn);
    }
}
