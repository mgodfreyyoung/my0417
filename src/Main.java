import java.util.Date;

public class Main {
    public static void main(String[] args) {
        try {
            CheckOut checkOut = new CheckOut("CHNS", 0, 100.0f, new Date());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}