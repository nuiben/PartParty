package ben.partparty.controller;

import ben.partparty.main.Main;
import ben.partparty.model.*;
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


public class MainViewController implements Initializable {
    @FXML
    private TableView<Part> partTable;
    @FXML
    private TableColumn<Part, Integer> partID;
    @FXML
    private TableColumn<Part, String> partName;
    @FXML
    private TableColumn<Part, Integer> partStock;
    @FXML
    private TableColumn<Part, Double> partPrice;
    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Part, Integer> productID;
    @FXML
    private TableColumn<Part, String> productName;
    @FXML
    private TableColumn<Part, Integer> productStock;
    @FXML
    private TableColumn<Part, Double> productPrice;
    @FXML
    private TextField partSearch;
    @FXML
    private TextField productSearch;
    @FXML
    private FilteredList<Part> filteredParts;
    private FilteredList<Product> filteredProducts;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(url);
        if (partTable != null) { loadPartTable();}
        if (productTable != null) { loadProductTable(); }
    }

    public void loadPartTable() {
        partTable.setItems(Inventory.getAllParts());
        partID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        filteredParts = new FilteredList<>(Inventory.getAllParts(), p -> true);
        partSearch.textProperty().addListener((observable, oldValue, newValue) -> filteredParts.setPredicate(part -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = newValue.toLowerCase();
            if (part.getName().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else return Integer.toString(part.getId()).contains(lowerCaseFilter);
        }));
        SortedList<Part> sortedParts = new SortedList<>(filteredParts);
        sortedParts.comparatorProperty().bind(partTable.comparatorProperty());
        partTable.setItems(sortedParts);
    }
    public void loadProductTable() {
        productTable.setItems(Inventory.getAllProducts());
        productID.setCellValueFactory(new PropertyValueFactory<>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        filteredProducts = new FilteredList<>(Inventory.getAllProducts(), p -> true);
        productSearch.textProperty().addListener((observable, oldValue, newValue) -> filteredProducts.setPredicate(product -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = newValue.toLowerCase();
            if (product.getName().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else return Integer.toString(product.getId()).contains(lowerCaseFilter);
        }));
        SortedList<Product> sortedProducts = new SortedList<>(filteredProducts);
        sortedProducts.comparatorProperty().bind(partTable.comparatorProperty());
        productTable.setItems(sortedProducts);
    }
    public void OnAddPart(ActionEvent addPart) throws IOException {
        setStage(addPart, fxmlLoad("/view/AddPartView.fxml"));
    }
    public void OnAddProduct(ActionEvent addProduct) throws IOException {
        setStage(addProduct, fxmlLoad("/view/AddProductView.fxml"));
    }

    public void OnModifyPart(ActionEvent modifyPart) {
        try {
            FXMLLoader loader = fxmlLoad("/view/ModifyPartView.fxml");
            ((ModifyPartController) loader.getController()).passSelectedPart(partTable.getSelectionModel().getSelectedIndex(),partTable.getSelectionModel().getSelectedItem());
            setStage(modifyPart, loader);
        } catch (Exception exception) {
            displayErrorMessage(exception);
        }
    }
    public void OnModifyProduct(ActionEvent modifyProduct) {
        try {
            FXMLLoader loader = fxmlLoad("/view/ModifyProductView.fxml");
            ((ModifyProductController) loader.getController()).passSelectedProduct(productTable.getSelectionModel().getSelectedIndex(),productTable.getSelectionModel().getSelectedItem());
            setStage(modifyProduct, loader);
        } catch (Exception exception) {
            displayErrorMessage(exception);
        }
    }

    public void OnDeletePart() {
        try {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Delete Part");
            confirmation.setContentText("Are you sure you want to delete this part?\n" + partTable.getSelectionModel().getSelectedItem().getName());
            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.get() == ButtonType.OK) {
                Inventory.deletePart(partTable.getSelectionModel().getSelectedItem());
            }
        } catch (Exception exception) {
            displayErrorMessage(exception);
        }
    }
    public void OnDeleteProduct() {
        try {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Delete Product");
            confirmation.setContentText("Are you sure you want to delete this product?\n" + productTable.getSelectionModel().getSelectedItem().getName());

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.get() == ButtonType.OK) {
                Inventory.deleteProduct(productTable.getSelectionModel().getSelectedItem());
            }

        } catch (Exception exception) {
            displayErrorMessage(exception);
        }
    }
    public void OnExitButtonClicked() {
        Main.quit();
    }

    // Helper Functions
    public void displayErrorMessage(Exception exception) {
        Alert errorMessage = new Alert(Alert.AlertType.ERROR);
        errorMessage.setTitle("Error Message");
        errorMessage.setContentText(exception.getClass() +"\n"+ exception.getMessage());
        errorMessage.show();
    }
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