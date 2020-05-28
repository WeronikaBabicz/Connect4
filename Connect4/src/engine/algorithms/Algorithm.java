package engine.algorithms;

import engine.gameplay.Game;
import utilities.Player;

public abstract class Algorithm {
    protected int column = 3;
    protected Player forPlayer;
    protected int depth;

    public int run(Game game) {
        Game gameCopy = game.deepCopy();
        forPlayer = gameCopy.getCurrentPlayer();
        minMax(gameCopy, depth, gameCopy.getCurrentPlayer());
        return column;
    }

    protected abstract void minMax(Game game, int depth, Player player);

    protected Player changePlayer(Game game){
        game.changePlayer();
        return game.getCurrentPlayer();
    }
}
