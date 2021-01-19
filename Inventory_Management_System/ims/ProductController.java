package ims;
import static ims.MainController.mainMenu;
import static ims.MainController.getCurrentProduct;
import javafx.collections.transformation.FilteredList;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.fxml.FXML;


/**
 * Product Controller - Controls Add/Modify logic for Product.fxml
 */
public class ProductController {
    // Current product in consideration
    public final Product currentProduct;
    // Text values for product display information
    @FXML
    private Label pageLabel;
    @FXML
    private TextField productID;
    @FXML
    private TextField productName;
    @FXML
    private TextField productMax;
    @FXML
    private TextField productMin;
    @FXML
    private TextField productStock;
    @FXML
    private TextField productPrice;
    @FXML
    private TextField productPartSearch;
    // Table and Table Column Values for parts available for association
    @FXML
    private TableView<Part> productPartsTable;
    @FXML
    private TableColumn<Part,Integer> productPartsIDCol;
    @FXML
    private TableColumn<Part,String> productPartsNameCol;
    @FXML
    private TableColumn<Part,Integer> productPartsCol;
    @FXML
    private TableColumn<Part,Double> productPartsPriceCol;
    // Table and Table Column Values for parts associated with product
    @FXML
    private TableView<Part> currentPartsTable;
    @FXML
    private TableColumn<Part,Integer> currentPartsIDCol;
    @FXML
    private TableColumn<Part,String> currentPartsNameCol;
    @FXML
    private TableColumn<Part,Integer> currentPartsCol;
    @FXML
    private TableColumn<Part,Double> currentPartsPriceCol;
    @FXML
    private ObservableList<Part> productParts = FXCollections.observableArrayList();

    /**
     * Product Controller Constructor
     * Initialize/Set data for product in consideration
     */
    public ProductController() { this.currentProduct = getCurrentProduct(); }

    /**
     * Add part to current parts table
     */
    @FXML
    void addAssociatedPart() {
        Part associatedPart = productPartsTable.getSelectionModel().getSelectedItem();
        productParts.add(associatedPart);
        fillCurrentParts();
    }

    /**
     * When 'Cancel' is clicked, prompt user for confirmation with alert.
     * If cancellation is confirmed, return to main menu.
     * @param event - Cancel button clicked event
     */
    @FXML
    void productCancelAlert(ActionEvent event) throws IOException {
        Alert cancelAlert = new Alert(Alert.AlertType.CONFIRMATION);
        cancelAlert.setTitle("Cancel Modification");
        cancelAlert.setContentText("Cancel " + productName.getText() + " modification?");
        // If user clicks OK button, return to main menu
        if (cancelAlert.showAndWait().orElseThrow() == ButtonType.OK) { mainMenu(event); }
    }

    /**
     * If user confirms deletion, delete associated part.
     */
    @FXML
    void deleteProductAlert() {
        Part part = currentPartsTable.getSelectionModel().getSelectedItem();
        Alert delAlert = new Alert(Alert.AlertType.CONFIRMATION);
        delAlert.setTitle("Delete Associated Part");
        delAlert.setContentText("Delete " + part.getName() + " from "+ productName.getText() + "?");
        // If user clicks OK button, remove part
        if (delAlert.showAndWait().orElseThrow() == ButtonType.OK) { productParts.remove(part); };
    }

