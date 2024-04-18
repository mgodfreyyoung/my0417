import java.util.Date;

public class CheckOut {

    public CheckOut(String toolCode, int rentalDayCount, float discountPercentage, Date checkOutDate) {
        try {
            validateRentalDayCount(rentalDayCount);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            validateDiscountPercentage(discountPercentage);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // validate that the rental day count is at least one or greater
    // if not throw back up to calling method so it can message user properly.
    public void validateRentalDayCount(int rentalDayCount) throws Exception {
        if (rentalDayCount < 1) {
            throw new Exception("Rental day count is not 1 or greater. Please enter a value for number of rental days that is at least 1.");
        }
    }

    public void validateDiscountPercentage(float discountPercentage) throws Exception {
        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new Exception("Discount percentage is not in the range of 0 - 100.  Please enter a value from 0 to 100.");
        }
    }

}
