import data.Charge;
import data.Tool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Calendar;

public class CheckOut {
    RentalAgreement rentalAgreement;
    final String toolCode;
    final int rentalDayCount;
    final float discountPercentage;
    final Calendar checkOutDate;

    public CheckOut(String toolCode, int rentalDayCount, int discountPercentage, Calendar checkOutDate) throws Exception {

        // validate rental day count and discount percentage...
        validateRentalDayCount(rentalDayCount);
        validateDiscountPercentage(discountPercentage);

        this.toolCode = toolCode;
        this.rentalDayCount = rentalDayCount;
        this.discountPercentage = discountPercentage;
        this.checkOutDate = checkOutDate;

        createRentalAgreement(queryForTool(toolCode));
    }

    public Tool queryForTool(String toolCode) throws Exception {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + SQL.pathToDatabase);

            var statement = connection.prepareStatement("select * from tool join charge on tool.charge_id = charge.id where tool_code = ? ");
            statement.setString(1, toolCode);
            var rs = statement.executeQuery();
            if (rs.next()) {
                return new Tool(new Charge(rs.getBigDecimal("daily_charge"), rs.getBoolean("weekday_charge"), rs.getBoolean("weekend_charge"), rs.getBoolean("holiday_charge")), rs.getLong("charge_id"), toolCode, rs.getString("tool_type"), rs.getString("brand"));
            }
        } catch (SQLException e) {
            throw new Exception(e);
        }
        throw new Exception("Tool not found, is tool id valid?");
    }


    void createRentalAgreement(Tool tool) {
        rentalAgreement = new RentalAgreement(tool, rentalDayCount, discountPercentage, checkOutDate);
    }

    // validate that the rental day count is at least one or greater
    // if not throw back up to calling method so it can message user properly.
    public void validateRentalDayCount(int rentalDayCount) throws Exception {
        if (rentalDayCount < 1) {
            throw new Exception("Rental day count is not 1 or greater. Please enter a value for number of rental days that is at least 1.");
        }
    }

    // validate that the discount percentage is from 0 to 100.
    public void validateDiscountPercentage(float discountPercentage) throws Exception {
        if (discountPercentage < 0.0 || discountPercentage > 100.0) {
            throw new Exception("Discount percentage is not in the range of 0 - 100.  Please enter a value from 0 to 100.");
        }
    }

}
