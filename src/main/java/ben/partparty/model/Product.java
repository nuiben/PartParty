package ben.partparty.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product extends Part{
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
        super(id, name, price, stock, min, max);
    }
    /** Takes a part, adds it to the ObservableList belonging to Product.
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
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}
