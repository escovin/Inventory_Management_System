package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.AccessibleAction;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for MainScreen.fxml
 * @author Erik Scovin
 */

public class MainScreenController implements Initializable {

    Stage stage;
    Parent scene;

    /**
     * Selected part in table view
     */
    private static Part partToModify;

    /**
     * Selected Product in table view
     */
    private static Product productToModify;

    /**
     * part search text field
     */
    @FXML
    private TextField partSearchTxt;

    /**
     * parts table view
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

    //************************************************

    /***
     * product search text field
     */
    @FXML
    private TextField productSearchTxt;

    /**
     * product table view
     */
    @FXML
    private TableView<Product> productTableView;

    /**
     * product table ID column
     */
    @FXML
    private TableColumn<Product, Integer> productIdColumn;

    /**
     * product table name column
     */
    @FXML
    private TableColumn<Product, String> productNameColumn;

    /**
     * product table inventory level column
     */
    @FXML
    private TableColumn<Product, Integer> productInvColumn;

    /**
     * product table price column
     */
    @FXML
    private TableColumn<Product, Double> productPriceColumn;

    /**
     * Gets part selected in part table
     *
     * @return A part, null if nothing is selected
     */
    public static Part getPartToModify() {
        return partToModify;

    }

    /**
     * Gets product selected in product table
     *
     * @return A product, null if nothing is selected
     */
    public static Product getProductToModify() {
        return productToModify;

    }

    /**
     * Initiates search in parts search text field and reloads parts
     *
     * @param event Search button action
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
     * Reloads part table to show all parts when search field is empty
     *
     * @param event Part search key pressed
     */
    @FXML
    void partSearchTxtKeyPressed(KeyEvent event) {
        if (partSearchTxt.getText().isEmpty()) {
            partTableView.setItems(Inventory.getAllParts());
        }

    }

    /**
     * Deletes selected part in part table
     *
     * @param event Delete button action
     */
    @FXML
    void partDeleteAction(ActionEvent event) {
        Part selectedPart = partTableView.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            displayAlert(3);
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert!");
            alert.setContentText("Delete selected part?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deletePart(selectedPart);

            }
        }

    }

    /**
     * Deletes selected product in product table
     * @param event Delete button action
     */
    @FXML
    void productDeleteAction(ActionEvent event) {
        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            displayAlert(4);
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert!");
            alert.setContentText("Delete selected product?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                ObservableList<Part> assocParts = selectedProduct.getAssociatedParts();

                if (assocParts.size() >= 1) {
                    displayAlert(5);
                } else {
                    Inventory.deleteProduct(selectedProduct);
                }
            }
        }

    }


    /**
     * Navigates to AddPart.fml
     * @param event Add part button action
     * @throws IOException from FMXLLoader
     */
    @FXML
    void onActionGoToAddPart(ActionEvent event) throws IOException {

        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/AddPart.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * Navigate to ModifyPart.fxml
     * @param event Modify Part button action
     * @throws IOException from FMXLLoader
     */
    @FXML
    void onActionGoToModifyPart(ActionEvent event) throws IOException {


        partToModify = partTableView.getSelectionModel().getSelectedItem();

        if (partToModify == null) {
            displayAlert(3);
        } else {
            Parent parent = FXMLLoader.load(getClass().getResource("../view/ModifyPart.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        }



    }

    /**
     * Navigates to AddProduct.fxml
     * @param event Add product button action
     * @throws IOException from FXMLLoader
     */
    @FXML
    void onActionGoToAddProduct(ActionEvent event) throws IOException {

        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/AddProduct.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }


    /**
     * Navigates to ModifyProduct.fxml
     * @param event Modify button action
     * @throws IOException from FMXLLoader
     */
    @FXML
    void modifyProductAction(ActionEvent event) throws IOException {

        productToModify = productTableView.getSelectionModel().getSelectedItem();

        if (productToModify == null) {
            displayAlert(4);
        }

        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/ModifyProduct.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

        }


    /**
     * Initiates search in products and reloads table view with results
     * Search by name or ID
     * @param event part search button action
     */
    @FXML
    void productSearchButtonAction(ActionEvent event) {

        ObservableList<Product> allProducts = Inventory.getAllProducts();
        ObservableList<Product> productsFound = FXCollections.observableArrayList();
        String searchString = productSearchTxt.getText();

        for (Product product : allProducts) {
            if (String.valueOf(product.getId()).contains(searchString) || product.getName().contains(searchString)) {
                productsFound.add(product);

            }
        }

        productTableView.setItems(productsFound);

        if (productsFound.size() == 0) {
            displayAlert(2);

        }

    }

    /**
     * Reloads product table view to display all products when search field is empty
     * @param event Products search text field pressed
     */
    @FXML
    void productSearchTxtKeyPressed(KeyEvent event) {
        if (productSearchTxt.getText().isEmpty()) {
            productTableView.setItems(Inventory.getAllProducts());

        }
    }

    /**
     * Displays alert messages
     * @param alertType alert type selector
     */
    private void displayAlert(int alertType) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Alert alertError = new Alert(Alert.AlertType.ERROR);

        switch (alertType) {
            case 1:
                alert.setTitle("Information");
                alert.setHeaderText("Part not found");
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle("Information");
                alert.setHeaderText("Product not found");
                alert.showAndWait();
                break;
            case 3:
                alert.setTitle("Error!");
                alert.setHeaderText("Part not selected");
                alert.showAndWait();
                break;
            case 4:
                alert.setTitle("Error!");
                alert.setHeaderText("Product not selected");
                alert.showAndWait();
                break;
            case 5:
                alert.setTitle("Error!");
                alert.setHeaderText("Parts Associated");
                alert.setContentText("All parts must be removed from product before deletion.");
                alert.showAndWait();
                break;
        }
    }

    /**
     * Exits application
     * @param event Exit button Applications
     */
    @FXML
    void exitButtonAction(ActionEvent event) {

        System.exit(0);

    }

    /**
     * Initializes contoller and populates table
     * @param url url
     * @param resourceBundle resources
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Populate parts table view
        partTableView.setItems(Inventory.getAllParts());
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Populate product table view
        productTableView.setItems(Inventory.getAllProducts());
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

    }
}
