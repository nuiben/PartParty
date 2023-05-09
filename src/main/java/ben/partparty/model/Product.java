package ben.partparty.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product extends Part{
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    public Product(int id, String name, double price, int stock, int min, int max) {
        super(id, name, price, stock, min, max);
    }
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) { return associatedParts.remove(selectedAssociatedPart);}
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}
