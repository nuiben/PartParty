package ben.partparty.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * Inventory Class to handle Part and Product Objects.
 */
public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Insert a Part object into observable list of type Part.
     *
     * @param newPart Part type object being added.
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * Insert a Product object into observable list of type Product.
     *
     * @param newProduct the Product object being added to the ObservableList.
     */

    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * Returns a parsed string passed by the TextField.
     * @param partId the integer value of the Part's Id
     * @return Part Object
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
     * Returns a match to the string passed by the TextField or Null.
     * @param partName the String Value of the Part's Name
     * @return Part Object
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> returnParts = FXCollections.observableArrayList();
        for (Part part : allParts) {
            if (part.getName().toLowerCase().contains(partName.toLowerCase())) {
                returnParts.add(part);
            }
        }
        return returnParts;
    }

    /**
     * Returns a parsed string passed by the TextField.
     * @param productId the integer value of the Part's Id
     * @return Part Object
     */
    public static Product lookupProduct(int productId) {
        for (Product product : getAllProducts()) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }

    /**
     * Returns a match to the string passed by the TextField or Null.
     * @param productName the value being searched.
     * @return Part Object
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> returnProducts = FXCollections.observableArrayList();
        for (Product product : allProducts) {
            if (product.getName().toLowerCase().contains(productName.toLowerCase())) {
                returnProducts.add(product);
            }
        }
        return returnProducts;
    }


    /**
     * Update a Part object inside the observable list of type Part.
     * @param selectedPart Part type object being added.
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * Update a Product object inside the observable list of type Part.
     * @param newProduct Product type object being added.
     */
    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    /**
     * Delete a Part object inside the observable list of type Part.
     * @param selectedPart Product type object being added.
     * @return boolean value if delete was successful
     */
    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    /**
     * Delete a Product object inside the observable list of type Part.
     *
     * @param selectedPart Product type object being added.
     * @return boolean  value if delete was successful
     */
    public static boolean deleteProduct(Product selectedPart) {
        return allProducts.remove(selectedPart);
    }

    /**
     * Retrieves the ObservableList of Parts
     *
     * @return ObservableList<Part>
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Retrieves the ObservableList of Products
     *
     * @return ObservableList<Product>
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}





