import org.junit.Test;

import java.util.Date;

public class CheckOutTest {


    @Test
    public void checkoutTestOne() {
        try {
            new CheckOut("JAKR", 5, 101f, new Date());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
