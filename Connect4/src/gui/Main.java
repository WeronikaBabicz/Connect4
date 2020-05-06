package gui;

import engine.algorithms.Algorithm;
import engine.algorithms.MinMaxAlgorithm;
import engine.gameplay.Game;
import engine.gameplay.PlayerPlayerGame;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UI.fxml"));
        Parent root = loader.load();
        Controller controller = (Controller) loader.getController();

        Game game = new PlayerPlayerGame();
        Algorithm algorithm = new MinMaxAlgorithm();

        controller.setAlgorithm(algorithm);
        controller.setGame(game);
        controller.startNewGame();

        primaryStage.setTitle("Connect4 game");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
