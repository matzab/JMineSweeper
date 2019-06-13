package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button restartGame;
    @FXML
    private Label remainingFlagsLabel;


    private Grid grid;
    private Canvas canvas;
    private Difficulty gameDifficulty;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameDifficulty = Difficulty.BEGINNER;
        canvas = new Canvas(gameDifficulty.getGridWidth(), gameDifficulty.getGridWidth());
        anchorPane.getChildren().add(canvas);
        grid = new Grid(canvas, gameDifficulty, remainingFlagsLabel);

        restartGame.setOnMouseClicked(event -> {
            grid.clearGrid();
            grid = new Grid(canvas, gameDifficulty,remainingFlagsLabel);
        });
    }
}
