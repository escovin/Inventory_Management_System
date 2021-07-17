package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Product;

/**
 * Inventory Management System implements an application for adding, modifying, and removing
 * parts and products to and from an inventory.
 * A future version of this program could implement an option to save the inventory lists
 * in an external file for record storage and recall.
 * @author Erik Scovin
 */

public class Main extends Application {

    /**
     * Start method creates FXML stage and loads scene.
     * @param primaryStage primaryStage
     * @throws Exception from FMXLLoader
     */

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/MainScreen.fxml"));
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * Main method is the entry point of the application.
     * Main method crates sample data and initializes the application.
     * @param args args
     */
    public static void main(String[] args) {

        //Sample part data
        int partId = Inventory.getNewPartId();
        InHouse computerMonitor = new InHouse(partId, "Computer Monitor", 200.00, 10, 1, 25, 100);
        partId = Inventory.getNewPartId();
        InHouse computerTower = new InHouse(partId, "Computer Tower", 500, 10, 1, 25, 100);
        partId = Inventory.getNewPartId();
        InHouse powerAdapter = new InHouse(partId, "Power Adapter",20.00, 10, 1, 25, 100);
        partId = Inventory.getNewPartId();
        Outsourced keyboard = new Outsourced(partId, "Keyboard", 60.00, 10, 1, 25,"IBM");
        Inventory.addPart(computerMonitor);
        Inventory.addPart(computerTower);
        Inventory.addPart(powerAdapter);
        Inventory.addPart(keyboard);

        //Sample product data
        int productId = Inventory.getNewProductId();
        Product computer = new Product(productId, "Desktop Computer", 780.00, 10, 1, 25);
        computer.addAssociatedPart(computerMonitor);
        computer.addAssociatedPart(computerTower);
        computer.addAssociatedPart(powerAdapter);
        computer.addAssociatedPart(keyboard);
        Inventory.addProduct(computer);


        launch(args);
    }
}
