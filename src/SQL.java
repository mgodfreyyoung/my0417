import java.math.BigDecimal;
import java.sql.*;

public class SQL {

    void createSqlDatabase() {
        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:src/SQLiteDB/inventory.db");

            var statement = connection.prepareStatement("create table if not exists tool (id INTEGER PRIMARY KEY, charge_id int, tool_code string, tool_type string, brand string)");
            statement.executeUpdate();
            statement = connection.prepareStatement("create table if not exists charge (id INTEGER PRIMARY KEY, daily_charge DECIMAL(10,2), weekday_charge bit, weekend_charge bit, holiday_charge bit)");
            statement.executeUpdate();

            populateSQL(connection);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    void populateSQL(Connection connection) throws SQLException {

        var id = insertIntoCharge(connection, new BigDecimal("1.99"), true, true, true);
        insertIntoTool(connection, id, "CNNS", "Chainsaw", "Stihl");

        id = insertIntoCharge(connection, new BigDecimal("1.49"), true, false, true);
        insertIntoTool(connection, id, "LADW", "Ladder", "Werner");

        id = insertIntoCharge(connection, new BigDecimal("2.99"), true, false, false);
        insertIntoTool(connection, id, "JAKD", "JakeHammer", "DeWalt");
        insertIntoTool(connection, id, "JAKR", "JakeHammer", "Ridgid");

        var stat = connection.prepareStatement("select * from tool");

        var rs = stat.executeQuery();
        while (rs.next()) {
            // read the result set
            System.out.println("tool_code = " + rs.getString("tool_code"));
            System.out.println("tool_type = " + rs.getString("tool_type"));
            System.out.println("brand = " + rs.getString("brand"));
            System.out.println("id = " + rs.getInt("ID"));
            System.out.println("charge_id = " + rs.getInt("charge_id"));

        }
    }

    private void insertIntoTool(Connection connection, long id, String toolCode, String toolThype, String brand) throws SQLException {
        String insertSQL = "INSERT INTO tool (charge_id, tool_code, tool_type, brand) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
        preparedStatement.setLong(1, id);
        preparedStatement.setString(2, toolCode);
        preparedStatement.setString(3, toolThype);
        preparedStatement.setString(4, brand);

        preparedStatement.executeUpdate();
    }

    private long insertIntoCharge(Connection connection, BigDecimal dailyCharge, boolean weekdayCharge, boolean weekendCharge, boolean holidayCharge) throws SQLException {
        String insertSQL = "INSERT INTO charge (daily_charge, weekday_charge, weekend_charge, holiday_charge) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
        preparedStatement.setBigDecimal(1, dailyCharge);
        preparedStatement.setBoolean(2, weekdayCharge);
        preparedStatement.setBoolean(3, weekendCharge);
        preparedStatement.setBoolean(4, holidayCharge);

        preparedStatement.executeUpdate();

        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        return generatedKeys.getLong(1);
    }

}

