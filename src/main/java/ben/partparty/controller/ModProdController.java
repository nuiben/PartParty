package ben.partparty.controller;

import ben.partparty.model.InHouse;
import ben.partparty.model.Inventory;
import ben.partparty.model.Part;
import ben.partparty.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModProdController extends AddProdController {

    @FXML
    private TableView<Part> assocPartTable;
    private ObservableList<Part> tempAssociatedParts = FXCollections.observableArrayList();
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
        tempAssociatedParts.addAll(product.getAssociatedParts());
        assocPartTable.setItems(tempAssociatedParts);
    }

    public void OnSave(ActionEvent save) throws IOException {
        try {
            Inventory.updateProduct(selectedIndex, saveProd(product));
            setStage(save, fxmlLoad("/view/mainView.fxml"));
        } catch (NumberFormatException exception) {
            emptyFieldError();
        }

    }
}