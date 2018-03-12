package ch.bbw.view;

import ch.bbw.FXMLDocumentController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author TheBromo
 */
public class FXMLTemplate extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ch/bbw/view/FXMLDocument.fxml"));
        Parent root1 = fxmlLoader.load();

        
        Scene scene = new Scene(root1);
        FXMLDocumentController controller = fxmlLoader.getController();
        controller.setPrimaryStage(stage);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
