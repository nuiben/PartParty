package ben.partparty.controller;

import ben.partparty.model.InHouse;
import ben.partparty.model.Inventory;
import ben.partparty.model.Outsourced;
import ben.partparty.model.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Tertiary Controller - Inherits only from AddPartController.
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
     *
     * @RUNTIME_ERROR Modified objects were not registering as changed between Outsourced and InHouse.
     * After type casting and checking types, it was revealed that the objects were changing, but the
     * Radio buttons were defaulting to InHouse even for Outsourced parts.
     */
    public void passSelectedPart(int index, Part selection) {
        selectedIndex = index;
        selectedPart = selection;
        setFields(selection);
        if (selection instanceof Outsourced) {
            optionOS.fire();
            optionTextBox.setText(String.valueOf(((Outsourced) selection).getCompanyName()));
        } else {
            optionTextBox.setText(String.valueOf(((InHouse) selection).getMachineId()));
        }
    }

    /**
     * Event Handler for Save Button
     * @param save Action event used by FXMLloader
     */
    public void OnModifyPartSave(ActionEvent save) {
        try {
            if (displayConfirmation("save").get() == ButtonType.OK) {
                Inventory.updatePart(selectedIndex, saveItem(selectedPart));
                loadFXML(save, "/view/MainView.fxml");
            }
        } catch (Exception exception) {
            displayErrorMessage(exception);
        }
    }

}
