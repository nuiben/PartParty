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
 * AddProductController Class
 */
public class AddProductController extends AddPartController {

    private static final String[] COLUMNS = {"id", "name", "stock", "price"};
    public Button addAssociatedPartButton;
    public Button saveProdButton;
    @FXML
    private TableView<Part> partTable;
    @FXML
    private TableView<Part> associatedPartTable;
    @FXML
    private ObservableList<Part> temporaryAssociatedParts = FXCollections.observableArrayList();
    public Product product;
    private int idCount;
    private int firstClickFlag = 0;

    /**
     * Initializes like AddPartController with the addition of a searchable
     *  * Parts table and associated parts (not searchable).
     * @param url String value of FXML filepath
     * @param resourceBundle Resource files provided in resources directory.
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(url);
        ObservableList<Product> products = Inventory.getAllProducts();
        idCount = getHighestID(products);
        idTextBox.setText(String.valueOf(idCount + 1));
        loadTable(partTable);
        int index = 0;
        for (String var : COLUMNS) {
            associatedPartTable.getColumns().get(index++).setCellValueFactory(new PropertyValueFactory<>(var));
        }
    }

    /**
     * Event Handler for Save button.
     * @param save lets FXMLloader know the button trigger for updating the current stage.
     * @throws IOException if the FXMLloader cannot locate resource.
     */
    public void OnSaveProduct(ActionEvent save) throws IOException{
        if (product == null) {
            try {
                testFields();
                product = newItem(getHighestID(Inventory.getAllProducts()));
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
            if (displayConfirmation(associatedPartTable).get() == ButtonType.OK) {
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