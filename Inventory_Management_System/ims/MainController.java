package ims;
import static ims.Inventory.*;
import javafx.collections.transformation.FilteredList;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.Parent;
import java.io.IOException;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.fxml.FXML;


/**
 * Main Controller - Controls main inventory management system FXML scene
 */
public class MainController {
    // Search Bars
    @FXML
    private TextField partSearchBar;
    @FXML
    private TextField productSearchBar;
    // Current Part/Product to be added or modified
    private static Part currentPart;
    private static Product currentProduct;
    // Parts Table
    @FXML
    private TableView<Part> partsTable;
    @FXML
    private TableColumn<Part, Integer> partIdCol;
    @FXML
    private TableColumn<Part, String> partNameCol;
    @FXML
    private TableColumn<Part, Integer> partInvCol;
    @FXML
    private TableColumn<Part, Double> partPriceCol;
    // Products Table
    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, Integer> productIdCol;
    @FXML
    private TableColumn<Product, String> productNameCol;
    @FXML
    private TableColumn<Product, Integer> productInvCol;
    @FXML
    private TableColumn<Product, Double> productPriceCol;
    // Empty Controller Constructor
    public MainController() {}

    /**
     * @return part in consideration
     */
    public static Part getCurrentPart() { return currentPart; }

    /**
     * Set new part as current part
     * @param newPart - Part class part object
     */
    public void setCurrentPart(Part newPart) { MainController.currentPart = newPart; }

    /**
     * @return product in consideration
     */
    public static Product getCurrentProduct() { return currentProduct; }

    /**
     * Set new product to current product
     * @param newProduct - Product class product object
     */
    public void setCurrentProduct(Product newProduct) { MainController.currentProduct = newProduct; }

