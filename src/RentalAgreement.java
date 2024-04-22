import data.Tool;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;


public class RentalAgreement {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

    private final LocalDate checkOutDate;
    private final LocalDate dueDate;

    private final BigDecimal preDiscountAmount;
    private final BigDecimal discountAmount;
    private final BigDecimal finalCharge;

    private final Tool tool;
    private final int rentalDays;
    private final int discount;

    private long numberOfChargeDays = 0;

    public RentalAgreement(Tool tool, int rentalDays, int discount, LocalDate checkOutDate) {
        this.tool = tool;
        this.rentalDays = rentalDays;
        this.discount = discount;
        this.checkOutDate = checkOutDate;
        this.dueDate = calculateDueDate(rentalDays);

        calculateChargeDays(tool);

        preDiscountAmount = tool.charge.dailyCharge().multiply(new BigDecimal(numberOfChargeDays)).setScale(2, RoundingMode.HALF_UP);

        discountAmount = preDiscountAmount.multiply((new BigDecimal(discount).divide(ONE_HUNDRED, 2, RoundingMode.DOWN))).setScale(2, RoundingMode.HALF_UP);

        finalCharge = preDiscountAmount.subtract(discountAmount);
    }

    private void printAgreement() {
        StringBuilder stringBuilder = new StringBuilder("Tool code: ").append(tool.toolCode).append(System.lineSeparator());
        stringBuilder.append("Tool type: ").append(tool.toolType).append(System.lineSeparator());
        stringBuilder.append("Tool brand: ").append(tool.brand).append(System.lineSeparator());
        stringBuilder.append("Rental days: ").append(rentalDays).append(System.lineSeparator());
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        stringBuilder.append("Check out date: ").append(checkOutDate.format(dateFormatter)).append(System.lineSeparator());
        stringBuilder.append("Due date: ").append(dueDate.format(dateFormatter)).append(System.lineSeparator());
        NumberFormat currencyFormater = NumberFormat.getCurrencyInstance(Locale.US);
        stringBuilder.append("Daily rental charge: ").append(currencyFormater.format(tool.charge.dailyCharge().doubleValue())).append(System.lineSeparator());
        stringBuilder.append("Charge days: ").append(numberOfChargeDays).append(System.lineSeparator());
        stringBuilder.append("Pre-discount charge: ").append(currencyFormater.format(preDiscountAmount.doubleValue())).append(System.lineSeparator());
        stringBuilder.append("Discount percent: ").append(discount).append("%").append(System.lineSeparator());
        stringBuilder.append("Discount amount: ").append(currencyFormater.format(discountAmount.doubleValue())).append(System.lineSeparator());
        stringBuilder.append("Final charge: ").append(currencyFormater.format(finalCharge.doubleValue())).append(System.lineSeparator());

        System.out.println(stringBuilder);
    }

    private void calculateChargeDays(Tool tool) {

        numberOfChargeDays = findNumberOfDaysInDateRange(checkOutDate, dueDate);

        // make sure to check the case where the tool is rented over multiple years (get number of years)
        var numberOfYears = getNumberOfYearsFromDateRange(checkOutDate, dueDate);

        // exclude any days that are not charged by subtracting them from the numberOfChargeDays

        // numberOfWeekEndDays will be used to calculate both the number of weekend days
        // and the number of weekdays to delete from total charge days as needed.
        var numberOfWeekEndDays = getWeekEndDays(checkOutDate, dueDate);

        // if excluding weekends and a holiday falls on the weekend, it should not be excluded twice
        // this boolean will handle that situation later.
        boolean excludeWeekends = false;
        // Exclude weekends from the range
        if (!tool.charge.weekendCharge()) {
            numberOfChargeDays -= numberOfWeekEndDays;
            excludeWeekends = true;
        }

        // if excluding weekends and a holiday falls on the weekday, it should not be excluded twice
        // this boolean will handle that situation later.
        boolean excludeWeekdays = false;
        // Exclude weekdays from the range
        if (!tool.charge.weekdayCharge()) {
            numberOfChargeDays -= numberOfChargeDays - numberOfWeekEndDays;
            excludeWeekdays = true;
        }

        if (!tool.charge.holidayCharge()) {
            checkForHolidayExcludes(numberOfYears, excludeWeekends, excludeWeekdays, (int year) -> {
                // labor day
                return getLaborDayFromYear(checkOutDate.getYear() + year);
            });

            checkForHolidayExcludes(numberOfYears, excludeWeekends, excludeWeekdays, (int year) -> {
                // july 4th
                return getJulyForthFromYear(checkOutDate.getYear() + year);
            });
        }
    }

