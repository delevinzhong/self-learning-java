import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConnectToMysql {

    private static final Logger LOG = LoggerFactory.getLogger(ConnectToMysql.class);

    public static final String TABLE_NAME = "personal_cost";
    private static final String COST_ITEM = "酸奶";
    private static final String COST = "13.40";
    private static final String COST_TYPE = "食";


    public static void main(String[] args) {

        LOG.info("----------------------------Connect TO MySQL----------------------------");

        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/cost_center?autoReconnect=true&useSSL=false",
                    "root",
                    "root"
            );

            // Create Table if not exists
//            String tableName = "personal_cost";
            createCostTable(connection, TABLE_NAME);

            // Insert new record
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = dateFormat.format(date);
            LOG.info("Current date is {}", currentDate);
//            String costItem = args[0];
//            String costItem = "酸奶";
//            String cost = args[1];
//            String cost = "13.40";
//            String costType = "食";
            insertNewRecord(connection, currentDate, COST_ITEM, COST, COST_TYPE, TABLE_NAME);

//            ResultSet resultSet = queryCostTable(connection, "select * from personal_cost;");
//            LOG.info("Result Set : {}", resultSet );
            connection.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertNewRecord(Connection connection,
                                       String currentDate,
                                       String costItem,
                                       String cost,
                                       String costType,
                                       String tableName) throws SQLException {
        LOG.info("Insert new records");
        String insertQuery = String.format("INSERT INTO `%s`" +
                " VALUES ('%s', '%s', '%s', '%s');", tableName, costItem, cost, costType, currentDate);
        LOG.info("Execute Query : {}", insertQuery);
        Statement stmt = connection.createStatement();
        stmt.execute(insertQuery);
    }

    public static void createCostTable(Connection connection, String tableName) throws SQLException {
        LOG.info("Create {} table", tableName);
//        String createStmt = String.format("CREATE TABLE IF NOT EXISTS `%s`(`current_date` datetime NOT NULL, " +
//                "`cost_items` VARCHAR(20) NOT NULL, " +
//                "`cost` DECIMAL(10,2) NOT NULL, " +
//                "`cost_type` VARCHAR(20) NOT NULL, " +
//                "PRIMARY KEY ( `current_date`, `cost_items`))ENGINE=InnoDB DEFAULT CHARSET=utf8;", tableName);
        String createStmt = String.format("CREATE TABLE IF NOT EXISTS `%s` (  \n" +
                "  `costItem` varchar(20) NOT NULL,  \n" +
                "  `cost` decimal(10,2) NOT NULL,  \n" +
                "  `costType` varchar(4) NOT NULL,  \n" +
                "  `currentDate` date NOT NULL,  \n" +
                "  PRIMARY KEY (`costItem`, `cost`)  \n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;", tableName);

        LOG.info("Execute Query : \n {}", createStmt);
        Statement stmt = connection.createStatement();
        stmt.execute(createStmt);
    }

    public static ResultSet queryCostTable(Connection connection, String query) throws SQLException {
        LOG.info("Execute query {}", query);
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(query);
    }


}
