import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Calendar;

public class CheckOutTest {

    @Test
    public void checkoutTestOne() {
        try {
            var calendar = Calendar.getInstance();
            calendar.set(15, Calendar.SEPTEMBER, 3);
            new CheckOut("JAKR", 5, 101f, calendar);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void checkoutTestTwo() {
        try {
            new SQL().createSqlDatabase();

            var checkOutDate = Calendar.getInstance();
            checkOutDate.set(20, Calendar.JULY, 2);
            var checkOut = new CheckOut("LADW", 3, 10, checkOutDate);

            var rentalAgreement = checkOut.createRentalAgreement();

            // since our original calendar was 7/2/20 (mm//dd//yyy) we need to make sure the
            // rental agreement has a due date of 7/5/20 (adding 3 days to checkoutDate)
            Calendar expectedDueDate = (Calendar) checkOutDate.clone();
            expectedDueDate.set(20, Calendar.JULY, 5);

            Assert.assertEquals(expectedDueDate.compareTo(rentalAgreement.dueDate), 0);




        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
