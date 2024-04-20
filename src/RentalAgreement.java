import data.Tool;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

public class RentalAgreement {
    private final Tool tool;
    private final int renatDays;
    private LocalDate checkOutDate;
    final float discount;

    public LocalDate dueDate;
    public long numberOfChargeDays = 0;


    public RentalAgreement(Tool tool, int rentalDays, float discount, LocalDate checkOutDate) {
        this.tool = tool;
        this.renatDays = rentalDays;
        this.checkOutDate = checkOutDate;
        this.discount = discount;

        this.dueDate = calculateDueDate(rentalDays, checkOutDate);

        calculateChargeDays(tool);
    }

    private void calculateChargeDays(Tool tool) {

        numberOfChargeDays = findNumberOfDaysInDateRange(checkOutDate, dueDate);

        // make sure to check the case where the tool is rented over multiple years (get number of years)
        var numberOfYears = getNumberOfYearsFromDateRange(checkOutDate, dueDate);

        // exclude any days that are not charged by subtracting them from the numberOfChargeDays

        // Exclude weekends from the range
        if (!tool.chaarge.weekendCharge) {

            var numberOfWeekEndDays = getWeekEndDays(checkOutDate, dueDate);
            numberOfChargeDays -= numberOfWeekEndDays;
        }

        checkForHolidayExcludes(numberOfYears, (int year) -> {
            // labor day
            return getLaborDayFromYear(checkOutDate.getYear() + year);
        });

        checkForHolidayExcludes(numberOfYears, (int year) -> {
            // july 4th
            return getJulyForthFromYear(checkOutDate.getYear() + year);
        });
    }

    private long getWeekEndDays(LocalDate checkOutDate, LocalDate dueDate) {
        var start = checkOutDate;
        var end = dueDate;

        var numberOfSaturdaysAndSundays = 0L;

        // adjusting our date to start and end on friday's in order to
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

    private void checkForHolidayExcludes(int numberOfYears, DateLambda dateLambda) {
        for (int i = 0; i < numberOfYears; i++) {
            LocalDate holidayDate = dateLambda.getHolidayDate(i);
            if (!tool.chaarge.holidayCharge && !(holidayDate.isBefore(checkOutDate) || holidayDate.isAfter(dueDate))) {
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
        // plus one because end day is not included in the between but we want to include endDate
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
