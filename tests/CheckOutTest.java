import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Calendar;

public class CheckOutTest {

    @Test
    public void checkoutTestOne() {
        try {
            var calendar = LocalDate.of(2015, Calendar.SEPTEMBER, 3);
            new CheckOut("JAKR", 5, 101, calendar);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void checkoutTestTwo() {
        try {
            new SQL().createSqlDatabase();

            var checkOutDate = LocalDate.of(2020, 7, 2);

            var checkOut = new CheckOut("LADW", 3, 10, checkOutDate);


            // since our original calendar was 7/2/20 (mm//dd//yyy) we need to make sure the
            // rental agreement has a due date of 7/5/20 (adding 3 days to checkoutDate)
            var expectedDueDate = LocalDate.of(2020, 7, 5);

            Assert.assertTrue("rental agreement date does not match our expected date.", expectedDueDate.isEqual(checkOut.rentalAgreement.dueDate));


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
