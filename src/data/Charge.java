package data;

import java.math.BigDecimal;

public record Charge(BigDecimal dailyCharge, boolean weekdayCharge, boolean weekendCharge, boolean holidayCharge) {
}
