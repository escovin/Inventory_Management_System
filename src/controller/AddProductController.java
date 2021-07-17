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

import javax.swing.text.html.Option;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * Controller class for AddProduct.fxml
 * @author Erik Scovin
 */
public class AddProductController implements Initializable {

    Stage stage;
    Parent scene;

    //Text fields
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
     * product price text field
     */
    @FXML
    private TextField productPriceTxt;

    /**
     * product minimum inventory level text field
     */
    @FXML
    private TextField productMinTxt;

    // ******************************

    /**
     * Product maximum inventory level text field
     */
    @FXML
    private TextField productMaxTxt;

    /**
     * list of parts associated with product
     */
    private ObservableList<Part> assocParts = FXCollections.observableArrayList();

    /**
     * Associated parts table view
     */
    @FXML
    private TableView<Part> assocPartTableView;

    /**
     * Associated parts table ID column
     */
    @FXML
    private TableColumn<Part, Integer> assocPartIdColumn;

    /**
     * Associated parts table name column
     */
    @FXML
    private TableColumn<Part, String> assocPartNameColumn;

    /**
     * Associated parts table inventory level column
     */
    @FXML
    private TableColumn<Part, Integer> assocPartInvColumn;

    /**
     * Associated parts table price column
     */
    @FXML
    private TableColumn<Part, Double> assocPartPriceColumn;

    // **************************************************

    /** all parts tableview
     *
     */
    @FXML
    private TableView<Part> partTableView;

    /**
     * Parts table ID column
     */
    @FXML
    private TableColumn<Part, Integer> partIdColumn;

    /**
     * Parts table name column
     */
    @FXML
    private TableColumn<Part, String> partNameColumn;

    /**
     * Parts table inventory level column
     */
    @FXML
    private TableColumn<Part, Integer> partInvColumn;

    /**
     * Parts table price column
     */
    @FXML
    private TableColumn<Part, Double> partPriceColumn;

    // ***********************************************8

    /**
     * Part search text field
     */
    @FXML
    private TextField partSearchTxt;

    /**
     * Adds selected part in parts table to associated parts table
     * Displays error if nothing is selected
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
     * Pop-up alert and navigation to MainScreen.fxml
     * @param event Cancel button Action
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
     * Initiates search based on in parts search text field and reloads parts table view with results
     * search values are name or ID
     * @param event Part search button action
     */
    @FXML
    void partSearchButtonAction(ActionEvent event) {
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
     * Reloads table view to display all parts when search field is empty
     * @param event Parts search text field key pressed
     */
    @FXML
    void partSearchKeyPressed(KeyEvent event) {
        if (partSearchTxt.getText().isEmpty()) {
            partTableView.setItems(Inventory.getAllParts());

        }
    }

    /**
     * Shows confirmation dialog box and removes selected part from associated parts table.
     * Displays error if nothing is selected
     * @param event Remove associated part button action
     */
    @FXML
    void  removeAssocPartButtonAction(ActionEvent event) {
        Part selectedPart =assocPartTableView.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            displayAlert(5);
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert!");
            alert.setContentText("Remove selected part?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                assocParts.remove(selectedPart);
                assocPartTableView.setItems(assocParts);
            }
        }

    }

    /**
     * Adds product to inventory and navigates to MainScreen.fxml
     * Text fields are validated with alerts preventing invalid or empty entries
     * @param event Save button action
     * @throws IOException from FXMLLoader
     */
    @FXML
    void saveButtonAction(ActionEvent event) throws IOException {
        try {
            int id = 0;
            String name = productNameTxt.getText();
            Double price = Double.parseDouble(productPriceTxt.getText());
            int stock = Integer.parseInt(productInvTxt.getText());
            int min = Integer.parseInt(productMinTxt.getText());
            int max = Integer.parseInt(productMaxTxt.getText());

            if(name.isEmpty()) {
                displayAlert(7);
            } else {
                if(minValid(min, max) && inventoryValid(min, max, stock)) {
                    Product newProduct = new Product(id, name, price, stock, min, max);
                    for (Part part : assocParts) {
                        newProduct.addAssociatedPart(part);

                    }

                    newProduct.setId(Inventory.getNewProductId());
                    Inventory.addProduct(newProduct);

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
     * Checks that minimum inventory level is greater than 0 and
     * less than maximum inventory level
     * @param min minimum inventory level
     * @param max maximum inventory level
     * @return boolean indicating if inventory level entry is valid
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
     * Checks inventory level is equal to or between minimum inventory level
     * and maximum inventory level
     * @param min minimum inventory level
     * @param max maximum inventory level
     * @param stock inventory level for part
     * @return Boolean indicating if inventory is valid
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
                alert.setHeaderText("Error Adding Product");
                alert.setContentText("Form contains invalid or blank entries.");
                alert.showAndWait();
                break;
            case 2:
                alertInfo.setTitle("Information");
                alertInfo.setHeaderText("Part not found");
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
                alert.setContentText("Inventory must be equal to or between Min and Max");
                alert.showAndWait();
                break;
            case 5:
                alert.setTitle("Error!");
                alert.setHeaderText("Part not selected");
                alert.showAndWait();
                break;
            case 6:
                alert.setTitle("Error!");
                alert.setHeaderText("Name empty");
                alert.setContentText("Name cannot be empty");
                alert.showAndWait();
                break;
        }
    }


    /**
     * Initializs controller and populates table views
     * @param url url
     * @param resourceBundle resources
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        partTableView.setItems(Inventory.getAllParts());

        assocPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

    }
}
