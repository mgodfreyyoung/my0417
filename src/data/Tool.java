package data;


public class Tool {

    public Tool(long chargeID, String toolCode, String toolType, String brand) {
        this.chargeID = chargeID;

        this.toolCode = toolCode;
        this.toolType = toolType;
        this.brand = brand;
    }

    public Tool(Charge charge, long chargeID, String toolCode, String toolType, String brand)
    {
        this(chargeID,toolCode, toolType,brand);
        this.chaarge = charge;
    }

    public Charge chaarge;

    public final long chargeID;

    public final String toolCode;
    public final String toolType;
    public final String brand;
}
