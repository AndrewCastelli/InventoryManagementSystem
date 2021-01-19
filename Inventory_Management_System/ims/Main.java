package ims;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * @author Andrew Castelli - StudentID: 001488272
 * ------------- Software I - C482 --------------
 * Main class, execute to start GUI
 *
 * ------------ FUTURE  ENHANCEMENT -------------
 * Future enhancements that could potentially be implemented:
 * 1. Sanitize all inputs attempted by user.
 * 2. Interrupt attempts to input identical parts or products.
 * 3. Verify that the correct min/max inventory numbers fit company space/standards.
 * 4. Check and block any other violations/contradictions with a master list of
 *    part/product specifications with which all inventory inputs should be aligned.
 * 5. Create part and/or product templates available in the ADD sections of the GUI
 *    for fast, simplified user input.
 * 6. Unique sort filters (radio buttons) near search bar to click on for quick
 *    sorting parts/products by certain useful parameters.
 */
public class Main extends Application {
    /**
     * Initiate and display main scene for our inventory system
     * @param mainStage - main FXML pane
     */
    @Override
    public void start(Stage mainStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        mainStage.setTitle("Inventory Management System");
        Scene startScene = new Scene(root);
        mainStage.setScene(startScene);
        mainStage.show();
    }
    public static void main(String[] args) { launch(args); }
}
