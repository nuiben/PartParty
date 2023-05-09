package ben.partparty.controller;

import ben.partparty.model.InHouse;
import ben.partparty.model.Inventory;
import ben.partparty.model.Outsourced;
import ben.partparty.model.Part;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddPartController extends MainViewController {
    @FXML
    public Text optionLabel;
    public TextField idTextBox;
    public TextField nameTextBox;
    public TextField invTextBox;
    public TextField priceTextBox;
    public TextField maxTextBox;
    public TextField minTextBox;
    public TextField optionTextBox;
    public RadioButton optionOS;
    private int idCount;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Path Loaded from source: " + url);
        idCount = getHighestID(Inventory.getAllParts());
    }

    public void OnOptionToggleInHouse() {
        optionLabel.setText("Machine ID");
    }

    public void OnOptionToggleOutsourced() {
        optionLabel.setText("Company Name");
    }

    public void OnCancel(ActionEvent cancel) throws IOException {
        setStage(cancel, fxmlLoad("/view/MainView.fxml"));
    }

    public void OnSave(ActionEvent save) {
        try{
            if (Integer.parseInt(minTextBox.getText()) > Integer.parseInt(maxTextBox.getText())) {
                throw new RuntimeException("Minimum value exceeds Maximum value.");
            } else if (Integer.parseInt(invTextBox.getText()) < Integer.parseInt(minTextBox.getText()) || Integer.parseInt(invTextBox.getText()) > Integer.parseInt(maxTextBox.getText())) {
                throw new RuntimeException("Inventory must be between Minimum and Maximum values.");
            } else if (Integer.parseInt(invTextBox.getText()) < 0 || Integer.parseInt(minTextBox.getText()) < 0 || Integer.parseInt(maxTextBox.getText()) < 0 || Double.parseDouble(priceTextBox.getText()) < 0) {
                throw new RuntimeException("Values must be non-negative.");
            } else {
                if (optionOS.isSelected()) {
                    Inventory.addPart(newOutsourcedPart());
                } else {
                    Inventory.addPart(newInHousePart());
                }
                setStage(save, fxmlLoad("/view/MainView.fxml"));
            }
        }
        catch (Exception exception) {
            Alert errorMessage = new Alert(Alert.AlertType.ERROR);
            errorMessage.setTitle("Invalid Input");
            errorMessage.setContentText(exception.getMessage());
            errorMessage.show();
        }
    }

    public Part newInHousePart() {
        return new InHouse(
//                Integer.parseInt(idTextBox.getText()),
                ++idCount,
                nameTextBox.getText(),
                Double.parseDouble(priceTextBox.getText()),
                Integer.parseInt(invTextBox.getText()),
                Integer.parseInt(minTextBox.getText()),
                Integer.parseInt(maxTextBox.getText()),
                Integer.parseInt(optionTextBox.getText()));

    }

    public Part newOutsourcedPart() {
        return new Outsourced(
//                Integer.parseInt(idTextBox.getText()),
                ++idCount,
                nameTextBox.getText(),
                Double.parseDouble(priceTextBox.getText()),
                Integer.parseInt(invTextBox.getText()),
                Integer.parseInt(minTextBox.getText()),
                Integer.parseInt(maxTextBox.getText()),
                optionTextBox.getText());
    }

    public int getHighestID(ObservableList<Part> partList) {
        int max = 0;
        for (Part i : partList) {
            if (i.getId() > max) {
                max = i.getId();
            }
        }
        return max;
    }

}
