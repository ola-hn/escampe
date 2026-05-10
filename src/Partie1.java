package src;

/**
 * Interface for the Escampe game
 */
public interface Partie1 {
    /**
     * Initialises a board from a text file
     * @param fileName the name of the file to read
     */
    public void setFromFile(String fileName);

    /**
     * Saves the configuration of the current state (board and remaining pieces) to a file
     * @param fileName the name of the file to save
     * The format must be compatible with that used for reading.
     */
    public void saveToFile(String fileName);

    /**
     * Indicates if the move <move> is valid for player <player> on the current board
     * @param move the move to play,
     *             in the form "B1-D1" in general,
     *             in the form "C6/A6/B5/D5/E6/F5" for the move that places the pieces
     * @param player the player playing, represented by "noir" or "blanc"
     */
    public boolean isValidMove(String move, String player);

    /**
     * Calculates possible moves for player <player> on the current board
     * @param player the player playing, represented by "noir" or "blanc"
     */
    public String[] possiblesMoves(String player);

    /**
     * Modifies the board by playing the move <move> with player <player>
     * @param move the move to play, in the form "C1-D1" or "C6/A6/B5/D5/E6/F5"
     * @param player the player playing, represented by "noir" or "blanc"
     */
    public void play(String move, String player);

    /**
     * True when the board corresponds to a game end
     */
    public boolean gameOver();
}
