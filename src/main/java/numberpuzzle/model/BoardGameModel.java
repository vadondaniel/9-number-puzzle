package numberpuzzle.model;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

public class BoardGameModel {

    public static final int BOARD_SIZE_X = 10;

    public static final int BOARD_SIZE_Y = 2;

    private ReadOnlyObjectWrapper<Square>[][] board = new ReadOnlyObjectWrapper[BOARD_SIZE_Y][BOARD_SIZE_X];

    public BoardGameModel() {
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

    public Square getSquare(Position p) {
        return board[p.row()][p.col()].get();
    }

    private void setSquare(Position p, Square square) {
        board[p.row()][p.col()].set(square);
    }

    public void move(Position from, Position to) {
        setSquare(to, getSquare(from));
        setSquare(from, Square.NONE);
    }

    public boolean canMove(Position from, Position to) {
        return isOnBoard(from) && isOnBoard(to) && !isEmpty(from) && isEmpty(to) && isOneMove(from, to) && isNotBlocked(from) && isNotBlocked(to);
    }

    public boolean isEmpty(Position p) {
        return getSquare(p) == Square.NONE;
    }

    public boolean isNotBlocked(Position p) {
        return getSquare(p) != Square.BLOCKED;
    }

    public static boolean isOnBoard(Position p) {
        return 0 <= p.row() && p.row() < BOARD_SIZE_Y && 0 <= p.col() && p.col() < BOARD_SIZE_X;
    }

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
        var model = new BoardGameModel();
        System.out.println(model);
    }

}
