import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

public class CheckOutTest {


    @Test
    public void checkoutTestOne() {
        try {
            var calendar = Calendar.getInstance();
            calendar.set(15, Calendar.SEPTEMBER,3);
            new CheckOut("JAKR", 5, 101f, calendar);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void checkoutTestTwo() {
        try {
            new SQL().createSqlDatabase();

            var calendar = Calendar.getInstance();
            calendar.set(20, Calendar.JULY,2);
            var checkOut = new CheckOut("LADW", 3, 10f, calendar);

            var rentalAgreement = checkOut.createRentalAgreement();

            Assert.assertEquals(rentalAgreement.discount, 10f, 2);


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
