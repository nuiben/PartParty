package ben.partparty.model;

/**
 * Outsourced Class - Child of Part Class
 */

public class Outsourced extends Part{
    private String companyName;
    /**
     * Constructor creates a new instance of an Outsourced Part Object.
     * @param id Unique integer value associated to the part
     * @param name String value indicating the name of the part
     * @param price Double numeric indicating the sales price
     * @param stock Integer value count of the units on hand, must be between min and max
     * @param min Integer floor for stock value.
     * @param max Integer ceiling for stock value.
     * @param companyName String value of company that made part.
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Get method for the machineID variable.
     * @param  companyName sets machineID with integer provided.
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    /**
     * Get method for the companyName variable.
     * @return companyName String value of company that made part.
     */
    public String getCompanyName() {
        return companyName;
    }
}
