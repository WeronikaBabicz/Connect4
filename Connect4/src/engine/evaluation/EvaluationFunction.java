package engine.evaluation;

import engine.gameplay.Game;

public abstract class EvaluationFunction {
    protected int score;
    protected Game game;

    public void calculateScore(Game game){
        this.game = game;
        this.score = game.getScore();
        calculateAndSetScore();
    }

    abstract void calculateAndSetScore();

    public int getScore() {
        return score;
    }
}
