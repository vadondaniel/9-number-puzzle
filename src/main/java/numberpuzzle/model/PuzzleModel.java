package numberpuzzle.model;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

/**
 * Represents the model of the puzzle.
 */
public class PuzzleModel {

    /**
     * The size of the board on the X axis.
     */
    public static final int BOARD_SIZE_X = 10;

    /**
     * The size of the board on the Y axis.
     */
    public static final int BOARD_SIZE_Y = 2;

    private ReadOnlyObjectWrapper<Square>[][] board = new ReadOnlyObjectWrapper[BOARD_SIZE_Y][BOARD_SIZE_X];

    /**
     * Creates a {@code PuzzleModel} object initializing the puzzle with the
     * pieces at the starting positions.
     */
    public PuzzleModel() {
        for (var i = 0; i < BOARD_SIZE_Y; i++) {
            for (var j = 0; j < BOARD_SIZE_X; j++) {
                board[i][j] = new ReadOnlyObjectWrapper<Square>(
                        switch (i) {
                            case 0 -> switch (j) {
                                case 0, 1, 2, 4, 6, 8, 9 -> Square.BLOCKED;
                                default -> Square.NONE;
                            };
                            case 1 -> switch (j) {
                                case 1 -> Square.TWO;
                                case 2 -> Square.THREE;
                                case 3 -> Square.FOUR;
                                case 4 -> Square.FIVE;
                                case 5 -> Square.SIX;
                                case 6 -> Square.SEVEN;
                                case 7 -> Square.EIGHT;
                                case 8 -> Square.NINE;
                                case 9 -> Square.ONE;
                                default -> Square.NONE;
                            };
                            default -> Square.NONE;
                        }
                );
            }
        }
    }

    public ReadOnlyObjectProperty<Square> squareProperty(int i, int j) {
        return board[i][j].getReadOnlyProperty();
    }

    /**
     * {@return the type of the Square that is on the specified location}
     *
     * @param p the position of the square
     */
    public Square getSquare(Position p) {
        return board[p.row()][p.col()].get();
    }

    /**
     * Sets the square's type on the specified location.
     *
     * @param p the position of the square
     * @param square the type of the square it needs to be set to
     */
    private void setSquare(Position p, Square square) {
        board[p.row()][p.col()].set(square);
    }

    /**
     * Moves a square to a new location.
     *
     * @param from the square's original location
     * @param to the square's new location
     */
    public void move(Position from, Position to) {
        setSquare(to, getSquare(from));
        setSquare(from, Square.NONE);
    }

    /**
     * {@return whether a square can move to the new location}.
     *
     * @param from the square's original location
     * @param to the square's new location
     */
    public boolean canMove(Position from, Position to) {
        return isOnBoard(from) && isOnBoard(to) && !isEmpty(from) && isEmpty(to) && isOneMove(from, to) && isNotBlocked(from) && isNotBlocked(to);
    }

    /**
     * {@return whether the puzzle is solved}
     */
    public boolean isGoal() {
        return false;
    }

    /**
     * {@return whether the specified location is empty}
     *
     * @param p the checked position
     */
    public boolean isEmpty(Position p) {
        return getSquare(p) == Square.NONE;
    }

    /**
     * {@return whether the specified location is not blocked}
     *
     * @param p the checked position
     */
    public boolean isNotBlocked(Position p) {
        return getSquare(p) != Square.BLOCKED;
    }

    /**
     * {@return whether the specified location is on the board}
     *
     * @param p the checked position
     */
    public static boolean isOnBoard(Position p) {
        return 0 <= p.row() && p.row() < BOARD_SIZE_Y && 0 <= p.col() && p.col() < BOARD_SIZE_X;
    }

    /**
     * {@return whether the move is by one square only in the four directions}
     *
     * @param from the square's original location
     * @param to the square's new location
     */
    public static boolean isOneMove(Position from, Position to) {
        var dx = Math.abs(to.row() - from.row());
        var dy = Math.abs(to.col() - from.col());
        return dx + dy == 1;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (var i = 0; i < BOARD_SIZE_Y; i++) {
            for (var j = 0; j < BOARD_SIZE_X; j++) {
                sb.append(board[i][j].get().ordinal()).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        var model = new PuzzleModel();
        System.out.println(model);
    }

}
