package ben.partparty.controller;

import ben.partparty.main.Main;
import ben.partparty.model.*;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** Primary Controller - Initiates Child Controllers to handle each FXML file in the view directory.
 *  The decision to use inheritance for the Controller, is due to the constraints placed by JavaFX.
 *  A Controller would normally handle all endpoints between the User (Client) and Model (Server), but
 *  FXML files load new instances of the controller, making inheritance necessary for the different
 *  instances to interface and share data.
 *  MainViewController -> AddPartController -> AddProductController -> ModifyProductController
 *                                ^-> ModifyPartController
 *  */
public class MainViewController implements Initializable {
    @FXML
    private TableView<Part> partTable;
    @FXML
    private TableView<Product> productTable;
    @FXML
    private TextField partSearch;
    @FXML
    private TextField productSearch;
    private final String[] COLUMNS = {"id", "name", "stock", "price"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(url);
        if (partTable != null) { loadTable(partTable);}
        if (productTable != null) { loadTable(productTable); }
    }

    /** Loads a TableView, binds contents to a SortedList which displays
     *  Search Results input to Table's search bar.
     * @param table TableView to be loaded
     * */
    public <E extends Part> void loadTable(TableView<E> table) {
        TextField searchBar = partSearch;
        if (table.getId().contains("part")) {
            table.setItems((ObservableList<E>) Inventory.getAllParts());
        } else {
            table.setItems((ObservableList<E>) Inventory.getAllProducts());
            searchBar = productSearch;
        }
        int index = 0;
        for (String var : COLUMNS) {
            table.getColumns().get(index++).setCellValueFactory(new PropertyValueFactory<>(var));
        }
        FilteredList<E> filteredItems = new FilteredList<>(table.getItems(), p -> true);
        searchBar.textProperty().addListener((input, previousInput, newInput) -> filteredItems.setPredicate(item -> {
            if (newInput == null || newInput.isEmpty()) { return true; }
            String lowerCase = newInput.toLowerCase();
            if (item.getName().toLowerCase().contains(lowerCase)) { return true; }
            else return Integer.toString(item.getId()).contains(lowerCase);}));
        SortedList<E> sortedItems = new SortedList<>(filteredItems);
        sortedItems.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedItems);
    }

    /** Loads AddPartView when addPart button is clicked.
     * @param addPart ActionEvent containing UID of a click on Add button underneath Part Table
     * */
    public void OnAddPart(ActionEvent addPart) throws IOException {
        loadFXML(addPart, "/view/AddPartView.fxml");
    }
    /** Loads AddProductView when addPart button is clicked.
     * @param addProduct ActionEvent containing UID of a click on Add button underneath Product Table
     * */
    public void OnAddProduct(ActionEvent addProduct) throws IOException {
        loadFXML(addProduct, "/view/AddProductView.fxml");
    }
    /** Loads ModifyProductView or ModifyPartView depending on which is clicked.
     * @param modifyEvent ActionEvent containing UID of click, allows method to determine which view to open
     * */
    public void OnModifyItem(ActionEvent modifyEvent) {
        try {
            String resource;
            if (((Button) modifyEvent.getSource()).getId().contains("Product")) {
                resource = "/view/ModifyProductView.fxml";
            } else {
                resource = "/view/ModifyPartView.fxml";
            }
            loadFXML(modifyEvent, resource);
        } catch (Exception exception) {
            displayErrorMessage(exception);
        }
    }
    /** Handles deletion of either Part or Product
     * @param delete the ActionEvent containing UID of click, allows method to determine table to check for a selection
     */
    public <T extends Part> void OnDeleteItem(ActionEvent delete) {
        try {
            if (displayConfirmation(productTable).get() == ButtonType.OK) {
                if (((Button) delete.getSource()).getId().contains("Product")) {
                    Inventory.deleteProduct(productTable.getSelectionModel().getSelectedItem());
                } else {
                    Inventory.deletePart(partTable.getSelectionModel().getSelectedItem());
                }
            }
        } catch(Exception exception) {
            displayErrorMessage(exception);
        }
    }
    public void OnExitButtonClicked() { Main.quit(); }

    /** Generic Error Message which reports the exception to the user
     * @param exception Any exception type
     */
    public void displayErrorMessage(Exception exception) {
        Alert errorMessage = new Alert(Alert.AlertType.ERROR);
        errorMessage.setTitle("Error Message");
        errorMessage.setContentText(exception.getClass() +"\n"+ exception.getMessage());
        errorMessage.show();
    }

    /** Generic Confirmation Message which asks the user to confirm a remove action
     * @param table the TableView associated with the Delete or Remove Associated Part button.
     */
    public <T extends Part> Optional<ButtonType> displayConfirmation(TableView<T> table) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Item");
        confirmation.setContentText("Are you sure you want to delete this item?\n" +
                table.getSelectionModel().getSelectedItem().getName());
        return confirmation.showAndWait();
    }

    /** Loads an FXML file given a provided resource.
     *  When loading ModifyPartView or ModifyProductView, the selected item
     *  has to be passed from the instance of MainViewController to an instance of
     *  either Modify View.
     *
     * @param resource String URL of the FXML filepath
     * @param event  ID of the Button click which initiated a loadFXML method call.
     * @throws IOException if FXML is not found.
     */
    public void loadFXML(ActionEvent event, String resource) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(resource));
        fxmlLoader.load();
        if (resource.contains("ModifyProduct")) {
            ((ModifyProductController) fxmlLoader.getController()).passSelectedProduct(
                    productTable.getSelectionModel().getSelectedIndex(),
                    productTable.getSelectionModel().getSelectedItem());
        } else if(resource.contains("ModifyPart")) {
            ((ModifyPartController) fxmlLoader.getController()).passSelectedPart(
                    partTable.getSelectionModel().getSelectedIndex(),
                    partTable.getSelectionModel().getSelectedItem());
        }
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.getRoot()));
        stage.show();
    }
}