import data.Tool;

import java.util.Calendar;
import java.util.Date;

public class RentalAgreement {
    private final Tool tool;
    private final int renatDays;
    private final Calendar checkOutDate;
    final float discount;

    public RentalAgreement(Tool tool, int rentalDays, float discount, Calendar checkOutDate){
        this.tool = tool;
        this.renatDays = rentalDays;
        this.checkOutDate = checkOutDate;
        this.discount = discount;
    }

}
