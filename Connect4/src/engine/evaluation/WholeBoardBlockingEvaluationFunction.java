package engine.evaluation;

import engine.gameplay.Point;
import utilities.Player;

import java.util.ArrayList;

public class WholeBoardBlockingEvaluationFunction extends EvaluationFunction {
    @Override
    void calculateAndSetScore() {
        score = 0;
        ArrayList<ArrayList<Point>> listsToConsider = new ArrayList<>();

        collectListsWholeBoard(listsToConsider);
        int scoreFirst = countScoreBlocking(listsToConsider, Player.FIRST_PLAYER);
        int scoreSecond = countScoreBlocking(listsToConsider, Player.SECOND_PLAYER);

        this.score = scoreFirst - scoreSecond;
    }
}
