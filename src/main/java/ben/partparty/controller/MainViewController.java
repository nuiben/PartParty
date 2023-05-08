package ben.partparty.controller;

import ben.partparty.main.Main;
import ben.partparty.model.Part;
import ben.partparty.model.Product;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ben.partparty.model.Inventory;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MainViewController implements Initializable  {
    @FXML
    public Label theLabel;
    @FXML
    private TableView<Part> partTable;
    @FXML
    private TableColumn<Part, Integer> partPartID;
    @FXML
    private TableColumn<Part, String> partPartName;
    @FXML
    private TableColumn<Part, Integer> partInvLevel;
    @FXML
    private TableColumn<Part, Double> partPriceCost;
    @FXML
    private TableView<Product> prodTable;
    @FXML
    private TableColumn<Part, Integer> prodProdID;
    @FXML
    private TableColumn<Part, String> prodProdName;
    @FXML
    private TableColumn<Part, Integer> prodInvLevel;
    @FXML
    private TableColumn<Part, Double> prodPriceCost;
    @FXML
    private TextField partSearch;
    @FXML
    private TextField prodSearch;
    @FXML
    private FilteredList<Part> filteredParts;
    private FilteredList<Product> filteredProducts;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(url);
        partTable.setItems(Inventory.getAllParts());
        partPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCost.setCellValueFactory(new PropertyValueFactory<>("price"));

        prodTable.setItems(Inventory.getAllProducts());
        prodProdID.setCellValueFactory(new PropertyValueFactory<>("id"));
        prodProdName.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodInvLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        prodPriceCost.setCellValueFactory(new PropertyValueFactory<>("price"));

        filteredParts = new FilteredList<>(Inventory.getAllParts(), p -> true);
        filteredProducts = new FilteredList<>(Inventory.getAllProducts(), p -> true);

        partSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredParts.setPredicate(part -> {
                if (newValue == null || newValue.isEmpty()) { return true; }
                String lowerCaseFilter = newValue.toLowerCase();
                if (part.getName().toLowerCase().contains(lowerCaseFilter)) { return true;
                } else return Integer.toString(part.getId()).contains(lowerCaseFilter);
            });
        });
            SortedList<Part> sortedParts = new SortedList<>(filteredParts);
            sortedParts.comparatorProperty().bind(partTable.comparatorProperty());
            partTable.setItems(sortedParts);

        prodSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredProducts.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty()) { return true; }
                String lowerCaseFilter = newValue.toLowerCase();
                if (product.getName().toLowerCase().contains(lowerCaseFilter)) { return true;
                } else return Integer.toString(product.getId()).contains(lowerCaseFilter);
            });
        });
            SortedList<Product> sortedProducts = new SortedList<>(filteredProducts);
            sortedProducts.comparatorProperty().bind(partTable.comparatorProperty());
            prodTable.setItems(sortedProducts);

    }

    public void OnAddPartButtonClicked(ActionEvent addPart) throws IOException { setStage(addPart, fxmlLoad("/view/AddPart.fxml")); }
    public void OnAddProdButtonClicked(ActionEvent addProd) throws IOException { setStage(addProd, fxmlLoad("/view/AddProd.fxml")); }
    public void OnModPartButtonClicked(ActionEvent modPart) throws IOException {
        try {
            FXMLLoader loader = fxmlLoad("/view/ModPart.fxml");
            ((ModPartController) loader.getController()).passSelectedPart(partTable.getSelectionModel().getSelectedIndex(),partTable.getSelectionModel().getSelectedItem());
            setStage(modPart, loader);
        }
        catch (NullPointerException exception) { noSelError(); }
    }
    public void OnModProdButtonClicked(ActionEvent modProd) throws IOException{
        try {
            FXMLLoader loader = fxmlLoad("/view/ModProd.fxml");
            ((ModProdController) loader.getController()).passSelectedProduct(prodTable.getSelectionModel().getSelectedIndex(),prodTable.getSelectionModel().getSelectedItem());
            setStage(modProd, loader);
        }
        catch (NullPointerException exception) { noSelError(); }
    }

    public void OnDelPart() {
        try { Inventory.deletePart(partTable.getSelectionModel().getSelectedItem()); }
        catch (NullPointerException exception) { noSelError(); }
    }

    public void OnDelProd(ActionEvent deleteProduct) throws IOException{
        try { Inventory.deleteProduct(prodTable.getSelectionModel().getSelectedItem()); }
        catch (NullPointerException exception) { noSelError(); }
    }

    public void noSelError() {
        Alert errorMessage = new Alert(Alert.AlertType.ERROR);
        errorMessage.setTitle("Error Message");
        errorMessage.setContentText("No selection detected. Please select an item.");
        errorMessage.show();
    }
    public FXMLLoader fxmlLoad(String resource) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(resource));
        fxmlLoader.load();
        return fxmlLoader;
    }
    public void setStage(ActionEvent load, FXMLLoader loader) {
        Stage stage = (Stage) ((Button)load.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.getRoot()));
        stage.show();
    }
    public void OnExitButtonClicked() {
        Main.quit();
    }
}