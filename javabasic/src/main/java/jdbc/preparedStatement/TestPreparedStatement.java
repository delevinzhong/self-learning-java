package jdbc.preparedStatement;

import jdbc.utils.JDBCUtils;
import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class TestPreparedStatement {

    public Connection getConnection() throws Exception {
        InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        properties.load(in);
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String url = properties.getProperty("url");
        String driverClass = properties.getProperty("driverClass");
        Class.forName(driverClass);
        return DriverManager.getConnection(url, user, password);
    }

    //添加数据
    @Test
    public void addRecord() throws Exception {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();

            String sql = "INSERT INTO t_user(userId,fullName,userType,addedTime) Values(?,?,?,?)";

            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, 7);
            preparedStatement.setString(2, "Garnett");
            preparedStatement.setString(3, "VIP");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(sdf.format(new Date()));
            preparedStatement.setDate(4, new java.sql.Date(date.getTime()));

            preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        String currentDate = sdf.format(date);
        System.out.println(currentDate);

    }

    // 修改记录
    @Test
    public void testUpdateRecord() {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            // 1. 获取数据库连接
            conn = JDBCUtils.getConnection();
            // 2. 预编译SQL语句，返回PreparedStatement的实例
            String sql = "UPDATE t_user SET userType = ? where userId = ?";
            ps = conn.prepareStatement(sql);
            // 3. 填充占位符
            ps.setString(1, "Normal");
            ps.setInt(2, 6);
            // 4. 执行SQL
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 5. 关闭连接
            JDBCUtils.closeResource(conn, ps);
        }
    }

    @Test
    public void testUpdate() throws Exception {
        String sql = "UPDATE t_user SET userType = ? where userId = ?";
        String userType = "Normal";
        int userId = 7;
        JDBCUtils.update(sql, userType, userId);
    }

    @Test
    public void testQuery() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT userId,fullName,userType,addedTime from t_user where userId = ?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1, 1);
            rs = ps.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt(1);
                String fullName = rs.getString(2);
                String userType = rs.getString(3);
                java.sql.Date addedTime = rs.getDate(4);

                User user = new User(userId, fullName, userType, addedTime);
                System.out.println(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
    }

    @Test
    public void testGenericQuery1() {

        String sql = "SELECT fullName,userType from t_user where fullName = ?";

        User user = JDBCUtils.queryForTableUser(sql, "Kobe");
        System.out.println(user);
    }

    @Test
    public void testGenericQuery2() {

//        String sql = "SELECT fullName fullName, userType userType FROM t_user where fullName = ?";
        String sql = "SELECT fullName fullName, userType userType FROM t_user";
        List<User> users = JDBCUtils.queryForGenericTablesReturnMultipleRecords(User.class, sql);
        for (User user : users) {
            System.out.println(user);
        }

    }
}

