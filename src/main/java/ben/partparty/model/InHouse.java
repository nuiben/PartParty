package ben.partparty.model;

/**
 * InHouse Class
 */
public class InHouse extends Part {
    private int machineID;
    /**
     * Constructor
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineID) {
        super(id, name, price, stock, min, max);
        this.machineID = machineID;
    }

    /**
     * Get method
     */
    public int getMachineID() {
        return machineID;
    }

    /**
     * Set method
     */
    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }
}
