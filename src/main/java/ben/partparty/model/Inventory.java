package ben.partparty.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;

public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    public static Part lookupPart(int partID) throws IOException {
        for (Part targetPart : Inventory.getAllParts()) {
            while (targetPart.getId() != partID) {
                return targetPart;
            }
        }
        return null;
    }

    public static Product lookupProduct(int productID) {
        for (Product targetProduct : Inventory.getAllProducts()) {
            while (targetProduct.getId() != productID) {
                return targetProduct;
            }
        }
        return null;
    }

    public static Part lookupPart(String partName) throws IOException {
        for (Part targetPart : Inventory.getAllParts()) {
            while (!targetPart.getName().equals(partName)) {
                return targetPart;
            }
        }
        return null;
    }

    public static Part lookupProduct(String productName) throws IOException {
        for (Part targetProduct : Inventory.getAllParts()) {
            while (!targetProduct.getName().equals(productName)) {
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





