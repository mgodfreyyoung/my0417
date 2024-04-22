import data.Charge;
import data.Tool;

import java.io.File;
import java.math.BigDecimal;
import java.sql.*;

public class SQL {
    final static String pathToDatabase = "src/SQLiteDB/inventory.db";

    void createSqlDatabase() {

        File databaseFile = new File(pathToDatabase);
        if (databaseFile.exists()) {
            return;
        }

        try {
            // create a database connection
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + pathToDatabase);

            // create tables
            var statement = connection.prepareStatement("create table if not exists tool (id INTEGER PRIMARY KEY, charge_id int, tool_code string, tool_type string, brand string)");
            statement.executeUpdate();
            statement = connection.prepareStatement("create table if not exists charge (id INTEGER PRIMARY KEY, daily_charge DECIMAL(10,2), weekday_charge bit, weekend_charge bit, holiday_charge bit)");
            statement.executeUpdate();

            populateSQL(connection);

            connection.close();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    void populateSQL(Connection connection) throws SQLException {

        var chargeID = insertIntoCharge(connection, new Charge(new BigDecimal("1.49"), true, false, true));
        insertIntoTool(connection, new Tool(chargeID, "CHNS", "Chainsaw", "Stihl"));

        chargeID = insertIntoCharge(connection, new Charge(new BigDecimal("1.99"), true, true, false));
        insertIntoTool(connection, new Tool(chargeID, "LADW", "Ladder", "Werner"));

        chargeID = insertIntoCharge(connection, new Charge(new BigDecimal("2.99"), true, false, false));
        insertIntoTool(connection, new Tool(chargeID, "JAKD", "JakeHammer", "DeWalt"));
        insertIntoTool(connection, new Tool(chargeID, "JAKR", "JakeHammer", "Ridgid"));

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

    private void insertIntoTool(Connection connection, Tool tool) throws SQLException {
        String insertSQL = "INSERT INTO tool (charge_id, tool_code, tool_type, brand) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
        preparedStatement.setLong(1, tool.chargeID);
        preparedStatement.setString(2, tool.toolCode);
        preparedStatement.setString(3, tool.toolType);
        preparedStatement.setString(4, tool.brand);

        preparedStatement.executeUpdate();
    }

    private long insertIntoCharge(Connection connection, Charge charge) throws SQLException {
        String insertSQL = "INSERT INTO charge (daily_charge, weekday_charge, weekend_charge, holiday_charge) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
        preparedStatement.setBigDecimal(1, charge.dailyCharge());
        preparedStatement.setBoolean(2, charge.weekdayCharge());
        preparedStatement.setBoolean(3, charge.weekendCharge());
        preparedStatement.setBoolean(4, charge.holidayCharge());

        preparedStatement.executeUpdate();

        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        return generatedKeys.getLong(1);
    }

}

