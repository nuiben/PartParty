package ben.partparty.controller;

import ben.partparty.model.Inventory;
import ben.partparty.model.Part;
import ben.partparty.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

/**
 * ModifyProductController
 * */

public class ModifyProductController extends AddProductController {
    @FXML
    private TableView<Part> associatedPartTable;
    private final ObservableList<Part> temporaryAssociatedParts = FXCollections.observableArrayList();
    private int selectedIndex;

    /** Passes an Index and Product from MainViewController to be used by ModifyProductController
     * @param index integer index of the selected value
     * @param selection Product object selected
     * */
    @FXML
    public void passSelectedProduct(int index, Product selection) {
        System.out.println(selection);
        selectedIndex = index;
        setFields(selection);
        product = selection;
        temporaryAssociatedParts.addAll(product.getAllAssociatedParts());
        associatedPartTable.setItems(temporaryAssociatedParts);
    }

    /** Called when save button is selected
     * @param save ActionEvent of save click
     * */
    public void OnModifyProductSave(ActionEvent save) {
            try {
                testFields();
                Inventory.updateProduct(selectedIndex, saveItem(product));
                product.getAllAssociatedParts().setAll(temporaryAssociatedParts);
                loadFXML(save, "/view/MainView.fxml");
            }
            catch (Exception exception) {
                displayErrorMessage(exception);
            }

    }
}