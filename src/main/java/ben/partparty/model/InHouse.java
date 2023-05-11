package ben.partparty.model;

/**
 * InHouse Class
 */
public class InHouse extends Part {
    private int machineID;
    /**
     * Constructor creates a new instance of an InHouse Part Object.
     * @param id Unique integer value associated to the part
     * @param name String value indicating the name of the part
     * @param price Double numeric indicating the sales price
     * @param stock Integer value count of the units on hand, must be between min and max
     * @param min Integer floor for stock value.
     * @param max Integer ceiling for stock value.
     * @param machineID Integer value of the machine that created product.
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineID) {
        super(id, name, price, stock, min, max);
        this.machineID = machineID;
    }

    /**
     * Get method for the machineID variable.
     * @return machineID Integer value of the machine that created product.
     */
    public int getMachineID() {
        return machineID;
    }

    /**
     * Get method for the machineID variable.
     * @param  machineID sets machineID with integer provided.
     */
    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }
}
