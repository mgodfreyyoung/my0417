package data;


public class Tool {

    public Tool(long chargeID, String toolCode, String toolType, String brand) {
        this.chargeID = chargeID;

        this.toolCode = toolCode;
        this.toolType = toolType;
        this.brand = brand;
    }

    public final long chargeID;

    public final String toolCode;
    public final String toolType;
    public final String brand;
}
