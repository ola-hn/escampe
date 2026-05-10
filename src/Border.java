package src;

/**
 * Represents the border type of a board cell
 */
public enum Border {
    SIMPLE(1),      // single border
    DOUBLE(2),      // double border
    TRIPLE(3);      // triple border

    private int movementDistance;

    Border(int distance) {
        this.movementDistance = distance;
    }

    public int getMovementDistance() {
        return movementDistance;
    }

    public static Border fromChar(char c) {
        switch (c) {
            case '.':
                return SIMPLE;
            case ':':
                return DOUBLE;
            case '#':
                return TRIPLE;
            default:
                return null;
        }
    }

    public char toChar() {
        switch (this) {
            case SIMPLE:
                return '.';
            case DOUBLE:
                return ':';
            case TRIPLE:
                return '#';
            default:
                return '?';
        }
    }
}