    private long getWeekEndDays(LocalDate checkOutDate, LocalDate dueDate) {
        var start = checkOutDate;
        var end = dueDate;

        var numberOfSaturdaysAndSundays = 0L;

        // adjusting our date to start and end on fridays in order to
        // use the number of weeks to determine the true number of weekends.

        if (start.getDayOfWeek().getValue() > 5) {
            start = start.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));

            // since we start on weekend we have to add in a day or two otherwise they would be skipped when we moved to next Friday
            numberOfSaturdaysAndSundays += start.getDayOfWeek() == DayOfWeek.SATURDAY ? 2 : 1;
        }

        if (end.getDayOfWeek().getValue() > 5) {
            end = end.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY));

            // since we end on weekend we have to add in a day or two otherwise they would be skipped when we moved to previous Friday
            numberOfSaturdaysAndSundays += end.getDayOfWeek() == DayOfWeek.SUNDAY ? 2 : 1;
        }

        // if already starts on friday do not move
        if (start.getDayOfWeek() != DayOfWeek.FRIDAY) {
            start = start.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
        }
        if (end.getDayOfWeek() != DayOfWeek.FRIDAY) {
            end = end.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));

        }

        numberOfSaturdaysAndSundays += (ChronoUnit.WEEKS.between(start, end) * 2);

        return numberOfSaturdaysAndSundays;
    }

    private void checkForHolidayExcludes(int numberOfYears, boolean excludeWeekends, boolean excludeWeekDays, DateLambda dateLambda) {
        for (int i = 0; i < numberOfYears; i++) {
            LocalDate holidayDate = dateLambda.getHolidayDate(i);
            if (!(holidayDate.isBefore(checkOutDate) || holidayDate.isAfter(dueDate)
                    && !(excludeWeekDays && holidayDate.getDayOfWeek().getValue() <= 5))
                    && !(excludeWeekends && holidayDate.getDayOfWeek().getValue() > 5)) {
                numberOfChargeDays--;
            }
        }
    }

    private int getNumberOfYearsFromDateRange(LocalDate checkOutDate, LocalDate dueDate) {
        // Calculate the number of years between the two dates
        var period = Period.between(checkOutDate, dueDate);

        // add one because even if the period does not include a full year we still need to check for that year
        return period.getYears() + 1;
    }

    private long findNumberOfDaysInDateRange(LocalDate checkOutDate, LocalDate dueDate) {
        // plus one because end day is not included in the between, but we want to include endDate
        // also plus one to checkOutDay because first day (check out day) does not count to rental days.
        return ChronoUnit.DAYS.between(checkOutDate.plusDays(1), dueDate) + 1;
    }

    private LocalDate getLaborDayFromYear(int year) {
        LocalDate laborDay = LocalDate.of(year, 9, 1);
        while (laborDay.getDayOfWeek() != DayOfWeek.MONDAY) {
            laborDay = laborDay.plusDays(1);
        }
        return laborDay;
    }


    private LocalDate calculateDueDate(long rentalDays) {
        return checkOutDate.plusDays(rentalDays);
    }

    private LocalDate getJulyForthFromYear(int year) {
        return LocalDate.of(year, 7, 4);
    }


    @FunctionalInterface
    interface DateLambda {
        LocalDate getHolidayDate(int year);
    }

    public class TestDecorator {
        public LocalDate dueDate() {
            return dueDate;
        }

        public long numberOfChargeDays() {
            return numberOfChargeDays;
        }

        public BigDecimal preDiscountAmount() {
            return preDiscountAmount;
        }

        public BigDecimal discountAmount() {
            return discountAmount;
        }

        public BigDecimal finalCharge() {
            return finalCharge;
        }

        public void printAgreement() {
            RentalAgreement.this.printAgreement();
        }
    }
}
