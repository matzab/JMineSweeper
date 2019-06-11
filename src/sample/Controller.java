package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;



public class Controller implements Initializable {
    @FXML
    private AnchorPane anchorPane;

    private Grid grid;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        grid = new Grid(anchorPane);
    }
}
