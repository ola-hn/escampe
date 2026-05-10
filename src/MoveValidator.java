package src;

import java.util.*;

/**
 * Validates moves according to Escampe rules
 */
public class MoveValidator {

    private Board board;

    public MoveValidator(Board board) {
        this.board = board;
    }

    /**
     * Check if a move is valid for a player
     */
    public boolean isValidMove(String move, String player) {
        if (isInitialPlacement(move)) {
            return validateInitialPlacement(move, player);
        } else {
            return validateRegularMove(move, player);
        }
    }

    /**
     * Get all possible moves for a player
     */
    public List<String> getPossibleMoves(String player) {
        List<String> moves = new ArrayList<>();
        List<Piece> playerPieces = board.getPieces(player);

        for (Piece piece : playerPieces) {
            Position startPos = piece.getPosition();
            List<Position> reachable = getReachablePositions(startPos, player);
            
            for (Position endPos : reachable) {
                moves.add(startPos.toNotation() + "-" + endPos.toNotation());
            }
        }

        return moves;
    }

    /**
     * Get all positions reachable from a starting position
     */
    public List<Position> getReachablePositions(Position startPos, String player) {
        Set<Position> reachable = new HashSet<>();
        Border border = board.getBorder(startPos);
        
        if (border == null) return new ArrayList<>();

        int maxDistance = border.getMovementDistance();

        // Check all four directions (up, down, left, right)
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] dir : directions) {
            explorePath(startPos, dir, maxDistance, new HashSet<>(), reachable, player);
        }

        return new ArrayList<>(reachable);
    }

    private void explorePath(Position current, int[] direction, int remainingDistance,
                            Set<Position> visited, Set<Position> reachable, String player) {
        if (remainingDistance <= 0) return;

        Position next = new Position(
            current.getRow() + direction[0],
            current.getCol() + direction[1]
        );

        if (!next.isValid()) return;
        if (visited.contains(next)) return;

        Piece pieceAtNext = board.getPieceAt(next);
        if (pieceAtNext != null && pieceAtNext.getColor().equals(player)) {
            return;  // Can't move through own pieces
        }

        visited.add(next);
        reachable.add(next);

        // Can capture opponent's unicorn but not move past it
        if (pieceAtNext != null && !pieceAtNext.getColor().equals(player)) {
            return;
        }

        // Continue exploring in same direction
        explorePath(next, direction, remainingDistance - 1, visited, reachable, player);
    }

    /**
     * Check if the move string is initial placement format
     */
    private boolean isInitialPlacement(String move) {
        return move.contains("/");
    }

    /**
     * Validate initial placement move
     */
    private boolean validateInitialPlacement(String move, String player) {
        String[] positions = move.split("/");
        if (positions.length != 6) return false;

        for (String pos : positions) {
            Position p;
            try {
                p = new Position(pos);
            } catch (Exception e) {
                return false;
            }

            if (!p.isValid()) return false;

            // Check if position is empty
            if (board.getPieceAt(p) != null) return false;

            // Check if within starting rows
            if (player.equals("noir")) {
                if (p.getRow() > 2) return false;
            } else if (player.equals("blanc")) {
                if (p.getRow() < 5) return false;
            } else {
                return false;
            }
        }

        return true;
    }

    /**
     * Validate regular movement
     */
    private boolean validateRegularMove(String move, String player) {
        String[] parts = move.split("-");
        if (parts.length != 2) return false;

        try {
            Position startPos = new Position(parts[0]);
            Position endPos = new Position(parts[1]);

            if (!startPos.isValid() || !endPos.isValid()) return false;

            Piece movingPiece = board.getPieceAt(startPos);
            if (movingPiece == null) return false;
            if (!movingPiece.getColor().equals(player)) return false;

            // Check if starting position matches last played position's border
            Position lastPos = board.getLastPlayedPosition();
            if (lastPos != null) {
                Border lastBorder = board.getBorder(lastPos);
                Border currentBorder = board.getBorder(startPos);
                if (lastBorder != currentBorder) return false;
            }

            // Check if move is in reachable positions
            List<Position> reachable = getReachablePositions(startPos, player);
            return reachable.contains(endPos);

        } catch (Exception e) {
            return false;
        }
    }
}
