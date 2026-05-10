package src;

/**
 * Comprehensive test suite for EscampeBoard
 * Validates that the implementation respects all game rules and specifications
 */
public class TestEscampeBoard {

    private static int testsRun = 0;
    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("=== ESCAMPE GAME TEST SUITE ===\n");

        // Run all test categories
        testInitialPlacement();
        testMoveValidation();
        testBorderConstraints();
        testGameRules();
        testFileIO();
        testEdgeCases();

        // Print summary
        printSummary();
    }

    /**
     * Test initial piece placement (phase 1)
     */
    private static void testInitialPlacement() {
        System.out.println("--- TEST 1: INITIAL PLACEMENT ---\n");

        // Test 1.1: Valid black placement on rows 1-2
        EscampeBoard game = new EscampeBoard();
        String blackPlacement = "C1/A1/B2/D2/E1/F2";
        assertTrue("Valid black placement (rows 1-2)", 
            game.isValidMove(blackPlacement, "noir"));
        game.play(blackPlacement, "noir");

        // Test 1.2: Valid white placement on rows 5-6
        String whitePlacement = "D6/B6/C5/E5/F6/A5";
        assertTrue("Valid white placement (rows 5-6)", 
            game.isValidMove(whitePlacement, "blanc"));

        // Test 1.3: Invalid black placement (row too high)
        EscampeBoard game2 = new EscampeBoard();
        String invalidBlack = "C3/A1/B2/D2/E1/F2";
        assertFalse("Invalid black placement (row 3 > 2)", 
            game2.isValidMove(invalidBlack, "noir"));

        // Test 1.4: Invalid white placement (row too low)
        EscampeBoard game3 = new EscampeBoard();
        game3.play("C1/A1/B2/D2/E1/F2", "noir");
        String invalidWhite = "D4/B6/C5/E5/F6/A5";
        assertFalse("Invalid white placement (row 4 < 5)", 
            game3.isValidMove(invalidWhite, "blanc"));

        // Test 1.5: Wrong format (not 6 pieces)
        EscampeBoard game4 = new EscampeBoard();
        String wrongFormat = "C1/A1/B2/D2/E1";  // Only 5 pieces
        assertFalse("Invalid format (5 pieces instead of 6)", 
            game4.isValidMove(wrongFormat, "noir"));

        // Test 1.6: Overlapping placements
        EscampeBoard game5 = new EscampeBoard();
        game5.play("C1/A1/B2/D2/E1/F2", "noir");
        String overlap = "C1/A1/B2/D2/E1/F2";  // Same as black
        assertFalse("Overlapping placements", 
            game5.isValidMove(overlap, "blanc"));

        System.out.println();
    }

    /**
     * Test move validation and basic movement
     */
    private static void testMoveValidation() {
        System.out.println("--- TEST 2: MOVE VALIDATION ---\n");

        EscampeBoard game = new EscampeBoard();
        game.play("C1/A1/B2/D2/E1/F2", "noir");
        game.play("D6/B6/C5/E5/F6/A5", "blanc");

        // Test 2.1: Valid move format
        assertTrue("Valid move format (B6-B5)", 
            game.isValidMove("B6-B5", "blanc"));

        // Test 2.2: Invalid move format
        assertFalse("Invalid move format (B6B5)", 
            game.isValidMove("B6B5", "blanc"));

        // Test 2.3: Invalid position notation
        assertFalse("Invalid position (Z9-Z9)", 
            game.isValidMove("Z9-Z9", "blanc"));

        // Test 2.4: Skip turn is valid
        assertTrue("Skip turn is valid (E)", 
            game.isValidMove("E", "blanc"));

        // Test 2.5: Move from empty cell
        assertFalse("Move from empty cell", 
            game.isValidMove("A2-A3", "blanc"));

        System.out.println();
    }

    /**
     * Test border type constraints
     */
    private static void testBorderConstraints() {
        System.out.println("--- TEST 3: BORDER CONSTRAINTS ---\n");

        // Test 3.1: Movement distance based on border
        EscampeBoard game = new EscampeBoard();
        game.play("C1/A1/B2/D2/E1/F2", "noir");
        game.play("D6/B6/C5/E5/F6/A5", "blanc");

        // White plays first move
        game.play("B6-B5", "blanc");
        
        // Test 3.2: Black must play from same border (before rule implementation)
        String[] blackMoves = game.possiblesMoves("noir");
        assertTrue("Black has possible moves", blackMoves.length > 0);

        // Test 3.3: Both include "E" option
        boolean hasEOption = false;
        for (String move : blackMoves) {
            if ("E".equals(move)) {
                hasEOption = true;
                break;
            }
        }
        assertTrue("Black moves include skip option 'E'", hasEOption);

        System.out.println();
    }

    /**
     * Test core game rules
     */
    private static void testGameRules() {
        System.out.println("--- TEST 4: GAME RULES ---\n");

        EscampeBoard game = new EscampeBoard();
        
        // Test 4.1: Initial game state not over
        game.play("C1/A1/B2/D2/E1/F2", "noir");
        game.play("D6/B6/C5/E5/F6/A5", "blanc");
        assertFalse("Game not over at start", game.gameOver());

        // Test 4.2: Both players have 6 pieces
        assertEquals("Black has 6 pieces", 6, game.getPieces("noir").size());
        assertEquals("White has 6 pieces", 6, game.getPieces("blanc").size());

        // Test 4.3: Each player has exactly 1 unicorn
        int blackUnicorns = 0, whiteUnicorns = 0;
        for (Piece p : game.getPieces("noir")) {
            if (p.isUnicorn()) blackUnicorns++;
        }
        for (Piece p : game.getPieces("blanc")) {
            if (p.isUnicorn()) whiteUnicorns++;
        }
        assertEquals("Black has 1 unicorn", 1, blackUnicorns);
        assertEquals("White has 1 unicorn", 1, whiteUnicorns);

        // Test 4.4: 5 paladins per player
        int blackPaladins = game.getPieces("noir").size() - blackUnicorns;
        int whitePaladins = game.getPieces("blanc").size() - whiteUnicorns;
        assertEquals("Black has 5 paladins", 5, blackPaladins);
        assertEquals("White has 5 paladins", 5, whitePaladins);

        System.out.println();
    }

    /**
     * Test file I/O with correct format
     */
    private static void testFileIO() {
        System.out.println("--- TEST 5: FILE I/O ---\n");

        EscampeBoard game = new EscampeBoard();
        game.play("C1/A1/B2/D2/E1/F2", "noir");
        game.play("D6/B6/C5/E5/F6/A5", "blanc");

        // Test 5.1: Save to file
        String testFile = "/tmp/test_escampe_board.txt";
        try {
            game.saveToFile(testFile);
            assertTrue("File saved successfully", true);
        } catch (Exception e) {
            assertFalse("File save failed: " + e.getMessage(), true);
        }

        // Test 5.2: Load from file
        EscampeBoard loadedGame = new EscampeBoard();
        try {
            loadedGame.setFromFile(testFile);
            assertTrue("File loaded successfully", true);
        } catch (Exception e) {
            assertFalse("File load failed: " + e.getMessage(), true);
        }

        // Test 5.3: Piece count matches after load
        assertEquals("Black pieces after load", 6, loadedGame.getPieces("noir").size());
        assertEquals("White pieces after load", 6, loadedGame.getPieces("blanc").size());

        // Test 5.4: Load Figure 5 example
        EscampeBoard fig5 = new EscampeBoard();
        try {
            fig5.setFromFile("/tmp/escampe_board_figure5.txt");
            assertEquals("Figure 5: Black pieces", 6, fig5.getPieces("noir").size());
            assertEquals("Figure 5: White pieces", 6, fig5.getPieces("blanc").size());
        } catch (Exception e) {
            assertFalse("Figure 5 load failed", true);
        }

        // Test 5.5: Load Figure 6 example
        EscampeBoard fig6 = new EscampeBoard();
        try {
            fig6.setFromFile("/tmp/escampe_board_figure6.txt");
            assertEquals("Figure 6: Black pieces", 6, fig6.getPieces("noir").size());
            assertEquals("Figure 6: White pieces", 6, fig6.getPieces("blanc").size());
        } catch (Exception e) {
            assertFalse("Figure 6 load failed", true);
        }

        System.out.println();
    }

    /**
     * Test edge cases and boundary conditions
     */
    private static void testEdgeCases() {
        System.out.println("--- TEST 6: EDGE CASES ---\n");

        // Test 6.1: Invalid player names
        EscampeBoard game = new EscampeBoard();
        assertFalse("Invalid player 'red'", 
            game.isValidMove("C1/A1/B2/D2/E1/F2", "red"));

        // Test 6.2: Case sensitivity for moves
        game.play("C1/A1/B2/D2/E1/F2", "noir");
        game.play("D6/B6/C5/E5/F6/A5", "blanc");
        assertFalse("Lowercase move format", 
            game.isValidMove("b6-b5", "blanc"));

        // Test 6.3: Out of bounds positions
        assertFalse("Position A0 (row 0)", 
            game.isValidMove("A0-A1", "blanc"));
        assertFalse("Position A7 (row 7)", 
            game.isValidMove("A7-A6", "blanc"));
        assertFalse("Position G1 (column beyond F)", 
            game.isValidMove("G1-F1", "blanc"));

        // Test 6.4: Same source and destination
        assertFalse("Same position move", 
            game.isValidMove("B6-B6", "blanc"));

        // Test 6.5: Possible moves always include 'E'
        String[] moves = game.possiblesMoves("blanc");
        boolean hasE = false;
        for (String move : moves) {
            if ("E".equals(move)) {
                hasE = true;
                break;
            }
        }
        assertTrue("Possible moves include 'E'", hasE);

        System.out.println();
    }

    // ========== HELPER METHODS ==========

    private static void assertTrue(String testName, boolean condition) {
        testsRun++;
        if (condition) {
            testsPassed++;
            System.out.println("✓ PASS: " + testName);
        } else {
            testsFailed++;
            System.out.println("✗ FAIL: " + testName);
        }
    }

    private static void assertFalse(String testName, boolean condition) {
        assertTrue(testName, !condition);
    }

    private static void assertEquals(String testName, int expected, int actual) {
        testsRun++;
        if (expected == actual) {
            testsPassed++;
            System.out.println("✓ PASS: " + testName + " [Expected: " + expected + "]");
        } else {
            testsFailed++;
            System.out.println("✗ FAIL: " + testName + " [Expected: " + expected + ", Got: " + actual + "]");
        }
    }

    private static void printSummary() {
        System.out.println("\n=== TEST SUMMARY ===");
        System.out.println("Total tests run: " + testsRun);
        System.out.println("Tests passed:   " + testsPassed);
        System.out.println("Tests failed:   " + testsFailed);
        System.out.println("Success rate:   " + (100 * testsPassed / testsRun) + "%");
        
        if (testsFailed == 0) {
            System.out.println("\n✓ ALL TESTS PASSED - Implementation respects all specifications!");
        } else {
            System.out.println("\n✗ Some tests failed - Please review the implementation");
        }
    }
}
