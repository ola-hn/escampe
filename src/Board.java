package src;

import java.util.*;

/**
 * Represents the game board
 */
public class Board {
    private Border[][] borders;  // 6x6 board with border types
    private List<Piece> pieces;  // All pieces (white and black)
    private Position lastPlayedPosition;  // Last position where opponent played

    public Board() {
        borders = new Border[6][6];
        pieces = new ArrayList<>();
        lastPlayedPosition = null;
        initializeBorders();
    }

    private void initializeBorders() {
        // Initialize with default border pattern
        // This should be loaded from file or configured based on game rules
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                borders[i][j] = Border.SIMPLE;  // Default
            }
        }
    }

    public Border getBorder(Position pos) {
        if (!pos.isValid()) return null;
        return borders[pos.getRow() - 1][pos.getCol()];
    }

    public void setBorder(Position pos, Border border) {
        if (pos.isValid()) {
            borders[pos.getRow() - 1][pos.getCol()] = border;
        }
    }

    public Piece getPieceAt(Position pos) {
        for (Piece p : pieces) {
            if (p.getPosition().equals(pos)) {
                return p;
            }
        }
        return null;
    }

    public void addPiece(Piece piece) {
        pieces.add(piece);
    }

    public void removePiece(Piece piece) {
        pieces.remove(piece);
    }

    public List<Piece> getPieces(String color) {
        List<Piece> result = new ArrayList<>();
        for (Piece p : pieces) {
            if (p.getColor().equals(color)) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Piece> getAllPieces() {
        return new ArrayList<>(pieces);
    }

    public Position getLastPlayedPosition() {
        return lastPlayedPosition;
    }

    public void setLastPlayedPosition(Position pos) {
        this.lastPlayedPosition = pos;
    }

    public Board copy() {
        Board newBoard = new Board();
        
        // Copy borders
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                newBoard.borders[i][j] = this.borders[i][j];
            }
        }
        
        // Copy pieces
        for (Piece p : this.pieces) {
            newBoard.pieces.add(p.copy());
        }
        
        // Copy last played position
        if (this.lastPlayedPosition != null) {
            newBoard.lastPlayedPosition = new Position(
                this.lastPlayedPosition.getRow(),
                this.lastPlayedPosition.getCol()
            );
        }
        
        return newBoard;
    }
}
