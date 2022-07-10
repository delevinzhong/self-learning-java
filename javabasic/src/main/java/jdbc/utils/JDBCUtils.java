package jdbc.utils;

import jdbc.preparedStatement.User;
import jdk.nashorn.internal.scripts.JD;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JDBCUtils {

    public static Connection getConnection() throws Exception {
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

    public static void closeResource(Connection conn, Statement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeResource(Connection conn, Statement ps, ResultSet rs) {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //通用更新
    public static void update(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            // 1. 创建数据库连接
            conn = JDBCUtils.getConnection();
            // 2.预编译SQL语句
            ps = conn.prepareStatement(sql);
            // 3. 填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            // 4. 执行
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 5. 关闭连接
            JDBCUtils.closeResource(conn, ps);
        }

    }

    //针对某一个表的通用查询
    public static User queryForTableUser(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();

            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //从元数据中获取列数
            int columnCount = rsmd.getColumnCount();
            if (rs.next()) {
                User user = new User();
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    // 从元数据中获取列名
                    String columnName = rsmd.getColumnName(i + 1);

                    //通过反射机制，给user对象指定的columnName赋值columnValue
                    Field field = User.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(user, columnValue);
                }
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }

        return null;
    }

    // 针对某个表的通用查询二， 返回一条记录
    public static User queryForTables(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();

            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //从元数据中获取列数
            int columnCount = rsmd.getColumnCount();
            if (rs.next()) {
                User user = new User();
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    // 从元数据中获取列名
                    //String columnName = rsmd.getColumnName(i + 1);
                    // 从元数据中获取列Label
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //通过反射机制，给user对象指定的columnName赋值columnValue
                    Field field = User.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(user, columnValue);
                }
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }

    // 针对所有表的通用查询一， 返回一条记录
    public static <T> T queryForGenericTablesReturnOneRecord(Class<T> tClass, String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //从元数据中获取列数
            int columnCount = rsmd.getColumnCount();
            if (rs.next()) {
                T t = tClass.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    // 从元数据中获取列名
                    //String columnName = rsmd.getColumnName(i + 1);
                    // 从元数据中获取列Label
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //通过反射机制，给user对象指定的columnName赋值columnValue
                    Field field = tClass.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }

    // 针对所有表的通用查询二， 返回多条记录
    public static <T> List<T> queryForGenericTablesReturnMultipleRecords(Class<T> tClass, String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //从元数据中获取列数
            int columnCount = rsmd.getColumnCount();
            ArrayList<T> list = new ArrayList<>();
            while (rs.next()) {
                T t = tClass.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    // 从元数据中获取列名
                    //String columnName = rsmd.getColumnName(i + 1);
                    // 从元数据中获取列Label
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //通过反射机制，给user对象指定的columnName赋值columnValue
                    Field field = tClass.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }

    // 操作Blob类型的数据
    @Test
    public void testBlobQuery() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT userId, fullName, userType, addedTime, photo from t_user where userId = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, 1);
            rs = ps.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("userId");
                String fullName = rs.getString("fullName");
                String userType = rs.getString("userType");
                Date addedTime = rs.getDate("addedTime");
                User user = new User(userId, fullName, userType, addedTime);
                System.out.println(user);
                // 将Blob类型的字段下载下来，以文件方式保存在本地
                Blob photo = rs.getBlob("photo");
                is = photo.getBinaryStream();
                fos = new FileOutputStream("output.jpg");
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
            try {
                is.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 使用PreparedStatement批量插入数据, 方式一
    @Test
    public void testPreparedStatementInsertMultipleData() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "insert into goods(name) values(?)";
            ps = conn.prepareStatement(sql);
            long start = System.currentTimeMillis();
            for (int i = 0; i < 20000; i++) {
                ps.setObject(1, "name_" + i);
                ps.execute();
            }
            long end = System.currentTimeMillis();
            System.out.println("费时 ： " + (end - start));
        } catch (Exception e ){
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
    }
    // 使用PreparedStatement批量插入数据, 方式二
    @Test
    public void testPreparedStatementInsertMultipleData3() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            // 设置不允许自动提交
            conn.setAutoCommit(false);
            String sql = "insert into goods(name) values(?)";
            ps = conn.prepareStatement(sql);
            long start = System.currentTimeMillis();
            for (int i = 1; i <= 2000000; i++) {
                ps.setObject(1, "name_" + i);
                // 将sql攒为多个batch
                ps.addBatch();
                if (i % 500 == 0) {
                    // 执行batch
                    ps.executeBatch();
                    // 清除已有batch
                    ps.clearBatch();
                }
            }
            conn.commit();
            long end = System.currentTimeMillis();
            System.out.println("费时 ： " + (end - start));
        } catch (Exception e ){
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
    }

    // 使用PreparedStatement批量插入数据, 方式三
    @Test
    public void testPreparedStatementInsertMultipleData2() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "insert into goods(name) values(?)";
            ps = conn.prepareStatement(sql);
            long start = System.currentTimeMillis();
            for (int i = 1; i <= 2000000; i++) {
                ps.setObject(1, "name_" + i);
                // 将sql攒为多个batch
                ps.addBatch();
                if (i % 500 == 0) {
                    // 执行batch
                    ps.executeBatch();
                    // 清除已有batch
                    ps.clearBatch();
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("费时 ： " + (end - start));
        } catch (Exception e ){
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
    }
}
