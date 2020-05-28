package engine.evaluation;

import engine.gameplay.Point;
import utilities.Player;

import java.util.ArrayList;

public class WholeBoardEvaluationFunction extends EvaluationFunction{
    @Override
    void calculateAndSetScore() {
        score = 0;
        ArrayList<ArrayList<Point>> listsToConsider = new ArrayList<>();

        collectListsWholeBoard(listsToConsider);
        int scoreFirst = countScoreNoBlocking(listsToConsider, Player.FIRST_PLAYER);
        int scoreSecond = countScoreNoBlocking(listsToConsider, Player.SECOND_PLAYER);

        this.score = scoreFirst - scoreSecond;
    }
}
