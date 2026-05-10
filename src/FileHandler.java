package src;

import java.io.*;
import java.util.*;

/**
 * Handles file I/O for board state
 * 
 * File format:
 * - Lines starting with "%" are comments (ignored)
 * - Board lines (6 consecutive) numbered 01-06 from bottom to top
 * - Each board line: "NN pieces---- NN" where N is the line number
 * - Pieces: 'N'=black unicorn, 'n'=black paladin, 'B'=white unicorn, 'b'=white paladin, '-'=empty
 * - Columns are A-F (left to right)
 */
public class FileHandler {

    /**
     * Load board configuration from file
     * File format:
     * % ABCDEF
     * 01 bb---- 01
     * 02 -Bb-bb 02
     * 03 ------ 03
     * 04 ------ 04
     * 05 -n-n-n 05
     * 06 n-N-n- 06
     * % ABCDEF
     */
    public static void loadFromFile(Board board, String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        int boardLinesRead = 0;

        while ((line = reader.readLine()) != null && boardLinesRead < 6) {
            line = line.trim();
            
            // Skip empty lines
            if (line.isEmpty()) continue;
            
            // Skip comment lines (starting with %)
            if (line.startsWith("%")) continue;

            // Parse board line
            if (line.length() >= 9) {
                // Extract row number from start (01-06)
                String rowStr = line.substring(0, 2);
                try {
                    int row = Integer.parseInt(rowStr);
                    
                    // Extract pieces (columns A-F)
                    String piecesStr = line.substring(3, 9);  // 6 characters for A-F
                    
                    for (int col = 0; col < 6; col++) {
                        char pieceChar = piecesStr.charAt(col);
                        Position pos = new Position(row, col);
                        
                        if (pieceChar != '-') {
                            Piece piece = createPieceFromChar(pieceChar, pos);
                            if (piece != null) {
                                board.addPiece(piece);
                            }
                        }
                    }
                    
                    boardLinesRead++;
                } catch (NumberFormatException e) {
                    // Skip invalid lines
                }
            }
        }

        reader.close();
    }

    /**
     * Save board configuration to file
     */
    public static void saveToFile(Board board, String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

        // Write header comment
        writer.write("% ABCDEF");
        writer.newLine();

        // Create a 6x6 grid of pieces
        char[][] grid = new char[6][6];
        
        // Initialize grid with empty cells
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                grid[i][j] = '-';
            }
        }

        // Place pieces on grid
        for (Piece piece : board.getAllPieces()) {
            Position pos = piece.getPosition();
            if (pos.isValid()) {
                grid[pos.getRow() - 1][pos.getCol()] = getPieceCharFromType(piece);
            }
        }

        // Write board lines (from row 1 up to row 6)
        for (int row = 1; row <= 6; row++) {
            StringBuilder line = new StringBuilder();
            line.append(String.format("%02d ", row));
            
            for (int col = 0; col < 6; col++) {
                line.append(grid[row - 1][col]);
            }
            
            line.append(String.format(" %02d", row));
            writer.write(line.toString());
            writer.newLine();
        }

        // Write footer comment
        writer.write("% ABCDEF");
        writer.newLine();

        writer.close();
    }

    /**
     * Create a piece from a character
     */
    private static Piece createPieceFromChar(char ch, Position pos) {
        switch (ch) {
            case 'N':
                return new Piece("noir", Piece.Type.UNICORN, pos);
            case 'n':
                return new Piece("noir", Piece.Type.PALADIN, pos);
            case 'B':
                return new Piece("blanc", Piece.Type.UNICORN, pos);
            case 'b':
                return new Piece("blanc", Piece.Type.PALADIN, pos);
            default:
                return null;
        }
    }

    /**
     * Get character representation for a piece
     */
    private static char getPieceCharFromType(Piece piece) {
        if (piece.getColor().equals("noir")) {
            return piece.isUnicorn() ? 'N' : 'n';
        } else {
            return piece.isUnicorn() ? 'B' : 'b';
        }
    }
}
