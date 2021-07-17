package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Models a product which is a sum of parts
 * @author Erik Scovin
 */
public class Product {

    /**
     * Product ID
     */
    private int id;

    /**
     * Product name
     */
    private String name;

    /**
     * Product price
     */
    private double price;

    /**
     * Product inventory level
     */
    private int stock;

    /**
     * Product minimum inventory count
     */
    private int min;

    /**
     *  Product maximum inventory count
     */
    private int max;

    /**
     * Associated parts for product
     */
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     * Constructor for new instance
     * @param id product ID
     * @param name product name
     * @param price product price
     * @param stock product stock
     * @param min product min
     * @param max product max
     */
    public Product(int id, String name, double price, int stock, int min, int max) {

        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;

    }

    /**
     * Getter for ID
     * @return product ID
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for ID
     * @param id product ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter for name
     * @return product name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name
     * @param name product name
     */
    public void  setName(String name) {
        this.name = name;
    }

    /**
     * Getter for price
     * @return product price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Setter for price
     * @param price product price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Getter for inventory level
     * @return product inventory level
     */
    public int getStock() {
        return stock;
    }


    /**
     * Setter for inventory level
     * @param stock product inventory level
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Getter for minimum inventory count
     * @return product minimum inventory count
     */
    public int getMin() {
        return min;
    }

    /**
     * Setter for minimum inventory count
     * @param min product minimum inventory count
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Getter for max
     * @return product maximum inventory count
     */
    public int getMax() {
        return max;
    }

    /**
     * Setter for max
     * @param max product maximum count
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Add part to associated parts list for product
     * @param part part to add
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        if (associatedParts.contains(selectedAssociatedPart)) {
            associatedParts.remove(selectedAssociatedPart);
            return true;

        }
        else
            return false;

    }

    /**
     * Retrieves list of associated parts for product
     * @return list of parts
     */
    public ObservableList<Part> getAssociatedParts() {return associatedParts;}
}
