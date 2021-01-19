package ims;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;


/**
 * Inventory Class - Final/Master list of all parts and products
 * for final display/manipulation in MainController
 * @see MainController ims
 */
public class Inventory {
    // Lists for all parts/products in inventory
    private final static ObservableList<Part> inventoryParts = FXCollections.observableArrayList();
    private final static ObservableList<Product> inventoryProducts = FXCollections.observableArrayList();

    /**
     * Empty Inventory Constructor
     */
    public Inventory() {}

    /**
     * Add part to inventory
     * @param newPart - part to create
     */
    public static void addPart(Part newPart){ inventoryParts.add(newPart); }

    /**
     * Add product to inventory
     * @param newProduct - product to create
     */
    public static void addProduct(Product newProduct){ inventoryProducts.add(newProduct); }

    /**
     * @return list of parts in inventory
     */
    public static ObservableList<Part> getInventoryParts() { return inventoryParts; }

    /**
     * @return list of inventoried products
     */
    public static ObservableList<Product> getInventoryProducts() { return inventoryProducts; }

    /**
     * Tracks count for incrementing part IDs
     * @return total part count
     */
    public static int getInventoryPartCount() { return inventoryParts.size(); }

    /**
     * Tracks count for incrementing product IDs
     * @return total product count
     */
    public static int getInventoryProductCount() { return inventoryProducts.size(); }

    /**
     * Modify inventory part with new user input
     * @param modifiedPart - part selected for modification
     */
    public static void modifyPart(Part modifiedPart) {
        inventoryParts.set(modifiedPart.getId(), modifiedPart);
    }

    /**
     * Modify inventory product with new user input
     * @param modifiedProduct - product selected for modification
     */
    public static void modifyProduct(Product modifiedProduct) {
        inventoryProducts.set(modifiedProduct.getId(), modifiedProduct);
    }

    /**
     * Validate product deletion
     * @return true if product being validated has 1 or more associated parts
     */
    public static boolean validateProductDeletion(Product product) {
        return product.associatedParts.size() == 0;
    }

    /**
     * Use collection.removeIf to delete part if id is a match
     * @param partId - id of part to be deleted
     */
    public static void deletePart(int partId) {
            inventoryParts.removeIf(p -> p.getId() == partId);
    }

    /**
     * Use collection.removeIf to delete product if id is a match
     * @param productId - id of product to be deleted
     */
    public static void deleteProduct(int productId) {
        inventoryProducts.removeIf(p -> p.getId() == productId);
    }
}
