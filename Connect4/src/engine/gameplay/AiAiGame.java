package engine.gameplay;

import engine.algorithms.Algorithm;
import utilities.Player;

import java.util.Map;
import java.util.stream.Collectors;

public class AiAiGame extends Game {
    private Algorithm firstPlayerAlgorithm;
    private Algorithm secondPlayerAlgorithm;

    public AiAiGame(Algorithm firstPlayerAlgorithm, Algorithm secondPlayerAlgorithm) {
        this.firstPlayerAlgorithm = firstPlayerAlgorithm;
        this.secondPlayerAlgorithm = secondPlayerAlgorithm;
    }


    private AiAiGame(AiAiGame game){
        this.firstPlayerAlgorithm = game.firstPlayerAlgorithm;
        this.secondPlayerAlgorithm = game.secondPlayerAlgorithm;
        this.board = boardDeepCopy(game.board);
        this.function = game.function;
        this.isFinished = game.isFinished;
        this.playerLastMove = game.playerLastMove.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        this.currentPlayer = (game.currentPlayer == Player.FIRST_PLAYER) ? Player.FIRST_PLAYER : Player.SECOND_PLAYER;
        this.winner = game.winner;
        this.score = game.score;
    }

    @Override
    public Game deepCopy() {
        return new AiAiGame(this);
    }


    public void playTurn(){
        makeMove(firstPlayerAlgorithm.run(this));
        changePlayer();
        makeMove(secondPlayerAlgorithm.run(this));
    }

    @Override
    public void startGame() {
        initializeGame();
    }


    public void playRandomTurn(){
        int rand = (int)(Math.random() * COLUMNS);
        makeMove(rand);
        changePlayer();
        int rand2 = (int)(Math.random() * COLUMNS);
        makeMove(rand2);
    }
}
