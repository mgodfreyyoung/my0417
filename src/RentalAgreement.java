import data.Tool;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

public class RentalAgreement {
    private final Tool tool;
    private final int renatDays;
    private final LocalDate checkOutDate;
    final float discount;

    public LocalDate dueDate;
    public long numberOfChargeDays = 0;


    public RentalAgreement(Tool tool, int rentalDays, float discount, LocalDate checkOutDate) {
        this.tool = tool;
        this.renatDays = rentalDays;
        this.checkOutDate = checkOutDate;
        this.discount = discount;

        this.dueDate = calculateDueDate(rentalDays, checkOutDate);

        calculateChargeDays(tool, checkOutDate);
    }

    private void calculateChargeDays(Tool tool, LocalDate checkOutDate) {

        numberOfChargeDays = findNumberOfDaysInDateRange(checkOutDate, dueDate);

        // make sure to check the case where the tool is rented over multiple years (get number of years)
        var numberOfYears = getNumberOfYearsFromDateRange(checkOutDate, dueDate);

        // exclude any days that are not charged by subtracting them from the numberOfChargeDays

       checkForHolidayExcludes(numberOfYears,(int year) -> {
           // labor day
           return getLaborDayFromYear(checkOutDate.getYear() + year);
       });

        checkForHolidayExcludes(numberOfYears,(int year) -> {
            // july forth
            return getJulyForthFromYear(checkOutDate.getYear() + year);
        });
    }

    private void checkForHolidayExcludes(int numberOfYears, DateLambda dateLambda) {
        for (int i = 0; i < numberOfYears; i++) {
            LocalDate laborDay = dateLambda.getHolidayDate(i);
            if (tool.chaarge.holidayCharge && !(laborDay.isBefore(checkOutDate) || laborDay.isAfter(dueDate))) {
                numberOfChargeDays--;
            }
        }
    }

    private int getNumberOfYearsFromDateRange(LocalDate checkOutDate, LocalDate dueDate) {
        // Calculate the number of years between the two dates
        Period period = Period.between(checkOutDate, dueDate);

        // add one because even if the period does not include a full year we still need to check for that year
        return period.getYears() + 1;
    }


    private long findNumberOfDaysInDateRange(LocalDate checkOutDate, LocalDate dueDate) {
        // plus one because end day is not included in the between but we want to include endDate
        return ChronoUnit.DAYS.between(checkOutDate, dueDate) + 1;
    }

    private LocalDate getLaborDayFromYear(int year) {
        LocalDate laborDay = LocalDate.of(year, 9, 1);
        while (laborDay.getDayOfWeek() != DayOfWeek.MONDAY) {
            laborDay = laborDay.plusDays(1);
        }
        return laborDay;
    }


    private LocalDate calculateDueDate(long rentalDays, LocalDate checkOutDate) {
        return checkOutDate.plusDays(rentalDays);
    }

    private LocalDate getJulyForthFromYear(int year) {
        return LocalDate.of(year, 7, 4);
    }


    @FunctionalInterface
    interface DateLambda {
        LocalDate getHolidayDate(int year);
    }
}
