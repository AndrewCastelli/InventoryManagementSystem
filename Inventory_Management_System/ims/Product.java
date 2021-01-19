package ims;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

/**
 * Product Class to represent a product in inventory
 * Products are comprised of at least one Part.
 */
public class Product {
    private int id;
    private String name;
    private double price;
    private int stock;
    private int max;
    private int min;
    public ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    public Product() {}
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * @return product identifier
     */
    public int getId() {
        return id;
    }

    /**
     * @param id - product identifier
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return product name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return product price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price - set price amount
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return currently stocked inventory
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock - stock inventory
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return maximum amount of product that can be stocked
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max amount of product that can be stocked
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * @return minimum amount of product that can be stocked
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min - amount of product that can be stocked
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @param associatedPart - part associated with product
     */
    public void addAssociatedPart(Part associatedPart) {
        this.associatedParts.add(associatedPart);
    }

    /**
     * @return list of all parts associated with a product
     */
    public ObservableList<Part> getAssociatedParts() {
        return associatedParts;
    }

    /**
     * Clear all product's associated parts to allow user to modify
     */
    public void emptyProductPartList() { associatedParts = FXCollections.emptyObservableList(); }

    /**
     * Validation test for specified requirements
     * If product fails a test, prompt user with relevant info
     *
     * @throws IVException - alert user to errors in form input
     */
    public void validateProduct() throws IVException {
        // Min should be less than Max
        if (getMin() > getMax()) {
            throw new IVException("Minimum stock must be less than maximum.");
        }
        // Inv should be between Min and Max values
        if (getStock() < getMin() || getStock() > getMax()) {
            throw new IVException("Inv input must be between min and max.");
        }
        // Don't leave name blank
        if (getName().isBlank()) {
            throw new IVException("Product name can not be left blank.");
        }
        // Don't let min max or inventory stock be 0
        if (getStock() == 0 || getMin() <= 0 || getMax() == 0) {
            throw new IVException("Min, Max, and Inv should not be 0");
        }
        // Price must be greater than 0.00
        if (getPrice() < 0.00) {
            throw new IVException("Product Price must be greater than 0");
        }
        // Product price must equal or supersede the price of each part added together
        double productPartsSum = 0;
        for(Part p : getAssociatedParts()) { productPartsSum += p.getPrice(); }

        if (productPartsSum > getPrice()) {
            throw new IVException("Product price must be equal to or greater than the sum of it's parts.");
        }

    }
}