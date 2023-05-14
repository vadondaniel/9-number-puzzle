package numberpuzzle.model;

/**
 * Represents a 2D position.
 */
public record Position(int row, int col) {

    @Override
    public String toString() {
        return String.format("(%d,%d)", row, col);
    }

}