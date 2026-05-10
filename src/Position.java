package src;

/**
 * Represents a position on the board
 */
public class Position {
    private int row;    // 1 to 6
    private int col;    // 0 to 5 (A to F)

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Position(String notation) {
        // notation format: "A1" to "F6"
        this.col = notation.charAt(0) - 'A';
        this.row = Character.getNumericValue(notation.charAt(1));
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String toNotation() {
        return String.valueOf((char)('A' + col)) + row;
    }

    public boolean isValid() {
        return row >= 1 && row <= 6 && col >= 0 && col <= 5;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Position)) return false;
        Position p = (Position) obj;
        return p.row == this.row && p.col == this.col;
    }

    @Override
    public int hashCode() {
        return row * 10 + col;
    }

    @Override
    public String toString() {
        return toNotation();
    }
}
