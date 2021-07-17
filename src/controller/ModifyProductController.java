package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;


import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for ModifyProduct.fxml
 * @author Erik Scovin
 */
/**
 */

public class ModifyProductController implements Initializable {

    Stage stage;
    Parent scene;

    /**
     * Product selected in main screen controller
     */
    Product selectedProduct;

    /**
     * list of associated parts with product
     */
    private ObservableList<Part> assocParts = FXCollections.observableArrayList();

    /**
     * Part search text field
     */
    @FXML
    private TextField partSearchTxt;

    /**
     * List of parts associated with product
     */
    @FXML
    private TableView<Part> assocPartTableView;

    /**
     * Associated parts table part ID
     */
    @FXML
    private TableColumn<Part, Integer> assocPartIdColumn;

    /**
     * Associated parts table part name
     */
    @FXML
    private TableColumn<Part, String> assocPartNameColumn;

    /**
     * Associated parts table part inventory level
     */
    @FXML
    private TableColumn<Part, Integer> assocPartInvColumn;

    /**
     * Associated parts table part price
     */
    @FXML
    private TableColumn<Part, Double> assocPartPriceColumn;

    //*************************************************

    /**
     * Parts table view
     */
    @FXML
    private TableView<Part> partTableView;

    /**
     * Parts table part ID
     */
    @FXML
    private TableColumn<Part, Integer> partIdColumn;

    /**
     * Parts table part name
     */
    @FXML
    private TableColumn<Part, String> partNameColumn;

    /**
     * Parts table part inventory level
     */
    @FXML
    private TableColumn<Part, Integer> partInvColumn;

    /**
     * Parts table part price
     */
    @FXML
    private TableColumn<Part, Double> partPriceColumn;

    //********************************************************

    /**
     * Product ID text field
     */
    @FXML
    private TextField productIdTxt;

    /**
     * Product name text field
     */
    @FXML
    private TextField productNameTxt;

    /**
     * Product inventory level text field
     */
    @FXML
    private TextField productInvTxt;

    /**
     * Product price text field
     */
    @FXML
    private TextField productPriceTxt;

    /**
     * Product minimum inventory level text field
     */
    @FXML
    private TextField productMinTxt;

    /**
     * Product Maximum inventory text field
     */
    @FXML
    private TextField productMaxTxt;

    //***********************************************

    /**
     * Adds selected part in parts table to associated parts table
     * Shows error message if nothing is selected
     * @param event Add button action
     */
    @FXML
    void addButtonAction(ActionEvent event) {
        Part selectedPart = partTableView.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            displayAlert(5);
        } else {
            assocParts.add(selectedPart);
            assocPartTableView.setItems(assocParts);

        }
    }

    /**
     * Pop-up confirmation and navigation to MainScreen.fxml
     * @param event Cancel button action
     * @throws IOException from FMXLLoader
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
     * Pop-up confirmation and selected part removal from associated parts table.
     * @param event Remove associated part button action
     */
    @FXML
    void removeAssocPartButtonAction(ActionEvent event) {

        Part selectedPart = assocPartTableView.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            displayAlert(5);

        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert!");
            alert.setContentText("Do you want to remove selected part?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                assocParts.remove(selectedPart);
                assocPartTableView.setItems(assocParts);
            }
        }
    }

    /**
     * Replaces product in inventory and navigates to main screen controller.
     * @param event Save button action
     */
    @FXML
    void saveButtonAction(ActionEvent event) {
        try {
            int id = selectedProduct.getId();
            String name = productNameTxt.getText();
            Double price = Double.parseDouble(productPriceTxt.getText());
            int stock = Integer.parseInt(productInvTxt.getText());
            int min = Integer.parseInt(productMinTxt.getText());
            int max = Integer.parseInt(productMaxTxt.getText());

            if (name.isEmpty()) {
                displayAlert(6);
            } else {
                if (minValid(min, max) && inventoryValid(min, max, stock)) {
                    Product newProduct = new Product(id, name, price, stock, min, max);

                    for (Part part : assocParts) {
                        newProduct.addAssociatedPart(part);

                    }

                    Inventory.addProduct(newProduct);
                    Inventory.deleteProduct(selectedProduct);

                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
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
     * Initiates search with search text field entry and reloads parts table with results
     * parts searched for by name or ID
     * @param event Part search button action
     */
    @FXML
    void searchButtonAction(ActionEvent event) {
        ObservableList<Part> allParts = Inventory.getAllParts();
        ObservableList<Part> partsFound = FXCollections.observableArrayList();
        String searchString = partSearchTxt.getText();

        for (Part part : allParts) {
            if (String.valueOf(part.getId()).contains(searchString) || part.getName().contains(searchString)) {
                partsFound.add(part);
            }
        }

        partTableView.setItems(partsFound);

        if (partsFound.size() == 0) {
            displayAlert(1);

        }

    }

    /**
     * Reloads part table view to show all arts when search field is empty.
     * @param event search key pressed
     */
    @FXML
    void searchKeyPressed(KeyEvent event) {

        if (partSearchTxt.getText().isEmpty()) {
            partTableView.setItems(Inventory.getAllParts());
        }

    }

    /**
     * Checks that minimum inventory level is greater than 0 and less than
     * maximum inventory level
     * @param min minimum inventory level
     * @param max maximum inventory level
     * @return boolean indicating if minimum inventory entry is valid
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
     * @param stock inventory level of part
     * @return boolean indicating if inventory level is valid
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
        Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);

        switch (alertType) {
            case 1:
                alert.setTitle("Error!");
                alert.setHeaderText("Error Modifying Product");
                alert.setContentText("Form contains invalid or blank entries.");
                alert.showAndWait();
                break;
            case 2:
                alertInfo.setTitle("Information");
                alert.setHeaderText("Part not found");
                alertInfo.showAndWait();
                break;
            case 3:
                alert.setTitle("Error!");
                alert.setHeaderText("Invalid entry for Min");
                alert.setContentText("Min must be greater than 0 and less than Max.");
                alert.showAndWait();
                break;
            case 4:
                alert.setTitle("Error!");
                alert.setHeaderText("Invalid entry for Inventory");
                alert.setContentText("Inventory must be equal to or between Min and Max.");
                alert.showAndWait();
                break;
            case 5:
                alert.setTitle("Error!");
                alert.setHeaderText("Part not selected");
                alert.showAndWait();
                break;
            case 6:
                alert.setTitle("Error!");
                alert.setHeaderText("Name Empty");
                alert.setContentText("Name cannot be empty.");
                alert.showAndWait();
                break;
        }
    }

    /**
     * Initializes controller and populates text fields with selected product in main screen controller
     * @param url url
     * @param resourceBundle resources
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        selectedProduct = MainScreenController.getProductToModify();
        assocParts = selectedProduct.getAssociatedParts();

        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        partTableView.setItems(Inventory.getAllParts());

        assocPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        assocPartTableView.setItems(assocParts);

        productIdTxt.setText(String.valueOf(selectedProduct.getId()));
        productNameTxt.setText(selectedProduct.getName());
        productInvTxt.setText(String.valueOf(selectedProduct.getStock()));
        productPriceTxt.setText(String.valueOf(selectedProduct.getPrice()));
        productMinTxt.setText(String.valueOf(selectedProduct.getMin()));
        productMaxTxt.setText(String.valueOf(selectedProduct.getMax()));

    }
}
