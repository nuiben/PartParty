package ben.partparty.controller;

import ben.partparty.model.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Tertiary Class - Passes only to ModifyProductController
 */
public class AddProductController extends AddPartController {

    private static final String[] COLUMNS = {"id", "name", "stock", "price"};
    @FXML
    private TableView<Part> partTable;
    @FXML
    private TableView<Part> associatedPartTable;
    @FXML
    public ObservableList<Part> temporaryAssociatedParts = FXCollections.observableArrayList();
    private Product product;
    private int firstClickFlag = 0;

    /**
     * Initializes like AddPartController with the addition of a searchable
     *  * Parts table and associated parts (not searchable).
     * @param url String value of FXML filepath
     * @param resourceBundle Resource files provided in resources directory.
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(url);
        int idCount = getHighestProductID();
        idTextBox.setText(String.valueOf(idCount + 1));
        loadPartTable();
        int index = 0;
        for (String var : COLUMNS) {
            associatedPartTable.getColumns().get(index++).setCellValueFactory(new PropertyValueFactory<>(var));
        }
    }

    public Product saveProduct(Product item) {
        testFields();
        item.setId(Integer.parseInt(idTextBox.getText()));
        item.setName(nameTextBox.getText());
        item.setPrice(Double.parseDouble(priceTextBox.getText()));
        item.setMin(Integer.parseInt(minTextBox.getText()));
        item.setMax(Integer.parseInt(maxTextBox.getText()));
        item.setStock(Integer.parseInt(invTextBox.getText()));
        return item;
    }

    public Product newProduct(int idCount) {
        int id = ++idCount;
        String name = nameTextBox.getText();
        double price = Double.parseDouble(priceTextBox.getText());
        int stock = Integer.parseInt(invTextBox.getText());
        int min = Integer.parseInt(minTextBox.getText());
        int max = Integer.parseInt(maxTextBox.getText());
        return new Product(id,name, price, stock, min, max);

    }

    /** Returns the current highest ID value of provided Part or Product List
     * @return max
     * */
    public int getHighestProductID() {
        int max = 0;
        for (Product i : Inventory.getAllProducts()) {
            if (i.getId() > max) {
                max = i.getId();
            }
        }
        return max;
    }

    public void setProductFields(Product item) throws NumberFormatException {
        idTextBox.setText(Integer.toString(item.getId()));
        nameTextBox.setText(item.getName());
        priceTextBox.setText(Double.toString(item.getPrice()));
        invTextBox.setText(Integer.toString(item.getStock()));
        minTextBox.setText(Integer.toString(item.getMin()));
        maxTextBox.setText(Integer.toString(item.getMax()));
    }

    /**
     * Event Handler for Save button.
     * @param save lets FXMLloader know the button trigger for updating the current stage.
     * @throws IOException if the FXMLloader cannot locate resource.
     *
     * The inclusion of a temporary associated parts list makes the visual experience when adding parts
     * much more flexible for the user. When using the remove and get methods from the Product class, there were many
     * bugs that would be created with duplicate values being added, or values not saving the way they appeared in
     * the table on screen. Instead of accessing and modifying the data structure directly
     * on every call, the temporary associated parts list replaces the existing ObservableList at the time of Save.
     */
    public void OnSaveProduct(ActionEvent save) throws IOException{
        if (product == null) {
            try {
                testFields();
                product = newProduct(getHighestProductID());
                product.getAllAssociatedParts().addAll(temporaryAssociatedParts);
                Inventory.addProduct(product);
                loadFXML(save, "/view/MainView.fxml");
            } catch (Exception exception) {
                displayErrorMessage(exception);
            }
        } else {
            product.getAllAssociatedParts().addAll(temporaryAssociatedParts);
            Inventory.addProduct(product);
            loadFXML(save, "/view/MainView.fxml");
        }
    }

    /**
     * Adds selected part to the temporary list displayed by associatedParts table.
     */
    public void OnAddAssociatedPart() {
        if (product == null) {
            try {
                if (partTable.getSelectionModel().getSelectedItem() == null) {
                    throw new RuntimeException("No Selection Indicated.");
                }
                temporaryAssociatedParts.add(partTable.getSelectionModel().getSelectedItem());
                associatedPartTable.setItems(temporaryAssociatedParts);
            } catch (Exception exception) {
                displayErrorMessage(exception);
            }
        } else {
            try {
                if (firstClickFlag == 0) {
                    temporaryAssociatedParts.addAll(product.getAllAssociatedParts());
                    firstClickFlag++;
                }
                if (partTable.getSelectionModel().getSelectedItem() == null) {
                    throw new RuntimeException("No Selection Indicated.");
                }
                temporaryAssociatedParts.add(partTable.getSelectionModel().getSelectedItem());
                associatedPartTable.setItems(temporaryAssociatedParts);

            } catch (Exception exception) {
                displayErrorMessage(exception);
            }
        }
    }

    /**
     * Event handler for Remove Associated Part button.
     * Removes selected element from temporary list displayed in associatedParts Tableview.
     */
    public void OnRemoveAssociatedPart() {
        try {
            if (associatedPartTable.getSelectionModel().getSelectedItem() == null) {
                throw new RuntimeException("No Selection Indicated.");
            }
            if (displayConfirmation(associatedPartTable.getSelectionModel().getSelectedItem().getName()).get() == ButtonType.OK) {
                if (firstClickFlag == 0 && product != null) {
                    temporaryAssociatedParts.addAll(product.getAllAssociatedParts());
                    firstClickFlag++;
                }
                temporaryAssociatedParts.remove(associatedPartTable.getSelectionModel().getSelectedItem());
                associatedPartTable.setItems(temporaryAssociatedParts);
            }
        } catch (Exception exception) {
            displayErrorMessage(exception);
        }

    }
}