    /**
     * If Exit is clicked, alert user, confirm exit, exit system
     */
    public void exitSystem() {
        Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION);
        exitAlert.setTitle("Exit Inventory Management System");
        exitAlert.setContentText("Exit?");
        if (exitAlert.showAndWait().orElseThrow() == ButtonType.OK) {
            System.exit(0);
        }
    }

    /**
     * Display part menu
     * @param event - Part Add or Modify button click event
     */
    @FXML
    void displayPartMenu(ActionEvent event) throws IOException { partMenu(event); }

    /**
     * Display product menu
     * @param event - Product Add or Modify button click event
     */
    @FXML
    void displayProductMenu(ActionEvent event) throws IOException {
        // If there are no parts in inventory, alert user
        if (getInventoryParts().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Parts Available");
            alert.setContentText("There are no parts available to add to a product.");
            alert.showAndWait();
        } else {
            productMenu(event);
        }
    }

    /**
     * Alert, confirm, execute part deletion
     */
    @FXML
    void alertPartDelete() {
        // Check if there are any parts to delete
        if (getInventoryParts().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Parts Available");
            alert.setContentText("There are no parts to delete.");
            alert.showAndWait();
        } else {
            Part part = partsTable.getSelectionModel().getSelectedItem();
            Alert delAlert = new Alert(Alert.AlertType.CONFIRMATION);
            delAlert.setTitle("Delete Part");
            delAlert.setContentText("Delete " + part.getName() + "?");
            // Display alert, if OK button is clicked ->
            if (delAlert.showAndWait().orElseThrow() == ButtonType.OK) {
                // Delete part
                deletePart(part.getId());
                // Re-fill part table to display deletion
                fillPartTable();
            }
        }
    }

    /**
     * Validate, Alert, Confirm and execute product deletion.
     */
    @FXML
    void alertProductDelete() {
        // Check if there are any products to delete
        if (getInventoryProducts().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Products Available");
            alert.setContentText("There are no products to delete.");
            alert.showAndWait();
        } else {
            Product product = productTable.getSelectionModel().getSelectedItem();
            // If product has a part associated with it, alert user
            if (!validateProductDeletion(product)) {
                Alert delAlert = new Alert(Alert.AlertType.INFORMATION);
                delAlert.setTitle("Product Deletion Error!");
                delAlert.setHeaderText("Product has associated parts.");
                delAlert.showAndWait();
            } else {
                // If product has no parts associated, prompt delete confirmation
                Alert delAlert = new Alert(Alert.AlertType.CONFIRMATION);
                delAlert.setTitle("Product Delete");
                delAlert.setHeaderText("Delete " + product.getName() + "?");
                // If user confirms the deletion alerts
                if (delAlert.showAndWait().orElseThrow() == ButtonType.OK) {
                    // If product has no parts associated, delete it
                    deleteProduct(product.getId());
                    // Re-fill part table
                    fillPartTable();
                }
            }
        }
    }

    /**
     * Set modifiedPart. modifiedPart is used to dynamically switch between add
     * and modify view.
     * @param event - modify part button click
     */
    @FXML
    void displayModifyPartMenu(ActionEvent event) throws IOException {
        if (getInventoryParts().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Parts Available");
            alert.setContentText("There are no parts to modify.");
            alert.showAndWait();
        } else {
            currentPart = partsTable.getSelectionModel().getSelectedItem();
            setCurrentPart(currentPart);
            partMenu(event);
        }
    }

    /**
     * Set modifiedProduct. modifiedProduct is used to dynamically switch
     * between add and modify view.
     * @param event - modify product button click
     */
    @FXML
    void displayModifyProductMenu(ActionEvent event) throws IOException {
        // If there are no products in inventory, alert user
        if (getInventoryProducts().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Products Available");
            alert.setContentText("There are no products to modify.");
            alert.showAndWait();
        } else {
            currentProduct = productTable.getSelectionModel().getSelectedItem();
            setCurrentProduct(currentProduct);
            productMenu(event);
        }
    }

    /**
     * Load Main Menu
     * @param event - action which completes a part or product addition/modification
     */
    public static void mainMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(MainController.class.getResource("Main.fxml"));
        Scene startScene = new Scene(root);
        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        mainStage.setScene(startScene);
        mainStage.show();
    }

    /**
     * Load Parts Menu
     * @param event - part add/modify action
     */
    public static void partMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(MainController.class.getResource("Part.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * Load Products Menu
     * @param event - product add/modify action
     */
    public static void productMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(MainController.class.getResource("Product.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * Populate the parts table.
     */
    public void fillPartTable() { partsTable.setItems(getInventoryParts()); }

    /**
     * Populate the product table.
     */
    public void fillProductTable() { productTable.setItems(getInventoryProducts()); }

    /**
     * Initiate Main Controller Logic
     * Implements part and product search logic
     * Filters tables based on search bar input
     * ------------------------ LOGICAL ERROR ------------------------
     * A logical error was corrected in main controller initialization:
     *    When creating the filtered lists for search bar logic,
     *    the program ran fine and even filtered items, however, the
     *    items would then multiply themselves in the list, appearing
     *    as multiple identical parts each time a search was input.
     *    To fix this error, I had to use the FilteredList Type as
     *    a wrapper around the original list, then populate tables
     *    with that, rather than altering the main parts/products lists.
     */
    @FXML
    public void initialize() {
        // Set Placeholder text for empty Table
        partsTable.setPlaceholder(new Label("No Parts found - or currently available"));
        productTable.setPlaceholder(new Label("No Products found - or currently available"));
        // Set our current part and product to null (non existent)
        setCurrentPart(null);
        setCurrentProduct(null);
        // Set Part Cell Values
        partIdCol.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getId()).asObject());
        partNameCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        partInvCol.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getStock()).asObject());
        partPriceCol.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getPrice()).asObject());
        // Set Product Cell Values
        productIdCol.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getId()).asObject());
        productNameCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        productInvCol.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getStock()).asObject());
        productPriceCol.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getPrice()).asObject());
        // Fill Part and Product Tables
        fillPartTable();
        fillProductTable();
        // Filter for part search
        FilteredList<Part> filteredParts = new FilteredList<>(getInventoryParts(), p -> true);
        partSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredParts.setPredicate(part -> {
                if (newValue == null || newValue.isEmpty()) { return true; }
                String nameValue = newValue.toLowerCase();
                if (part.getName().toLowerCase().contains(nameValue)) {
                    return true;
                } else if (Integer.valueOf(part.getId()).toString().equals(newValue)) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        // Filter for product search
        FilteredList<Product> filteredProducts = new FilteredList<>(getInventoryProducts(), p -> true);
        productSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredProducts.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty()) { return true; }
                String nameValue = newValue.toLowerCase();
                if (product.getName().toLowerCase().contains(nameValue)) {
                    return true;
                } else if (Integer.valueOf(product.getId()).toString().equals(newValue)) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        // Refresh Parts and Products Tables with search filter matches
        partsTable.setItems(filteredParts);
        productTable.setItems(filteredProducts);
    }
}