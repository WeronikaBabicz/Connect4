package engine.gameplay;

import engine.algorithms.Algorithm;
import utilities.Player;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class AiAiGame extends Game {
    private Algorithm algorithm;

    public AiAiGame(Algorithm algorithm) {
        this.algorithm = algorithm;
    }


    private AiAiGame(AiAiGame game){
        this.algorithm = game.algorithm;
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
        //if (getCurrentPlayer() == Player.FIRST_PLAYER) {
            makeMove(algorithm.run(this, 5));
            changePlayer();
            makeMove(algorithm.run(this, 5));
        //}
    }
}
