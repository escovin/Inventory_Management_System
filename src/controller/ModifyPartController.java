package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;


import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for ModifyPart.fxml
 * @author Erik Scovin
 */
public class ModifyPartController implements Initializable {

    Stage stage;
    Parent scene;


    /**
     * Part selected in main screen controller
     */
    private Part selectedPart;

    /**
     * Toggle group for inHouseRBtn and outsourcedRBtn
     */
    @FXML
    private ToggleGroup sourceTG;

    /**
     * Machine ID/Company Name label
     */
    @FXML
    private Label partIdNameLab;

    /**
     * In-House radio button
     */
    @FXML
    private RadioButton inHouseRBtn;


    /**
     * part ID text field
     */
    @FXML
    private TextField partIdTxt;

    /**
     * part name text field
     */
    @FXML
    private TextField partNameTxt;

    /**
     * Outsourced radio button
     */
    @FXML
    private RadioButton outsourcedRBtn;

    /**
     * part inventory level text field
     */
    @FXML
    private TextField partInvTxt;

    /**
     * part price text field
     */
    @FXML
    private TextField partPriceTxt;

    /**
     * part maximum inventory level text field
     */
    @FXML
    private TextField partMaxTxt;

    /**
     * Machine ID/Company Name text field
     */
    @FXML
    private TextField partIdNameTxt;

    /**
     * part minimum inventory level text field
     */
    @FXML
    private TextField partMinTxt;


    /**
     * Pop-up confirmation and navigation to MainScreen.fxml
     * @param event Cancel button action
     * @throws IOException from FXMLLoader
     */
    @FXML
    void cancelButtonAction(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert!");
        alert.setContentText("Cancel changes and return to main screen?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("../view/MainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();

        }

    }

    /**
     * Sets Machine ID/Company Name label to "Machine ID"
     * @param event In-House radio button action
     */
    @FXML
    void inHouseRBtnAction(ActionEvent event) {
        partIdNameLab.setText("Machine ID");

    }

    /**
     * Sets Machine ID/Company Name label to "Company Name"
     * @param event Outsourced radio button action
     */
    @FXML
    void outsourcedRBtnAction(ActionEvent event) {
        partIdNameLab.setText("Company Name");

    }

    /**
     * Replaces part in inventory and navigates to MainScreen.fxml
     * Text fields are validated with alert messages preventing invalid or empty entries
     * @param event Save button action
     * @throws IOException from FMXLLoader
     */
    @FXML
    void saveButtonAction(ActionEvent event) throws IOException {
        try {
            int id = selectedPart.getId();
            String name = partNameTxt.getText();
            Double price = Double.parseDouble(partPriceTxt.getText());
            int stock = Integer.parseInt(partInvTxt.getText());
            int min = Integer.parseInt(partMinTxt.getText());
            int max = Integer.parseInt(partMaxTxt.getText());
            int machineId;
            String companyName;
            boolean partAddSuccessful = false;

            if (minValid(min, max) && inventoryValid(min, max, stock)) {
                if (inHouseRBtn.isSelected()) {
                    try {
                        machineId = Integer.parseInt(partIdNameTxt.getText());
                        InHouse newInHousePart = new InHouse(id, name, price, stock, min, max, machineId);
                        Inventory.addPart(newInHousePart);
                        partAddSuccessful = true;
                    } catch (Exception e) {
                        displayAlert(2);
                    }
                }

                if (outsourcedRBtn.isSelected()) {
                    companyName = partIdNameTxt.getText();
                    Outsourced newOutsourcedPart = new Outsourced(id, name, price, stock, min, max, companyName);
                    Inventory.addPart(newOutsourcedPart);
                    partAddSuccessful = true;
                }

                if (partAddSuccessful) {
                    Inventory.deletePart(selectedPart);

                    stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("../view/MainScreen.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();

                }
            }

        } catch (Exception e) {
            displayAlert(1);

        }
    }

    /**
     * Checks that minimum inventory level is is greater than 0 and
     * less than maximum inventory level
     * @param min minimum inventory level
     * @param max maximum inventory level
     * @return boolean indicating if minimum inventory level is valid
     */
    private boolean minValid(int min, int max) {
        boolean isValid = true;
        if (min <= 0 || min >= max) {

            isValid = false;
            displayAlert(3);

        }

        return isValid;

    }

    /**
     * Checks that inventory level is equal to or between minimum inventory level
     * and maximum inventory level
     * @param min minimum inventory level
     * @param max maximum inventory level
     * @param stock inventory level for part
     * @return boolean indicating if Inventory entry is valid
     */
    private boolean inventoryValid(int min, int max, int stock) {
        boolean isValid = true;
        if (stock < min || stock > max) {
            isValid = false;
            displayAlert(4);

        }

        return isValid;

    }

    /**
     * Displays error messages
     * @param alertType Alert message selector
     */
    private void displayAlert(int alertType) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        switch (alertType) {
            case 1:
                alert.setTitle("Error!");
                alert.setHeaderText("Error Modifying Part");
                alert.setContentText("Form contains invalid or blank entries.");
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle("Error!");
                alert.setHeaderText("Invalid entry for Machine ID");
                alert.setContentText("Machine ID may only contain numbers");
                alert.showAndWait();
                break;
            case 3:
                alert.setTitle("Error!");
                alert.setHeaderText("Invalid entry for Min");
                alert.setContentText("Min must be a number greater than 0 and less than Max");
                alert.showAndWait();
                break;
            case 4:
                alert.setTitle("Error!");
                alert.setHeaderText("Invalid entry for Inventory");
                alert.setContentText("Inventory must be a number equal to or between Min and Max.");
                alert.showAndWait();
                break;
        }
    }


    /**
     * Initializes controller and populates text fields with selected part in main screen controller
     * @param url url
     * @param resourceBundle resources
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedPart = MainScreenController.getPartToModify();
        if (selectedPart instanceof InHouse) {
            inHouseRBtn.setSelected(true);
            partIdNameLab.setText("Machine ID");
            partIdNameTxt.setText(String.valueOf(((InHouse) selectedPart).getMachineId()));

        }

        if (selectedPart instanceof Outsourced) {
            outsourcedRBtn.setSelected(true);
            partIdNameLab.setText("Company Name");
            partIdNameTxt.setText(((Outsourced)selectedPart).getCompanyName());

        }

        partIdTxt.setText(String.valueOf(selectedPart.getId()));
        partNameTxt.setText(selectedPart.getName());
        partInvTxt.setText(String.valueOf(selectedPart.getStock()));
        partPriceTxt.setText(String.valueOf(selectedPart.getPrice()));
        partMaxTxt.setText(String.valueOf(selectedPart.getMax()));
        partMinTxt.setText(String.valueOf(selectedPart.getMin()));

    }
}
