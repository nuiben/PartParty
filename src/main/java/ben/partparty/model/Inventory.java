package ben.partparty.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.text.Text;

import java.io.IOException;

/**
 * Inventory Class
 */
public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Insert a Part object into observable list of type Part
     * @param newPart
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * Insert a Product object into observable list of type Product
     * @param newProduct
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * Currently being handled by lambda function in MainViewController
     * @param partId the integer value of the Part's Id
     */
    public static Part lookupPart(int partId) {
        for (Part targetPart : getAllParts()) {
            if (targetPart.getId() == partId) {
                return targetPart;
            }
        }
        return null;
    }

    /**
     * Currently being handled by lambda function in MainViewController
     * @param partName the String Value of the Part's Name
     */
    public static Part lookupPart(String partName) {
        for (Part targetPart : getAllParts()) {
            if (targetPart.getName().equals(partName)) {
                return targetPart;
            }
        }
        return null;
    }

    /**
     * Currently being handled by lambda function in MainViewController
     * @param productId the integer value of the Part's Id
     */
    public static Part lookupProduct(int productId) {
        for (Part targetPart : getAllParts()) {
            if (targetPart.getId() == productId) {
                return targetPart;
            }
        }
        return null;
    }

    /**
     * Currently being handled by lambda function in MainViewController
     * @param productName
     */
    public static Part lookupProduct(String productName) throws IOException {
        for (Part targetProduct : getAllParts()) {
            if (targetProduct.getName().equals(productName)) {
                return targetProduct;
            }
        }
        return null;
    }

    public static void updatePart(int index, Part selectedPart) { allParts.set(index, selectedPart); }
    public static void updateProduct(int index, Product selectedProduct) { allProducts.set(index, selectedProduct); }
    public static boolean deletePart(Part part) { return allParts.remove(part); }
    public static boolean deleteProduct(Product index) { return allProducts.remove(index); }
    public static ObservableList<Part> getAllParts() { return allParts; }
    public static ObservableList<Product> getAllProducts(){ return allProducts; }
}





