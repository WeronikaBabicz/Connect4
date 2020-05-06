package engine.evaluation;

import engine.gameplay.Game;
import engine.gameplay.Point;
import utilities.Player;

import java.util.ArrayList;
import java.util.List;

public class EvaluationFunction {
    private int score;
    private Game game;

    public void calculateScore(Game game){
        this.game = game;
        this.score = game.getScore();
        calculateAndSetScore();
    }

    private void calculateAndSetScore(){
        Point last = game.getCurrentPlayerLastMove();

        ArrayList<Point> leftDiagonal = game.createLeftDiagonal(last);
        ArrayList<Point> rightDiagonal = game.createRightDiagonal(last);
        ArrayList<Point> row = game.createRow(last);
        ArrayList<Point> column = game.createColumn(last);

        ArrayList<ArrayList<Point>> listsToConsider = new ArrayList<>();
        listsToConsider.add(leftDiagonal);
        listsToConsider.add(rightDiagonal);
        listsToConsider.add(row);
        listsToConsider.add(column);

        int score = 0;
        for (ArrayList<Point> list : listsToConsider){
            //list.removeIf(Point::isEmpty);
            score += countScoreInList(list, 4);
            score += countScoreInList(list, 3);
            score += countScoreInList(list, 2);
            score += countScoreInList(list, 1);
        }

        this.score += game.getCurrentPlayer() == Player.FIRST_PLAYER ? score : - score;
    }

    private int countScoreInList(ArrayList<Point> list, int numberOfDesiredSeq){
        int score = 0;
        if (list.size() >= numberOfDesiredSeq){
            for (int i = 0; i <= list.size() - numberOfDesiredSeq; i++){
                List<Point> sublist = list.subList(i, i + numberOfDesiredSeq);
                if (sublist.stream().allMatch(t -> t.isPickedByPlayer(game.getCurrentPlayer()))) {
                    score += Math.pow(10, numberOfDesiredSeq - 1);
                    list.removeIf(sublist::contains);
                }
            }
        }
        return score;
    }

    public int getScore() {
        return score;
    }
}
