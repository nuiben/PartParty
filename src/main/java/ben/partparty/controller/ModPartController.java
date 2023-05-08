package ben.partparty.controller;

import ben.partparty.model.InHouse;
import ben.partparty.model.Inventory;
import ben.partparty.model.Outsourced;
import ben.partparty.model.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModPartController extends AddPartController {
    public int selectedIndex;
    public Part selectedPart;
    public RadioButton optionOS;
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Path Loaded from source: " + url);
    }

    public void passSelectedPart(int idx, Part selection) {
        System.out.println("Passed the Part: " + selection);
        selectedIndex = idx;
        selectedPart = selection;
        idTextBox.setText(String.valueOf(selection.getId()));
        nameTextBox.setText(String.valueOf(selection.getName()));
        invTextBox.setText(String.valueOf(selection.getStock()));
        priceTextBox.setText(String.valueOf(selection.getPrice()));
        maxTextBox.setText(String.valueOf(selection.getMax()));
        minTextBox.setText(String.valueOf(selection.getMin()));
        if (selection instanceof Outsourced) {
            optionOS.fire();
            optionTextBox.setText(String.valueOf(((Outsourced) selection).getCompanyName()));
        } else {
            optionTextBox.setText(String.valueOf(((InHouse) selection).getMachineID()));
        }
    }

    public void OnSave(ActionEvent save) throws IOException {
        try {
            Inventory.updatePart(selectedIndex, savePart(selectedPart));
            setStage(save, fxmlLoad("/view/mainView.fxml"));
        } catch (Exception exception) {
            Alert errorMessage = new Alert(Alert.AlertType.ERROR);
            errorMessage.setTitle("Invalid Input");
            errorMessage.setContentText(exception.getMessage());
            errorMessage.show();
        }
    }

    public Part savePart(Part part) {
        part.setId(Integer.parseInt(idTextBox.getText()));
        part.setName(nameTextBox.getText());
        part.setPrice(Double.parseDouble(priceTextBox.getText()));

        if (Integer.parseInt(minTextBox.getText()) > Integer.parseInt(maxTextBox.getText())) {
            throw new RuntimeException("Minimum value exceeds Maximum value.");
        } else if (Integer.parseInt(invTextBox.getText()) < Integer.parseInt(minTextBox.getText()) || Integer.parseInt(invTextBox.getText()) > Integer.parseInt(maxTextBox.getText())) {
            throw new RuntimeException("Inventory must be between Minimum and Maximum values.");
        } else if (Integer.parseInt(invTextBox.getText()) < 0 || Integer.parseInt(minTextBox.getText()) < 0 || Integer.parseInt(maxTextBox.getText()) < 0 || Double.parseDouble(priceTextBox.getText()) < 0) {
            throw new RuntimeException("Values must be non-negative.");
        } else {
            part.setMin(Integer.parseInt(minTextBox.getText()));
            part.setMax(Integer.parseInt(maxTextBox.getText()));
            part.setStock(Integer.parseInt(invTextBox.getText()));
        }
        if (optionOS.isSelected()) {
            try { ((Outsourced) part).setCompanyName(optionTextBox.getText()); }
            catch(ClassCastException exception) {
                part = newOSPart();
            }
        } else {
            try { ((InHouse) part).setMachineID(Integer.parseInt(optionTextBox.getText())); }
            catch(ClassCastException exception) {
                part = newIHPart();
            }
        }
        return part;
    }

}
