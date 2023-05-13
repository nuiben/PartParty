package ben.partparty.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Instantiates Abstract Part Super Class
 * @author Ben Porter
 */
public class Product{
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /** Default Constructor for Product
     * @param id Unique integer value associated to the product
     * @param name String value indicating the name of the product
     * @param price Double numeric indicating the sales price
     * @param stock Integer value count of the units on hand, must be between min and max
     * @param min Integer floor for stock value.
     * @param max Integer ceiling for stock value.
     * */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * Returns the given id.
     * @return id integer value.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id variable to integer provided.
     * @param  id integer value.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name variable to String provided.
     * @param  name String value.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price variable to double provided.
     * @param price the numeric double indicating price.
     */
    public void setPrice(double price) {
        this.price = price;
    }
    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * Sets the stock variable to the integer provided.
     * @param stock indicates the inventory count of the given product.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * Sets the min variable to the integer provided.
     * @param min indicates inventory floor.
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * Sets the max variable to the integer provided.
     * @param max indicates inventory ceiling.
     */
    public void setMax(int max) {
        this.max = max;
    }



    /** Takes an individual part, adds it to the ObservableList belonging to Product.
     * @param part instance of the part class.
     * */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }
    /** Searches associatedParts list for the part, removes it from the list.
     * Not used as use of temporary lists in Product Controller Classes nullify utility of method.
     * @param part instance of the part class.
     * */
    public boolean deleteAssociatedPart(Part part) { return associatedParts.remove(part);}

    /**
     * Retrieves the Associated Parts List for the given product.
     * @return associatedParts ObservableList containing Part objects.
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}