    /**
     * Save new product or update product with modification
     * @param event - Save button clicked event
     */
    @FXML
    void saveProduct(ActionEvent event) throws IOException {
        try {
            String productName = this.productName.getText();
            String productInv = productStock.getText();
            String productPrice = this.productPrice.getText();
            String productMax = this.productMax.getText();
            String productMin = this.productMin.getText();
            // Initiate new product object, fill product information
            Product newProduct = new Product();
            newProduct.setName(productName);
            newProduct.setPrice(Double.parseDouble(productPrice));
            newProduct.setStock(Integer.parseInt(productInv));
            newProduct.setMin(Integer.parseInt(productMin));
            newProduct.setMax(Integer.parseInt(productMax));
            // When modifying existing product, clear current parts list.
            if (currentProduct != null) { currentProduct.emptyProductPartList(); }
            // Loop through parts, adding them to product
            for (Part p : productParts) { newProduct.addAssociatedPart(p); }

            try {
                // Ensure product fulfills all requirements
                newProduct.validateProduct();
                // If current product doesn't exist, create new product
                if (currentProduct == null) {
                    newProduct.setId(Inventory.getInventoryProductCount());
                    Inventory.addProduct(newProduct);
                    // If current product exists, update info with new inputs
                } else {
                    newProduct.setId(currentProduct.getId());
                    Inventory.modifyProduct(newProduct);
                }
                // Return to main
                mainMenu(event);
                // If validation fails, catch error, throw IVException, show relevant error message
            } catch (IVException err) {
                Alert validityAlert = new Alert(Alert.AlertType.INFORMATION);
                validityAlert.setTitle("ERROR");
                validityAlert.setContentText(err.getMessage());
                validityAlert.showAndWait();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ERROR");
            alert.setHeaderText("Invalid Number-Field Input");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Fill parts table with all inventoried parts
     */
    public void fillParts() { productPartsTable.setItems(Inventory.getInventoryParts()); }

    /**
     * Fill current parts table with parts associated with current product
     */
    public void fillCurrentParts() { currentPartsTable.setItems(productParts); }

    /**
     * Initiate Main Controller Logic
     * Set up part search logic to filter based on part search bar input
     */
    @FXML
    public void initialize() {
        // Set placeholder text for empty table
        productPartsTable.setPlaceholder(new Label("No Parts found - or currently available"));
        currentPartsTable.setPlaceholder(new Label("No Parts currently associated with this product"));
        // If product does not yet exist, set page to Add Product
        if (currentProduct == null) {
            pageLabel.setText("Add Product");
            // Increment Auto Generated ID value based on product count
            int productCount = Inventory.getInventoryProductCount();
            productID.setText("Disabled - AUTO GEN: " + productCount);
        } else {
            // If product is not null, set page to Modify Product, get product values
            pageLabel.setText("Modify Product");
            productID.setText(Integer.toString(currentProduct.getId()));
            productName.setText(currentProduct.getName());
            productStock.setText(Integer.toString(currentProduct.getStock()));
            productPrice.setText(Double.toString(currentProduct.getPrice()));
            productMin.setText(Integer.toString(currentProduct.getMin()));
            productMax.setText(Integer.toString(currentProduct.getMax()));
            productParts = currentProduct.getAssociatedParts();
        }
        // All Parts available for product association
        productPartsIDCol.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getId()).asObject());
        productPartsNameCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        productPartsCol.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getStock()).asObject());
        productPartsPriceCol.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getPrice()).asObject());
        // Current Parts associated with product
        currentPartsIDCol.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getId()).asObject());
        currentPartsNameCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        currentPartsCol.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getStock()).asObject());
        currentPartsPriceCol.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getPrice()).asObject());
        // Populate/Update Table values
        fillParts();
        fillCurrentParts();
        // Filter for part search
        FilteredList<Part> filteredProductParts = new FilteredList<>(Inventory.getInventoryParts(), p -> true);
        productPartSearch.textProperty().addListener((observable, oldValue, newValue) ->
                filteredProductParts.setPredicate(part -> {
            if (newValue == null || newValue.isEmpty()) { return true; }
            String nameValue = newValue.toLowerCase();
            if (part.getName().toLowerCase().contains(nameValue)) {
                return true;
            } else if (Integer.valueOf(part.getId()).toString().equals(newValue)) {
                return true;
            } else {
                return false;
            }
        }));
        // Refresh Parts Table with search filter matches
        productPartsTable.setItems(filteredProductParts);
    }
}
