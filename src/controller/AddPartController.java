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
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller class for AddPart.fxml
 * @author Erik Scovin
 */

public class AddPartController implements Initializable {

    Stage stage;
    Parent scene;


    /**
     * Pop-up confirmation and navigation to MainScreen.fxml
     * @param event Cancel button action
     * @throws IOException from FXXLoader
     */
    @FXML
    void onActionGoToMainScreen(ActionEvent event) throws IOException {


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
     * In-House radio button
     */
    @FXML
    private RadioButton inHouseRBtn;

    /**
     * Outsourced radio button
     */
    @FXML
    private RadioButton outsourcedRBtn;

    /**
     * Toggle group for inHouseRBtn and outsourcedRBtn
     */
    @FXML
    private ToggleGroup sourceTG;

    /**
     * MachineID/CompanyName label
     */
    @FXML
    private Label partIdNameLab;

    /**
     * Part ID text field
     */
    @FXML
    private TextField partIdTxt;

    /**
     * Part name text field
     */
    @FXML
    private TextField partNameTxt;

    /**
     * Part inventory level text field
     */
    @FXML
    private TextField partInvTxt;

    /**
     * Part price text field
     */
    @FXML
    private TextField partPriceTxt;

    /**
     * Part maximum inventory count
     */
    @FXML
    private TextField partMaxTxt;

    /**
     * Part minimum inventory count
     */
    @FXML
    private TextField partMinTxt;

    /**
     * MachineId/CompanyName text field
     */
    @FXML
    private TextField partIdNameTxt;

    /**
     * Sets partIdNameLab to "Machine ID"
     * @param event In-House radio button action
     */
    @FXML
    void inHouseRBtnAction(ActionEvent event) {

        partIdNameLab.setText("Machine ID");

    }

    /**
     * Sets partIdNameLab to "Company Name"
     * @param event Outsourced radio button action
     */
    @FXML
    void outsourcedRBtnAction(ActionEvent event) {

        partIdNameLab.setText("Company Name");
    }

    /**
     * Adds part to inventory and returns to MainScreen.fxml
     * @param event Text fields are validated with error messages preventing invalid entries
     * @throws IOException from FXMLLoader
     */
    @FXML
    void onActionSavePart(ActionEvent event) throws IOException {

        try {
            int id = 0;
            String name = partNameTxt.getText();
            Double price = Double.parseDouble(partPriceTxt.getText());
            int stock = Integer.parseInt(partInvTxt.getText());
            int min = Integer.parseInt(partMinTxt.getText());
            int max = Integer.parseInt(partMaxTxt.getText());
            int machineId;
            String companyName;
            boolean partAddSuccessful = false;

            if (name.isEmpty()) {
                displayAlert(5);
            } else {
                if (minValid(min, max) && inventoryValid(min, max, stock)) {
                    if (inHouseRBtn.isSelected()) {
                        try {
                            machineId = Integer.parseInt(partIdNameTxt.getText());
                            InHouse newInHousePart = new InHouse(id, name, price, stock, min, max, machineId);
                            newInHousePart.setId(Inventory.getNewPartId());
                            Inventory.addPart(newInHousePart);
                            partAddSuccessful = true;
                        } catch (Exception e) {
                            displayAlert(2);
                        }
                    }

                    if (outsourcedRBtn.isSelected()) {
                        companyName = partIdNameTxt.getText();
                        Outsourced newOutsourcedPart = new Outsourced(id, name, price, stock, min, max, companyName);
                        newOutsourcedPart.setId(Inventory.getNewPartId());
                        Inventory.addPart(newOutsourcedPart);
                        partAddSuccessful = true;

                    }

                    if (partAddSuccessful) {
                        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        scene = FXMLLoader.load(getClass().getResource("../view/MainScreen.fxml"));
                        stage.setScene(new Scene(scene));
                        stage.show();
                    }
                }
            }

        } catch (Exception e) {
            displayAlert(1);
        }
    }

    /**
     * Checks if min is great than 0 and less than max
     * @param min minimum inventory level for part
     * @param max maximum inventory level for part
     * @return boolean indicating if min entry is valid
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
     * Checks if inventory level is between minimum inventory level and maximum inventory level
     * or equal to minimum/maximum inventory level
     * @param min minimum inventory level for part
     * @param max maximum inventory level for part
     * @param stock inventory level for part
     * @return boolean indicating if inventory entry is valid
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
     * Displays alert messages
     * @param alertType Alert selector
     */
    private void displayAlert(int alertType) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

            switch (alertType) {
                case 1:
                    alert.setTitle("Error");
                    alert.setHeaderText("Error Adding Part");
                    alert.setContentText("Form contains invalid or blank fields.");
                    alert.showAndWait();
                    break;
                case 2:
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid entry for Machine ID");
                    alert.setContentText("Machine ID may only contain numbers.");
                    alert.showAndWait();
                    break;
                case 3:
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid entry for Min");
                    alert.setContentText("Min must be a number greater than 0 and less than Max.");
                    alert.showAndWait();
                    break;
                case 4:
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid entry for Inventory");
                    alert.setContentText("Inventory must be a number equal to or between Min and Max.");
                    alert.showAndWait();
                    break;
                case 5:
                    alert.setTitle("Error");
                    alert.setHeaderText("Name empty");
                    alert.setContentText("Name cannot be empty.");
                    alert.showAndWait();
                    break;
            }

        }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
