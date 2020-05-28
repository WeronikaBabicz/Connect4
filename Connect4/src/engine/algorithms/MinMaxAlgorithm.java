package engine.algorithms;

import engine.gameplay.Game;
import utilities.Player;

import java.util.ArrayList;

public class MinMaxAlgorithm extends Algorithm {

    public MinMaxAlgorithm(int depth) {
        this.depth = depth;
    }

    @Override
    protected void minMax(Game game, int depth, Player player) {
        if (player == Player.FIRST_PLAYER) {
            max(game, depth, player, true);
        } else
            min(game, depth, player, true);
    }

    private int max(Game game, int depth, Player player, boolean isRoot){
        if (depth < 0 || game.isFinished())
            return game.getScore();

        else {
            ArrayList<Integer> allowedColumns = game.getAllowedColumns();
            int maxScore = 0;

            depth--;
            for (int i = 0; i < allowedColumns.size(); i++) {
                Integer allowedColumn = allowedColumns.get(i);
                Game newGame = game.deepCopy();
                newGame.makeMove(allowedColumn);

                int newScore = min(newGame, depth, changePlayer(newGame), false);
                if (i == 0 || maxScore < newScore) {
                    maxScore = newScore;
                    if (forPlayer == Player.FIRST_PLAYER && isRoot)
                        this.column = allowedColumn;
                }

            }
            return maxScore;
        }
    }

    private int min(Game game, int depth, Player player, boolean isRoot){
        if (depth < 0 || game.isFinished())
            return game.getScore();

        else {
            ArrayList<Integer> allowedColumns  = game.getAllowedColumns();
            int minScore = 0;

            depth--;
            for (int i = 0; i < allowedColumns.size(); i++) {
                Integer allowedColumn = allowedColumns.get(i);
                Game newGame = game.deepCopy();
                newGame.makeMove(allowedColumn);

                int newScore = max(newGame, depth, changePlayer(newGame), false);
                if (i == 0 || minScore > newScore) {
                    minScore = newScore;
                    if (forPlayer == Player.SECOND_PLAYER && isRoot)
                        this.column = allowedColumn;
                }
            }
            return minScore;
        }
    }
}
