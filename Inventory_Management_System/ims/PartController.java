package ims;
import static ims.MainController.mainMenu;
import static ims.MainController.getCurrentPart;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.Modality;
import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXML;


/**
 * Part Controller - Controls Add/Modify Part logic for Part.fxml
 */
public class PartController {
    // Current part in consideration
    public final Part currentPart;
    // Text values for part display info
    @FXML
    private TextField partID;
    @FXML
    private TextField partName;
    @FXML
    private TextField partStock;
    @FXML
    private TextField partPrice;
    @FXML
    private TextField partMax;
    @FXML
    private TextField partMin;
    @FXML
    private TextField partManufacturer;
    // Title & Labels to display Add/Modify/Manufacturing info
    @FXML
    private Label manufacturerLabel;
    @FXML
    private Label pageTitle;
    // Radio Buttons for InHouse or Outsource part manufacturer
    @FXML
    private RadioButton partInHouse;
    @FXML
    private RadioButton partOutsource;
    @FXML
    private boolean isInHouse;


    /**
     * Constructor, sets currentPart to control addition/modification of
     */
    public PartController() { this.currentPart = getCurrentPart(); }

    /**
     * When 'InHouse' radiobutton is clicked, set text to 'Machine ID'
     */
    @FXML
    void radioInHouseClick() {
        isInHouse = true;
        manufacturerLabel.setText("Machine ID");
    }

    /**
     * When 'Outsourced' radio button is clicked, set text to 'Company Name'
     */
    @FXML
    void radioOutsourcedClick() {
        isInHouse = false;
        manufacturerLabel.setText("Company Name");
    }

    /**
     * When 'Cancel' is clicked, prompts user for confirmation with alert.
     * If user confirms cancellation, return to main menu.
     */
    @FXML
    void partCancelAlert(ActionEvent event) throws IOException {
        Alert cancelAlert = new Alert(Alert.AlertType.CONFIRMATION);
        cancelAlert.initModality(Modality.NONE);
        cancelAlert.setTitle("Cancel Current Changes?");
        cancelAlert.setContentText("Confirm " + partName.getText() + " Cancellation?");
        if (cancelAlert.showAndWait().orElseThrow() == ButtonType.OK) { mainMenu(event); }
    }

    /**
     * Save new part or update part with modification
     */
    @FXML
    void savePart(ActionEvent event) throws IOException {
        // Get data from the GUI
        String currPartName = this.partName.getText();
        String currPartInventory = partStock.getText();
        String currPartPrice = this.partPrice.getText();
        String currPartMinimum = this.partMin.getText();
        String currPartMaximum = this.partMax.getText();
        String currPartManufacturer = partManufacturer.getText();

        if ("".equals(currPartInventory)) {
            currPartInventory = "0";
        }
        // If InHouse radio button is checked
        try {
            if (isInHouse) {
                PartInHouse newPart = new PartInHouse();
                newPart.setName(currPartName);
                newPart.setPrice(Double.parseDouble(currPartPrice));
                newPart.setStock(Integer.parseInt(currPartInventory));
                newPart.setMax(Integer.parseInt(currPartMaximum));
                newPart.setMin(Integer.parseInt(currPartMinimum));
                newPart.setMachineId(Integer.parseInt(currPartManufacturer));

                try {
                    // Validate user inputs meet part criteria
                    newPart.validatePart();
                    // If current part is not null, update old part with modification
                    if (currentPart != null) {
                        int partID = currentPart.getId();
                        newPart.setId(partID);
                        Inventory.modifyPart(newPart);
                        // When current part is null - add it as new part
                    } else {
                        newPart.setId(Inventory.getInventoryPartCount());
                        Inventory.addPart(newPart);
                    }
                    // Return to the main screen
                    mainMenu(event);
                    // If validation fails, catch error, throw IVException, show relevant error message
                } catch (IVException e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Invalid Input");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }

            } else {
                // Create new or modify OutsourcedPart
                PartOutsourced newPart = new PartOutsourced();
                newPart.setName(currPartName);
                newPart.setPrice(Double.parseDouble(currPartPrice));
                newPart.setStock(Integer.parseInt(currPartInventory));
                newPart.setMax(Integer.parseInt(currPartMaximum));
                newPart.setMin(Integer.parseInt(currPartMinimum));
                newPart.setCompanyName(currPartManufacturer);

                try {
                    newPart.validatePart();
                    // If part null -> add part, if part not null -> modify part
                    if (currentPart == null) {
                        newPart.setId(Inventory.getInventoryPartCount());
                        Inventory.addPart(newPart);
                        // If part does not exist, create new part
                    } else {
                        int partID = currentPart.getId();
                        newPart.setId(partID);
                        Inventory.modifyPart(newPart);
                    }
                    // Return to the main screen
                    mainMenu(event);

                } catch (IVException e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Invalid Input");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
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
     * Initiate Part Controller Logic -
     * Sets part page and manufacturer labels
     */
    @FXML
    public void initialize() {
        // For null parts, set part page title to Add Part, manufacturer label to MachineID
        if (currentPart == null) {
            pageTitle.setText("Add Part");
            manufacturerLabel.setText("Machine ID");
            int partAutoID = Inventory.getInventoryPartCount();
            partID.setText("AUTO GEN: " + partAutoID);
            isInHouse = true;
        }
        else{
            // For non-null (already existing) parts, set part page title to Modify Part
            // Set
            pageTitle.setText("Modify Part");
            partID.setText(Integer.toString(currentPart.getId()));
            partName.setText(currentPart.getName());
            partStock.setText(Integer.toString(currentPart.getStock()));
            partPrice.setText(Double.toString(currentPart.getPrice()));
            partMin.setText(Integer.toString(currentPart.getMin()));
            partMax.setText(Integer.toString(currentPart.getMax()));

            // If current part is an instance of PartInHouse, manufacturer label -> Machine ID
            if (currentPart instanceof PartInHouse) {
                partManufacturer.setText(Integer.toString(((PartInHouse) currentPart).getMachineID()));
                manufacturerLabel.setText("Machine ID");
                partInHouse.setSelected(true);
            // If current part is an instance of PartOutsourced, manufacturer label -> Company Name
            } else {
                partManufacturer.setText(((PartOutsourced) currentPart).getCompanyName());
                manufacturerLabel.setText("Company Name");
                partOutsource.setSelected(true);
            }
        }
    }
}
