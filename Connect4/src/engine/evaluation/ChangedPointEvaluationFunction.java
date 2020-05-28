package engine.evaluation;

import engine.gameplay.Point;
import utilities.Player;

import java.util.ArrayList;

public class ChangedPointEvaluationFunction extends EvaluationFunction{
    @Override
    void calculateAndSetScore(){
        ArrayList<ArrayList<Point>> listsToConsider = new ArrayList<>();

        collectListsOnChangedPoint(listsToConsider);
        int score = countScoreNoBlocking(listsToConsider, game.getCurrentPlayer());

        this.score += game.getCurrentPlayer() == Player.FIRST_PLAYER ? score : - score;
    }
}
