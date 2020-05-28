package engine.gameplay;

import engine.evaluation.ChangedPointEvaluationFunction;
import engine.evaluation.EvaluationFunction;
import utilities.Player;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public abstract class Game {
    public static final int WINNING_SEQ_NUMBER = 4;
    static final int COLUMNS = 7;
    private static final int ROWS = 6;

    EvaluationFunction function;
    boolean isFinished = false;
    ArrayList<ArrayList<Point>> board = new ArrayList<ArrayList<Point>>();
    Player currentPlayer;
    Player winner;
    Map<Player, Point> playerLastMove = new EnumMap<Player, Point>(Player.class);
    int score = 0;

    public Game(EvaluationFunction function) {
        this.function = function;
    }

    public abstract Game deepCopy();


    ArrayList<ArrayList<Point>> boardDeepCopy(ArrayList<ArrayList<Point>> board){
        ArrayList<ArrayList<Point>> newBoard = new ArrayList<ArrayList<Point>>();
        for (ArrayList<Point> row : board){
            ArrayList<Point> newRow = new ArrayList<Point>();
            for (Point p : row){
                newRow.add(new Point(p));
            }
            newBoard.add(newRow);
        }
        return newBoard;
    }


    public void startGame() {
        initializeGame();
    }

    public boolean makeMove(int columnIndex){
        if (!isFinished && isMovePossible(columnIndex)) {
            int rowIndex = getFirstEmptyPointIndex(columnIndex);
            pickPointByCurrentPlayer(board.get(rowIndex).get(columnIndex));
            playerLastMove.put(currentPlayer, board.get(rowIndex).get(columnIndex));

            if (checkIfGameIsWon(getCurrentPlayerLastMove())){
                winner = currentPlayer;
                isFinished = true;
            }

            if (checkIfBoardIsFull()){
                isFinished = true;
            }

            calculateAndSetScore();
            return true;
        }
        return false;
    }

    private boolean checkIfBoardIsFull(){
        for (int i = 0; i < board.get(0).size(); i++)
            if (isMovePossible(i))
                return false;

        return true;
    }

    private boolean isMovePossible(int column){
        return board.get(0).get(column).isEmpty();
    }

    public ArrayList<Point> createRow(Point point){
        int rowIndex = getRowIndexOfPoint(point);
        return new ArrayList<>(board.get(rowIndex));
    }

    public ArrayList<Point> createColumn(Point point){
        int columnIndex = getColumnIndexOfPoint(point);
        ArrayList<Point> column = new ArrayList<>();
        for (ArrayList<Point> row: board)
            column.add(row.get(columnIndex));
        return column;
    }

    public ArrayList<Point> createLeftDiagonal(Point point){
        int rowIndex = getRowIndexOfPoint(point);
        int columnIndex = getColumnIndexOfPoint(point);

        ArrayList<Point> leftDiagonal = new ArrayList<>();
        int dist = Math.min(rowIndex, columnIndex);
        int startRow = rowIndex - dist;
        int startColumn = columnIndex - dist;

        int i = 0;
        while (startRow + i < ROWS && startColumn + i < COLUMNS){
            leftDiagonal.add(board.get(startRow + i).get(startColumn + i));
            i++;
        }
        return leftDiagonal;
    }

    public ArrayList<Point> createRightDiagonal(Point point) {
        int rowIndex = getRowIndexOfPoint(point);
        int columnIndex = getColumnIndexOfPoint(point);

        ArrayList<Point> rightDiagonal = new ArrayList<>();
        int dist = Math.min(rowIndex, board.get(rowIndex).size() - 1 - columnIndex);
        int startRow = rowIndex - dist;
        int startColumn = columnIndex + dist;

        int i = 0;
        while (startRow + i < ROWS && startColumn - i >= 0){
            rightDiagonal.add(board.get(startRow + i).get(startColumn - i));
            i++;
        }
        return rightDiagonal;
    }

    public void changePlayer(){
        currentPlayer = (currentPlayer == Player.FIRST_PLAYER ? Player.SECOND_PLAYER : Player.FIRST_PLAYER);
    }

    public int getRowIndexOfPoint(Point point){
        return board.indexOf(getRowContainingPoint(point));
    }

    public int getColumnIndexOfPoint(Point point){
        return board.get(getRowIndexOfPoint(point)).indexOf(point);
    }

    public ArrayList<Integer> getAllowedColumns(){
        ArrayList<Integer> allowedColumns = new ArrayList<>();
        for (int i = 0; i < COLUMNS; i++){
            if (isMovePossible(i))
                allowedColumns.add(i);
        }
        return allowedColumns;
    }









    void initializeGame(){
        initializeBoard();
        currentPlayer = Player.FIRST_PLAYER;
    }

    private void initializeBoard(){
        for (int i = 0; i < ROWS; i++){
            ArrayList<Point> row = new ArrayList<Point>();
            for (int j = 0; j < COLUMNS; j++)
                row.add(new Point((Player)null));

            board.add(row);
        }
    }

    private void pickPointByCurrentPlayer(Point point){
        point.setPickedByPlayer(currentPlayer);
    }

    private int getFirstEmptyPointIndex(int columnIndex){
        for (int i = 0; i < board.size(); i++){
            if (!board.get(i).get(columnIndex).isEmpty())
                return i-1;
        }
        return board.size() -1; //when empty column
    }

    private void calculateAndSetScore(){
        function.calculateScore(this);
        score = function.getScore();
    }

    private boolean checkIfGameIsWon(Point lastMove){
        return checkRow(lastMove) || checkColumn(lastMove)|| checkDiagonals(lastMove);
    }

    private boolean doesListContainWinningSeq(ArrayList<Point> list){
        for (int i = 0; i <= list.size() - WINNING_SEQ_NUMBER; i++){
            if (list.subList(i, i + WINNING_SEQ_NUMBER).stream().allMatch(t -> t.isPickedByPlayer(currentPlayer)))
                return true;
        }
        return false;
    }

    private boolean checkRow(Point point){
        return doesListContainWinningSeq(createRow(point));
    }

    private boolean checkColumn(Point point){
        return doesListContainWinningSeq(createColumn(point));
    }

    private boolean checkDiagonals(Point point){
        ArrayList<Point> leftDiagonal = createLeftDiagonal(point);

        if (doesListContainWinningSeq(leftDiagonal))
            return true;

        ArrayList<Point> rightDiagonal = createRightDiagonal(point);

        return doesListContainWinningSeq(rightDiagonal);
    }

    private ArrayList<Point> getRowContainingPoint(Point point){
        for (ArrayList<Point> row : board){
            if (row.contains(point))
                return row;
        }
        return null;
    }






    public Player getWinner() {
        return winner;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    public int getScore() {
        return score;
    }

    public Point getPlayerLastMove(Player player){
        return playerLastMove.get(player);
    }

    public Point getCurrentPlayerLastMove(){
        return getPlayerLastMove(currentPlayer);
    }

    public ArrayList<ArrayList<Point>> getBoard() {
        return board;
    }
}