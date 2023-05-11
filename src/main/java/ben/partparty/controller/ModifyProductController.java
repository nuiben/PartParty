package ben.partparty.controller;

import ben.partparty.model.Inventory;
import ben.partparty.model.Part;
import ben.partparty.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
public class ModifyProductController extends AddProductController {

    @FXML
    private TableView<Part> associatedPartTable;
    private final ObservableList<Part> temporaryAssociatedParts = FXCollections.observableArrayList();
    private int selectedIndex;

    @FXML
    public void passSelectedProduct(int idx, Product selection) {
        selectedIndex = idx;
        idTextBox.setText(String.valueOf(selection.getId()));
        nameTextBox.setText(String.valueOf(selection.getName()));
        invTextBox.setText(String.valueOf(selection.getStock()));
        priceTextBox.setText(String.valueOf(selection.getPrice()));
        maxTextBox.setText(String.valueOf(selection.getMax()));
        minTextBox.setText(String.valueOf(selection.getMin()));
        product = selection;
        temporaryAssociatedParts.addAll(product.getAllAssociatedParts());
        associatedPartTable.setItems(temporaryAssociatedParts);
    }

    public void OnModifyProductSave(ActionEvent save) {
            try {
                testFields();
                Inventory.updateProduct(selectedIndex, saveFieldValues(product));
                setStage(save, fxmlLoad("/view/MainView.fxml"));
            }
            catch (Exception exception) {
                displayErrorMessage(exception);
            }

    }
}