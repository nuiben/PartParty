package ben.partparty.controller;

import ben.partparty.model.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
        System.out.println(url);
        idCount = getHighestID(Inventory.getAllParts());
        idTextBox.setText(String.valueOf(idCount+1));
    }

    /**
     * On Toggle of InHouse or Outsource Radio Button, Label is set to appropriate variable name.
     */
    public void OnOptionToggleInHouse() {
        optionLabel.setText("Machine ID");
    }

    public void OnOptionToggleOutsourced() {
        optionLabel.setText("Company Name");
    }

    /**
     * Handler for Cancel Button
     * @param cancel ActionEvent passed for FXMLlaoder to point to the current stage before returning to Main Screen.
     * @throws IOException if FXMLloader cannot locate filepath.
     */
    public void OnCancel(ActionEvent cancel) throws IOException {
        loadFXML(cancel, "/view/MainView.fxml");
    }

    /**
     * When Save Button is clicked, performs validation check on fields, adds a new item
     *  then returns to MainScreenView.
     *  @param save ActionEvent containing the save click UID.
     * */
    public void OnSave(ActionEvent save) {
        try {
            testFields();
            Inventory.addPart(newItem(idCount));
            loadFXML(save, "/view/MainView.fxml");
        } catch (Exception exception) {
            displayErrorMessage(exception);
        }
    }

    /** Performs Standard Input Validations for values in Field
     * */
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
     * @param idCount int value of the current highest idCount
     * @return Part A new Outsourced or InHouse part depending on the toggle status of the Option Button
     * */
    public <T extends Part> T newItem(int idCount) {
        int id = ++idCount;
        String name = nameTextBox.getText();
        double price = Double.parseDouble(priceTextBox.getText());
        int stock = Integer.parseInt(invTextBox.getText());
        int min = Integer.parseInt(minTextBox.getText());
        int max = Integer.parseInt(maxTextBox.getText());
        if (optionOS != null && optionOS.isSelected()) {
            return (T) new Outsourced(id, name, price, stock, min, max, optionTextBox.getText());
        } else if (optionOS != null) {
            return (T) new InHouse(id, name, price, stock, min, max,Integer.parseInt(optionTextBox.getText()));
        } else {
            return (T) new Product(id,name, price, stock, min, max);
        }
    }

    /** Calls input validation, then sets the designated Part or Products values to the values
     *  in the input fields.
     * @param item any Instance derived from Part
     * @return T updated item.
     * */
    public <T extends Part> T saveItem(T item) {
        testFields();
        item.setId(Integer.parseInt(idTextBox.getText()));
        item.setName(nameTextBox.getText());
        item.setPrice(Double.parseDouble(priceTextBox.getText()));
        item.setMin(Integer.parseInt(minTextBox.getText()));
        item.setMax(Integer.parseInt(maxTextBox.getText()));
        item.setStock(Integer.parseInt(invTextBox.getText()));
        try {
            if (optionOS != null && optionOS.isSelected()) {
                ((Outsourced) item).setCompanyName(optionTextBox.getText());
            }
            else if (optionOS != null) {
                ((InHouse) item).setMachineID(Integer.parseInt(optionTextBox.getText()));
            }
        }
        catch(ClassCastException exception) {
            item = (T) newItem(item.getId() - 1);
        }
        return item;
    }

    /** Returns the current highest ID value of provided Part or Product List
     * @param list generic part or product ObservableList
     * @return max
     * */
    public <T extends Part> int getHighestID(ObservableList<T> list) {
        int max = 0;
        for (T i : list) {
            if (i.getId() > max) {
                max = i.getId();
            }
        }
        return max;
    }

    /**
     * Updates the text fields to match Part or Product provided.
     * @param item Part or Product Object.
     * @param <T> Generic for Part or Product.
     * @throws NumberFormatException if a number is used for a String field.
     */
    public <T extends Part> void setFields(T item) throws NumberFormatException {
        idTextBox.setText(Integer.toString(item.getId()));
        nameTextBox.setText(item.getName());
        priceTextBox.setText(Double.toString(item.getPrice()));
        invTextBox.setText(Integer.toString(item.getStock()));
        minTextBox.setText(Integer.toString(item.getMin()));
        maxTextBox.setText(Integer.toString(item.getMax()));
    }

}
