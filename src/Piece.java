package src;

/**
 * Represents a piece on the board
 */
public class Piece {
    public enum Type {
        UNICORN,
        PALADIN
    }

    private String color;  // "blanc" or "noir"
    private Type type;
    private Position position;

    public Piece(String color, Type type, Position position) {
        this.color = color;
        this.type = type;
        this.position = position;
    }

    public String getColor() {
        return color;
    }

    public Type getType() {
        return type;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean isUnicorn() {
        return type == Type.UNICORN;
    }

    public Piece copy() {
        return new Piece(color, type, new Position(position.getRow(), position.getCol()));
    }
}
