package ben.partparty.controller;

import ben.partparty.model.*;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddProdController extends AddPartController{

    public Button addAssocPartButton;
    public Button saveProdButton;
    @FXML
    private TableView<Part> partTable;
    @FXML
    private TableColumn<Part, Integer> partID;
    @FXML
    private TableColumn<Part, String> partName;
    @FXML
    private TableColumn<Part, Integer> partStock;
    @FXML
    private TableColumn<Part, Double> partCost;

    @FXML
    private TableView<Part> assocPartTable;
    @FXML
    private TableColumn<Part, Integer> assocPartID;
    @FXML
    private TableColumn<Part, String> assocPartName;
    @FXML
    private TableColumn<Part, Integer> assocPartStock;
    @FXML
    private TableColumn<Part, Double> assocPartCost;
    @FXML
    private TextField partSearch;
    private FilteredList<Part> filteredParts;
    private ObservableList<Part> tempAssociatedParts = FXCollections.observableArrayList();
    public Product product;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(url);
        partTable.setItems(Inventory.getAllParts());
        partID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        assocPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartCost.setCellValueFactory(new PropertyValueFactory<>("price"));

        filteredParts = new FilteredList<>(Inventory.getAllParts(), p -> true);

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
        }


    public void OnSave(ActionEvent save) throws IOException {
        if (product == null) {
            try {
                product = newProd();
                Inventory.addProduct(product);
                setStage(save, fxmlLoad("/view/mainView.fxml"));
            } catch (NumberFormatException exception) { emptyFieldError(); }
        } else {
            product.getAssociatedParts().addAll(tempAssociatedParts);
            Inventory.addProduct(product);
            setStage(save, fxmlLoad("/view/mainView.fxml"));
        }
    }

    public void OnAddAssocPart(ActionEvent add) throws NumberFormatException {

        if (product == null) {
            try {
                product = newProd();
                tempAssociatedParts.add(partTable.getSelectionModel().getSelectedItem());
                assocPartTable.setItems(tempAssociatedParts);
            } catch (NumberFormatException exception) { emptyFieldError(); }
        } else {
            tempAssociatedParts.add(partTable.getSelectionModel().getSelectedItem());
            assocPartTable.setItems(tempAssociatedParts);
        }

    }

    public Product newProd() throws NumberFormatException {
        return new Product(
                Integer.parseInt(idTextBox.getText()),
                nameTextBox.getText(),
                Double.parseDouble(priceTextBox.getText()),
                Integer.parseInt(invTextBox.getText()),
                Integer.parseInt(minTextBox.getText()),
                Integer.parseInt(maxTextBox.getText()));
    }

    public Product saveProd(Product productToSave) throws NumberFormatException {
        productToSave.setId(Integer.parseInt(idTextBox.getText()));
        productToSave.setName(nameTextBox.getText());
        productToSave.setPrice(Double.parseDouble(priceTextBox.getText()));
        productToSave.setStock(Integer.parseInt(invTextBox.getText()));
        productToSave.setMin(Integer.parseInt(minTextBox.getText()));
        productToSave.setMax(Integer.parseInt(maxTextBox.getText()));
        productToSave.getAssociatedParts().addAll(tempAssociatedParts);
        return productToSave;
    }

    public void emptyFieldError() {
        Alert errorMessage = new Alert(Alert.AlertType.ERROR);
        errorMessage.setTitle("Error Message");
        errorMessage.setContentText("NumberExceptionError: Check if you have an empty field.");
        errorMessage.show();
    }
}
