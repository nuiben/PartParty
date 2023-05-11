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

/**
 * ModifyPartController Class
 */
public class ModifyPartController extends AddPartController {
    public int selectedIndex;
    public Part selectedPart;
    public RadioButton optionOS;

    /**
     * Initializes - performs near identically to AddPartController
     */
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(url);
    }

    /**
     * Called from MainViewController, updates field values to match Part variables and type.
     * @param index integer value of the selected Part.
     * @param selection Part object selected to modify.
     */
    public void passSelectedPart(int index, Part selection) {
        selectedIndex = index;
        selectedPart = selection;
        setFields(selection);
        if (selection instanceof Outsourced) {
            optionOS.fire();
            optionTextBox.setText(String.valueOf(((Outsourced) selection).getCompanyName()));
        } else {
            optionTextBox.setText(String.valueOf(((InHouse) selection).getMachineID()));
        }
    }

    /**
     * Event Handler for Save Button
     * @param save Action event used by FXMLloader
     */
    public void OnModifyPartSave(ActionEvent save) {
        try {
            Inventory.updatePart(selectedIndex, saveItem(selectedPart));
            loadFXML(save, "/view/MainView.fxml");
        } catch (Exception exception) {
            displayErrorMessage(exception);
        }
    }

}
