package eu.syplex.scoreboard.exception;

/**
 * Represents an exception thrown when an entered team name is longer than 16 chars.
 */
public class TeamNameTooLongException extends Exception {

    /**
     * Instantiates a new {@link TeamNameTooLongException} with the conflicting team name.
     *
     * @param name The team name
     */
    public TeamNameTooLongException(String name) {
        super("The name of the given team is too long! It cannot be longer than 16 chars. (" + name.length() + " chars present)");
    }
}
