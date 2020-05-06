package engine.algorithms;

import engine.gameplay.Game;
import utilities.Player;

import java.util.ArrayList;

public class MinMaxAlgorithm implements Algorithm {

    private int column = 0;
    private Player forPlayer;

    @Override
    public int run(Game game, int depth) {
        Game gameCopy = game.deepCopy();
        forPlayer = gameCopy.getCurrentPlayer();
        minMax(gameCopy, depth, gameCopy.getCurrentPlayer());
        return column;
    }



    private void minMax(Game game, int depth, Player player) {
        if (player == Player.FIRST_PLAYER) { //maximizing
            max(game, depth, player);
        } else
            min(game, depth, player);
    }

    private Player changePlayer(Game game){
        game.changePlayer();
        return game.getCurrentPlayer();
    }


    private int max(Game game, int depth, Player player){
        if (depth <= 0)
            return game.getScore();

        else {
            ArrayList<Integer> allowedColumns = game.getAllowedColumns();
            int maxScore = game.getScore();

            for (Integer allowedColumn : allowedColumns) {
                Game newGame = game.deepCopy();
                newGame.makeMove(allowedColumn);
                int newScore = min(newGame, --depth, changePlayer(newGame));
                if (maxScore < newScore) {
                    maxScore = newScore;
                    this.column = allowedColumn;
                }
            }
            return maxScore;
        }
    }

    private int min(Game game, int depth, Player player){
        if (depth <= 0)
            return game.getScore();

        else {
            ArrayList<Integer> allowedColumns  = game.getAllowedColumns();
            int minScore = game.getScore();

            for (Integer allowedColumn : allowedColumns) {
                Game newGame = game.deepCopy();
                newGame.makeMove(allowedColumn);
                int newScore = max(newGame, depth, changePlayer(newGame));
                if (minScore > newScore) {
                    minScore = newScore;
                }
            }
            return minScore;
        }
    }
}
