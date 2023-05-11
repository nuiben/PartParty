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
        idTextBox.setText(String.valueOf(idCount+1));
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

    /** When Save Button is clicked
     // @param ActionEvent save
     * */
    public void OnSave(ActionEvent save) {
        try {
            testFields();
            if (optionOS.isSelected()) {
                Inventory.addPart(newPart(idCount));
            }
            else {
                Inventory.addPart(newPart(idCount));
            }
            setStage(save, fxmlLoad("/view/MainView.fxml"));
        }
        catch (Exception exception) {
            Alert errorMessage = new Alert(Alert.AlertType.ERROR);
            errorMessage.setTitle("Invalid Input");
            errorMessage.setContentText(exception.getClass() +"\n"+ exception.getMessage());
            errorMessage.show();
        }
    }

    public void testFields() {
        if (Integer.parseInt(minTextBox.getText()) > Integer.parseInt(maxTextBox.getText())) {
            throw new RuntimeException("Minimum value exceeds Maximum value.");
        }
        else if (Integer.parseInt(invTextBox.getText()) < Integer.parseInt(minTextBox.getText()) || Integer.parseInt(invTextBox.getText()) > Integer.parseInt(maxTextBox.getText())) {
            throw new RuntimeException("Inventory must be between Minimum and Maximum values.");
        }
        else if (Integer.parseInt(invTextBox.getText()) < 0 || Integer.parseInt(minTextBox.getText()) < 0 || Integer.parseInt(maxTextBox.getText()) < 0 || Double.parseDouble(priceTextBox.getText()) < 0) {
            throw new RuntimeException("Values must be non-negative.");
        }
    }

    /** Creates a new Outsourced Part based on the field values of the
     *  current scene.
     * @
     * @param idCount
     * @return Part A new Outsourced or InHouse part depending on the toggle status of the Option Button
     * */
    public Part newPart(int idCount) {
        int id = ++idCount;
        String name = nameTextBox.getText();
        double price = Double.parseDouble(priceTextBox.getText());
        int stock = Integer.parseInt(invTextBox.getText());
        int min = Integer.parseInt(minTextBox.getText());
        int max = Integer.parseInt(maxTextBox.getText());
        if (optionOS.isSelected()) {
            return new Outsourced(id, name, price, stock, min, max, optionTextBox.getText());
        } else {
            return new InHouse(id, name, price, stock, min, max,Integer.parseInt(optionTextBox.getText()));
        }
    }

    /** Returns the current highest ID value of provided Parts List
     * @param part
     * @return max
     * */
    public int getHighestID(ObservableList<Part> part) {
        int max = 0;
        for (Part i : part) {
            if (i.getId() > max) {
                max = i.getId();
            }
        }
        return max;
    }

}
