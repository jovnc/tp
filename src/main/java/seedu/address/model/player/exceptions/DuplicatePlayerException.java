package seedu.address.model.player.exceptions;

/**
 * Signals that the operation will result in duplicate Players (Players are considered duplicates if they have the same
 * identity).
 */
public class DuplicatePlayerException extends RuntimeException {
    public DuplicatePlayerException() {
        super("Operation would result in duplicate players");
    }
}
