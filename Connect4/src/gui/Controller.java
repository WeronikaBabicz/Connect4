package gui;

import engine.algorithms.Algorithm;
import engine.algorithms.AlphaBetaAlgorithm;
import engine.algorithms.MinMaxAlgorithm;
import engine.gameplay.*;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import utilities.Player;
import javafx.event.ActionEvent;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class Controller {
    private Game game;

    @FXML
    private Label winner, turn, turnColor, gameType, winnerColor, leader, winnerTitle, firstPlayerInfo, secondPlayerInfo;

    private ArrayList<ArrayList<Circle>> circles = new ArrayList<>();

    @FXML
    private Circle c00, c01, c02, c03, c04, c05, c06,
                   c10, c11, c12, c13, c14, c15, c16,
                   c20, c21, c22, c23, c24, c25, c26,
                   c30, c31, c32, c33, c34, c35, c36,
                   c40, c41, c42, c43, c44, c45, c46,
                   c50, c51, c52, c53, c54, c55, c56;

    @FXML
    private Rectangle col0, col1, col2, col3, col4, col5, col6;

    @FXML
    private ComboBox algorithmPA = new ComboBox();

    @FXML
    private ComboBox algorithmAAFirst = new ComboBox();

    @FXML
    private ComboBox algorithmAASecond = new ComboBox();

    @FXML
    private ComboBox depthPA = new ComboBox();

    @FXML
    private ComboBox depthAAFirst = new ComboBox();

    @FXML
    private ComboBox depthAASecond = new ComboBox();

    @FXML
    private Label algPA = new Label();

    @FXML
    private Label algAAF = new Label();

    @FXML
    private Label algAAS = new Label();

    @FXML
    private Label depPA = new Label();

    @FXML
    private Label depAAF = new Label();

    @FXML
    private Label depAAS = new Label();

    @FXML
    private Button newGamePlayerPlayer, newGamePlayerAi, newGameAiAi;

    void setGame(Game game) {
        this.game = game;
    }

    @FXML
    void col0(MouseEvent event){
        makeMove(0);
    }

    @FXML
    void col1(MouseEvent event){
        makeMove(1);
    }

    @FXML
    void col2(MouseEvent event){
        makeMove(2);
    }

    @FXML
    void col3(MouseEvent event){
        makeMove(3);
    }

    @FXML
    void col4(MouseEvent event){
        makeMove(4);
    }

    @FXML
    void col5(MouseEvent event){
        makeMove(5);
    }

    @FXML
    void col6(MouseEvent event){
        makeMove(6);
    }


    @FXML
    CheckBox randomFirstTurn;

    @FXML
    void newGamePP(ActionEvent event){
        Game newGame = new PlayerPlayerGame();
        setGame(newGame);
        gameType.setText("Player vs. Player");
        firstPlayerInfo.setText("1st: Player");
        secondPlayerInfo.setText("2nd: Player");
        start();
    }

    @FXML
    void newGamePA(ActionEvent event){
        Algorithm algorithm = collectInfoOfAlgorithm(algPA, depPA);
        if (algorithm != null) {
            Game newGame = new PlayerAiGame(algorithm);
            setGame(newGame);
            gameType.setText("Player vs. AI");
            firstPlayerInfo.setText("1st: Player");
            secondPlayerInfo.setText("2nd: AI, " + algPA.getText()+", depth: " + depPA.getText());
            start();
        }
    }

    @FXML
    void newGameAA(ActionEvent event){
        Algorithm firstAlgorithm = collectInfoOfAlgorithm(algAAF, depAAF);
        Algorithm secondAlgorithm = collectInfoOfAlgorithm(algAAS, depAAS);

        if (firstAlgorithm != null && secondAlgorithm != null){
            Game newGame = new AiAiGame(firstAlgorithm, secondAlgorithm);
            setGame(newGame);
            gameType.setText("AI vs. AI");
            firstPlayerInfo.setText("1st: AI, " + algAAF.getText()+", depth: " + depAAF.getText());
            secondPlayerInfo.setText("2nd: AI, " + algAAS.getText()+", depth: " + depAAS.getText());
            start();


            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (randomFirstTurn.isSelected()) {
                        ((AiAiGame)game).playRandomTurn();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                runLater();
                            }
                        });
                    }

                    while (!game.isFinished()){
                        ((AiAiGame)game).playTurn();

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                runLater();
                            }
                        });
                    }
                }
            }).start();
        }
    }

    private void runLater(){
        markMoveWithAI();
        game.changePlayer();
        updateLabels();
    }



    private Algorithm collectInfoOfAlgorithm(Label label, Label depthLabel){
        Algorithm algorithm = null;
        if (label.getText().equals("Min-Max")) {
            algorithm = new MinMaxAlgorithm(Integer.parseInt(depthLabel.getText()));
        } else if (label.getText().equals("Alpha-Beta")) {
            algorithm = new AlphaBetaAlgorithm(Integer.parseInt(depthLabel.getText()));
        }
        return algorithm;
    }

    void startWindow(){
        initializeBoxes();
        initializeDepthBoxes();
        start();
    }

    private void start(){
        initializeCirclesBoard();
        initializeCircleColors();
        initializeButtonsStyles();
        initializeLabels();
        game.startGame();
    }

    private void initializeDepthBoxes(){
        ObservableList<String> depths = FXCollections.observableArrayList("1", "2", "3", "4", "5", "6");
        depthPA.getItems().addAll(depths);
        setListener(depthPA, depPA);
        depthPA.getSelectionModel().select(4);

        depthAAFirst.getItems().addAll(depths);
        setListener(depthAAFirst, depAAF);
        depthAAFirst.getSelectionModel().select(4);

        depthAASecond.getItems().addAll(depths);
        setListener(depthAASecond, depAAS);
        depthAASecond.getSelectionModel().select(4);

    }

    private void initializeBoxes(){
        ObservableList<String> algs = FXCollections.observableArrayList("Min-Max", "Alpha-Beta");
        algorithmPA.getItems().addAll(algs);
        setListener(algorithmPA, algPA);
        algorithmPA.getSelectionModel().selectFirst();

        algorithmAAFirst.getItems().addAll(algs);
        setListener(algorithmAAFirst, algAAF);
        algorithmAAFirst.getSelectionModel().selectFirst();

        algorithmAASecond.getItems().addAll(algs);
        setListener(algorithmAASecond, algAAS);
        algorithmAASecond.getSelectionModel().selectFirst();
    }

    private void setListener(ComboBox box, Label label){
        box.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                label.setText(t1.toString());
            }
        });
    }

    private void initializeButtonsStyles(){
        Font font = new Font("Consolas", 18); //Button font's size should increase to 40
        newGamePlayerPlayer.setFont(font);
        newGamePlayerAi.setFont(font);
        newGameAiAi.setFont(font);

        newGamePlayerPlayer.setStyle("-fx-background-color: #d9d2d2; -fx-border-color: #5e5e5e; -fx-border-width: 1px");
        newGamePlayerAi.setStyle("-fx-background-color: #d9d2d2; -fx-border-color: #5e5e5e; -fx-border-width: 1px");
        newGameAiAi.setStyle("-fx-background-color: #d9d2d2; -fx-border-color: #5e5e5e; -fx-border-width: 1px");

        setListenersOnButtons();
    }

    private void setListenersOnButtons(){
        setListenerOnButton(newGamePlayerPlayer);
        setListenerOnButton(newGamePlayerAi);
        setListenerOnButton(newGameAiAi);
    }

    private void setListenerOnButton(Button button){
        button.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue)
                button.setStyle("-fx-background-color: #e8e3e3; -fx-border-color: #5e5e5e; -fx-border-width: 1px");
            else
                button.setStyle("-fx-background-color: #d9d2d2; -fx-border-color: #5e5e5e; -fx-border-width: 1px");
        });
    }

    private void initializeLabels(){
        winner.setText("");
        winnerColor.setText("");
        winnerTitle.setText("");
        turn.setText(String.valueOf(Player.FIRST_PLAYER));
        turnColor.setText("Yellow");
        leader.setText("0");
        leader.setStyle("");
        winner.setStyle("");

    }

    private void initializeCircleColors(){
        for (int i = 0; i < circles.size(); i++){
            for (int j = 0; j < circles.get(i).size(); j++)
                setColor(i, j, Color.web("#b5b5b5"));
        }
    }

    private void markMovePP(){
        markPlayerMove(game.getCurrentPlayer());
    }

    private void markMoveWithAI(){
        markPlayerMove(Player.FIRST_PLAYER);
        markPlayerMove(Player.SECOND_PLAYER);
    }

    private void markPlayerMove(Player player){






        int row = game.getRowIndexOfPoint(game.getPlayerLastMove(player));
        int column = game.getColumnIndexOfPoint(game.getPlayerLastMove(player));

        System.out.println(player + " " + row + "," + column);
        setColor(row, column, getPlayerColor(player));
    }

    private void makeMove(int columnIndex){
        boolean succ = false;

        if (game instanceof PlayerPlayerGame) {
            succ = game.makeMove(columnIndex);
            markMovePP();
        }

        if (game instanceof PlayerAiGame) {
            succ = ((PlayerAiGame) game).playTurn(columnIndex);
            markMoveWithAI();
        }

        if (succ)
            game.changePlayer();

        updateLabels();
    }

    private Color getPlayerColor(Player player){
        return player == Player.FIRST_PLAYER ? Color.LIGHTYELLOW : Color.valueOf("#d98686");
    }

    private void setColor(int row, int column, Color color){
        circles.get(row).get(column).setFill(color);
    }

    private void updateLabels(){
        if (game.isFinished()) {
            winnerTitle.setText("Winner");
            winner.setText((game.getWinner() != null) ? String.valueOf(game.getWinner()) : "Draw");
            winnerColor.setText((game.getWinner() != null) ? game.getWinner().getColor(): "");
            setLabelStyleAndColor(game.getWinner(), winner);
            //setLabelStyleAndColor(game.getWinner(), winnerColor);
            leader.setStyle("");
        }
        else {
            turn.setText(String.valueOf(game.getCurrentPlayer()));
            turnColor.setText(game.getCurrentPlayer().getColor());
            setLabelStyleAndColor(getLeader(), leader);
        }
        leader.setText(getLeaderWithScore());
    }

    private void setLabelStyleAndColor(Player player, Label labelToUpdate){
        if (player == Player.FIRST_PLAYER)
            labelToUpdate.setStyle("-fx-background-color: LIGHTYELLOW; -fx-border-color:black;");

        else if (player == Player.SECOND_PLAYER)
            labelToUpdate.setStyle("-fx-background-color: #d98686; -fx-border-color:black");
        else
            labelToUpdate.setStyle("");
    }

    private Player getLeader(){
        return game.getScore() > 0 ? Player.FIRST_PLAYER : game.getScore() == 0 ? null : Player.SECOND_PLAYER;
    }

    private String getLeaderWithScore(){
        if (getLeader() == null) return "It's a draw!";
        return getLeader() + " (" + game.getScore() + ")";
    }


    private void initializeCirclesBoard(){
        ArrayList<Circle> row0 = new ArrayList<>();
        row0.add(c00); row0.add(c01); row0.add(c02); row0.add(c03); row0.add(c04); row0.add(c05); row0.add(c06);
        circles.add(row0);

        ArrayList<Circle> row1 = new ArrayList<>();
        row1.add(c10); row1.add(c11); row1.add(c12); row1.add(c13); row1.add(c14); row1.add(c15); row1.add(c16);
        circles.add(row1);

        ArrayList<Circle> row2 = new ArrayList<>();
        row2.add(c20); row2.add(c21); row2.add(c22); row2.add(c23); row2.add(c24); row2.add(c25); row2.add(c26);
        circles.add(row2);

        ArrayList<Circle> row3 = new ArrayList<>();
        row3.add(c30); row3.add(c31); row3.add(c32); row3.add(c33); row3.add(c34); row3.add(c35); row3.add(c36);
        circles.add(row3);

        ArrayList<Circle> row4 = new ArrayList<>();
        row4.add(c40); row4.add(c41); row4.add(c42); row4.add(c43); row4.add(c44); row4.add(c45); row4.add(c46);
        circles.add(row4);

        ArrayList<Circle> row5 = new ArrayList<>();
        row5.add(c50); row5.add(c51); row5.add(c52); row5.add(c53); row5.add(c54); row5.add(c55); row5.add(c56);
        circles.add(row5);
    }
}
