package RushHour.ia.problemes;

import RushHour.ia.framework.common.State;
import RushHour.ia.framework.recherche.HasHeuristic;

import java.util.Arrays;

public class RushHourState extends State implements HasHeuristic {

    private int[][] board;

    public RushHourState(int[][] b){
        board = b;
    }

    @Override
    protected State cloneState() {
        return new RushHourState(board.clone());
    }

    @Override
    protected boolean equalsState(State o) {
        RushHourState other = (RushHourState) o;
        return Arrays.deepEquals(board, other.board);
    }

    @Override
    protected int hashState() {
        return Arrays.deepHashCode(board);
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    private int getRedCarPos(){
        boolean found = false;
        int j = 0;
        while (!found){
            if (board[2][j] == 3){
                found = true;
            } else {
                j++;
            }
        }

        return j;
    }

    @Override
    public double getHeuristic() {
        int j = this.getRedCarPos() + 1;
        double nbCases = 0;
        double nbCarsBlocking = 0;

        while (j < 6){
            nbCases++;
            if (board[2][j] >= 1 && board[2][j] <=4) nbCarsBlocking++;
            j++;
        }

        return nbCarsBlocking/nbCases;
    }
}
