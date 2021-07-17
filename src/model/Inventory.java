package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Models inventory for parts and products
 * Class provides data for application
 * @author Erik Scovin
 */
public class Inventory {

    /**
     * Part ID. Variable for part IDs
     */
    private static int partId = 0;

    /**
     * Product ID. Variable for product IDs
     */
    private static int productId = 0;

    /**
     * List of parts in inventory
     */
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();

    /**
     * List of products in inventory
     */
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Retrieves list of parts in inventory
     * @return list of parts
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Retrieves list of products in inventory
     * @return list of products
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /**
     * Add part to inventory
     * @param newPart part to add
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * Add product to inventory
     * @param newProduct product to add
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * Creates new part ID
     * @return new part ID
     */
    public static int getNewPartId() {
        return ++partId;
    }

    /**
     * Creates new product ID
     * @return new product ID
     */
    public static int getNewProductId() {
        return ++productId;
    }

    /**
     * Searches parts by ID
     * @param partId part ID
     * @return part if found, otherwise null
     */
    public static Part lookupPart(int partId) {
        Part partFound = null;

        for (Part part : allParts) {
            if (part.getId() == partId) {
                partFound = part;
            }
        }
        return partFound;
    }

    /**
     * Searches parts by name
     * @param partName part name
     * @return list of parts
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> partsFound = FXCollections.observableArrayList();

        for (Part part : allParts) {
            if (part.getName().equals(partName)) {
                partsFound.add(part);
            }
        }
        return partsFound;
    }

    /**
     * Searches products by ID
     * @param productId product ID
     * @return product if found, otherwise null
     */
    public static Product lookupProduct(int productId) {
        Product productFound = null;

        for (Product product : allProducts) {
            if (product.getId() == productId) {
                productFound = product;

            }
        }

        return productFound;
    }


    /**
     * Searches products by name
     * @param productName product name
     * @return list of products
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> productsFound = FXCollections.observableArrayList();

        for (Product product : allProducts) {
            if (product.getName().equals(productName)) {
                productsFound.add(product);
            }
        }
        return productsFound;
    }

    /**
     * Replaces part in list
     * @param index index of part
     * @param selectedPart part being replaced
     */
    public static void updatePart (int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * Replaces product in list
     * @param index index of product
     * @param selectedProduct product being replaced
     */
    public static void updateProduct (int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);

    }

    /**
     * Deletes part from list
     * @param selectedPart part to be deleted
     * @return boolean indicating deletion status.
     */
    public static boolean deletePart(Part selectedPart) {
        if (allParts.contains(selectedPart)) {
            allParts.remove(selectedPart);
            return true;

        }
        else {
            return false;
        }
    }

    /**
     * Deletes product from list
     * @param selectedProduct product to be deleted
     * @return boolean indicating deletion status
     */
    public  static boolean deleteProduct(Product selectedProduct) {
        if (allProducts.contains(selectedProduct)) {
            allProducts.remove(selectedProduct);
            return true;
        }
        else {
            return false;
        }
    }
}
