package ben.partparty.controller;

import ben.partparty.model.InHouse;
import ben.partparty.model.Inventory;
import ben.partparty.model.Outsourced;
import ben.partparty.model.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;

import java.net.URL;
import java.util.ResourceBundle;

public class ModifyPartController extends AddPartController {
    public int selectedIndex;
    public Part selectedPart;
    public RadioButton optionOS;
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Path Loaded from source: " + url);
    }

    public void passSelectedPart(int idx, Part selection) {
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

    public void OnModifyPartSave(ActionEvent save) {
        try {
            Inventory.updatePart(selectedIndex, savePart(selectedPart));
            setStage(save, fxmlLoad("/view/MainView.fxml"));
        } catch (Exception exception) {
            displayErrorMessage(exception);
        }
    }

    public Part savePart(Part part) {
        part.setId(Integer.parseInt(idTextBox.getText()));
        part.setName(nameTextBox.getText());
        part.setPrice(Double.parseDouble(priceTextBox.getText()));
        testFields();
        part.setMin(Integer.parseInt(minTextBox.getText()));
        part.setMax(Integer.parseInt(maxTextBox.getText()));
        part.setStock(Integer.parseInt(invTextBox.getText()));
        try {
            if (optionOS.isSelected()) {
                ((Outsourced) part).setCompanyName(optionTextBox.getText());
            }
            else {
                ((InHouse) part).setMachineID(Integer.parseInt(optionTextBox.getText()));
            }
        }
        catch(ClassCastException exception) {
            part = newPart(part.getId() - 1);
        }
        return part;
    }

}
