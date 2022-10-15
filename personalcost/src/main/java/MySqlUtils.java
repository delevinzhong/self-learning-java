import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySqlUtils {

    private static final Logger LOG = LoggerFactory.getLogger(MySqlUtils.class);

    public static final String TABLE = "personal_cost";

    public static void insertNewRecord(Connection conn, String currentDate, String costItem, String cost,
                                       String costType, String tableName) throws SQLException {
        LOG.info("Insert new records");
        String insertQuery = String.format("INSERT INTO %s VALUES ('%s', '%s', '%s', '%s');", tableName,
                costItem, cost, costType, currentDate);
        LOG.info("Executing query : {}", insertQuery);
        Statement statement = conn.createStatement();
        statement.execute(insertQuery);
    }

    public static void coreateCostTable(Connection conn, String tableName) throws SQLException {
        LOG.info("Creating {} table", tableName);
        String createStmt = String.format("CREATE TABLE IF NOT EXISTS `%s` (  \n" +
                "  `costItem` varchar(20) NOT NULL,  \n" +
                "  `cost` decimal(10,2) NOT NULL,  \n" +
                "  `costType` varchar(4) NOT NULL,  \n" +
                "  `currentDate` date NOT NULL,  \n" +
                "  PRIMARY KEY (`costItem`, `cost`)  \n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;", tableName);

        LOG.info("Execute Query : \n {}", createStmt);
        Statement statement = conn.createStatement();
        statement.execute(createStmt);
    }

    public static ResultSet queryCostTable(Connection connection, String query) throws SQLException {
        LOG.info("Execute query {}", query);
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(query);
    }
}
