package ch.bbw;

import ch.bbw.model.CalcRoute;
import ch.bbw.model.Field;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * @author TheBromo
 */
public class FXMLDocumentController implements Initializable {

    Thread runningThread;
    @FXML
    private ProgressBar progress;
    @FXML
    private Label distance;
    @FXML
    private HBox trainingHBox;
    @FXML
    private VBox log;
    @FXML
    private Canvas canvas;
    @FXML
    private TextField amount;
    @FXML
    private CheckBox showNumbers, showPaths, showHelpLines;
    @FXML
    private RadioButton greedy, random, tour,genetic,sm;
    @FXML
    private GraphicsContext gc;
    private TextField iterations;
    private double mouseX, mouseY;
    private Field field;
    private CalcRoute calcRoute;
    private Stage primaryStage;
    private boolean pathShowing, animationPlaying,calculated;

    @FXML
    public void handleButtonCalculate(ActionEvent event) {
        calculated =true;
        showPaths.setDisable(false);
        removeControls();
        setProgress();
        if (greedy.isSelected()) {
            Runnable runnable = () -> calcRoute.setGreedy((ArrayList<Point2D>) field.getPoint2DS());
            runningThread = new Thread(runnable);
            runningThread.start();

        } else if (random.isSelected()) {
            calcRoute.setRandomPath((ArrayList<Point2D>) field.getPoint2DS());
        } else if (tour.isSelected()) {
            addControls();
            Runnable runnable = () -> calcRoute.setTwoOpt((ArrayList<Point2D>) field.getPoint2DS());
            runningThread = new Thread(runnable);
            runningThread.start();
        }else if (genetic.isSelected()){
            Runnable runnable =()-> calcRoute.geneticAlg((ArrayList<Point2D>) field.getPoint2DS());
            runningThread = new Thread(runnable);
            runningThread.start();
        }
        else if (sm.isSelected()){
            Runnable runnable =()-> calcRoute.simulatedAnnealing((ArrayList<Point2D>) field.getPoint2DS());
            runningThread = new Thread(runnable);
            runningThread.start();
        }
        showPaths.setSelected(true);
        pathShowing = true;
        draw();
        distance.setText(calcRoute.getDistance() + "");

    }

    @FXML
    public void showPath(ActionEvent event) {
        //needs to be deactiveated when animation is rolling
        pathShowing = showPaths.isSelected();
        draw();

    }

    @FXML
    public void handleButtonImport(ActionEvent event) {
        if (!animationPlaying) {

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
        if (!animationPlaying) {

            field.addPoint2D(event.getX(), event.getY());
            draw();
        }
    }

    @FXML
    public void handleMouseMoved(MouseEvent event) {
        if (!animationPlaying) {

            mouseX = event.getX();
            mouseY = event.getY();
            draw();
        }
    }

    @FXML
    public void handleButtonGenerate(ActionEvent event) {
        if (!animationPlaying) {

            int amout = Integer.parseInt(amount.getText());
            field.generate(amout, (int) canvas.getWidth(), (int) canvas.getHeight());
            draw();
        }
    }

    @FXML
    public void handleButtonReset(ActionEvent event) {
        if (!animationPlaying) {
            field.clear();
            draw();
            calcRoute.getPoints().clear();
        }
    }

    public void setAnimationPlaying(boolean animationPlaying) {
        this.animationPlaying = animationPlaying;
    }

    @FXML
    public void handleRunTraining(ActionEvent event) {

        Runnable runnable = () -> calcRoute.setTwoOptAnimation((ArrayList<Point2D>) field.getPoint2DS(), Integer.parseInt(iterations.getText()));

        new Thread(runnable).start();
    }

    @FXML
    public void handleRunTrainingFast(ActionEvent event) {

        Runnable runnable = () -> calcRoute.setTwoOpt((ArrayList<Point2D>) field.getPoint2DS());

        new Thread(runnable).start();
    }

    public void setProgress() {
        Platform.runLater(() -> progress.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS));
    }

    public void finishProcess() {
        Platform.runLater(() -> progress.setProgress(1));
    }

    private void addControls() {
        Button button = new Button("Run Training");
        button.setOnAction(this::handleRunTraining);
        trainingHBox.getChildren().add(button);

        TextField textField = new TextField();
        iterations = textField;
        trainingHBox.getChildren().add(textField);

        Button button1 = new Button("Run Fast Training");
        button1.setOnAction(this::handleRunTrainingFast);
        trainingHBox.getChildren().add(button1);
    }

    private void removeControls() {
        trainingHBox.getChildren().clear();
    }

    public void alert() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Still Running");
            alert.setContentText("CalcRoute searching process already running");
            alert.showAndWait();
        });

    }


    public void showNumbersClicked(ActionEvent event) {
        draw();
    }

    private void updateLog() {
        log.getChildren().clear();
        int count = 0;
        for (Point2D point2D : field.getPoint2DS()) {
            count++;
            log.getChildren().add(new Label(count + ") x:" + point2D.getX() + " y:" + point2D.getY()));
        }
    }

    private void draw() {

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.BLACK);
        if (showHelpLines.isSelected()) {
            //heightline
            gc.strokeLine(mouseX, 0, mouseX, mouseY);
            gc.strokeText(Double.toString(mouseX), mouseX / 2, mouseY);

            //widthline
            gc.strokeLine(0, mouseY, mouseX, mouseY);
            gc.strokeText(Double.toString(mouseY), mouseX, mouseY / 2);
        }

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
        if (pathShowing) {
            count = 0;
            gc.setStroke(Color.BLUE);
            for (Point2D point2D : calcRoute.getPoints()) {
                count++;
                if (calcRoute.getPoints().size() > count) {
                    Point2D point = calcRoute.getPoints().get(count);
                    gc.strokeLine(point2D.getX(), point2D.getY(), point.getX(), point.getY());
                }
            }
            if(calcRoute.getPoints().size()!=0) {
                Point2D first = calcRoute.getPoints().get(0);
                Point2D last = calcRoute.getPoints().get(calcRoute.getPoints().size() - 1);
                gc.strokeLine(first.getX(), first.getY(), last.getX(), last.getY());
            }
        }

            distance.setText(calcRoute.getDistance() + "");

    }

    void drawPoints(ArrayList<Point2D>points){
        if (pathShowing) {
            int count = 0;
            gc.setStroke(Color.color(1,0,0,0.3));
            for (Point2D point2D : points) {
                count++;
                if (points.size() > count) {
                    Point2D point = points.get(count);
                    gc.strokeLine(point2D.getX(), point2D.getY(), point.getX(), point.getY());
                }
            }
            if(points.size()!=0) {
                Point2D first = points.get(0);
                Point2D last = points.get(points.size() - 1);
                gc.strokeLine(first.getX(), first.getY(), last.getX(), last.getY());
            }
        }
    }

    public void drawExtern() {

        Platform.runLater(this::draw);
    }

    public void drawExternGA(ArrayList<Point2D> secondbest) {

        Platform.runLater(()->{
            draw();
            drawPoints(secondbest);
        });
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gc = canvas.getGraphicsContext2D();
        field = new Field();
        calcRoute = new CalcRoute(this);
        showPaths.setDisable(true);
        animationPlaying = false;
        calculated=false;
    }

}
