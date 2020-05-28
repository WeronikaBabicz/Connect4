package engine.gameplay;

import engine.evaluation.EvaluationFunction;
import utilities.Player;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayerPlayerGame extends Game {

    public PlayerPlayerGame(EvaluationFunction evaluationFunction) {
        super(evaluationFunction);
    }

    private PlayerPlayerGame(PlayerPlayerGame game){
        super(game.function);
        this.board = boardDeepCopy(game.board);
        this.isFinished = game.isFinished;
        this.playerLastMove = game.playerLastMove.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        this.currentPlayer = (game.currentPlayer == Player.FIRST_PLAYER) ? Player.FIRST_PLAYER : Player.SECOND_PLAYER;
        this.winner = game.winner;
        this.score = game.score;
    }


    @Override
    public Game deepCopy() {
        return new PlayerPlayerGame(this);
    }

}
