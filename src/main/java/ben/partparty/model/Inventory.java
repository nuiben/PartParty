package ben.partparty.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;

/**
 * Inventory Class to handle Part and Product Objects.
 */
public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Insert a Part object into observable list of type Part.
     * @param part Part type object being added.
     */
    public static void addPart(Part part) {
        allParts.add(part);
    }

    /**
     * Insert a Product object into observable list of type Product.
     * @param newProduct the Product object being added to the ObservableList.
     */

    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * Update a Part object inside the observable list of type Part.
     * @param part Part type object being added.
     */
    public static void updatePart(int index, Part part) {
        allParts.set(index, part);
    }

    /**
     * Update a Product object inside the observable list of type Part.
     * @param product Product type object being added.
     */
    public static void updateProduct(int index, Product product) {
        allProducts.set(index, product);
    }
    /**
     * Delete a Part object inside the observable list of type Part.
     * @param part Product type object being added.
     * @return boolean value if delete was successful
     */
    public static boolean deletePart(Part part) {
        return allParts.remove(part);
    }
    /**
     * Delete a Product object inside the observable list of type Part.
     * @param product Product type object being added.
     * @return boolean  value if delete was successful
     */
    public static boolean deleteProduct(Product product) {
        return allProducts.remove(product);
    }
    /**
     * Retrieves the ObservableList of Parts
     * @return ObservableList<Part>
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }
    /**
     * Retrieves the ObservableList of Products
     * @return boolean value if delete was successful
     */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }

    /**
     * Currently being handled by EventListener and Lambda function in MainViewController
     * @deprecated lookupPart
     * @param partId the integer value of the Part's Id
     */
    @Deprecated
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
     * @deprecated
     * @param partName the String Value of the Part's Name
     */
    @Deprecated
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
     * @deprecated
     * @param productId the integer value of the Part's Id
     */
    @Deprecated
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
     * @deprecated
     * @param productName the value being searched.
     */
    @Deprecated
    public static Part lookupProduct(String productName) {
        for (Part targetProduct : getAllParts()) {
            if (targetProduct.getName().equals(productName)) {
                return targetProduct;
            }
        }
        return null;
    }
}





