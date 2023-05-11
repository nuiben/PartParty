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
        if (partTable != null) { loadTable(Inventory.getAllParts(), partTable, partSearch);}
        if (productTable != null) { loadTable(Inventory.getAllProducts(), productTable, productSearch); }
    }

    /** Loads a Table and it's search bar
     * */
    public <E extends Part> void loadTable(ObservableList<E> items, TableView<E> table, TextField searchBox) {
        table.setItems(items);
        int index = 0;
        for (String var : COLUMNS) {
            table.getColumns().get(index++).setCellValueFactory(new PropertyValueFactory<>(var));
        }
        FilteredList<E> filteredItems = new FilteredList<>(table.getItems(), p -> true);
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> filteredItems.setPredicate(item -> {
            if (newValue == null || newValue.isEmpty()) { return true; }
            String lowerCaseFilter = newValue.toLowerCase();
            if (item.getName().toLowerCase().contains(lowerCaseFilter)) { return true; }
            else return Integer.toString(item.getId()).contains(lowerCaseFilter);}));
        SortedList<E> sortedItems = new SortedList<>(filteredItems);
        sortedItems.comparatorProperty().bind(partTable.comparatorProperty());
        table.setItems(sortedItems);
    }
    public void OnAddPart(ActionEvent addPart) throws IOException {
        setStage(addPart, fxmlLoad("/view/AddPartView.fxml"));
    }
    public void OnAddProduct(ActionEvent addProduct) throws IOException {
        setStage(addProduct, fxmlLoad("/view/AddProductView.fxml"));
    }
    public void OnModifyPart(ActionEvent modifyPart) {
        modifyItem(partTable, modifyPart, "/view/ModifyPartView.fxml");
    }
    public void OnModifyProduct(ActionEvent modifyProduct) {
        modifyItem(productTable, modifyProduct, "/view/ModifyProductView.fxml");
    }
    public <T extends Part> void modifyItem(TableView<T> table, ActionEvent event, String resource) {
        try {
            FXMLLoader loader = fxmlLoad(resource);
            int selectedIndex = table.getSelectionModel().getSelectedIndex();
            T item = table.getSelectionModel().getSelectedItem();
            if (item instanceof Product) {
                ((ModifyProductController) loader.getController()).passSelectedProduct(selectedIndex, (Product) item);
            } else {
                ((ModifyPartController) loader.getController()).passSelectedPart(selectedIndex, item);
            }
            setStage(event, loader);
        } catch (Exception exception) {
            displayErrorMessage(exception);
        }
    }
    public void OnDeletePart() { deleteItem(partTable); }
    public void OnDeleteProduct() { deleteItem(productTable); }
    public <T extends Part> void deleteItem(TableView<T> table) {
        try {
            if (displayConfirmation(table).get() == ButtonType.OK) {
                if (table.getSelectionModel().getSelectedItem() instanceof Product) {
                    Inventory.deleteProduct((Product) table.getSelectionModel().getSelectedItem());
                }
                else {
                    Inventory.deletePart(table.getSelectionModel().getSelectedItem());
                }
            }
        }
        catch(Exception exception) {
            displayErrorMessage(exception);
        }

    }
    public void OnExitButtonClicked() { Main.quit(); }

    // Helper Functions
    public void displayErrorMessage(Exception exception) {
        Alert errorMessage = new Alert(Alert.AlertType.ERROR);
        errorMessage.setTitle("Error Message");
        errorMessage.setContentText(exception.getClass() +"\n"+ exception.getMessage());
        errorMessage.show();
    }

    public <T extends Part> Optional<ButtonType> displayConfirmation(TableView<T> table) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Item");
        confirmation.setContentText("Are you sure you want to delete this item?\n" +
                table.getSelectionModel().getSelectedItem().getName());
        return confirmation.showAndWait();
    }

    /** Loads an FXML file given a provided resource.
     *
     * @param resource String
     * @return fxmlLoader FXMLLoader
     * @throws IOException
     */
    public FXMLLoader fxmlLoad(String resource) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(resource));
        fxmlLoader.load();
        return fxmlLoader;
    }

    public void setStage(ActionEvent load, FXMLLoader loader) {
        Stage stage = (Stage) ((Button) load.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.getRoot()));
        stage.show();
    }
}