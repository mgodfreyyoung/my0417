import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Calendar;

public class CheckOutTest {

    @Before
    public void initialize() {
        new SQL().createSqlDatabase();
    }

    @Test
    public void checkoutTestOne() {
        try {
            var calendar = LocalDate.of(2015, Calendar.SEPTEMBER, 3);
            new CheckOut("JAKR", 5, 101, calendar);
        } catch (Exception e) {
            Assert.assertEquals("Error message incorrect for discount", "Discount percentage is not in the range of 0 - 100.  Please enter a value from 0 to 100.", e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void checkoutTestTwo() {
        try {
            var checkOutDate = LocalDate.of(2020, 7, 2);

            var checkOut = new CheckOut("LADW", 3, 10, checkOutDate);


            // test expected due date

            // since our original calendar was 7/2/20 (mm//dd//yyy) we need to make sure the
            // rental agreement has a due date of 7/5/20 (adding 3 days to checkoutDate)
            var expectedDueDate = LocalDate.of(2020, 7, 5);
            Assert.assertEquals("rental agreement date does not match our expected date.", expectedDueDate, checkOut.rentalAgreement.dueDate);

            // test number of chargeable rental days

            // since july forth falls within date range (July 2-5) and
            // since tool code LADW excludes holidays than number of
            // chargeable days should be 2 rather than 3 (excluding July 4th and first day (2nd) does not count to rental days)

            Assert.assertEquals("number of charge days is not correct", 2, checkOut.rentalAgreement.numberOfChargeDays);

        } catch (Exception e) {

            Assert.fail("an exception was thrown which should not have been; message = " + e.getMessage());
        }
    }

    @Test
    public void checkoutTestThree() {
        try {
            var checkOutDate = LocalDate.of(2015, 7, 2);

            var checkOut = new CheckOut("CHNS", 5, 25, checkOutDate);


            // test expected due date

            // since our original calendar was 7/2/20 (mm//dd//yyy) we need to make sure the
            // rental agreement has a due date of 7/7/20 (adding 5 days to checkoutDate)
            var expectedDueDate = LocalDate.of(2015, 7, 7);
            Assert.assertEquals("rental agreement date does not match our expected date.", expectedDueDate, checkOut.rentalAgreement.dueDate);

            // test number of chargeable rental days

            // number of charge days should be 3 - 5 total days rental - 2 (weekend days)

            Assert.assertEquals("number of charge days is not correct", 3, checkOut.rentalAgreement.numberOfChargeDays);

        } catch (Exception e) {

            Assert.fail("an exception was thrown which should not have been; message = " + e.getMessage());
        }
    }

    @Test
    public void checkoutTestMyOwn() {
        try {
            var checkOutDate = LocalDate.of(2015, 7, 2);

            var checkOut = new CheckOut("CHNS", 12, 25, checkOutDate);


            // test expected due date

            // since our original calendar was 7/2/20 (mm//dd//yyy) we need to make sure the
            // rental agreement has a due date of 7/7/20 (adding 5 days to checkoutDate)
            var expectedDueDate = LocalDate.of(2015, 7, 14);
            Assert.assertEquals("rental agreement date does not match our expected date.", expectedDueDate, checkOut.rentalAgreement.dueDate);

            // test number of chargeable rental days

            // number of charge days should be 3 - 5 total days rental - 2 (weekend days)

            Assert.assertEquals("number of charge days is not correct", 8, checkOut.rentalAgreement.numberOfChargeDays);

        } catch (Exception e) {

            Assert.fail("an exception was thrown which should not have been; message = " + e.getMessage());
        }
    }
}
