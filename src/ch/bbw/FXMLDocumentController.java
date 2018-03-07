package ch.bbw;

import ch.bbw.model.Field;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author TheBromo
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Label log, distance;
    @FXML
    private Canvas canvas;
    @FXML
    private TextField amount;
    @FXML
    private CheckBox showNumbers;
    @FXML
    private RadioButton greedy, random, tour;
    @FXML
    private GraphicsContext gc;
    private double mouseX, mouseY;
    private Field field;
    private Stage primaryStage;

    @FXML
    public void handleButtonCalculate(ActionEvent event) {

    }

    @FXML
    public void handleButtonImport(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TS files (*.ts)", "*.travel");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(primaryStage);
        try {
            if (file != null) {
                field.importData(file.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        draw();
    }

    @FXML
    public void handleButtonExport(ActionEvent event) {
        try {
            field.exportData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleMouseClicked(MouseEvent event) {
        field.addPoint2D(event.getX(), event.getY());
        draw();
    }

    @FXML
    public void handleMouseMoved(MouseEvent event) {
        mouseX = event.getX();
        mouseY = event.getY();
        draw();
    }

    @FXML
    public void handleButtonGenerate(ActionEvent event) {
        int amout = Integer.parseInt(amount.getText());
        field.generate(amout, (int) canvas.getWidth(), (int) canvas.getHeight());
        draw();
    }

    public void handleButtonReset(ActionEvent event) {
        field.clear();
        draw();
    }

    public void showNumbersClicked(ActionEvent event) {
        draw();
    }

    private void updateLog() {
        log.setText("");
        int count = 0;
        for (Point2D point2D : field.getPoint2DS()) {
            count++;
            log.setText(log.getText() + "\n" + count + ") x:" + point2D.getX() + " y:" + point2D.getY());
        }
    }

    private void draw() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        //heightline
        gc.strokeLine(mouseX, 0, mouseX, mouseY);
        gc.strokeText(Double.toString(mouseX), mouseX / 2, mouseY);

        //widthline
        gc.strokeLine(0, mouseY, mouseX, mouseY);
        gc.strokeText(Double.toString(mouseY), mouseX, mouseY / 2);

        //dots
        int count = 0;
        for (Point2D point : field.getPoint2DS()) {
            gc.setStroke(Color.BLACK);
            count++;
            gc.strokeLine(point.getX() - 3, point.getY(), point.getX() + 3, point.getY());
            gc.strokeLine(point.getX(), point.getY() - 3, point.getX(), point.getY() + 3);

            if (showNumbers.isSelected()) {
                gc.setStroke(Color.LIGHTGRAY);
                gc.strokeText(Integer.toString(count), point.getX() + 5, point.getY() + 14);
            }
        }
        updateLog();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gc = canvas.getGraphicsContext2D();
        field = new Field();
    }

    public void showPath(ActionEvent event) {
        //needs to be deactiveated when animation is rolling
    }
}
