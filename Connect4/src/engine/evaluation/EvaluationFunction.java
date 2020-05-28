package engine.evaluation;

import engine.gameplay.Game;
import engine.gameplay.Point;
import utilities.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class EvaluationFunction {
    protected int score;
    protected Game game;

    public void calculateScore(Game game){
        this.game = game;
        this.score = game.getScore();
        calculateAndSetScore();
    }

    abstract void calculateAndSetScore();



    private int countScoreInListNoBlocking(ArrayList<Point> list2, int numberOfDesiredSeq, Player player){
        ArrayList<Point> list = (ArrayList<Point>) list2.clone();
        int score = 0;
        if (list.size() >= numberOfDesiredSeq){
            for (int i = 0; i <= list.size() - numberOfDesiredSeq; i++){
                List<Point> sublist = list.subList(i, i + numberOfDesiredSeq);
                if (sublist.stream().allMatch(t -> t.isPickedByPlayer(player))) {
                    if (numberOfDesiredSeq == Game.WINNING_SEQ_NUMBER)
                        return 10000000;
                    score += Math.pow(10, numberOfDesiredSeq - 1);
                    list.removeIf(sublist::contains);
                }
            }
        }
        return score;
    }

    private int countScoreInListBlocking(ArrayList<Point> list2, int numberOfDesiredSeq, Player player){
        ArrayList<Point> list = (ArrayList<Point>) list2.clone();
        int score = 0;
        if (list.size() >= numberOfDesiredSeq){
            for (int i = 0; i <= list.size() - Game.WINNING_SEQ_NUMBER; i++){
                List<Point> sublist = list.subList(i, i + Game.WINNING_SEQ_NUMBER); // fours
                long count = 0;
                for (Point p : sublist)
                    if (p.isEmpty())
                        count++;
                if (sublist.stream().noneMatch(p -> p.isPickedByPlayer(player == Player.FIRST_PLAYER ? Player.SECOND_PLAYER : Player.FIRST_PLAYER))
                    && count != Game.WINNING_SEQ_NUMBER)
                    score += (Game.WINNING_SEQ_NUMBER - count) * 2;

                else
                    score -= 10;
            }
            for (int i = 0; i <= list.size() - numberOfDesiredSeq; i++){
                List<Point> sublist = list.subList(i, i + numberOfDesiredSeq);
                if (sublist.stream().allMatch(t -> t.isPickedByPlayer(player))) {
                    if (numberOfDesiredSeq == Game.WINNING_SEQ_NUMBER)
                        return 10000000;

                    score += Math.pow(10, numberOfDesiredSeq - 1);
                    list.removeIf(sublist::contains);
                }
            }
        }
        return score;
    }

    int countScoreBlocking(ArrayList<ArrayList<Point>> listsToConsider, Player player){
        int score = 0;
        for (ArrayList<Point> list : listsToConsider){
            //list.removeIf(Point::isEmpty);
            score += countScoreInListBlocking(list, 4, player);
            score += countScoreInListBlocking(list, 3, player);
            score += countScoreInListBlocking(list, 2, player);
            score += countScoreInListBlocking(list, 1, player);
        }
        return score;
    }


    int countScoreNoBlocking(ArrayList<ArrayList<Point>> listsToConsider, Player player){
        int score = 0;
        for (ArrayList<Point> list : listsToConsider){
            //list.removeIf(Point::isEmpty);
            score += countScoreInListNoBlocking(list, 4, player);
            score += countScoreInListNoBlocking(list, 3, player);
            score += countScoreInListNoBlocking(list, 2, player);
            score += countScoreInListNoBlocking(list, 1, player);
        }
        return score;
    }

    void collectListsOnChangedPoint(ArrayList<ArrayList<Point>> listsToConsider){
        Point last = game.getCurrentPlayerLastMove();

        ArrayList<Point> leftDiagonal = game.createLeftDiagonal(last);
        ArrayList<Point> rightDiagonal = game.createRightDiagonal(last);
        ArrayList<Point> row = game.createRow(last);
        ArrayList<Point> column = game.createColumn(last);


        listsToConsider.add(leftDiagonal);
        listsToConsider.add(rightDiagonal);
        listsToConsider.add(row);
        listsToConsider.add(column);
    }


    void collectListsWholeBoard(ArrayList<ArrayList<Point>> listsToConsider){
        collectRowsToConsider(listsToConsider);
        collectColumnsToConsider(listsToConsider);
        collectLeftDiagonalsToConsider(listsToConsider);
        collectRightDiagonalsToConsider(listsToConsider);
    }


    private void collectRowsToConsider(ArrayList<ArrayList<Point>> listsToConsider){
        for (int i = 0; i < game.getBoard().size(); i++){
            ArrayList<Point> row = game.createRow(game.getBoard().get(i).get(0));
            if (!row.stream().allMatch(Point::isEmpty))
                listsToConsider.add(row);
        }
    }

    private void collectColumnsToConsider(ArrayList<ArrayList<Point>> listsToConsider){
        for (int i = 0; i < game.getBoard().get(0).size(); i++){
            ArrayList<Point> column = game.createColumn(game.getBoard().get(0).get(i));
            if (!column.stream().allMatch(Point::isEmpty))
                listsToConsider.add(column);
        }
    }

    private void collectLeftDiagonalsToConsider(ArrayList<ArrayList<Point>> listsToConsider){
        for (int i = 0; i < game.getBoard().size(); i++){
            ArrayList<Point> leftDiagonal = game.createLeftDiagonal(game.getBoard().get(i).get(0));
            if (!leftDiagonal.stream().allMatch(Point::isEmpty))
                listsToConsider.add(leftDiagonal);
        }

        for (int i = 0; i < game.getBoard().get(0).size(); i++){
            ArrayList<Point> leftDiagonal = game.createLeftDiagonal(game.getBoard().get(0).get(i));
            if (!leftDiagonal.stream().allMatch(Point::isEmpty))
                listsToConsider.add(leftDiagonal);
        }
    }

    private void collectRightDiagonalsToConsider(ArrayList<ArrayList<Point>> listsToConsider){
        for (int i = 0; i < game.getBoard().size(); i++){
            ArrayList<Point> rightDiagonal = game.createRightDiagonal(game.getBoard().get(i).get(0));
            if (!rightDiagonal.stream().allMatch(Point::isEmpty))
                listsToConsider.add(rightDiagonal);
        }

        for (int i = 0; i < game.getBoard().get(game.getBoard().size() - 1).size(); i++){
            ArrayList<Point> rightDiagonal = game.createRightDiagonal(game.getBoard().get(game.getBoard().size() - 1).get(i));
            if (!rightDiagonal.stream().allMatch(Point::isEmpty))
                listsToConsider.add(rightDiagonal);
        }
    }



    public int getScore() {
        return score;
    }
}
