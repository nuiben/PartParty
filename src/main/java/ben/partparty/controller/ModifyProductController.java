package ben.partparty.controller;

import ben.partparty.model.Inventory;
import ben.partparty.model.Part;
import ben.partparty.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.io.IOException;

public class ModifyProductController extends AddProductController {

    @FXML
    private TableView<Part> associatedPartTable;
    @FXML
    private TableView<Part> partTable;
    private final ObservableList<Part> temporaryAssociatedParts = FXCollections.observableArrayList();
    public int selectedIndex;

    @FXML
    public void passSelectedProduct(int idx, Product selection) {
        System.out.println("Passed the Product: " + selection);
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
                if (Integer.parseInt(minTextBox.getText()) > Integer.parseInt(maxTextBox.getText())) {
                    throw new RuntimeException("Minimum value exceeds Maximum value.");
                } else if (Integer.parseInt(invTextBox.getText()) < Integer.parseInt(minTextBox.getText()) || Integer.parseInt(invTextBox.getText()) > Integer.parseInt(maxTextBox.getText())) {
                    throw new RuntimeException("Inventory must be between Minimum and Maximum values.");
                } else if (Integer.parseInt(invTextBox.getText()) < 0 || Integer.parseInt(minTextBox.getText()) < 0 || Integer.parseInt(maxTextBox.getText()) < 0 || Double.parseDouble(priceTextBox.getText()) < 0) {
                    throw new RuntimeException("Values must be non-negative.");
                } else {
                    Inventory.updateProduct(selectedIndex, saveFieldValues(product));
                    setStage(save, fxmlLoad("/view/MainView.fxml"));
                }
            } catch (Exception exception) {
                displayErrorMessage(exception);
            }

    }
}