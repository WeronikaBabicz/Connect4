package engine.gameplay;

import engine.algorithms.Algorithm;
import utilities.Player;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class AiAiGame extends Game {
    private Algorithm firstPlayerAlgorithm;
    private Algorithm secondPlayerAlgorithm;
    private ArrayList<Long> firstPlayerMoveTimes = new ArrayList<>();
    private ArrayList<Long> secondPlayerMoveTimes = new ArrayList<>();

    public AiAiGame(Algorithm firstPlayerAlgorithm, Algorithm secondPlayerAlgorithm) {
        this.firstPlayerAlgorithm = firstPlayerAlgorithm;
        this.secondPlayerAlgorithm = secondPlayerAlgorithm;
    }


    private AiAiGame(AiAiGame game){
        this.firstPlayerMoveTimes = game.firstPlayerMoveTimes;
        this.secondPlayerMoveTimes = game.secondPlayerMoveTimes;
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
        long aiMoveStartTime = System.currentTimeMillis();
        makeMove(firstPlayerAlgorithm.run(this));
        Long firstAIMoveTime = System.currentTimeMillis() - aiMoveStartTime;
        firstPlayerMoveTimes.add(firstAIMoveTime);
        System.out.println("AI 1 move time: " + firstAIMoveTime);

        changePlayer();

        aiMoveStartTime = System.currentTimeMillis();
        makeMove(secondPlayerAlgorithm.run(this));
        Long secondAIMoveTime = System.currentTimeMillis() - aiMoveStartTime;
        secondPlayerMoveTimes.add(secondAIMoveTime);
        System.out.println("AI 2 move time: " + secondAIMoveTime);
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


    public ArrayList<Long> getFirstPlayerMoveTimes() {
        return firstPlayerMoveTimes;
    }

    public ArrayList<Long> getSecondPlayerMoveTimes() {
        return secondPlayerMoveTimes;
    }

    public double getFirstPlayerAverageMoveTime(){
        return firstPlayerMoveTimes.stream().mapToDouble(a -> a).average().orElse(-1.0);
    }

    public double getSecondPlayerAverageMoveTime(){
        return secondPlayerMoveTimes.stream().mapToDouble(a -> a).average().orElse(-1.0);
    }

    public int getFirstPlayerNumberOfMoves(){
        return firstPlayerMoveTimes.size();
    }

    public int getSecondPlayerNumberOfMoves(){
        return secondPlayerMoveTimes.size();
    }
}
