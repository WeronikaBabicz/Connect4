package engine.algorithms;

import engine.gameplay.Game;
import utilities.Player;

import java.util.ArrayList;

public class AlphaBetaAlgorithm implements Algorithm{

    private int column = 3;
    private Player forPlayer;
    private int depth;

    public AlphaBetaAlgorithm(int depth) {
        this.depth = depth;
    }

    @Override
    public int run(Game game) {
        Game gameCopy = game.deepCopy();
        forPlayer = gameCopy.getCurrentPlayer();
        minMax(gameCopy, depth, gameCopy.getCurrentPlayer());
        return column;
    }


    private void minMax(Game game, int depth, Player player) {
        if (player == Player.FIRST_PLAYER) { //maximizing
            max(game, depth, player, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        } else
            min(game, depth, player, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
    }

    private Player changePlayer(Game game){
        game.changePlayer();
        return game.getCurrentPlayer();
    }


    private int max(Game game, int depth, Player player, int alpha, int beta, boolean isRoot){
        if (depth <= 0 || game.isFinished())
            return game.getScore();

        else {
            ArrayList<Integer> allowedColumns = game.getAllowedColumns();
            int bestScore = 0;

            for (int i = 0; i < allowedColumns.size(); i++) {
                Integer allowedColumn = allowedColumns.get(i);
                Game newGame = game.deepCopy();
                newGame.makeMove(allowedColumn);

                int newScore = min(newGame, depth, changePlayer(newGame), alpha, beta, false);

                if (newScore > beta)
                    return newScore; //prune


                alpha = Math.max(alpha, newScore);
                if (i == 0 || bestScore < alpha) {
                    bestScore = alpha;
                    if (forPlayer == Player.FIRST_PLAYER && isRoot)
                        this.column = allowedColumn;
                }

            }
            return bestScore;
        }
    }

    private int min(Game game, int depth, Player player, int alpha, int beta, boolean isRoot){
        if (depth <= 0 || game.isFinished())
            return game.getScore();

        else {
            ArrayList<Integer> allowedColumns  = game.getAllowedColumns();
            int bestScore = 0;

            depth--;
            for (int i = 0; i < allowedColumns.size(); i++) {
                Integer allowedColumn = allowedColumns.get(i);
                Game newGame = game.deepCopy();
                newGame.makeMove(allowedColumn);

                int newScore = max(newGame, depth, changePlayer(newGame), alpha, beta, false);

                if (newScore < alpha)
                    return newScore; //prune

                beta = Math.min(beta, newScore);
                if (i == 0 || bestScore > beta) {
                    bestScore = beta;
                    if (forPlayer == Player.SECOND_PLAYER && isRoot) {
                        this.column = allowedColumn;
                    }
                }

            }
            return bestScore;
        }
    }
}
