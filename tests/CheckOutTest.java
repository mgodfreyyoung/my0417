import org.junit.Assert;
import org.junit.Before;
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

            //  this will throw an exception that has the message that explains the issue.

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

            var rentalAgreementTestDecorator =  checkOut.rentalAgreement.new TestDecorator();

            // test expected due date

            // since our original calendar was 7/2/20 (mm//dd//yyy) we need to make sure the
            // rental agreement has a due date of 7/5/20 (adding 3 days to checkoutDate)
            var expectedDueDate = LocalDate.of(2020, 7, 5);
            Assert.assertEquals("rental agreement date does not match our expected date.", expectedDueDate, rentalAgreementTestDecorator.dueDate());

            // test number of chargeable rental days

            // since july forth falls within date range (July 2-5) and
            // since tool code LADW excludes holidays than number of
            // chargeable days should be 2 rather than 3 (excluding July 4th and first day (2nd) does not count to rental days)

            Assert.assertEquals("number of charge days is not correct", 2, rentalAgreementTestDecorator.numberOfChargeDays());

            // check pre-discount charge
            // daily charge of $1.99 x 2 = $3.98
            Assert.assertEquals("pre-discount amount incorrect", 3.98, rentalAgreementTestDecorator.preDiscountAmount().doubleValue(),0);

            // discount amount: 3.98  x 10% = .40 (0.398 rounded up)
            Assert.assertEquals("discount total incorrect",.40, rentalAgreementTestDecorator.discountAmount().doubleValue(), 0);

            // final amount: 3.98 - .40 = $3.58
            Assert.assertEquals("final total incorrect",3.58, rentalAgreementTestDecorator.finalCharge().doubleValue(),0);

            rentalAgreementTestDecorator.printAgreement();

        } catch (Exception e) {

            Assert.fail("an exception was thrown which should not have been; message = " + e.getMessage());
        }
    }

    @Test
    public void checkoutTestThree() {
        try {
            var checkOutDate = LocalDate.of(2015, 7, 2);

            var checkOut = new CheckOut("CHNS", 5, 25, checkOutDate);

            var rentalAgreementTestDecorator =  checkOut.rentalAgreement.new TestDecorator();

            // test expected due date

            // since our original calendar was 7/2/20 (mm//dd//yyy) we need to make sure the
            // rental agreement has a due date of 7/7/20 (adding 5 days to checkoutDate)
            var expectedDueDate = LocalDate.of(2015, 7, 7);
            Assert.assertEquals("rental agreement date does not match our expected date.", expectedDueDate,  rentalAgreementTestDecorator.dueDate());

            // test number of chargeable rental days
            // number of charge days should be 3 - 5 total days rental - 2 (weekend days)
            Assert.assertEquals("number of charge days is not correct", 3, rentalAgreementTestDecorator.numberOfChargeDays());

            // check pre-discount charge
            // daily charge of $1.49 x 2 = $4.47
            Assert.assertEquals("pre-discount amount incorrect", 4.47, rentalAgreementTestDecorator.preDiscountAmount().doubleValue(),0);

            // discount amount: 4.47  x 25% = 1.12
            Assert.assertEquals("discount total incorrect",1.12, rentalAgreementTestDecorator.discountAmount().doubleValue(), 0);

            // final amount: 4.47 - 1.12 = 3.35
            Assert.assertEquals("final total incorrect",3.35, rentalAgreementTestDecorator.finalCharge().doubleValue(),0);

            rentalAgreementTestDecorator.printAgreement();

        } catch (Exception e) {

            Assert.fail("an exception was thrown which should not have been; message = " + e.getMessage());
        }
    }

    @Test
    public void checkoutTestFour() {
        try {
            var checkOutDate = LocalDate.of(2015, 9, 3);

            var checkOut = new CheckOut("JAKD", 6, 0, checkOutDate);

            var rentalAgreementTestDecorator =  checkOut.rentalAgreement.new TestDecorator();

            // test expected due date

            // 6 total rental days. Sept 3rd to the 9th
            // jackhammers only have charges on weekdays so 3 days total chargeable.(skipping weekends)
            // Labor day falls on the 7th so no charge that day plus 2 weekend days
            // total chargeable = 3 days chargeable out of the 6 rental days
            var expectedDueDate = LocalDate.of(2015, 9, 9);
            Assert.assertEquals("rental agreement date does not match our expected date.", expectedDueDate, rentalAgreementTestDecorator.dueDate());

            // test number of chargeable rental days

            // number of charge days should be 3 - 5 total days rental - 2 (weekend days)

            Assert.assertEquals("number of charge days is not correct", 3, rentalAgreementTestDecorator.numberOfChargeDays());

            // check pre-discount charge
            // daily charge of $2.99 x 3 = $8.97
            Assert.assertEquals("pre-discount amount incorrect", 8.97, rentalAgreementTestDecorator.preDiscountAmount().doubleValue(),0);

            // discount amount: 8.97  x 0 = 0
            Assert.assertEquals("discount total incorrect",0, rentalAgreementTestDecorator.discountAmount().doubleValue(), 0);

            // final amount: 8.97 - 0 = 8.97
            Assert.assertEquals("final total incorrect",8.97, rentalAgreementTestDecorator.finalCharge().doubleValue(),0);

            rentalAgreementTestDecorator.printAgreement();

        } catch (Exception e) {

            Assert.fail("an exception was thrown which should not have been; message = " + e.getMessage());
        }
    }

    @Test
    public void checkoutTestFive() {
        try {
            var checkOutDate = LocalDate.of(2015, 7, 2);

            var checkOut = new CheckOut("JAKR", 9, 0, checkOutDate);

            var rentalAgreementTestDecorator =  checkOut.rentalAgreement.new TestDecorator();

            // test expected due date

            // Total days = July 3rd to 11th = 9 rental days
            // charge on weekdays only
            // 6 chargeable days (excluding weekends and holidays)
            var expectedDueDate = LocalDate.of(2015, 7, 11);
            Assert.assertEquals("rental agreement date does not match our expected date.", expectedDueDate, rentalAgreementTestDecorator.dueDate());

            // test number of chargeable rental days
            // number of charge days should be 3 - 5 total days rental - 2 (weekend days)
            Assert.assertEquals("number of charge days is not correct", 6, rentalAgreementTestDecorator.numberOfChargeDays());

            // check pre-discount charge
            // daily charge of $2.99 x 5 = 17.94
            Assert.assertEquals("pre-discount amount incorrect", 17.94, rentalAgreementTestDecorator.preDiscountAmount().doubleValue(),0);

            // discount amount: 17.94  x 0 = 0
            Assert.assertEquals("discount total incorrect",0, rentalAgreementTestDecorator.discountAmount().doubleValue(), 0);

            // final amount: 17.94 - 0 = 17.94
            Assert.assertEquals("final total incorrect",17.94, rentalAgreementTestDecorator.finalCharge().doubleValue(),0);

            rentalAgreementTestDecorator.printAgreement();
        } catch (Exception e) {

            Assert.fail("an exception was thrown which should not have been; message = " + e.getMessage());
        }
    }

    @Test
    public void checkoutTestSix() {
        try {
            var checkOutDate = LocalDate.of(2020, 7, 2);

            var checkOut = new CheckOut("JAKR", 4, 50, checkOutDate);

            var rentalAgreementTestDecorator =  checkOut.rentalAgreement.new TestDecorator();

            // test due date
            // due date = 2 - 6 = July 6th
            var expectedDueDate = LocalDate.of(2020, 7, 6);
            Assert.assertEquals("rental agreement date does not match our expected date.", expectedDueDate, rentalAgreementTestDecorator.dueDate());

            // test number of chargeable rental days
            // only weekdays so 2 total
            Assert.assertEquals("number of charge days is not correct", 2, rentalAgreementTestDecorator.numberOfChargeDays());

            // check pre-discount charge
            // daily charge of $2.99 x 2 = 5.98
            Assert.assertEquals("pre-discount amount incorrect", 5.98, rentalAgreementTestDecorator.preDiscountAmount().doubleValue(),0);

            // discount amount: 5.98  x .50 = 2.99
            Assert.assertEquals("discount total incorrect",2.99, rentalAgreementTestDecorator.discountAmount().doubleValue(), 0);

            // final amount: 5.98 - 2.99 = 2.99
            Assert.assertEquals("final total incorrect",2.99, rentalAgreementTestDecorator.finalCharge().doubleValue(),0);

            rentalAgreementTestDecorator.printAgreement();
        } catch (Exception e) {

            Assert.fail("an exception was thrown which should not have been; message = " + e.getMessage());
        }
    }
}
