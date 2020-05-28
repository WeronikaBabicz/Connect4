package engine.gameplay;

import engine.algorithms.Algorithm;
import utilities.Player;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayerAiGame extends Game{
    private Algorithm algorithm;
    private ArrayList<Long> aiPlayerMoveTimes = new ArrayList<>();

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
        long aiMoveStartTime = System.currentTimeMillis();
        boolean succ = makeMove(algorithm.run(this));
        long aiMoveTime = System.currentTimeMillis() - aiMoveStartTime;
        aiPlayerMoveTimes.add(aiMoveTime);
        System.out.println("AI move time: " + aiMoveTime);

        return succ;
    }

    public ArrayList<Long> getAiPlayerMoveTimes() {
        return aiPlayerMoveTimes;
    }

    public double getAIAverageMoveTime(){
        return aiPlayerMoveTimes.stream().mapToDouble(a -> a).average().orElse(-1.0);
    }
}
