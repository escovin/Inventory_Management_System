package model;

/**
 * Models outsourced part
 * @author Erik Scovin
 */

public class InHouse extends Part {

    /**
     * part machine ID
     */
    private int machineId;

    /**
     * Constructor for new instance
     * @param id ID for part
     * @param name name for part
     * @param price price for part
     * @param stock inventory level for part
     * @param min minimum level for part
     * @param max maximum level for part
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * Getter for machineId
     * @return machineId
     */
     public int getMachineId() {
         return machineId;
     }

    /**
     * Setter for machineId
     * @param machineId machineId of part
     */
     public void setMachineId(int machineId) {
         this.machineId = machineId;
     }
}
