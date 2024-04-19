import data.Tool;

import java.math.BigDecimal;
import java.util.Calendar;

public class RentalAgreement {
    private final Tool tool;
    private final int renatDays;
    private final Calendar checkOutDate;
    final float discount;

    public Calendar dueDate;
    public int chargeDays;


    public RentalAgreement(Tool tool, int rentalDays, float discount, Calendar checkOutDate){
        this.tool = tool;
        this.renatDays = rentalDays;
        this.checkOutDate = checkOutDate;
        this.discount = discount;

        this.dueDate = calculateDueDate(rentalDays,checkOutDate);

        this.chargeDays = calculateChargeDays(tool);
    }

    private int calculateChargeDays(Tool tool) {
        int chargeDays = 0;

        return chargeDays;
    }


    Calendar calculateDueDate(int rentalDays, Calendar checkOutDate)
    {
        Calendar dueDate = (Calendar) checkOutDate.clone();
        dueDate.add(Calendar.DAY_OF_MONTH, rentalDays);
        return dueDate;
    }
}
