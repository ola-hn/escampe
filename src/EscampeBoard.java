package src;

import java.io.IOException;
import java.util.*;

/**
 * Main implementation of the Escampe game - represents a game board configuration
 */
public class EscampeBoard implements Partie1 {

    private Board board;
    private MoveValidator validator;
    private String currentPlayer;
    private boolean gameStarted;

    public EscampeBoard() {
        this.board = new Board();
        this.validator = new MoveValidator(board);
        this.currentPlayer = "blanc";
        this.gameStarted = false;
    }

    /**
     * Get the board for testing purposes
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Get the list of pieces for a player
     */
    public List<Piece> getPieces(String player) {
        return board.getPieces(player);
    }

    /**
     * Get all pieces on the board
     */
    public List<Piece> getAllPieces() {
        return board.getAllPieces();
    }

    @Override
    public void setFromFile(String fileName) {
        try {
            FileHandler.loadFromFile(board, fileName);
            gameStarted = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveToFile(String fileName) {
        try {
            FileHandler.saveToFile(board, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isValidMove(String move, String player) {
        // "E" is always valid (skip turn)
        if ("E".equals(move)) {
            return true;
        }
        
        if (isInitialPlacementPhase() && !gameStarted) {
            return isValidInitialPlacement(move, player);
        }
        return validator.isValidMove(move, player);
    }

    @Override
    public String[] possiblesMoves(String player) {
        List<String> moves = validator.getPossibleMoves(player);
        
        // If no moves possible, add "E" (skip turn)
        if (moves.isEmpty()) {
            return new String[]{"E"};
        }
        
        // Always include "E" as an option to skip turn
        moves.add("E");
        return moves.toArray(new String[0]);
    }

    @Override
    public void play(String move, String player) {
        if (!isValidMove(move, player)) {
            throw new IllegalArgumentException("Invalid move: " + move);
        }

        if (isInitialPlacementPhase() && !gameStarted) {
            executeInitialPlacement(move, player);
            // After both players place pieces, white plays first
            if (board.getPieces("blanc").size() == 6 && board.getPieces("noir").size() == 6) {
                gameStarted = true;
                currentPlayer = "blanc";
            }
        } else {
            // Handle skip turn
            if ("E".equals(move)) {
                currentPlayer = currentPlayer.equals("blanc") ? "noir" : "blanc";
            } else {
                executeMove(move, player);
                board.setLastPlayedPosition(getEndPosition(move));
                currentPlayer = currentPlayer.equals("blanc") ? "noir" : "blanc";
            }
        }
    }

    @Override
    public boolean gameOver() {
        // Check if white's unicorn has been captured
        List<Piece> blackPieces = board.getPieces("noir");
        boolean whiteUnicornTaken = true;
        for (Piece p : blackPieces) {
            if (p.isUnicorn()) {
                whiteUnicornTaken = false;
                break;
            }
        }
        if (whiteUnicornTaken) return true;

        // Check if black's unicorn has been captured
        List<Piece> whitePieces = board.getPieces("blanc");
        boolean blackUnicornTaken = true;
        for (Piece p : whitePieces) {
            if (p.isUnicorn()) {
                blackUnicornTaken = false;
                break;
            }
        }
        return blackUnicornTaken;
    }

    private boolean isInitialPlacementPhase() {
        return board.getPieces("blanc").size() < 6 || board.getPieces("noir").size() < 6;
    }

    private boolean isValidInitialPlacement(String move, String player) {
        if (!move.contains("/")) return false;

        String[] positions = move.split("/");
        if (positions.length != 6) return false;

        for (String pos : positions) {
            try {
                Position p = new Position(pos);
                
                if (!p.isValid()) return false;
                if (board.getPieceAt(p) != null) return false;

                // Check if within starting rows
                if (player.equals("noir")) {
                    if (p.getRow() > 2) return false;
                } else if (player.equals("blanc")) {
                    if (p.getRow() < 5) return false;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }

        return true;
    }

    private void executeInitialPlacement(String move, String player) {
        String[] positions = move.split("/");
        
        // First piece is unicorn, rest are paladins
        for (int i = 0; i < positions.length; i++) {
            Position pos = new Position(positions[i]);
            Piece.Type type = (i == 0) ? Piece.Type.UNICORN : Piece.Type.PALADIN;
            board.addPiece(new Piece(player, type, pos));
        }
    }

    private void executeMove(String move, String player) {
        String[] parts = move.split("-");
        Position startPos = new Position(parts[0]);
        Position endPos = new Position(parts[1]);

        Piece movingPiece = board.getPieceAt(startPos);
        if (movingPiece == null) {
            throw new IllegalArgumentException("No piece at position: " + startPos);
        }

        Piece targetPiece = board.getPieceAt(endPos);
        if (targetPiece != null) {
            // Capture: can only capture opponent's unicorn
            if (!targetPiece.isUnicorn() || targetPiece.getColor().equals(player)) {
                throw new IllegalArgumentException("Invalid capture");
            }
            board.removePiece(targetPiece);
        }

        movingPiece.setPosition(endPos);
    }

    private Position getEndPosition(String move) {
        String[] parts = move.split("-");
        if (parts.length == 2) {
            return new Position(parts[1]);
        }
        return null;
    }

    /**
     * Prints the current state of the board to the console
     */
    public void printBoardState() {
        System.out.println("--- BOARD STATE ---");
        System.out.println("  A B C D E F");
        
        for (int row = 1; row <= 6; row++) {
            System.out.print(row + " ");
            for (int col = 0; col < 6; col++) {
                Position pos = new Position(row, col);
                Piece piece = board.getPieceAt(pos);
                
                if (piece == null) {
                    System.out.print("- ");
                } else if (piece.getColor().equals("noir")) {
                    System.out.print((piece.isUnicorn() ? "N" : "n") + " ");
                } else {
                    System.out.print((piece.isUnicorn() ? "B" : "b") + " ");
                }
            }
            System.out.println(row);
        }
        System.out.println("  A B C D E F\n");
    }

    /**
     * Main method demonstrating the usage of EscampeBoard
     */
    public static void main(String[] args) {
        System.out.println("=== DEMONSTRATION OF ESCAMPE GAME ===\n");

        // --- PART 1: FILE I/O WITH FIGURES 5 AND 6 ---
        System.out.println("=== PART 1: FILE I/O DEMONSTRATION ===\n");

        // Load Figure 5 example from file
        System.out.println("Loading initial board configuration from file (Figure 5):");
        String figure5File = "src/figure5_board.txt";
        System.out.println("File: " + figure5File);
        EscampeBoard gameFig5 = new EscampeBoard();
        gameFig5.setFromFile(figure5File);
        System.out.println("Board loaded successfully!");
        System.out.println("Black pieces: " + gameFig5.board.getPieces("noir").size());
        System.out.println("White pieces: " + gameFig5.board.getPieces("blanc").size());
        System.out.println("Game over? " + gameFig5.gameOver() + "\n");

        // Load Figure 6 example from file
        System.out.println("Loading intermediate board configuration from file (Figure 6):");
        String figure6File = "src/figure6_board.txt";
        System.out.println("File: " + figure6File);
        EscampeBoard gameFig6 = new EscampeBoard();
        gameFig6.setFromFile(figure6File);
        System.out.println("Board loaded successfully!");
        System.out.println("Black pieces: " + gameFig6.board.getPieces("noir").size());
        System.out.println("White pieces: " + gameFig6.board.getPieces("blanc").size());
        System.out.println("Game over? " + gameFig6.gameOver() + "\n");

        // Save and reload
        System.out.println("Testing save/load cycle:");
        String saveFile = "/tmp/escampe_game_saved.txt";
        gameFig5.saveToFile(saveFile);
        System.out.println("Board saved to: " + saveFile);
        
        EscampeBoard gameLoaded = new EscampeBoard();
        gameLoaded.setFromFile(saveFile);
        System.out.println("Board reloaded successfully!");
        System.out.println("Black pieces match: " + (gameLoaded.board.getPieces("noir").size() == 6));
        System.out.println("White pieces match: " + (gameLoaded.board.getPieces("blanc").size() == 6) + "\n");

        // --- PART 2: GAME DEMONSTRATION ON FIGURE 5 ---
        System.out.println("=== PART 2: GAME DEMONSTRATION ON FIGURE 5 ===\n");

        System.out.println("Using the loaded Figure 5 board for gameplay demonstration.\n");
        gameFig5.printBoardState();

        // White's first move
        System.out.println("White's turn (player chooses freely):");
        String[] possibleWhiteMoves = gameFig5.possiblesMoves("blanc");
        System.out.println("Possible moves for white: " + possibleWhiteMoves.length + " options");
        if (possibleWhiteMoves.length > 0) {
            System.out.println("First 5 possible moves:");
            for (int i = 0; i < Math.min(5, possibleWhiteMoves.length); i++) {
                System.out.println("  - " + possibleWhiteMoves[i]);
            }
        }

        String move1 = "B6-B5";
        System.out.println("\nWhite plays: " + move1);
        System.out.println("Is valid? " + gameFig5.isValidMove(move1, "blanc"));
        gameFig5.play(move1, "blanc");
        System.out.println("Move executed\n");
        gameFig5.printBoardState();

        // Black's move - must start from same border as white's last move
        System.out.println("Black's turn (must start from same border type as white's last position):");
        String[] possibleBlackMoves = gameFig5.possiblesMoves("noir");
        System.out.println("Possible moves for noir: " + possibleBlackMoves.length + " options");
        if (possibleBlackMoves.length > 0) {
            System.out.println("First 5 possible moves:");
            for (int i = 0; i < Math.min(5, possibleBlackMoves.length); i++) {
                System.out.println("  - " + possibleBlackMoves[i]);
            }
        }

        String move2 = "C1-C2";
        System.out.println("\nBlack plays: " + move2);
        System.out.println("Is valid? " + gameFig5.isValidMove(move2, "noir"));
        gameFig5.play(move2, "noir");
        System.out.println("Move executed\n");

        // Test invalid moves
        System.out.println("--- TESTING INVALID MOVES ---\n");

        String invalidMove1 = "A5-A3";  // Wrong border constraint
        System.out.println("Attempting invalid move: " + invalidMove1 + " for white");
        System.out.println("Is valid? " + gameFig5.isValidMove(invalidMove1, "blanc"));
        System.out.println("(Should be false - not starting from correct border)\n");

        String invalidMove2 = "Z9-Z9";  // Invalid position
        System.out.println("Attempting invalid move: " + invalidMove2);
        System.out.println("Is valid? " + gameFig5.isValidMove(invalidMove2, "blanc"));
        System.out.println("(Should be false - invalid position)\n");

        // Continue with more valid moves
        System.out.println("--- CONTINUING GAMEPLAY ---\n");

        String move3 = "C2-C3";
        System.out.println("White plays: " + move3);
        gameFig5.play(move3, "blanc");
        System.out.println("Move executed\n");

        String move4 = "E1-E2";
        System.out.println("Black plays: " + move4);
        gameFig5.play(move4, "noir");
        System.out.println("Move executed\n");

        // Test skip turn
        System.out.println("--- TESTING SKIP TURN (\"E\") ---\n");
        System.out.println("Is \"E\" a valid move? " + gameFig5.isValidMove("E", "blanc"));
        System.out.println("White plays skip turn: E");
        gameFig5.play("E", "blanc");
        System.out.println("Turn skipped\n");

        // Test game state
        System.out.println("--- GAME STATE ---\n");
        System.out.println("Game over? " + gameFig5.gameOver());
        System.out.println("(Should be false - no unicorn captured yet)\n");

        // Save game state
        System.out.println("--- SAVING GAME STATE ---\n");
        String midgameFile = "/tmp/escampe_midgame.txt";
        gameFig5.saveToFile(midgameFile);
        System.out.println("Game state saved to: " + midgameFile + "\n");

        System.out.println("=== DEMONSTRATION COMPLETE ===");
    }
}
