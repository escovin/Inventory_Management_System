package model;

/**
 * Models outsourced part.
 * @author Erik Scovin
 */

public class Outsourced extends Part{

    /**
     * Part company name
     */
    private String companyName;

    /**
     * Constructor for new instance
     * @param id ID for part
     * @param name name for part
     * @param price price of part
     * @param stock inventory level of part
     * @param min minimum level for part
     * @param max maximum level for part
     * @param companyName company name for part
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Getter for companyName
     * @return companyName
     */
    public String getCompanyName() {
        return companyName;

    }

    /**
     * Setter for companyName
     * @param companyName companyName of part
     */
    public void setCompanyName(String companyName) {

        this.companyName = companyName;
    }

}
