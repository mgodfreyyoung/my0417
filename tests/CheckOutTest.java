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
            Assert.assertTrue("rental agreement date does not match our expected date.", expectedDueDate.isEqual(checkOut.rentalAgreement.dueDate));

            // test number of chargeable rental days

            // since july forth falls within date range (July 2-5) and
            // since tool code LADW excludes holidays than number of
            // chargeable days should be 3 rather than 4 (excluding July 4th)

            Assert.assertEquals("number of charge days is not correct", 3, checkOut.rentalAgreement.numberOfChargeDays);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
