package engine.gameplay;

import engine.algorithms.Algorithm;
import utilities.Player;

import java.util.Map;
import java.util.stream.Collectors;

public class PlayerAiGame extends Game{
    private Algorithm algorithm;

    public PlayerAiGame(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    private PlayerAiGame(PlayerAiGame game){
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
        return new PlayerAiGame(this);
    }


    public boolean playTurn(int columnIndex){
        if (!makeMove(columnIndex))
            return false;
        changePlayer();
        return makeAIMove();
    }

    private boolean makeAIMove(){
        int columnIndex = algorithm.run(this);
        return makeMove(columnIndex);
    }
}
