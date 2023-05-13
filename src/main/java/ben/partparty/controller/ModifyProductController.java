package ben.partparty.controller;

import ben.partparty.model.Inventory;
import ben.partparty.model.Part;
import ben.partparty.model.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

/**
 * ModifyProductController - Child class.
 * */
public class ModifyProductController extends AddProductController {
    @FXML
    private TableView<Part> associatedPartTable;
    private int selectedIndex;
    private Product product;

    /** Passes an Index and Product from MainViewController to be used by ModifyProductController
     * @param index integer index of the selected value
     * @param selection Product object selected
     * */
    @FXML
    public void passSelectedProduct(int index, Product selection) {
        System.out.println(selection);
        selectedIndex = index;
        setProductFields(selection);
        temporaryAssociatedParts.addAll(selection.getAllAssociatedParts());
        associatedPartTable.setItems(temporaryAssociatedParts);
        product = selection;
    }

    /** Called when save button is selected
     * @param save ActionEvent of save click
     *
     * @RUNTIME_ERROR Products are not being saved properly after refactoring and changing variables
     * from public to private.
     * */
    public void OnModifyProductSave(ActionEvent save) {
            try {
                testFields();
                Inventory.updateProduct(selectedIndex, saveProduct(product));
                product.getAllAssociatedParts().setAll(temporaryAssociatedParts);
                System.out.println(product.getAllAssociatedParts().toString());
                loadFXML(save, "/view/MainView.fxml");
            }
            catch (Exception exception) {
                displayErrorMessage(exception);
            }

    }
}