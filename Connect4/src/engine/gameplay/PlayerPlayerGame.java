package engine.gameplay;

import utilities.Player;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayerPlayerGame extends Game {

    public PlayerPlayerGame() {
    }

    private PlayerPlayerGame(PlayerPlayerGame game){
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
        return new PlayerPlayerGame(this);
    }

}
