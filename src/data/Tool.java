package data;


public class Tool {

    public Tool(String toolCode, String toolType, String brand, Charge charge) {
        this.toolCode = toolCode;
        this.toolType = toolType;
        this.brand = brand;

        this.charge = charge;
    }

    public final Charge charge;

    public final String toolCode;
    public final String toolType;
    public final String brand;
}
