package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


//TODO add timer
//TODO add win condition
//TODO refactor code to reflect OOP/functional programming
//TODO add comments

public class Controller implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button restartGame;
    @FXML
    private Label remainingFlagsLabel, gameOverLabel;
    @FXML
    private ComboBox difficultyComboBox;

    private Grid grid;
    private Canvas canvas;
    private Difficulty gameDifficulty;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameDifficulty = Difficulty.BEGINNER;
        canvas = new Canvas(gameDifficulty.getGridWidth(), gameDifficulty.getGridWidth());
        canvas.setLayoutX(20);
        canvas.setLayoutY(40);

        difficultyComboBox.getItems().addAll(Difficulty.BEGINNER, Difficulty.INTERMEDIATE, Difficulty.EXPERT);
        difficultyComboBox.setPromptText("Difficulty");

        difficultyComboBox.setOnAction((Event ev) -> {
            gameDifficulty = (Difficulty) difficultyComboBox.getSelectionModel().getSelectedItem();
            resetGrid();
            Stage root = (Stage) ((Node) ev.getSource()).getScene().getWindow();
            root.setHeight(gameDifficulty.getGridWidth() + 100);
        });

        anchorPane.getChildren().add(canvas);

        grid = new Grid(canvas, gameDifficulty, remainingFlagsLabel, gameOverLabel);

        restartGame.setOnMouseClicked(event -> {
           resetGrid();
        });
    }


    private void resetGrid(){
        grid.clearGrid();
        canvas.setWidth(gameDifficulty.getGridWidth());
        canvas.setHeight(gameDifficulty.getGridWidth());
        grid = new Grid(canvas, gameDifficulty, remainingFlagsLabel, gameOverLabel);
    }

}
