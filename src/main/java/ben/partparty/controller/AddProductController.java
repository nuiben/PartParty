package ben.partparty.controller;

import ben.partparty.model.*;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddProductController extends AddPartController {

    public Button addAssociatedPartButton;
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
    private TableColumn<Part, Double> partPrice;

    @FXML
    private TableView<Part> associatedPartTable;
    @FXML
    private TableColumn<Part, Integer> associatedPartID;
    @FXML
    private TableColumn<Part, String> associatedPartName;
    @FXML
    private TableColumn<Part, Integer> associatedPartStock;
    @FXML
    private TableColumn<Part, Double> associatedPartPrice;
    @FXML
    private TextField partSearch;
    @FXML
    private FilteredList<Part> filteredParts;
    private ObservableList<Part> temporaryAssociatedParts = FXCollections.observableArrayList();
    public Product product;
    private int idCount;
    private int firstClickFlag = 0;
    int col = 0;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(url);
        idCount = getHighestProductID(Inventory.getAllProducts());
        idTextBox.setText(String.valueOf(idCount + 1));
        partTable.setItems(Inventory.getAllParts());
        partID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        associatedPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        filteredParts = new FilteredList<>(Inventory.getAllParts(), p -> true);

        partSearch.textProperty().addListener((observable, oldValue, newValue) -> filteredParts.setPredicate(part -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = newValue.toLowerCase();
            if (part.getName().toLowerCase().contains(lowerCaseFilter)) {
                System.out.println("ping " + col++);
                return true;
            } else return Integer.toString(part.getId()).contains(lowerCaseFilter);
        }));
        SortedList<Part> sortedParts = new SortedList<>(filteredParts);
        sortedParts.comparatorProperty().bind(partTable.comparatorProperty());
        partTable.setItems(sortedParts);
    }


    public void OnSaveProduct(ActionEvent save) throws IOException {
        if (product == null) {
            try {
                if (Integer.parseInt(minTextBox.getText()) > Integer.parseInt(maxTextBox.getText())) {
                    throw new RuntimeException("Minimum value exceeds Maximum value.");
                } else if (Integer.parseInt(invTextBox.getText()) < Integer.parseInt(minTextBox.getText()) || Integer.parseInt(invTextBox.getText()) > Integer.parseInt(maxTextBox.getText())) {
                    throw new RuntimeException("Inventory must be between Minimum and Maximum values.");
                } else if (Integer.parseInt(invTextBox.getText()) < 0 || Integer.parseInt(minTextBox.getText()) < 0 || Integer.parseInt(maxTextBox.getText()) < 0 || Double.parseDouble(priceTextBox.getText()) < 0) {
                    throw new RuntimeException("Values must be non-negative.");
                } else {
                    product = newProduct();
                    product.getAllAssociatedParts().addAll(temporaryAssociatedParts);
                    Inventory.addProduct(product);
                    setStage(save, fxmlLoad("/view/MainView.fxml"));
                }
            } catch (Exception exception) {
                displayErrorMessage(exception);
            }
        } else {
            product.getAllAssociatedParts().addAll(temporaryAssociatedParts);
            Inventory.addProduct(product);
            setStage(save, fxmlLoad("/view/MainView.fxml"));
        }
    }

    public void OnAddAssociatedPart() throws NumberFormatException {

        if (product == null) {
            try {
                if (partTable.getSelectionModel().getSelectedItem() == null) {
                    throw new RuntimeException("No Selection Indicated.");
                }
                temporaryAssociatedParts.add(partTable.getSelectionModel().getSelectedItem());
                associatedPartTable.setItems(temporaryAssociatedParts);
            } catch (Exception exception) {
                displayErrorMessage(exception);
            }
        } else {
            try {
                if (firstClickFlag == 0) {
                    temporaryAssociatedParts.addAll(product.getAllAssociatedParts());
                    firstClickFlag++;
                }
                if (partTable.getSelectionModel().getSelectedItem() == null) {
                    throw new RuntimeException("No Selection Indicated.");
                }
                temporaryAssociatedParts.add(partTable.getSelectionModel().getSelectedItem());
                associatedPartTable.setItems(temporaryAssociatedParts);

            } catch (Exception exception) {
                displayErrorMessage(exception);
            }
        }

    }

    public void OnRemoveAssociatedPart() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Remove Associated Part");
        confirmation.setContentText("Are you sure you want to remove from associated parts?\n" + associatedPartTable.getSelectionModel().getSelectedItem().getName());
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (firstClickFlag == 0 && product != null) {
                temporaryAssociatedParts.addAll(product.getAllAssociatedParts());
                firstClickFlag++;
            }
            temporaryAssociatedParts.remove(associatedPartTable.getSelectionModel().getSelectedItem());
            associatedPartTable.setItems(temporaryAssociatedParts);
        }

    }

    public Product newProduct() throws NumberFormatException {
        return new Product(
                ++idCount,
                nameTextBox.getText(),
                Double.parseDouble(priceTextBox.getText()),
                Integer.parseInt(invTextBox.getText()),
                Integer.parseInt(minTextBox.getText()),
                Integer.parseInt(maxTextBox.getText()));
    }

    public Product saveFieldValues(Product productToSave) throws NumberFormatException {
        productToSave.setId(Integer.parseInt(idTextBox.getText()));
        productToSave.setName(nameTextBox.getText());
        productToSave.setPrice(Double.parseDouble(priceTextBox.getText()));
        productToSave.setStock(Integer.parseInt(invTextBox.getText()));
        productToSave.setMin(Integer.parseInt(minTextBox.getText()));
        productToSave.setMax(Integer.parseInt(maxTextBox.getText()));
        productToSave.getAllAssociatedParts().setAll(temporaryAssociatedParts);
        return productToSave;
    }


    public int getHighestProductID(ObservableList<Product> product) {
        int max = 0;
        for (Part i : product) {
            if (i.getId() > max) {
                max = i.getId();
            }
        }
        return max;
    }
}