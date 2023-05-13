package ben.partparty.controller;

import ben.partparty.main.Main;
import ben.partparty.model.*;
import javafx.collections.FXCollections;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** Primary Controller - Initiates Child Controllers to handle each FXML file in the view directory.
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

    /**
     * Default Class Constructor - checks if tables exist before attempting to load.
     * @param url filepath of this FXML.
     * @param resourceBundle location of FXML files.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(url);
        if (partTable != null) { loadPartTable();}
        if (productTable != null) { loadProductTable(); }
    }

    /** Loads a TableView, binds contents to a SortedList which displays
     *  Search Results input to Table's search bar.
     * */
    public void loadPartTable() {
        this.partTable.setItems(Inventory.getAllParts());
        int index = 0;
        for (String var : COLUMNS) {
            partTable.getColumns().get(index++).setCellValueFactory(new PropertyValueFactory<>(var));
        }
        TextField searchBar = partSearch;
        FilteredList<Part> filteredItems = new FilteredList<>(partTable.getItems(), p -> true);
        searchBar.textProperty().addListener((input, previousInput, newInput) -> filteredItems.setPredicate(item -> {
            if (newInput == null || newInput.isEmpty()) { return true; }
            String lowerCase = newInput.toLowerCase();
            if (item.getName().toLowerCase().contains(lowerCase)) { return true; }
            else return Integer.toString(item.getId()).contains(lowerCase);}));
        SortedList<Part> sortedItems = new SortedList<>(filteredItems);
        sortedItems.comparatorProperty().bind(partTable.comparatorProperty());
        partTable.setItems(sortedItems);

    }

    /** Overloaded Method for Product Table
     *  Search Results input to Table's search bar.
     * */
    public void loadProductTable() {
        TextField searchBar = productSearch;
        productTable.setItems(Inventory.getAllProducts());
        int index = 0;
        for (String var : COLUMNS) {
            productTable.getColumns().get(index++).setCellValueFactory(new PropertyValueFactory<>(var));
        }
        FilteredList<Product> filteredItems = new FilteredList<>(productTable.getItems(), p -> true);
        searchBar.textProperty().addListener((input, previousInput, newInput) -> filteredItems.setPredicate(item -> {
            if (newInput == null || newInput.isEmpty()) { return true; }
            String lowerCase = newInput.toLowerCase();
            if (item.getName().toLowerCase().contains(lowerCase)) { return true; }
            else return Integer.toString(item.getId()).contains(lowerCase);}));
        SortedList<Product> sortedItems = new SortedList<>(filteredItems);
        sortedItems.comparatorProperty().bind(productTable.comparatorProperty());
        productTable.setItems(sortedItems);
    }

    /** Loads AddPartView when addPart button is clicked.
     * @param addPart ActionEvent containing UID of a click on Add button underneath Part Table
     * @throws IOException if the FXML could not be found
     * @RUNTIME_ERROR Throws an error when resource file has not been installed.
     * */
    public void OnAddPart(ActionEvent addPart) throws IOException {
        loadFXML(addPart, "/view/AddPartView.fxml");
    }
    /** Loads AddProductView when addPart button is clicked.
     * @param addProduct ActionEvent containing UID of a click on Add button underneath Product Table
     * @throws IOException if the FXML could not be found
     * @RUNTIME_ERROR Throws an error when resource file has not been installed.
     *
     * */
    public void OnAddProduct(ActionEvent addProduct) throws IOException {
        loadFXML(addProduct, "/view/AddProductView.fxml");
    }
    /** Loads ModifyProductView or ModifyPartView depending on which is clicked.
     * @param modifyEvent ActionEvent containing UID of click, allows method to determine which view to open
     * @throws NullPointerException if no selection is indicated.
     *      @RUNTIME_ERROR If an item has not been clicked inside the matching table to the "modify" button
     *      clicked, the system will not continue to loading the corresponding FXML file.
     * */
    public void OnModifyItem(ActionEvent modifyEvent) throws NullPointerException{
        try {
            String resource;
            if (((Button) modifyEvent.getSource()).getId().contains("Product")) {
                if (productTable.getSelectionModel().getSelectedItem() == null) {
                    throw new NullPointerException("No Selection Indicated");
                }
                resource = "/view/ModifyProductView.fxml";
            } else {
                if (partTable.getSelectionModel().getSelectedItem() == null) {
                    throw new NullPointerException("No Selection Indicated");
                }
                resource = "/view/ModifyPartView.fxml";
            }
            loadFXML(modifyEvent, resource);
        } catch (Exception exception) {
            displayErrorMessage(exception);
        }
    }
    /** Handles deletion of either Part or Product
     * @param delete the ActionEvent containing UID of click, allows method to determine table to check for a selection
     *  The inclusion of generics significantly reduced reused codes which were identical for both parts and products.
     *
     * @throws NullPointerException if no selection is indicated
     *  @RUNTIME_ERROR Before the button ID check placed in the line under the displayConfirmation, the method calls to this function
     *  were very verbose and required their own FXML loader, oftentimes this would create NullPointer and IOExceptions
     *  that required additional code to check for. The simple getId() function cut down on 10-15 lines of code and resolved
     *  the core issue.
     * @RUNTIME_ERROR  if a Product still has associated parts when selected for deletion.
     * @FUTURE_ENHANCEMENT A check prior to deletion which prevents the user from deleting a part without first
     * removing it as an associated part from a product could be added if parts were updated to include a list of
     * "Associated Products". An implementation of this without updating part would create a runtime complexity of
     * O(N^2) as a part would have to check all Products for all Asssociated Parts lists to confirm if it exists
     * before proceeding to delete.
     */
    public void OnDeleteItem(ActionEvent delete) throws NullPointerException {
        try {
            if (((Button) delete.getSource()).getId().contains("Product")) {
                Product doomedProduct = productTable.getSelectionModel().getSelectedItem();
                if (doomedProduct == null){
                    throw new NullPointerException("No selection indicated");
                } else if (!doomedProduct.getAllAssociatedParts().isEmpty()) {
                    throw new RuntimeException("This Product still has Associated Parts");
                }
                if (displayConfirmation(doomedProduct.getName()).get() == ButtonType.OK) {
                    if (Inventory.deleteProduct(doomedProduct)) {
                        Alert deleteConfirmation = new Alert(Alert.AlertType.INFORMATION);
                        deleteConfirmation.setTitle("Product Deleted");
                        deleteConfirmation.setContentText("Product ID: " + doomedProduct.getId() + " \"" + doomedProduct.getName() + "\" was deleted.");
                        deleteConfirmation.show();
                    }
                }
            } else if (partTable.getSelectionModel().getSelectedItem() == null) {
                throw new NullPointerException("No selection indicated");
            } else {
                Part doomedPart = partTable.getSelectionModel().getSelectedItem();
                if(displayConfirmation(doomedPart.getName()).get() == ButtonType.OK) {
                    if (Inventory.deletePart(doomedPart)) {
                        Alert deleteConfirmation = new Alert(Alert.AlertType.INFORMATION);
                        deleteConfirmation.setTitle("Part Deleted");
                        deleteConfirmation.setContentText("Part ID: " + doomedPart.getId() + " \"" + doomedPart.getName() + "\" was deleted.");
                        deleteConfirmation.show();
                    }
                }
            }
        } catch(Exception exception) {
            displayErrorMessage(exception);
        }
    }

    /**
     * Event Handler for Main Screen Exit Button.
     */
    public void OnExitButtonClicked() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Close Application");
        confirmation.setContentText("Are you sure you want Exit?");
        if (confirmation.showAndWait().get() == ButtonType.OK) {
            Main.quit();
        }
    }

    /** Generic Error Message which reports the exception to the user
     * @param exception Any exception type
     */
    public void displayErrorMessage(Exception exception) {
        Alert errorMessage = new Alert(Alert.AlertType.ERROR);
        errorMessage.setTitle("Error Message");
        errorMessage.setContentText(exception.getMessage());
        errorMessage.show();
    }

    /** Generic Confirmation Message which asks the user to confirm a remove action
     * @param itemName the String name associated with the Delete or Remove Associated Part button.
     */
    public Optional<ButtonType> displayConfirmation(String itemName) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        if (itemName.isEmpty()) {
            confirmation.setTitle("Cancel");
            confirmation.setContentText("Cancel and Return to Main Screen?\nAll Unsaved Progress will be lost");
        } else if (itemName.contains("save")){
            confirmation.setTitle("Save Item");
            confirmation.setContentText("Do you wish to save this item?");
        } else {
            confirmation.setTitle("Delete Item");
            confirmation.setContentText("Are you sure you want to delete this item?\n" + itemName);
        }
        return confirmation.showAndWait();
    }

    /** Loads an FXML file given a provided resource. When loading ModifyPartView or
     *  ModifyProductView, the selected item has to be passed from the instance of
     *  MainViewController to an instance of either Modify View.
     *
     * @param resource String URL of the FXML filepath
     * @param event  ID of the Button click which initiated a loadFXML method call.
     * @throws IOException if FXML is not found.
     *      @RUNTIME_ERROR A significant logical bug was created by the Modify Controllers which required
     *      the use of a FXMLLoader to receive the selection and index from the MainViewController.
     *      The inclusion of the cast type allowed for the Modify event handlers to use this method
     *      without instantiating their own loaders a minor behavior change.
     *
     * @FUTURE_ENHANCEMENT Relying on the FXML loader constrained normal object-oriented principals,
     * exploring alternative options for displaying a GUI would greatly reduce redundant calls and methods,
     * potentially allowing for all Controllers to be consolidated instead of using inheritance.
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

    /**
     * This event handler responds to any key pressed while using one of the
     * Part or Product search bars. If the "ENTER" key is selected, then the
     * System will collect the input text, attempt to match it as an integer
     * to an existing Part (or Product depending on the source), if the parse
     * fails, it is tried as a String. Null values returned will cause a
     * "No Results Found" message, a partial text completion will select the
     * first result displayed by the SortedList.
     *
     * @param keyEvent source object identifying which FXML object is being used.
     * @throws NullPointerException If a search result returns "NULL"
     *      @RUNTIME_ERROR System presents an error message if there is not a matching
     *      search result to the text inside the TextField Object.
     * */
    @FXML
    public void Enter(KeyEvent keyEvent) throws NullPointerException{
        try {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                String source = ((TextField) keyEvent.getSource()).getId();
                String query;
                ObservableList<Product> searchProduct = FXCollections.observableArrayList();
                if (source.contains("product")) {
                    query = productSearch.getText();
                } else {
                    query = partSearch.getText();
                }
                ObservableList<Part> searchPart = FXCollections.observableArrayList();
                int queryParsed;
                if (query.isEmpty()) {
                    throw new NumberFormatException("Search Field Empty: Please Type an ID or Name");
                }
                try {
                    queryParsed = Integer.parseInt(query);
                    if (source.contains("product")) {
                        searchProduct.add(Inventory.lookupProduct(queryParsed));
                        if (searchProduct.isEmpty()) {
                            throw new NullPointerException("Part Not Found");
                        } else {
                            productTable.getSelectionModel().select(searchProduct.get(0));
                        }
                    } else {
                        searchPart.add(Inventory.lookupPart(queryParsed));
                        if (searchPart.isEmpty()) {
                            throw new NullPointerException("Part Not Found");
                        } else {
                            partTable.getSelectionModel().select(searchPart.get(0));
                        }
                    }

                }
                catch (NumberFormatException exception) {
                    if (source.contains("product")) {
                        searchProduct = Inventory.lookupProduct(query);
                        if (searchProduct.isEmpty() && productTable.getItems().isEmpty()) {
                            throw new NullPointerException("Part Not Found");
                        } else {
                            productTable.getSelectionModel().select(productTable.getItems().get(0));
                        }
                    } else {
                        searchPart = Inventory.lookupPart(query);
                        if (searchPart.isEmpty() && partTable.getItems().isEmpty()) {
                            throw new NullPointerException("Part Not Found");
                        } else {
                            partTable.getSelectionModel().select(partTable.getItems().get(0));
                        }
                    }
                }
            }
        } catch(Exception exception) {
            displayErrorMessage(exception);
        }

    }
}