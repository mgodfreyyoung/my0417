package data;

import java.math.BigDecimal;

public class Charge {

    public Charge(BigDecimal dailyCharge, boolean weekdayCharge, boolean weekendCharge, boolean holidayCharge) {
        this.dailyCharge = dailyCharge;
        this.weekdayCharge = weekdayCharge;
        this.weekendCharge = weekendCharge;
        this.holidayCharge = holidayCharge;

    }

    final BigDecimal dailyCharge;
    final boolean weekdayCharge;
    final boolean weekendCharge;
    final boolean holidayCharge;
}
